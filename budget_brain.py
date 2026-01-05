import sys
import sqlite3
import os
import time
import io

# --- 1. RITHMISI ENCODING ---
sys.stdin = io.TextIOWrapper(sys.stdin.buffer, encoding='utf-8')
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding='utf-8')

# --- 2. IMPORT LIBRARY ---
try:
    from google import genai
    from google.genai import types
except ImportError:
    print("Σφάλμα: Η βιβλιοθήκη 'google-genai' λείπει.")
    sys.exit(1)

# --- 3. LOAD API KEY ---
script_dir = os.path.dirname(os.path.abspath(__file__))
key_paths = [
    os.path.join(script_dir, "statewallet", "api_key.txt"),
    os.path.join(script_dir, "api_key.txt")
]

API_KEY = None
for path in key_paths:
    try:
        with open(path, "r", encoding='utf-8') as f:
            API_KEY = f.read().strip()
        if API_KEY: break
    except:
        continue

if not API_KEY:
    print("Σφάλμα: Δεν βρέθηκε το αρχείο api_key.txt")
    sys.exit(1)

client = genai.Client(api_key=API_KEY)

# --- 4. MODEL LIST ---
MODEL_LIST = [
    "gemini-2.0-flash",
    "gemini-2.0-flash-lite",
    "gemini-1.5-flash",
    "gemini-1.5-pro"
]

def generate_safe(prompt):
    for model_name in MODEL_LIST:
        try:
            response = client.models.generate_content(
                model=model_name,
                contents=prompt
            )
            if response.text:
                return response.text.replace("```html", "").replace("```", "") # Καθαρισμός αν στείλει κώδικα
                
        except Exception as e:
            err_msg = str(e)
            if "429" in err_msg or "503" in err_msg or "ResourceExhausted" in err_msg:
                continue 
            elif "NotFound" in err_msg or "404" in err_msg:
                continue
            else:
                continue

    return "❌ ΣΦΑΛΜΑ: Κανένα μοντέλο AI δεν είναι διαθέσιμο."

# ---------------------------------------------------------
def get_summary(db_path):
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
        
        cursor.execute("SELECT name, amount FROM ypourgeia ORDER BY amount DESC LIMIT 5")
        ministries = cursor.fetchall()
        ministries_text = "".join([f"<li><b>{m[0]}</b>: {m[1]:,.0f}€</li>" for m in ministries]) # HTML List
        
        conn.close()

        deficit = total_expenses - total_income
        state = "<span style='color:green'>ΠΛΕΟΝΑΣΜΑ</span>" if deficit < 0 else "<span style='color:red'>ΕΛΛΕΙΜΜΑ</span>"
        
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
    global_context = get_summary(db_path)
    
    # print("⏳...", flush=True) # Δεν τυπώνουμε για να μην χαλάσει το HTML
    
    prompt = f"""
    Είσαι ο ψηφιακός οικονομικός σύμβουλος.
    
    ΔΕΔΟΜΕΝΑ:
    {global_context}
    
    ΛΟΓΑΡΙΑΣΜΟΣ: '{item_name}' (Ποσό: {amount}€)
    ΣΤΟΧΟΣ: "{goal}"
    
    ΟΔΗΓΙΕΣ:
    1. Απάντησε χρησιμοποιώντας **ΜΟΝΟ HTML** tags (χωρίς <html> ή <body> tags).
    2. Χρησιμοποίησε <b>για έντονα</b>, <ul><li>για λίστες</li></ul>.
    3. ΜΗΝ χρησιμοποιείς Markdown (** ή *).
    4. Αν προτείνεις μείωση, γράψτο με <span style='color:#ff5555'>κόκκινο</span>.
    5. Αν προτείνεις αύξηση/κέρδος, γράψτο με <span style='color:#50fa7b'>πράσινο</span>.
    6. Σύντομα και περιεκτικά στα Ελληνικά.
    """
    print(generate_safe(prompt))

def analyze_global(db_path, goal):
    global_context = get_summary(db_path)
    
    # print("⏳...", flush=True)

    prompt = f"""
    Είσαι ο ψηφιακός οικονομικός σύμβουλος.
    
    ΔΕΔΟΜΕΝΑ:
    {global_context}
    
    ΕΝΤΟΛΗ: "{goal}"
    
    ΟΔΗΓΙΕΣ:
    1. Απάντησε χρησιμοποιώντας **ΜΟΝΟ HTML** tags (χωρίς <html> ή <body> tags).
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
    if not sys.stdin.isatty():
        try: goal = sys.stdin.read().strip()
        except: pass
    if not goal: goal = "Γενική Ανάλυση"

    if mode == "specific":
        if len(sys.argv) >= 5: analyze_specific(db_path, sys.argv[3], sys.argv[4], goal)
    elif mode == "global":
        analyze_global(db_path, goal)