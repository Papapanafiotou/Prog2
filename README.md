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
      ## UML Class Diagram

```mermaid
classDiagram
    class Accounts {
        -String DB_URL
        -int MIN_PASS_LENGTH
        +createTable()
        +createAccount(name, pass, numID)
        +getPassword(username)
        +logIn(pass1, pass2)
        +newPass(password, name)
        +validatePassword(password)
        +forgotPass(username)
        +getId(username)
    }

    class AiAdvisorDialog {
        -AiBridge aiBridge
        -String dbPath
        -JEditorPane responseArea
        -JTabbedPane tabbedPane
        +AiAdvisorDialog(parent, databasePath, recId, recName, recAmount)
        -initComponents(id, name, amount)
        -runAiTask(mode)
    }

    class AiBridge {
        -String SCRIPT_NAME
        -findScript()
        -runPythonScript(goal, args)
        +getSpecificAdvice(dbPath, name, amount, goal)
        +getGlobalStrategy(dbUrl, goal)
    }

    class BudgetGUI {
        -BudgetManager manager
        -String dbPath
        -JTable dataTable
        -DefaultTableModel tableModel
        +BudgetGUI(path)
        -loadSelectedTable()
        -updateAmount()
        -loadChangesFromDb()
        -updateBudgetUI()
    }

    class TableInfo {
        -String displayName
        -String tableName
        -String idColumnName
        +toString()
    }

    class BudgetManager {
        -String url
        +setUrl(dbUrl)
        +printTable(tableName, idColumnName)
        +updateAmount(tableName, idColName, id, newAmount)
        +showChanges()
        +getTotal(tablename)
        +getBudgetCharacterism(revenue, expenses)
        +getCurrentAmount(tableName, idColName, id)
        +getNameById(tableName, idColName, id)
    }

    class BudgetMenu {
        -DatabaseChooser chooser
        -String url
        -BudgetManager manager
        +start()
        -printMenuOptions()
        -processChoice(choice)
        -handleCharacterism()
        -handleAiSpecific()
        -handleAiGlobal()
        -predictValue()
    }

    class Constrains {
        +negativeAmount(scanner, amount)
        +isReasonableChange(originalAmount, newAmount)
        +deficitLimit(esoda, eksoda)
    }

    class DatabaseChooser {
        +getUrl()
    }

    class DatabaseFinder {
        +findYearbase(year)
    }

    class DataforGrade {
        +getData(year)
        +chooseYear()
    }

    class EconElemGrades {
        +getGDPGrowthGrade(percent)
        +getPublicDebtGrade(percent)
        +getSurplusGrade(percent)
        +getEconomicGrade(w1, w2, w3, surplus, debt, gdp)
    }

    class EconomicsChart {
        +displayGraph(titlos, xronies, vathmoi)
        +showPieChart(names, percentages)
    }

    class EnvElemGrades {
        +getResGrade(percent)
        +getEmissionGrade(percent)
        +getRecycleGrade(percent)
        +getEnvironmentalGrade(w1, w2, w3, res, emission, recycle)
    }

    class GradeChar {
        +gdpGrowthChar(grade)
        +surplusChar(grade)
        +giniChar(grade)
        +crimeRateChar(grade)
        +edHealthChar(grade)
    }

    class Log {
        +logMenu()
        -handleCreate(scan, acc)
        -handleLogin(scan, acc)
        -handleChange(scan, acc)
    }

    class LogUi {
        -Accounts acc
        +LogUi()
        -createAccount()
        -login()
        -changePassword()
        -forgotPassword()
    }

    class MinMaX {
        -String databaseUrl
        +showMinMax()
        +getMinMax(opType, catType)
    }

    class Pdftocsv {
        +run(year)
    }

    class PercentageUI {
        +PercentageUI(manager, dbPath)
    }

    class PinakesImporter {
        -String dbUrl
        +importAll()
        -createTables(conn)
        -importEsoda(conn, csvPath)
    }

    class PredictionUI {
        -String tableName
        -int idValue
        +PredictionUI(dbPath, table, idCol, idVal, name)
        -collectDataFromYears()
    }

    class Search {
        -String url
        +searchAmount(name)
        +searchString(amount1)
        +searchTable(name2)
        +searchAmountInTable(name, tableName)
    }

    class SocElemGrades {
        +getGINIGrade(gini)
        +getCrimeGrade(percent)
        +getMentalHealthGrade(percent)
        +getSocialGrade(weights, gini, crime, mental, edu)
    }

    class StateWallet {
        +main(args)
    }

    class StateWalletLauncher {
        -JComboBox yearSelector
        -JButton startButton
        -startProcess()
    }

    class StateWalletUi {
        +main(args)
    }

    class TotalGrade {
        +getTotalGrade()
    }

    class TotalsPanel {
        -BudgetManager manager
        +updateTotals(tableName)
    }

    class Weights {
        -Scanner scanner
        +getWeight()
        +showTotalWeights(a, w1, w2, w3)
        +getAllGrades(a)
    }

    %% Relationships
    AiAdvisorDialog *-- AiBridge
    BudgetGUI *-- BudgetManager
    BudgetGUI o-- TableInfo
    BudgetMenu *-- DatabaseChooser
    BudgetMenu *-- BudgetManager
    Log ..> Accounts
    LogUi *-- Accounts
    PercentageUI ..> BudgetManager
    TotalsPanel *-- BudgetManager
