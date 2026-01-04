import sys
import sqlite3
import os
import warnings
import io
import time 

# --- 1. ΡΥΘΜΙΣΗ ENCODING ---
sys.stdin = io.TextIOWrapper(sys.stdin.buffer, encoding='utf-8')
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')
sys.stderr = io.TextIOWrapper(sys.stderr.buffer, encoding='utf-8')

warnings.filterwarnings("ignore")
os.environ["GRPC_VERBOSITY"] = "ERROR"
os.environ["GLOG_minloglevel"] = "2"

import google.generativeai as genai
from google.api_core import exceptions

# --- 2. ΦΟΡΤΩΣΗ API KEY ---
script_dir = os.path.dirname(os.path.abspath(__file__))
key_path = os.path.join(script_dir, "statewallet", "api_key.txt")

try:
    with open(key_path, "r", encoding='utf-8') as f:
        API_KEY = f.read().strip()
except FileNotFoundError:
    try:
        alt_path = os.path.join(script_dir, "api_key.txt")
        with open(alt_path, "r", encoding='utf-8') as f:
             API_KEY = f.read().strip()
    except:
        print(f"Error: Το αρχείο api_key.txt δεν βρέθηκε.")
        sys.exit(1)

genai.configure(api_key=API_KEY)

# --- 3. ΕΠΙΛΟΓΗ ΜΟΝΤΕΛΟΥ (ΚΛΕΙΔΩΜΕΝΟ ΣΤΟ FLASH) ---

try:
    model = genai.GenerativeModel('gemini-2.0-flash-001')
except:
    model = genai.GenerativeModel('gemini-2.5-flash')

# --- 4. ΛΕΙΤΟΥΡΓΙΑ "SLEEP & RETRY" ---
def generate_safe(prompt):
    """Αν φάμε πόρτα (429), περιμένουμε 20 δευτερόλεπτα και ξαναδοκιμάζουμε."""
    max_retries = 3
    for attempt in range(max_retries):
        try:
            time.sleep(3)
            response = model.generate_content(prompt)
            return response.text
        except exceptions.ResourceExhausted:
            # Εδώ ενεργοποιείται το Sleep
            print(f"⏳ Το σύστημα είναι φορτωμένο. Αναμονή 20 δευτερόλεπτα... (Προσπάθεια {attempt+1}/{max_retries})", flush=True)
            time.sleep(10) # <--- Η Παύση
        except Exception as e:
            return f"AI Error: {str(e)}"
            
    return "Το σύστημα AI είναι προσωρινά μη διαθέσιμο. Δοκιμάστε αργότερα."

# ---------------------------------------------------------

def analyze_specific(item_name, amount, goal):
    
    prompt = f"""
    Είσαι ψηφιακός οικονομικός σύμβουλος του κράτους.
    Δεδομένα: Ο λογαριασμός '{item_name}' έχει ύψος {amount} EUR.
    Στόχος Χρήστη: "{goal}".
    
    ΟΔΗΓΙΕΣ:
    1. Απάντησε ΜΟΝΟ με βάση τον στόχο.
    2. Αν ο στόχος είναι εφικτός πρότεινε συγκεκριμένη αλλαγή στο ποσό (π.χ. μείωση 10%) αλλιώς εξήγησε γιατί δεν είναι εφικτός.
    Απάντησε σύντομα αλλά και περιεκτικά στα Ελληνικά.
    """
    print(generate_safe(prompt))
    

def analyze_global(db_path, goal):
    try:
        real_path = db_path.replace("jdbc:sqlite:", "")
        if not os.path.exists(real_path):
            print(f"Error: DB not found")
            return

        conn = sqlite3.connect(real_path)
        cursor = conn.cursor()
        
        cursor.execute("SELECT SUM(amount) FROM esoda")
        res = cursor.fetchone(); total_income = res[0] if res and res[0] else 0
        cursor.execute("SELECT SUM(amount) FROM eksoda")
        res = cursor.fetchone(); total_expenses = res[0] if res and res[0] else 0
        cursor.execute("SELECT name, amount FROM ypourgeia ORDER BY amount DESC LIMIT 6")
        top_ministries = cursor.fetchall()
        ministries_text = "\n".join([f"- {m[0]}: {m[1]:,.0f}€" for m in top_ministries])
        conn.close()

        deficit = total_expenses - total_income
        deficit_percent = (deficit / total_income * 100) if total_income > 0 else 0
        
        summary = f"""
        Συνολικά Έσοδα: {total_income:,.0f}€
        Συνολικά Έξοδα: {total_expenses:,.0f}€
        Τρέχον Έλλειμμα: {deficit:,.0f}€ ({deficit_percent:.2f}%)
        
        Τα κυριότερα Υπουργεία (Δαπάνες):
        {ministries_text}
        """

        
        prompt = f"""
        Είσαι ο ψηφιακός βοηθός του Κράτους.
        ΔΕΔΟΜΕΝΑ: {summary}
        ΕΝΤΟΛΗ ΧΡΗΣΤΗ: "{goal}"
        
        ΟΔΗΓΙΕΣ:
        1. Εστίασε ΑΠΟΚΛΕΙΣΤΙΚΑ στην εντολή "{goal}".
        2. Αν ζητείται παροχή χρημάτων, βρες από ποιο από τα Υπουργεία θα κόψεις ώστε να είναι το λιγότερο ζημιωγόνο και περισσότερο οικονομικά ορθό.
        3. Αν ο στόχος είναι εφικτός πρότεινε μέχρι 5 συγκεκριμένες κινήσεις με νούμερα αλλιώς εξήγησε γιατί δεν είναι εφικτός.
        4. Ότι αλλαγές κάνεις να προσέχεις το έλλειμμα να μη ξεπερνάει το 3%.
        """
        
        print(generate_safe(prompt)) # Χρήση της safe μεθόδου


    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    try:
        goal = sys.stdin.read().strip()
    except Exception:
        goal = ""

    if not goal: goal = "Γενική ανάλυση"

    if len(sys.argv) < 2:
        sys.exit(1)
    
    mode = sys.argv[1]

    if mode == "specific":
        if len(sys.argv) >= 4:
            analyze_specific(sys.argv[2], sys.argv[3], goal)
    elif mode == "global":
        if len(sys.argv) >= 3:
            analyze_global(sys.argv[2], goal)