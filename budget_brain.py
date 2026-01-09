import sys
import sqlite3
import os
import time
import io
from dotenv import load_dotenv

# --- 1. ΡΥΘΜΙΣΗ ENCODING (Για Ελληνικά στα Windows) ---
sys.stdin = io.TextIOWrapper(sys.stdin.buffer, encoding='utf-8')
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding='utf-8')

# --- 2. ΕΙΣΑΓΩΓΗ ΒΙΒΛΙΟΘΗΚΗΣ ---
try:
    from google import genai
    from google.genai import types
except ImportError:
    print("Σφάλμα: Η βιβλιοθήκη 'google-genai' λείπει.")
    print("Εκτελέστε: pip install google-genai")
    sys.exit(1)

# --- ΦΟΡΤΩΣΗ API KEY ΑΠΟ .ENV ---
load_dotenv() # Διαβάζει το αρχείο .env
API_KEY = os.getenv("GOOGLE_API_KEY") # Παίρνει την τιμή

if not API_KEY:
    print("Σφάλμα: Δεν βρέθηκε το GOOGLE_API_KEY στο αρχείο .env")
    sys.exit(1)

# --- ΑΡΧΙΚΟΠΟΙΗΣΗ CLIENT  ---
client = genai.Client(api_key=API_KEY)

# --- 4. ΛΙΣΤΑ ΜΟΝΤΕΛΩΝ (ΑΥΤΟΜΑΤΗ ΕΝΑΛΛΑΓΗ) ---
# Δοκιμάζουμε αυτά τα μοντέλα με τη σειρά μέχρι να πετύχουμε κάποιο ενεργό
MODEL_LIST = [
    "gemini-2.0-flash",        # Ο βασικός στόχος
    "gemini-2.0-flash-lite",   # Ελαφρύ backup
    "gemini-2.5-flash",        # Σταθερό backup
    "gemini-2.5-flash-lite"    # Ισχυρό backup
]

def generate_safe(prompt):
    """
    Δοκιμάζει όλα τα μοντέλα της λίστας. Αν αποτύχει το ένα (λόγω ορίου), πάει στο επόμενο.
    """
    for model_name in MODEL_LIST:
        try:
            # print(f"👉 Δοκιμή με μοντέλο: {model_name}...", flush=True) # (Για debugging)
            response = client.models.generate_content(
                model=model_name,
                contents=prompt
            )
            if response.text:
                return response.text
                
        except Exception as e:
            err_msg = str(e)
            # Αν είναι θέμα υπερφόρτωσης (503) ή ορίου (429), πάμε αμέσως στο επόμενο
            if "429" in err_msg or "503" in err_msg or "ResourceExhausted" in err_msg:
                continue 
            # Αν το μοντέλο δεν υπάρχει (404), πάμε στο επόμενο
            elif "NotFound" in err_msg or "404" in err_msg:
                continue
            else:
                # Άλλο σφάλμα
                continue

    return "❌ ΣΦΑΛΜΑ: Κανένα μοντέλο AI δεν είναι διαθέσιμο αυτή τη στιγμή (Όριο Χρήσης)."

# ---------------------------------------------------------
def get_summary(db_path):
    """
    Διαβάζει τη βάση και επιστρέφει τη συνολική οικονομική εικόνα.
    """
    # print(f"⏳ Ανάλυση βάσης στο: {db_path}...", flush=True) # Debug
    try:
        # Καθαρισμός του path αν έρχεται από Java JDBC URL
        real_path = db_path.replace("jdbc:sqlite:", "")
        
        if not os.path.exists(real_path):
            return f"Σφάλμα: Δεν βρέθηκε η βάση δεδομένων στη διαδρομή: {real_path}"

        conn = sqlite3.connect(real_path)
        cursor = conn.cursor()
        
        cursor.execute("SELECT SUM(amount) FROM esoda")
        res = cursor.fetchone(); total_income = res[0] if res and res[0] else 0
        
        cursor.execute("SELECT SUM(amount) FROM eksoda")
        res = cursor.fetchone(); total_expenses = res[0] if res and res[0] else 0
        
        # Παίρνουμε τα υπουργεία για context
        cursor.execute("SELECT name, amount FROM ypourgeia ORDER BY amount")
        ministries = cursor.fetchall()
        ministries_text = "\n".join([f"- {m[0]}: {m[1]:,.0f}€" for m in ministries])
        
        conn.close()

        deficit = total_expenses - total_income
        state = "ΠΛΕΟΝΑΣΜΑ" if deficit < 0 else "ΕΛΛΕΙΜΜΑ"
        
        return f"""
        ΣΥΝΟΠΤΙΚΗ ΕΙΚΟΝΑ ΚΡΑΤΙΚΩΝ ΟΙΚΟΝΟΜΙΚΩΝ:
        Συνολικά Έσοδα: {total_income:,.0f}€
        Συνολικά Έξοδα: {total_expenses:,.0f}€
        Τρέχουσα Κατάσταση: {state} ύψους {abs(deficit):,.0f}€
        
        ΔΑΠΑΝΕΣ (ΥΠΟΥΡΓΕΙΑ):
        {ministries_text}
        """
    except Exception as e:
        return f"Σφάλμα Βάσης Δεδομένων: {str(e)}"

def analyze_specific(db_path, item_name, amount, goal):
    # Εδώ καλούμε τη συνάρτηση get_summary για να πάρουμε τα δεδομένα
    global_context = get_summary(db_path)
    
    print("⏳ Ο AI σύμβουλος επεξεργάζεται τα δεδομένα...", flush=True)
    
    prompt = f"""
    Είσαι ο ψηφιακός οικονομικός σύμβουλος του κράτους.
    
    --- ΓΕΝΙΚΗ ΕΙΚΟΝΑ ΠΡΟΫΠΟΛΟΓΙΣΜΟΥ ---
    {global_context}
    
    --- ΣΤΟΙΧΕΙΑ ΣΥΓΚΕΚΡΙΜΕΝΟΥ ΛΟΓΑΡΙΑΣΜΟΥ ---
    Λογαριασμός: '{item_name}'
    Τρέχον Ποσό: {amount} EUR
    
    --- ΣΤΟΧΟΣ ΧΡΗΣΤΗ ---
    "{goal}"
    
    ΟΔΗΓΙΕΣ:
    1. Ανάλυσε τον στόχο του χρήστη για τον συγκεκριμένο λογαριασμό.
    2. ΛΑΒΕ ΥΠΟΨΗ τη Γενική Εικόνα. (Π.χ. αν ζητάει αύξηση δαπάνης ενώ υπάρχει τεράστιο έλλειμμα, προειδοποίησέ τον).
    3. Πρότεινε μια συγκεκριμένη κίνηση (π.χ. "Μείωση κατά 10%") ή εξήγησε γιατί δεν είναι εφικτό.
    4. Απάντησε σύντομα και περιεκτικά στα Ελληνικά.
    """
    print(generate_safe(prompt))

def analyze_global(db_path, goal):
    # Εδώ καλούμε τη συνάρτηση get_summary
    global_context = get_summary(db_path)
    
    print("⏳ Ο AI σύμβουλος σκέφτεται...", flush=True)

    prompt = f"""
    Είσαι ο ψηφιακός οικονομικός σύμβουλος του κράτους.
    
    --- ΟΙΚΟΝΟΜΙΚΑ ΔΕΔΟΜΕΝΑ ---
    {global_context}
    
    --- ΕΝΤΟΛΗ ΧΡΗΣΤΗ ---
    "{goal}"
    
    ΟΔΗΓΙΕΣ:
    1. Πρότεινε μέχρι 5 συγκεκριμένες στρατηγικές κινήσεις (περικοπές/αυξήσεις) για να επιτευχθεί η εντολή.
    2. Αν ζητείται εξεύρεση χρημάτων, εντόπισε από ποιο υπουργείο θα κόψεις ώστε να είναι το λιγότερο ζημιωγόνο.
    3. Αν ο στόχος είναι ανέφικτος (π.χ. "Μηδενισμός ελλείμματος" όταν είναι τεράστιο), εξήγησε γιατί.
    4. Απάντησε με αριθμημένη λίστα στα Ελληνικά.
    """
    print(generate_safe(prompt))

# --- ΚΥΡΙΑ ΕΚΤΕΛΕΣΗ (MAIN) ---
if __name__ == "__main__":
    
    # Χρειαζόμαστε τουλάχιστον: όνομα script, mode, db_path
    if len(sys.argv) < 3:
        # print("Χρήση: python budget_brain.py [global/specific] [db_path] [optional_args...]")
        sys.exit(1)

    mode = sys.argv[1]
    db_path = sys.argv[2] # Το db_path είναι πάντα το 2ο όρισμα πλέον

    # --- ΛΗΨΗ ΣΤΟΧΟΥ (INPUT) ---
    goal = ""
    # Προσπάθεια ανάγνωσης από το pipe (αν το στέλνει η Java)
    if not sys.stdin.isatty():
        try:
            goal = sys.stdin.read().strip()
        except: pass

    # Αν δεν υπάρχει στόχος, βάζουμε έναν γενικό
    if not goal:
        goal = "Γενική Ανάλυση"

    # --- ΕΚΤΕΛΕΣΗ ΛΕΙΤΟΥΡΓΙΩΝ ---
    if mode == "specific":
        # Αναμένουμε: python budget_brain.py specific <db_path> <name> <amount>
        if len(sys.argv) >= 5:
            item_name = sys.argv[3]
            amount = sys.argv[4]
            analyze_specific(db_path, item_name, amount, goal)
        else:
            print("Σφάλμα: Λείπουν ορίσματα για το specific mode (όνομα, ποσό).")
            
    elif mode == "global":
        # Αναμένουμε: python budget_brain.py global <db_path>
        analyze_global(db_path, goal)