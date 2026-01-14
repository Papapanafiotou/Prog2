# StateWallet
## 1. Περιγραφή & Σκοπός
* **Στόχος:** Εφαρμογή διαχείρισης κρατικού προυπολογισμού.
* **Λειτουργικότητα:**
    * Σύστημα διαχείρισης χρηστών
    * Εξαγωγή δεδομένων από PDF
    * Δημιουργία κατάλληλων CSV και SQL αρχείων
    * Επεξεργασία δεδομένων (εμφάνιση ποσών, αλλαγή ποσών, εμφάνιση αλλαγών)
    * Αποθήκευση δεδομένων στη βάση
    *   **ΠΡΟΣΘΕΤΕΣ ΛΕΙΤΟΥΡΓΙΕΣ :**
        * Χαρακτηρισμός προυπολογισμού
        * Εμφάνιση συνόλων
        * Εμφάνιση ποσοστών
        * Εμφάνιση μεγίστου-ελαχίστου
        * Προβλέψεις επόμενου έτους
        * AI σύμβουλος
        * Σύστημα βαθμολόγησης του κράτους
        * Ανάπτυξη GUI
          
## 2. Τεχνολογίες που χρησιμοποιήθηκαν
* **Γλώσσα:** Java 17, Python 3.9
* **Framework:** Java Swing, JavaFx
* **Βάση Δεδομένων:** SQLite
* **Άλλα εργαλία:** Git, Maven, Java Checkstyle, JaCoCo

## 3. Οδηγίες Εγκατάστασης (Installation)
Ακολουθήστε τα παρακάτω βήματα για τοπική εκτέλεση:

1. Αντιγραφή αποθετηρίου:
   git clone git@github.com:Papapanafiotou/Prog2.git
2. Εκτέλεση του setup_to_run.bat (για windows)
3. Για την εκτέλεση της λειτουργίας του AI σύμβουλου απαιτείται:
    * Δημιουργία API_Key από το Google AI Studio
    * Χρήση μεταβλητής περιβάλλοντος για τη λειτουργία του API_Key
      
## 4. Οδηγίες Μεταγλώττισης
   mvn compile
     
## 5. Οδηγίες Εκτέλεσης
   Για εκκίνηση του προγράμματος από την γραμμή εντολών: java -cp target/statewallet.jar mainapp.StateWallet<br />
   Για εκκίνηση του προγράμματος από την γραφική διεπαφή: java -cp target/statewallet.jar mainapp.StateWalletUi<br />
   Για έλεγχο Checkstyle : mvn checkstyle:check<br />

## 6. Οδηγίες χρήσης της εφαρμογής   
   Κατά την είσοδό σας στην εφαρμογή, είτε δημιουργείτε λογαριασμό, είτε συνδέεστε σε έναν ήδη υπάρχοντα. Περιλαμβάνεται σύστημα ανάκτησης κωδικού. Όταν θέλετε να δημιουργήσετε νέο κωδικό, θα πρέπει να περιλαμβάνει τουλάχιστον ένα κεφαλαίο γράμμα, έναν αριθμό, έναν ειδικό χαρακτήρα και είναι τουλάχιστον 8 ψηφία. Έπειτα επιλέγετε έτος επεξεργασίας και αν έχει γίνει ήδη επεξεργασία,         επιλέγετε αν θα ξεκινήσετε από την αρχή ή όχι. Στη συνέχεια εμφανίζεται το βασικό μενού επιλογών με όλες τις λειτουργίες της εφαρμογής όπου εσείς επιλέγεται ποια σας ενδιαφέρει. (Για την αλλαγή έτους στο UI υπάρχει         ειδικό κουμπί "Πίσω")

## 7. Δομή περιεχομένων αποθετηρίου
   src/main: εδώ βρίσκεται ο πηγαίος κώδικας, τα αρχεία PDF και CSV, ένα python script και τα test.<br />
   Στον κύριο φάκελο με όνομα statewallet, βρίσκεται οι βάσεις δεδομένων και άλλο ενα python script και το pom.xml.<br />
   Όλα αυτά περιλαμβάνονται στο main branch, ενώ έχουν δημιουργηθεί 28 branches όπου αναπτύχθηκαν και δοκιμάστηκαν οι λειτουργίες πρωτού ενσωματωθούν στο main branch.<br />
   

## 8. Δομή δεδομένων
  Για τις ανάγκες της εφαρμογής, αξιοποιήθηκαν βασικές δομές δεδομένων της Java, όπως Lists και Maps, οι οποίες προσφέρουν αποδοτική διαχείριση των αντικειμένων στη μνήμη κατά τη διάρκεια της εκτέλεσης.
  
  Αλγόριθμοι και Ροή Δεδομένων:
  
  * **File Parsing και Pattern matching:** Υλοποιήθηκε αλγόριθμος συντακτικής ανάλυσης για την εξαγωγή οικονομικών στοιχείων (προϋπολογισμού) απευθείας από αρχεία PDF.
  * **File management**: Υλοποιήθηκε αλγόριθμος διαχείρισης αρχείων για την κατάλληλη ονομασίας τους ώστε να περνάνε σωστά ως ορίσματα στις μεθόδους.
  * **Data Transformation Pipeline:** Τα δεδομένα υφίστανται μετασχηματισμό από μορφή *PDF* σε ενδιάμεση μορφή *CSV* και, εν τέλει, αποθηκεύονται στη βάση δεδομένων *(SQLite)* για μόνιμη διατήρηση.
  * **ETL (Extract, Transform, Load):** Τα δεδομένα εξάγονται από τις βάσεις δεδομένων, επεξεργάζονται και ξαναεισάγονται για αποθήκευση.
  * **Authentication Workflow:** Υλοποιήθηκε πλήρης σύστημα διαχείρισης χρηστών για την εγγραφή και σύνδεσή τους στην εφαρμογή.
  * **Αναζήτηση:** Εφαρμόζεται αλγόριθμος Σειριακής Αναζήτησης για τον εντοπισμό εγγραφών μέσα στις δομές δεδομένων.
  * **Έλεγχος Εγκυρότητας:** Χρησιμοποιούνται αλγόριθμοι ελέγχου περιορισμών για να διασφαλιστεί ότι τα δεδομένα πληρούν τις απαραίτητες προϋποθέσεις πριν την εισαγωγή τους στο σύστημα.
  * **Linear Regression:** Χρησιμοποιείται αλγόριθμος **γραμμικής παλινδρόμισης** για την πρόβλεψη τιμών επόμενου έτους.
  * **Part of Whole / Ratio:** Υλοποιήθηκε αλγόριθμος για υπολογισμό ποσοστών των στοιχείων του προϋπολογισμού σε σχέση με το σύνολο.
  * **Summation:** Υλοποιήθηκε αλγόριθμος για τον υπολογισμό συνόλων στοιχείων του προϋπολογισμού.
  * **MinMax:** Υλοποιήθηκε αλγόριθμος για την εύρεση **Μεγίστου** ή **Ελαχίστου** σε στοιχεία του προϋπολογισμου.
  * **API Integration:** Υλοποιήθηκε η ένταξη GoogleAI-Studio API.

## 9. Τεχνική τεκμηρίωση
   Κάλυψη απο JavaDoc<br />
   Java Checkstyle errors: 0<br />
   Κάλυψη από JaCoCo<br />

## 10. Διάγραμμα UML
     

```mermaid
classDiagram
    class Accounts {
        -String DB_URL
        +createAccount()
        +logIn()
        +validatePassword()
    }

    class AiAdvisorDialog {
        -AiBridge aiBridge
        +AiAdvisorDialog()
        -runAiTask()
    }

    class AiBridge {
        -String SCRIPT_NAME
        +getSpecificAdvice()
        +getGlobalStrategy()
    }

    class BudgetGUI {
        -BudgetManager manager
        -JTable dataTable
        +BudgetGUI()
        -updateBudgetUI()
    }

    class TableInfo {
        -String displayName
        +toString()
    }

    class BudgetManager {
        -String url
        +printTable()
        +updateAmount()
        +getTotal()
        +getBudgetCharacterism()
    }

    class BudgetMenu {
        -DatabaseChooser chooser
        -BudgetManager manager
        +start()
        -processChoice()
    }

    class Constrains {
        +negativeAmount()
        +isReasonableChange()
    }

    class DatabaseChooser {
        +getUrl()
    }

    class DataforGrade {
        +getData()
    }

    class EconElemGrades {
        +getGDPGrowthGrade()
        +getEconomicGrade()
    }

    class EconomicsChart {
        +displayGraph()
        +showPieChart()
    }

    class EnvElemGrades {
        +getResGrade()
        +getEnvironmentalGrade()
    }

    class GradeChar {
        +gdpGrowthChar()
    }

    class Log {
        +logMenu()
    }

    class LogUi {
        -Accounts acc
        +LogUi()
    }

    class MinMaX {
        +showMinMax()
    }

    class Pdftocsv {
        +run()
    }

    class PercentageUI {
        +PercentageUI()
    }

    class PinakesImporter {
        +importAll()
    }

    class PredictionUI {
        +PredictionUI()
        -collectDataFromYears()
    }

    class Search {
        +searchAmount()
        +searchTable()
    }

    class SocElemGrades {
        +getSocialGrade()
    }

    class StateWallet {
        +main()
    }

    class StateWalletLauncher {
        -JButton startButton
        -startProcess()
    }

    class TotalsPanel {
        -BudgetManager manager
        +updateTotals()
    }

    class Weights {
        +getWeight()
        +getAllGrades()
    }

    %% --- ΣΥΝΔΕΣΕΙΣ (RELATIONSHIPS) ---
    %% Βασική ροή
    StateWallet ..> StateWalletLauncher : launches
    StateWalletLauncher ..> LogUi : opens
    LogUi ..> BudgetMenu : on success
    LogUi *-- Accounts

    %% BudgetMenu Συνδέσεις (Το κέντρο ελέγχου)
    BudgetMenu *-- BudgetManager
    BudgetMenu *-- DatabaseChooser
    BudgetMenu ..> Search : uses
    BudgetMenu ..> MinMaX : uses
    BudgetMenu ..> PredictionUI : opens
    BudgetMenu ..> PercentageUI : opens
    BudgetMenu ..> AiAdvisorDialog : opens
    BudgetMenu ..> GradeChar : uses
    BudgetMenu ..> EconomicsChart : shows
    BudgetMenu ..> Pdftocsv : calls
    BudgetMenu ..> PinakesImporter : calls

    %% Υποσυστήματα
    AiAdvisorDialog *-- AiBridge
    BudgetGUI *-- BudgetManager
    BudgetGUI o-- TableInfo
    TotalsPanel *-- BudgetManager
    PercentageUI ..> BudgetManager
    Log ..> Accounts
    
    %% Grades Logic
    Weights ..> EconElemGrades : uses
    Weights ..> SocElemGrades : uses
    Weights ..> EnvElemGrades : uses
    Weights ..> DataforGrade : reads
