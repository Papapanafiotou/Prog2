import sys
import sqlite3
import os
import time
import io

# --- 1. ΕΙΔΙΚΗ ΡΥΘΜΙΣΗ ΓΙΑ ΕΛΛΗΝΙΚΑ ΣΕ WINDOWS ---
# Διασφαλίζει ότι input/output δουλεύουν σωστά με UTF-8
sys.stdin = io.TextIOWrapper(sys.stdin.buffer, encoding='utf-8')
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding='utf-8')

# --- 2. IMPORT ΤΗΣ ΝΕΑΣ ΒΙΒΛΙΟΘΗΚΗΣ ---
try:
    from google import genai
    from google.genai import types
except ImportError:
    print("Error: Η βιβλιοθήκη 'google-genai' λείπει.")
    print("Εντολή εγκατάστασης: pip install google-genai")
    sys.exit(1)

# --- 3. ΦΟΡΤΩΣΗ API KEY ---
script_dir = os.path.dirname(os.path.abspath(__file__))
key_path = os.path.join(script_dir, "statewallet", "api_key.txt")

API_KEY = None
try:
    with open(key_path, "r", encoding='utf-8') as f:
        API_KEY = f.read().strip()
except FileNotFoundError:
    try:
        # Fallback στο root folder
        alt_path = os.path.join(script_dir, "api_key.txt")
        with open(alt_path, "r", encoding='utf-8') as f:
             API_KEY = f.read().strip()
    except:
        pass

if not API_KEY:
    print("Error: Δεν βρέθηκε το αρχείο api_key.txt")
    sys.exit(1)

client = genai.Client(api_key=API_KEY)

# --- 4. ΕΠΙΛΟΓΗ ΜΟΝΤΕΛΟΥ (GEMINI 2.0) ---
# Το 1.5 καταργήθηκε, οπότε πάμε στο stable 2.0 Flash
MODEL_ID = "gemini-2.0-flash" 

client = genai.Client(api_key=API_KEY)

# --- 4. ΛΙΣΤΑ ΜΟΝΤΕΛΩΝ (AUTO-FALLBACK) ---
# Το script θα δοκιμάσει αυτά τα μοντέλα με τη σειρά μέχρι να βρει κάποιο που δουλεύει.
# Συνήθως το '-exp' είναι το δωρεάν για developers.
MODEL_LIST = [
    "gemini-2.0-flash-lite-001",   # Συχνά το free tier της 2.0
    "gemini-2.0-flash-001",       # Η stable έκδοση (συχνά paid)
    "gemini-2.5-pro",       # Το κλασικό γρήγορο (αν υπάρχει ακόμα)
    "gemini-2.5-flash-lite"     # Το πολύ ελαφρύ (super fast/cheap)
]

def generate_safe(prompt):
    """
    Δοκιμάζει όλα τα διαθέσιμα μοντέλα στη λίστα.
    Αν αποτύχει το ένα, πάει στο επόμενο.
    """
    global MODEL_LIST
    
    # Πρώτη προσπάθεια: Loop μέσα στα μοντέλα
    for model_name in MODEL_LIST:
        try:
            # print(f"👉 Δοκιμή με μοντέλο: {model_name}...", flush=True) # (Uncomment για debug)
            
            response = client.models.generate_content(
                model=model_name,
                contents=prompt
            )
            
            # Αν πετύχει, επιστρέφουμε το κείμενο
            # και βάζουμε αυτό το μοντέλο πρώτο στη λίστα για την επόμενη φορά (optimization)
            if response.text:
                return response.text
                
        except Exception as e:
            err_msg = str(e)
            # Αν είναι θέμα ορίου (429), δοκιμάζουμε το επόμενο μοντέλο αμέσως
            if "429" in err_msg or "ResourceExhausted" in err_msg:
                continue 
            # Αν το μοντέλο δεν βρέθηκε (404), πάμε στο επόμενο
            elif "NotFound" in err_msg or "404" in err_msg:
                continue
            else:
                # Αν είναι άλλο περίεργο error, το τυπώνουμε για να ξέρουμε τι φταίει
                print(f"\n⚠️ Error στο {model_name}: {err_msg}")
                continue

    # Αν εξαντληθούν όλα τα μοντέλα και αποτύχουν
    return "❌ ΣΦΑΛΜΑ: Κανένα μοντέλο δεν είναι διαθέσιμο αυτή τη στιγμή (Όριο Χρήσης)."

# ---------------------------------------------------------

def analyze_specific(item_name, amount, goal):
    print("⏳ Επεξεργασία αιτήματος...", flush=True)
    prompt = f"""
    Είσαι ψηφιακός οικονομικός σύμβουλος του κράτους.
    Δεδομένα: Ο λογαριασμός '{item_name}' έχει ύψος {amount} EUR.
    Στόχος Χρήστη: "{goal}".
    
    Οδηγίες:
    Αν ο στόχος είναι εφικτός πρότεινε αλλαγή στο ποσό (π.χ. -10%) και πως θα μπορούσε να υλοποιηθεί, αλλιώς εξήγησε γιατί δεν είναι.
    Απάντησε σύντομα αλλά και περιεκτικά στα Ελληνικά.
    """
    print(generate_safe(prompt))

def analyze_global(db_path, goal):
    print("⏳ Ανάλυση βάσης δεδομένων...", flush=True)
    try:
        real_path = db_path.replace("jdbc:sqlite:", "")
        if not os.path.exists(real_path):
            print(f"Error: Δεν βρέθηκε η βάση στο: {real_path}")
            return

        conn = sqlite3.connect(real_path)
        cursor = conn.cursor()
        
        cursor.execute("SELECT SUM(amount) FROM esoda")
        res = cursor.fetchone(); total_income = res[0] if res and res[0] else 0
        
        cursor.execute("SELECT SUM(amount) FROM eksoda")
        res = cursor.fetchone(); total_expenses = res[0] if res and res[0] else 0
        
        cursor.execute("SELECT name, amount FROM ypourgeia ORDER BY amount")
        ministries = cursor.fetchall()
        ministries_text = "\n".join([f"- {m[0]}: {m[1]:,.0f}€" for m in ministries])
        conn.close()

        deficit = total_expenses - total_income
        
        summary = f"""
        Έσοδα: {total_income:,.0f}€ | Έξοδα: {total_expenses:,.0f}€
        Έλλειμμα: {deficit:,.0f}€
        Έξοδα Υπουργείων:
        {ministries_text}
        """
        
        prompt = f"""
        Είσαι ο ψηφιακός οικονομικός σύμβουλος του κράτους.
        ΔΕΔΟΜΕΝΑ: {summary}
        ΕΝΤΟΛΗ: "{goal}"
        
        ΟΔΗΓΙΕΣ:
        1. Πρότεινε μέχρι 5 συγκεκριμένες κινήσεις (ποσά/περικοπές) για να επιτευχθεί η εντολή (αν είναι εφικτή).
        2. Αν ζητάνε λεφτά, βρες από ποιο υπουργείο θα κόψεις ώστε να είναι το λιγότερο ζημιωγώνο.
        3. Άν η εντολή που ζητάει ο χρήστης δεν είναι εφικτή εξήγησε γιατί δεν είναι εφικτή
        4. Απάντησε με λίστα (αρίθμηση) στα Ελληνικά.
        """
        
        print(generate_safe(prompt))

    except Exception as e:
        print(f"DB Error: {e}")

# --- MAIN EXECUTION ---
if __name__ == "__main__":
    
    # Έλεγχος βασικών ορισμάτων (mode και path είναι υποχρεωτικά)
    if len(sys.argv) < 3:
        print("Usage: python script.py [global/specific] [path/value]")
        sys.exit(1)

    mode = sys.argv[1]
    param = sys.argv[2] # DB path ή Item Name

    # --- ΔΙΑΧΕΙΡΙΣΗ INPUT (ΣΤΟΧΟΣ) ---
    goal = ""

    # 1. Έλεγχος αν υπάρχουν δεδομένα από PIPE (echo "..." | python ...)
    if not sys.stdin.isatty():
        try:
            goal = sys.stdin.read().strip()
        except:
            pass

    # 2. Αν δεν δόθηκε goal, ζητάμε ΔΙΑΔΡΑΣΤΙΚΑ από τον χρήστη
    if not goal:
        try:
            print("\n📝 Ποιος είναι ο οικονομικός στόχος;", end=" ", flush=True)
            # Χρησιμοποιούμε input() που περιμένει τον χρήστη να γράψει
            goal = input()
        except KeyboardInterrupt:
            print("\nΑκυρώθηκε από τον χρήστη.")
            sys.exit(0)
    
    if not goal.strip():
        print("⚠️ Δεν δόθηκε στόχος. Γίνεται γενική ανάλυση.")
        goal = "Γενική βελτίωση οικονομικών δεικτών"

    # --- ΕΚΤΕΛΕΣΗ ---
    if mode == "specific":
        # Στο specific θέλουμε και το amount ως 3ο όρισμα
        if len(sys.argv) >= 4:
            analyze_specific(param, sys.argv[3], goal)
        else:
            print("Error: Λείπει το ποσό (amount) για το specific mode.")
            
    elif mode == "global":
        analyze_global(param, goal)