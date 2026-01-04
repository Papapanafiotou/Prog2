package mainapp;
import java.util.Scanner;

/**
 * Το μενού κονσόλας της εφαρμογής.
 */
public final class BudgetMenu {

    /** Επιλογή: Εμφάνιση. */
    private static final int OPTION_SHOW = 1;
    /** Επιλογή: Αλλαγή. */
    private static final int OPTION_CHANGE = 2;
    /** Επιλογή: Λίστα Αλλαγών. */
    private static final int OPTION_CHANGES_LIST = 3;
    /** Επιλογή: Σύνολο. */
    private static final int OPTION_TOTAL = 4;
    /** Επιλογή: Αλλαγή Έτους. */
    private static final int OPTION_YEAR = 5;
    /** Επιλογή: Αναζήτηση. */
    private static final int OPTION_SEARCH = 6;
    /** Επιλογή: Χαρακτηρισμός. */
    private static final int OPTION_CHAR = 7;
    /** Επιλογή: Έξοδος. */

    private static final int OPTION_MINMAX = 8;

    private static final int OPTION_PERCENTANCE = 9;

    private static final int OPTION_AI_SPECIFIC = 10;
    
    private static final int OPTION_AI_GLOBAL = 11;

    private static final int OPTION_EXIT = 12;


    /** Πίνακας: Έσοδα. */
    private static final int TABLE_ESODA = 1;
    /** Πίνακας: Έξοδα. */
    private static final int TABLE_EKSODA = 2;
    /** Πίνακας: Κράτος. */
    private static final int TABLE_KRATOS = 3;
    /** Πίνακας: Υπουργεία. */
    private static final int TABLE_YPOURGEIA = 4;
    /** Πίνακας: Αποκεντρωμένες. */
    private static final int TABLE_APOK = 5;
    /** Πίνακας: Όλα. */
    private static final int TABLE_ALL = 6;
    /** Πίνακας: Πίσω. */
    private static final int TABLE_BACK = 7;

    /** Επιλογή διαχείρισης βάσης. */
    private final DatabaseChooser chooser = new DatabaseChooser();
    /** URL βάσης δεδομένων. */
    private String url;
    /** Διαχειριστής προϋπολογισμού. */
    private final BudgetManager manager;
    /** Scanner εισόδου. */
    private final Scanner scanner;

    /**
     * Κατασκευαστής.
     *
     * @param dbUrl Το URL της βάσης δεδομένων.
     */
    public BudgetMenu(final String dbUrl) {
        this.url = dbUrl;
        this.manager = new BudgetManager(dbUrl);
        // Αναγκάζουμε τον Scanner να διαβάζει UTF-8, όπως το τερματικό του VS Code
        this.scanner = new Scanner(System.in , "CP737");
    }

    /**
     * Ξεκινάει το μενού επιλογών.
     */
    public void start() {
        while (true) {
            System.out.println("\n-------------------------------------------");
            System.out.println("Επιλέξτε μία από τις παρακάτω λειτουργίες");
            System.out.println("1. Εμφάνιση στοιχείων");
            System.out.println("2. Αλλαγή στοιχείου");
            System.out.println("3. Εμφάνιση αλλαγών");
            System.out.println("4. Εμφάνιση συνόλου");
            System.out.println("5. Αλλαγή έτους");
            System.out.println("6. Αναζήτηση στοιχείου");
            System.out.println("7. Χαρακτηρισμός προϋπολογισμού");
            System.out.println("8. Εμφάνιση Μέγιστου-Ελάχιστου");
            System.out.println("9. Εμφάνιση Ποσοστού σε σχέση με σύνολο");
            System.out.println("10. Χρήση AI για συγκεκριμένο λογαριασμό");
            System.out.println("11. Χρήση AI για πιο γενική αναφορά");
            System.out.println("12. Έξοδος");
            System.out.print("Επιλογή: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case OPTION_SHOW -> showBudgetSelection();
                case OPTION_CHANGE -> changeBudget();
                case OPTION_CHANGES_LIST -> manager.showChanges();
                case OPTION_TOTAL -> showTotalSelection();
                case OPTION_YEAR -> {
                    String newURL = chooser.getUrl();
                    this.url = newURL;
                    manager.setUrl(newURL);
                }
                case OPTION_SEARCH -> {
                    Search s = new Search(url);
                    System.out.println("Εισάγετε το στοιχείο αναζήτησης");
                    String name = scanner.nextLine().trim();
                    s.searchAmount(name);
                }
                case OPTION_CHAR -> handleCharacterism();
                case OPTION_EXIT -> {
                    System.out.println("Έξοδος...");
                    scanner.close();
                    return;
                }
                case OPTION_AI_SPECIFIC -> handleAiSpecific();
                case OPTION_AI_GLOBAL -> handleAiGlobal();
                case OPTION_MINMAX -> {
                    MinMaX minmaxfinder = new MinMaX(url);
                    minmaxfinder.showMinMax();
                }
                case OPTION_PERCENTANCE -> getPrecentage();
                default -> System.out.println("Λάθος επιλογή.");
            }
        }
    }

    private void handleCharacterism() {
        System.out.println("Θέλετε χαρακτηρισμό στα αρχικά ή επεξεργασμένα;");
        System.out.println("(1 για αρχικά 2 για επεξεργασμένα)");
        System.out.print("Επιλογή: ");
        int cho = scanner.nextInt();
        scanner.nextLine();
        double[] revenue = manager.getTotal("esoda");
        double[] expenses = manager.getTotal("eksoda");
        if (cho == 1) {
            System.out.println(manager.getBudgetCharacterism(
                    revenue[0], expenses[0]));
        } else {
            System.out.println(manager.getBudgetCharacterism(
                    revenue[1], expenses[1]));
        }
    }

    private void showBudgetSelection() {
        System.out.println("\nΠοιον πίνακα θέλετε να δείτε;");
        System.out.println("1. Έσοδα");
        System.out.println("2. Έξοδα");
        System.out.println("3. Κράτος");
        System.out.println("4. Υπουργεία");
        System.out.println("5. Αποκεντρωμένες Διοικήσεις");
        System.out.println("6. Όλα");
        System.out.println("7. Πίσω");
        System.out.print("Επιλογή: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case TABLE_ESODA -> manager.printTable("esoda", "code");
            case TABLE_EKSODA -> manager.printTable("eksoda", "code");
            case TABLE_KRATOS -> manager.printTable("kratos", "number");
            case TABLE_YPOURGEIA -> manager.printTable("ypourgeia", "number");
            case TABLE_APOK -> manager.printTable("apokentromenes", "number");
            case TABLE_ALL -> {
                manager.printTable("esoda", "code");
                manager.printTable("eksoda", "code");
                manager.printTable("kratos", "number");
                manager.printTable("ypourgeia", "number");
                manager.printTable("apokentromenes", "number");
            }
            case TABLE_BACK -> {
                return;
            }
            default -> System.out.println("Λάθος επιλογή.");
        }
    }

    private void changeBudget() {
        System.out.println("\nΕπιλέξτε πίνακα:");
        System.out.println("1. Έσοδα");
        System.out.println("2. Έξοδα");
        System.out.println("3. Κράτος");
        System.out.println("4. Υπουργεία");
        System.out.println("5. Αποκεντρωμένες Διοικήσεις");
        System.out.print("Επιλογή: ");

        int tableChoice = scanner.nextInt();
        scanner.nextLine();

        String tableName;
        String idColName;

        switch (tableChoice) {
            case TABLE_ESODA -> {
                tableName = "esoda";
                idColName = "code";
            }
            case TABLE_EKSODA -> {
                tableName = "eksoda";
                idColName = "code";
            }
            case TABLE_KRATOS -> {
                tableName = "kratos";
                idColName = "number";
            }
            case TABLE_YPOURGEIA -> {
                tableName = "ypourgeia";
                idColName = "number";
            }
            case TABLE_APOK -> {
                tableName = "apokentromenes";
                idColName = "number";
            }
            default -> {
                System.out.println("Άκυρη επιλογή.");
                return;
            }
        }

        try {
            System.out.print("Δώσε το ID (" + idColName + "): ");
            int id = Integer.parseInt(scanner.nextLine());

            // εδω παιρνουμε το αρχικο ποσο του λογαριασμου //
            double oldAmount = manager.getCurrentAmount(tableName, idColName, id);

            if (oldAmount == -1) {
                System.out.println("Το ID δεν βρέθηκε.");
                return;
            }
            
            System.out.print("Δώσε το νέο ποσό: ");
            double newAmount = Double.parseDouble(scanner.nextLine());

            newAmount = Constrains.negativeAmount(scanner, newAmount);
            boolean success = manager.updateAmount(
                    tableName, idColName, id, newAmount);

            // Αν η αλλαγη ξεπερνα το 50% ρωτατε ο χρηστης αν θελει να συνεχισει σε αυτη την αλλαγη //

            if (!Constrains.isReasonableChange(oldAmount, newAmount)){
                System.out.println("ΠΡΟΣΟΧΗ! Η αλλαγή που επιθυμείτε να κάνετε υπερβαίνει το 50% του αρχικού ποσού.");
                System.out.println("Αν εξακολουθείτε να επιθυμείτε να αλλάξετε το ποσό με αυτόν τον τρόπο πληκτρολογήστε 1");
                int confirm = scanner.nextInt();
                scanner.nextLine(); 
                if (confirm != 1){
                    return;
                }
            }

            // Ελεγχος για ελλειμα μεγαλυτερο του 3% //
            
            double[] rev = manager.getTotal("esoda");
            double[] exp = manager.getTotal("eksoda");
            if (!Constrains.deficitLimit(rev[1], exp[1])) {
                System.out.println("Η αλλαγή αυτή οδηγεί σε έλλειμα μεγαλύτερο του 3% που είναι το επιτρεπτό. Δεν γίνεται να συνεχίσετε.)");
                return;
            }
            
            success = manager.updateAmount(tableName, idColName, id, newAmount);
            
            if (success) System.out.println("Επιτυχής ενημέρωση!");
            else System.out.println("Αποτυχία: Δεν βρέθηκε το ID.");

        } catch (NumberFormatException e) {
            System.out.println("Λάθος είσοδος (μόνο αριθμοί).");
        }
    }

    private void showTotalSelection() {
        System.out.println("\nΠοιανού πίνακα θέλετε να δείτε το σύνολο ;");
        System.out.println("1. Έσοδα");
        System.out.println("2. Έξοδα");
        System.out.println("3. Κράτος");
        System.out.println("4. Υπουργεία");
        System.out.println("5. Αποκεντρωμένες Διοικήσεις");
        System.out.print("Επιλογή: ");
        double[] results;
        int tableChoice = scanner.nextInt();
        scanner.nextLine();
        switch (tableChoice) {
            case TABLE_ESODA -> {
                results = manager.getTotal("esoda");
            }
            case TABLE_EKSODA -> {
                results = manager.getTotal("eksoda");
            }
            case TABLE_KRATOS -> {
                results = manager.getTotal("kratos");
            }
            case TABLE_YPOURGEIA -> {
                results = manager.getTotal("ypourgeia");
            }
            case TABLE_APOK -> {
                results = manager.getTotal("apokentromenes");
            }
            default -> {
                System.out.println("Άκυρη επιλογή.");
                return;
            }
        }
        System.out.println("--- Αποτελέσματα για πίνακα: " + tableChoice
                + " ---");
        System.out.printf("Συνολικό Ποσό(αρχικό): %,.2f%n", results[0]);
        System.out.printf("Συνολικό Ποσό(επεξεργασμένο): %,.2f%n", results[1]);
    }

        public double getPrecentage() {
            double precent = 0.0;
            System.out.println("Για ποιον λογαριασμό θέλετε να υπολογίσετε " +
             "το ποσοστό;");
            String name = scanner.nextLine();
            Search search = new Search(url);
            String table = search.searchTable(name);
            double[] total;
            double amount = search.searchAmount(name);
            if (table == "esoda") {
                total = manager.getTotal("esoda");
                double t = total[0];
            try {
                precent = (amount / t) * 100;
                System.out.println("Το ποσοστό του " + name +
                " στα συνολικά έσοδα έιναι " + precent + " %");
            } catch (ArithmeticException e) {
                System.out.println("Δεν είναι δυνατή η διαίρεση με το μηδέν!");
            }
            } else if (table == "eksoda") {
            total = manager.getTotal("eksoda");
            double t = total[0];
            try {
                precent = (amount / t) * 100;
                System.out.println("Το ποσοστό του " + name +
                " στα συνολικά έξοδα έιναι " + precent + " %");
            } catch (ArithmeticException e) {
                System.out.println("Δεν είναι δυνατή η διαίρεση με το μηδέν!");
            }
        } 
        return precent;
    }

    // Στο BudgetMenu.java

private void handleAiSpecific() {
    System.out.println("\n--- AI Σύμβουλος για συγκεκριμένο λογαριασμό ---");
    System.out.println("Επιλέξτε πίνακα:");
    System.out.println("1. Έσοδα");
    System.out.println("2. Έξοδα");
    System.out.println("3. Κράτος");
    System.out.println("4. Υπουργεία");
    System.out.print("Επιλογή: ");

    int tableChoice = scanner.nextInt();
    scanner.nextLine(); // Καθαρισμός buffer

    String tableName;
    String idColName;

    // Αντιστοίχιση επιλογής με ονόματα πινάκων (όπως στο changeBudget)
    switch (tableChoice) {
        case 1 -> { tableName = "esoda"; idColName = "code"; }
        case 2 -> { tableName = "eksoda"; idColName = "code"; }
        case 3 -> { tableName = "kratos"; idColName = "number"; }
        case 4 -> { tableName = "ypourgeia"; idColName = "number"; }
        default -> {
            System.out.println("Άκυρη επιλογή πίνακα.");
            return;
        }
    }

    System.out.print("Δώσε το ID (" + idColName + "): ");
    if (!scanner.hasNextInt()) {
        System.out.println("Πρέπει να δώσετε αριθμό!");
        scanner.nextLine();
        return;
    }
    int id = scanner.nextInt();
    scanner.nextLine();

    // 1. Βρίσκουμε το όνομα από το ID (Νέα μέθοδος)
    String name = manager.getNameById(tableName, idColName, id);
    
    // 2. Βρίσκουμε το ποσό από το ID (Υπάρχουσα μέθοδος)
    double amount = manager.getCurrentAmount(tableName, idColName, id);

    if (name == null || amount == -1) {
        System.out.println("Δεν βρέθηκε εγγραφή με αυτό το ID.");
        return;
    }

    System.out.println("------------------------------------------------");
    System.out.println("Επιλέξατε: " + name);
    System.out.printf("Τρέχον Ποσό: %,.2f €\n", amount);
    System.out.println("------------------------------------------------");
    
    
    System.out.println("Τι θέλετε να πετύχετε; ...");
    System.out.print("Στόχος: ");
    String goal = scanner.nextLine();

    System.out.println("Ο ψηφιακός βοηθός σκέφτεται...");
    AiBridge ai = new AiBridge();
    // Στέλνουμε το όνομα που βρήκαμε αυτόματα από τη βάση!
    System.out.println(ai.getSpecificAdvice(name, amount, goal));
}

    private void handleAiGlobal() {
        System.out.println("\n--- AI Στρατηγικός Σχεδιασμός για γενική βοήθεια σε επίτευξη στόχων ---");
        System.out.println("Το AI θα μελετήσει τα σύνολα εσόδων/εξόδων και τα Υπουργεία.");
        System.out.println("Ποιο είναι το όραμά σας; (π.χ. 'Θέλω να μηδενίσω το έλλειμμα')");
        System.out.print("Στόχος: ");
        String goal = scanner.nextLine();
        
        System.out.println("Ανάλυση βάσης δεδομένων...");
        AiBridge ai = new AiBridge();
        // Περνάμε το url (π.χ. jdbc:sqlite:budget_2024.db) που έχει το BudgetMenu
        System.out.println(ai.getGlobalStrategy(this.url, goal));
    }
}

