import sys
import sqlite3
import os
import google.generativeai as genai

# --- ΡΥΘΜΙΣΗ ΑΣΦΑΛΕΙΑΣ ---
# 1. Βρίσκουμε τον φάκελο που βρίσκεται ΑΥΤΟ το script (budget_brain.py)
script_dir = os.path.dirname(os.path.abspath(__file__))

# 2. Φτιάχνουμε την πλήρη διαδρομή για το api_key.txt
key_path = os.path.join(script_dir, "api_key.txt")

try:
    # 3. Ανοίγουμε το αρχείο χρησιμοποιώντας την πλήρη διαδρομή
    with open(key_path, "r", encoding='utf-8') as f:
        API_KEY = f.read().strip()
except FileNotFoundError:
    print(f"Error: Το αρχείο δεν βρέθηκε στη διαδρομή: {key_path}")
    # Χρήσιμο hint για debug:
    print(f"Βεβαιώσου ότι το api_key.txt είναι στον φάκελο: {script_dir}")
    sys.exit(1)

genai.configure(api_key=API_KEY)
model = genai.GenerativeModel('gemini-pro')

def analyze_specific(item_name, amount, goal):
    """Λειτουργία 1: Συμβουλή για συγκεκριμένο λογαριασμό."""
    prompt = f"""
    Είσαι οικονομικός σύμβουλος του κράτους.
    Δεδομένα: Ο λογαριασμός '{item_name}' έχει τρέχον ύψος {amount} EUR.
    Στόχος χρήστη: "{goal}".
    
    Δώσε μια σύντομη συμβουλή (μέχρι 3-4 γραμμές):
    1. Είναι ρεαλιστικός ο στόχος;
    2. Τι συγκεκριμένη αλλαγή προτείνεις στο ποσό;
    Απάντησε στα Ελληνικά.
    """
    try:
        response = model.generate_content(prompt)
        print(response.text)
    except Exception as e:
        print(f"AI Error: {e}")

def analyze_global(db_path, goal):
    """Λειτουργία 2: Στρατηγική με βάση όλη τη βάση δεδομένων."""
    try:
        # Καθαρισμός του path (η Java στέλνει jdbc:sqlite:...)
        real_path = db_path.replace("jdbc:sqlite:", "")
        
        if not os.path.exists(real_path):
            print(f"Error: Database file '{real_path}' not found.")
            return

        conn = sqlite3.connect(real_path)
        cursor = conn.cursor()

        # Χρήση των πινάκων που υπάρχουν στο PinakesImporter.java
        cursor.execute("SELECT SUM(amount) FROM esoda")
        res = cursor.fetchone()
        total_income = res[0] if res and res[0] else 0

        cursor.execute("SELECT SUM(amount) FROM eksoda")
        res = cursor.fetchone()
        total_expenses = res[0] if res and res[0] else 0

        # Τα 3 "πιο ακριβά" Υπουργεία
        cursor.execute("SELECT name, amount FROM ypourgeia ORDER BY amount DESC LIMIT 3")
        top_ministries = cursor.fetchall()

        conn.close()

        summary = f"""
        Σύνολο Εσόδων: {total_income:,.0f}€
        Σύνολο Εξόδων: {total_expenses:,.0f}€
        Ισοζύγιο: {total_income - total_expenses:,.0f}€
        Top Δαπάνες Υπουργείων: {', '.join([f'{m[0]} ({m[1]:,.0f}€)' for m in top_ministries])}
        """

        prompt = f"""
        Είσαι ο Υπουργός Οικονομικών. Έχεις τη γενική εικόνα:
        {summary}
        
        Ο Πρωθυπουργός σου ζητάει: "{goal}".
        
        1. Ανάλυσε αν ο στόχος είναι εφικτός.
        2. Πρότεινε 3 στρατηγικές κινήσεις.
        Απάντησε στα Ελληνικά, σοβαρά και επαγγελματικά.
        """
        
        response = model.generate_content(prompt)
        print(response.text)

    except Exception as e:
        print(f"Database/AI Error: {e}")

if __name__ == "__main__":
    # Τρόπος κλήσης από Java: python budget_brain.py [MODE] [ARGS...]
    if len(sys.argv) < 2:
        print("Error: No arguments provided")
        sys.exit(1)

    # Φροντίζουμε για το encoding στα Windows/Maven περιβάλλοντα
    if sys.stdout.encoding != 'utf-8':
        sys.stdout.reconfigure(encoding='utf-8')

    mode = sys.argv[1]

    if mode == "specific":
        # Args: mode, name, amount, goal
        analyze_specific(sys.argv[2], sys.argv[3], sys.argv[4])
    elif mode == "global":
        # Args: mode, db_path, goal
        analyze_global(sys.argv[2], sys.argv[3])
    else:
        print("Unknown mode")