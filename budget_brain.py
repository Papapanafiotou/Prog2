import sys
import sqlite3
import os
import io
from dotenv import load_dotenv # 1. Import για το .env

# --- 1. ΡΥΘΜΙΣΗ ENCODING ---
# Εξασφαλίζει ότι τα Ελληνικά θα περάσουν σωστά από/προς την Java
sys.stdin = io.TextIOWrapper(sys.stdin.buffer, encoding='utf-8')
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding='utf-8')

# --- 2. ΕΙΣΑΓΩΓΗ ΒΙΒΛΙΟΘΗΚΗΣ ---
try:
    from google import genai
    from google.genai import types
except ImportError:
    print("Σφάλμα: Η βιβλιοθήκη 'google-genai' λείπει.")
    sys.exit(1)

# --- 3. ΦΟΡΤΩΣΗ API KEY ΑΠΟ .ENV (Ο Επαγγελματικός Τρόπος) ---
load_dotenv() # Διαβάζει το αρχείο .env
API_KEY = os.getenv("GOOGLE_API_KEY") 

if not API_KEY:
    print("Σφάλμα: Δεν βρέθηκε το GOOGLE_API_KEY στο αρχείο .env")
    sys.exit(1)

# --- 4. ΑΡΧΙΚΟΠΟΙΗΣΗ CLIENT ---
client = genai.Client(api_key=API_KEY)

# --- 5. ΛΙΣΤΑ ΜΟΝΤΕΛΩΝ ---
# Λίστα με διαθέσιμα μοντέλα για Failover (αν αποτύχει το ένα, δοκιμάζει το επόμενο)
MODEL_LIST = [
    "gemini-2.0-flash-001",
    "gemini-2.0-flash-lite-001",
    "gemini-gemini-2.5-flash-lite",
    "gemini-2.0-flash-001",
    "gemini-2.5-pro"
]

def generate_safe(prompt):
    """ 
    Στέλνει το αίτημα στο Google Gemini με μηχανισμό ασφαλείας (Failover).
    
    Δοκιμάζει τα μοντέλα της λίστας MODEL_LIST σειριακά. Αν κάποιο μοντέλο
    επιστρέψει σφάλμα (π.χ. 429 Too Many Requests ή 503 Service Unavailable),
    η συνάρτηση δοκιμάζει αυτόματα το επόμενο.
    
    Args:
        prompt (str): Το κείμενο της εντολής προς το AI.
        
    Returns:
        str: Η απάντηση του AI καθαρισμένη από Markdown tags, έτοιμη για HTML render.
    """
    for model_name in MODEL_LIST:
        try:
            response = client.models.generate_content(
                model=model_name,
                contents=prompt
            )
            # Καθαρισμός από Markdown ```html αν το στείλει το AI
            if response.text:
                return response.text.replace("```html", "").replace("```", "").strip()
                
        except Exception as e:
            err_msg = str(e)
            # Εκτύπωση του πραγματικού σφάλματος στο stderr για να το δεις στην κονσόλα
            print(f"⚠️ Αποτυχία στο {model_name}: {err_msg}", file=sys.stderr)
            if "429" in err_msg or "503" in err_msg or "ResourceExhausted" in err_msg:
                continue 
            elif "NotFound" in err_msg or "404" in err_msg:
                continue
            else:
                continue

    return "<b>❌ ΣΦΑΛΜΑ:</b> Κανένα μοντέλο AI δεν είναι διαθέσιμο."

# ---------------------------------------------------------
def get_summary(db_path):
    """ 
    Ανακτά συνοπτικά οικονομικά δεδομένα από τη βάση SQLite και τα επιστρέφει ως HTML.
    
    Args:
        db_path (str): Το μονοπάτι της βάσης δεδομένων.
        
    Returns:
        str: HTML κώδικας (λίστες <ul>, <li>) με τα σύνολα εσόδων/εξόδων και τα top υπουργεία.
    """
    try:
        real_path = db_path.replace("jdbc:sqlite:", "")
        if not os.path.exists(real_path):
            return f"Σφάλμα: Δεν βρέθηκε η βάση: {real_path}"

        conn = sqlite3.connect(real_path)
        cursor = conn.cursor()
        
        cursor.execute("SELECT SUM(amount) FROM esoda")
        res = cursor.fetchone(); total_income = res[0] if res and res[0] else 0
        
        cursor.execute("SELECT SUM(amount) FROM eksoda")
        res = cursor.fetchone(); total_expenses = res[0] if res and res[0] else 0
        
        # HTML Λίστα για τα υπουργεία
        cursor.execute("SELECT name, amount FROM ypourgeia ORDER BY amount DESC LIMIT 5")
        ministries = cursor.fetchall()
        ministries_text = "".join([f"<li><b>{m[0]}</b>: {m[1]:,.0f}€</li>" for m in ministries])
        
        conn.close()

        deficit = total_expenses - total_income
        state = "<span style='color:#50fa7b'>ΠΛΕΟΝΑΣΜΑ</span>" if deficit < 0 else "<span style='color:#ff5555'>ΕΛΛΕΙΜΜΑ</span>"
        
        return f"""
        <ul>
            <li>Συνολικά Έσοδα: <b>{total_income:,.0f}€</b></li>
            <li>Συνολικά Έξοδα: <b>{total_expenses:,.0f}€</b></li>
            <li>Κατάσταση: {state} ύψους <b>{abs(deficit):,.0f}€</b></li>
        </ul>
        <br>
        <b>ΚΥΡΙΟΤΕΡΕΣ ΔΑΠΑΝΕΣ:</b>
        <ul>{ministries_text}</ul>
        """
    except Exception as e:
        return f"Σφάλμα Βάσης: {str(e)}"

def analyze_specific(db_path, item_name, amount, goal):
    """
    Δημιουργεί Prompt για ανάλυση συγκεκριμένου λογαριασμού και ζητά HTML απάντηση.
    """
    global_context = get_summary(db_path)
    
    # Ζητάμε HTML output από το AI
    prompt = f"""
    Είσαι ο ψηφιακός οικονομικός σύμβουλος.
    
    ΔΕΔΟΜΕΝΑ:
    {global_context}
    
    ΛΟΓΑΡΙΑΣΜΟΣ: '{item_name}' (Ποσό: {amount}€)
    ΣΤΟΧΟΣ: "{goal}"
    
    ΟΔΗΓΙΕΣ:
    1. Απάντησε χρησιμοποιώντας **ΜΟΝΟ HTML** tags (χωρίς <html> ή <body>).
    2. Χρησιμοποίησε <b>για έντονα</b>, <ul><li>για λίστες</li></ul>.
    3. ΜΗΝ χρησιμοποιείς Markdown (** ή *).
    4. Αν προτείνεις μείωση, γράψτο με <span style='color:#ff5555'>κόκκινο</span>.
    5. Αν προτείνεις αύξηση/κέρδος, γράψτο με <span style='color:#50fa7b'>πράσινο</span>.
    6. Σύντομα και περιεκτικά στα Ελληνικά.
    """
    print(generate_safe(prompt))

def analyze_global(db_path, goal):
    """
    Δημιουργεί Prompt για τη γενική στρατηγική και ζητά HTML απάντηση.
    """
    global_context = get_summary(db_path)
    
    prompt = f"""
    Είσαι ο ψηφιακός οικονομικός σύμβουλος.
    
    ΔΕΔΟΜΕΝΑ:
    {global_context}
    
    ΕΝΤΟΛΗ: "{goal}"
    
    ΟΔΗΓΙΕΣ:
    1. Απάντησε χρησιμοποιώντας **ΜΟΝΟ HTML** tags (χωρίς <html> ή <body>).
    2. Χρησιμοποίησε <h3> για τίτλους, <b> για έντονα.
    3. Χρησιμοποίησε <ol><li> για τα βήματα στρατηγικής.
    4. ΜΗΝ χρησιμοποιείς Markdown (**).
    5. Βάλε τα ποσά σε <b>bold</b>.
    6. Σύντομα στα Ελληνικά.
    """
    print(generate_safe(prompt))

if __name__ == "__main__":
    if len(sys.argv) < 3: sys.exit(1)
    
    mode = sys.argv[1]
    db_path = sys.argv[2]
    
    goal = ""
    # Ανάγνωση του στόχου από το stdin (μέσω pipe από την Java)
    if not sys.stdin.isatty():
        try: goal = sys.stdin.read().strip()
        except: pass
    if not goal: goal = "Γενική Ανάλυση"

    if mode == "specific":
        if len(sys.argv) >= 5:
            analyze_specific(db_path, sys.argv[3], sys.argv[4], goal)
    elif mode == "global":
        analyze_global(db_path, goal)