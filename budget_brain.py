import sys
import sqlite3
import os
import warnings
import io

# Ρύθμιση για να διαβάζει και να γράφει UTF-8 (ΕΛΛΗΝΙΚΑ) σωστά
# Αγνοούμε την κωδικοποίηση της κονσόλας και επιβάλλουμε UTF-8 για την επικοινωνία με Java
sys.stdin = io.TextIOWrapper(sys.stdin.buffer, encoding='utf-8')
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding='utf-8')

warnings.filterwarnings("ignore")
os.environ["GRPC_VERBOSITY"] = "ERROR"
os.environ["GLOG_minloglevel"] = "2"

import google.generativeai as genai

# --- ΕΥΡΕΣΗ ΦΑΚΕΛΟΥ & ΚΛΕΙΔΙΟΥ ---
script_dir = os.path.dirname(os.path.abspath(__file__))
key_path = os.path.join(script_dir, "statewallet", "api_key.txt")

try:
    with open(key_path, "r", encoding='utf-8') as f:
        API_KEY = f.read().strip()
except FileNotFoundError:
    print(f"Error: Missing api_key.txt in {script_dir}")
    sys.exit(1)

genai.configure(api_key=API_KEY)

# --- ΕΠΙΛΟΓΗ ΜΟΝΤΕΛΟΥ ---
try:
    available_models = [m.name for m in genai.list_models() if 'generateContent' in m.supported_generation_methods]
    preferred_order = ['models/gemini-1.5-flash', 'models/gemini-1.5-pro', 'models/gemini-pro']
    selected_model_name = next((m for m in preferred_order if m in available_models), available_models[0] if available_models else None)
    
    if selected_model_name:
        model = genai.GenerativeModel(selected_model_name.replace("models/", ""))
    else:
        sys.exit(1)
except:
    model = genai.GenerativeModel('gemini-pro')

# ---------------------------------------------------------

def analyze_specific(item_name, amount, goal):
    prompt = f"""
    Είσαι ψηφιακός οικονομικός σύμβουλος του κράτους.
    Δεδομένα: Ο λογαριασμός '{item_name}' έχει ύψος {amount} EUR.
    Στόχος Χρήστη: "{goal}".
    
    ΟΔΗΓΙΕΣ:
    1. Απάντησε ΜΟΝΟ με βάση τον στόχο.
    2. Αν ο στόχος είναι εφικτός πρότεινε συγκεκριμένη αλλαγή στο ποσό (π.χ. μείωση 10%) αλλιώς εξήγησε γιατί δεν είναι εφικτός.
    Απάντησε σύντομα στα Ελληνικά.
    """
    try:
        response = model.generate_content(prompt)
        print(response.text)
    except Exception as e:
        print(f"AI Error: {e}")

def analyze_global(db_path, goal):
    try:
        real_path = db_path.replace("jdbc:sqlite:", "")
        if not os.path.exists(real_path):
            print(f"Error: DB not found at {real_path}")
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

        summary = f"""
        Έσοδα: {total_income:,.0f}€ | Έξοδα: {total_expenses:,.0f}€
        Διαφορά: {total_income - total_expenses:,.0f}€
        Δαπάνες:
        {ministries_text}
        """

        prompt = f"""
        Είσαι ο ψηφιακός βοηθός του Κράτους.
        ΔΕΔΟΜΕΝΑ: {summary}
        ΕΝΤΟΛΗ ΧΡΗΣΤΗ: "{goal}"
        
        ΟΔΗΓΙΕΣ:
        1. Εστίασε ΑΠΟΚΛΕΙΣΤΙΚΑ στην εντολή "{goal}".
        2. Αν ζητείται παροχή χρημάτων, βρες από ποιο από τα Υπουργεία θα κόψεις ώστε να είναι το λιγότερο ζημιωγώνω και περισσότερο οικονομικά ορθό.
        3. Αν ο στόχος είναι εφικτός πρότεινε μέχρι 5 συγκεκριμένες κινήσεις με νούμερα αλλιώς εξήγησε γιατί δεν είναι εφικτός.
        4. Ότι αλλαγές κάνεις να προσέχεις το έλλειμμα να μη ξεπερνάει το 3%
        """
        
        response = model.generate_content(prompt)
        print(response.text)

    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    # --- ΔΙΑΒΑΣΜΑ ΑΠΟ STDIN ---
    # Εδώ διαβάζει αυτό που στέλνει η Java "κρυφά" μέσω του σωλήνα
    try:
        goal = sys.stdin.read().strip()
    except Exception:
        goal = ""

    if not goal:
        goal = "Γενική ανάλυση"

    if len(sys.argv) < 2:
        sys.exit(1)
    
    mode = sys.argv[1]

    if mode == "specific":
        if len(sys.argv) >= 4:
            analyze_specific(sys.argv[2], sys.argv[3], goal)
    elif mode == "global":
        if len(sys.argv) >= 3:
            analyze_global(sys.argv[2], goal)