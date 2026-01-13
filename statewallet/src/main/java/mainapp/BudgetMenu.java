package mainapp;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
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
    /** Επιλογή: Μέγιστο-Ελάχιστο. */
    private static final int OPTION_MINMAX = 8;
    /** Επιλογή: Ποσοστό. */
    private static final int OPTION_PERCENTAGE = 9;
    /** Επιλογή: AI Specific. */
    private static final int OPTION_AI_SPECIFIC = 10;
    /** Επιλογή: AI Global. */
    private static final int OPTION_AI_GLOBAL = 11;
    /** Επιλογή: Πρόβλεψη 2027. */
    private static final int OPTION_PREDICT = 12;
    /** Επιλογή: Σύστημα βαθμολόγησης. */
    private static final int OPTION_GRADE = 13;
    /** Επιλογή: Έξοδος. */
    private static final int OPTION_EXIT = 14;

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

    /** Έτος έναρξης ιστορικών δεδομένων. */
    private static final int START_YEAR = 2023;
    /** Έτος λήξης ιστορικών δεδομένων. */
    private static final int END_YEAR = 2026;
    /** Έτος πρόβλεψης. */
    private static final int PREDICT_YEAR = 2027;
    /** Επιλογή χρήστη: ΝΑΙ (1). */
    private static final int CHOICE_YES = 1;
    /** Πολλαπλασιαστής ποσοστού. */
    private static final int PERCENT_MULTIPLIER = 100;

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
        // Αναγκάζουμε τον Scanner να διαβάζει CP737 (Ελληνικά DOS)
        this.scanner = new Scanner(System.in, "CP737");
    }

    /**
     * Ξεκινάει το μενού επιλογών.
     */
    public void start() {
        while (true) {
            printMenuOptions();
            System.out.print("Επιλογή: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Μη έγκυρη είσοδος.");
                scanner.nextLine();
                continue;
            }

            if (processChoice(choice)) {
                return;
            }
        }
    }
    /**
     * Εμφάνιση μενού επιλογών.
     */
    private void printMenuOptions() {
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
        System.out.println("12. Πρόβλεψη Τιμής για το 2027");
        System.out.println("13. Σύστημα βαθμολόγησης κράτους (ΕΛΣΤΑΤ)");
        System.out.println("14. Έξοδος");
    }
    /**
     * Διαχειριστής επιλογών.
     * @param choice επιλογή του χρήστη
     * @return false για τον τερματισμό του προγράμματος
     */
    private boolean processChoice(final int choice) {
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
                return true;
            }
            case OPTION_AI_SPECIFIC -> handleAiSpecific();
            case OPTION_AI_GLOBAL -> handleAiGlobal();
            case OPTION_MINMAX -> {
                MinMaX minmaxfinder = new MinMaX(url);
                minmaxfinder.showMinMax();
            }
            case OPTION_PERCENTAGE -> getPercentage();
            case OPTION_PREDICT -> predictValue();
            case OPTION_GRADE -> {
                TotalGrade t = new TotalGrade();
                t.getTotalGrade();
            }
            default -> System.out.println("Λάθος επιλογή.");
        }
        return false;
    }
    /**
     * Μέθοδος διαχείρισης χαρακτηρισμού του προϋπολογισμού.
     * Καλεί την getBudgetCharacterism αναλογα με τις επιλογές
     * του χρήστη.
     */
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
    /**
     * Μέθοδος διαχείρισης εμφάνισης προϋπολογισμού.
     * Καλεί την printTable ανάλογα με τις επιλογές του χρήστη.
     */
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

    /**
     * Διαχειρίζεται τη διαδικασία τροποποίησης του προϋπολογισμού (ποσού)
     * για μια συγκεκριμένη εγγραφή σε έναν από τους διαθέσιμους πίνακες.
     * <p>
     * Η ροή εκτέλεσης περιλαμβάνει:
     * <ol>
     * <li>Επιλογή της κατηγορίας (Έσοδα, Έξοδα, Κράτος, Υπουργεία, Αποκεντρωμένες).</li>
     * <li>Ανάκτηση της τρέχουσας τιμής βάσει του ID που δίνει ο χρήστης.</li>
     * <li>Εισαγωγή του νέου ποσού.</li>
     * <li>Έλεγχο περιορισμών (Constraints) μέσω της κλάσης {@code Constrains}:
     * <ul>
     * <li>Έλεγχος για αρνητικά ποσά.</li>
     * <li>Προειδοποίηση αν η απόκλιση τιμής υπερβαίνει το 50%.</li>
     * <li>Προειδοποίηση αν το δημοσιονομικό έλλειμμα ξεπεράσει το 3%.</li>
     * </ul>
     * </li>
     * <li>Ενημέρωση της βάσης δεδομένων μέσω του {@code manager}, εφόσον εγκριθεί η αλλαγή.</li>
     * </ol>
     *
     * @see Constrains
     */

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

            double oldAmount = manager.getCurrentAmount(
                    tableName, idColName, id);

            if (oldAmount == -1) {
                System.out.println("Το ID δεν βρέθηκε.");
                return;
            }

            System.out.print("Δώσε το νέο ποσό: ");
            double newAmount = Double.parseDouble(scanner.nextLine());

            newAmount = Constrains.negativeAmount(scanner, newAmount);

            if (!Constrains.isReasonableChange(oldAmount, newAmount)) {
                System.out.println("ΠΡΟΣΟΧΗ! Η αλλαγή υπερβαίνει το 50%.");
                System.out.println("Για συνέχεια πατήστε 1, αλλιώς 2.");
                int confirm = scanner.nextInt();
                scanner.nextLine();
                if (confirm != CHOICE_YES) {
                    return;
                }
            }

            double[] rev = manager.getTotal("esoda");
            double[] exp = manager.getTotal("eksoda");
            if (!Constrains.deficitLimit(rev[1], exp[1])) {
                System.out.println("Η αλλαγή οδηγεί σε έλλειμα > 3%.");
                System.out.println("Για συνέχεια πατήστε 1, αλλιώς 2.");
                int choice = scanner.nextInt();
                scanner.nextLine();
                if (choice != CHOICE_YES) {
                    return;
                }
            }

            boolean success = manager.updateAmount(
                    tableName, idColName, id, newAmount);

            if (success) {
                System.out.println("Επιτυχής ενημέρωση!");
            } else {
                System.out.println("Αποτυχία: Δεν βρέθηκε το ID.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Λάθος είσοδος (μόνο αριθμοί).");
        }
    }
    /**
     * Μέθοδος διαχείριης εμφάνισης συνόλου.
     * Καλεί την getTotal ανάλογα με τις επιλογές
     * του χρήστη.
     */
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
        System.out.printf("Συνολικό Ποσό(επεξεργασμένο): %,.2f%n",
                results[1]);
    }

    /**
     * Υπολογίζει και εμφανίζει τα ποσοστά των εγγραφών.
     */
    public void getPercentage() {
        System.out.println("Για έσοδα πατήστε 1, έξοδα 2, υπουργεία 3.");
        String tablename = null;
        do {
            int answer = scanner.nextInt();
            switch (answer) {
                case TABLE_ESODA -> {
                    tablename = "esoda";
                }
                case TABLE_EKSODA -> {
                    tablename = "eksoda";
                }
                case TABLE_KRATOS -> {
                    tablename = "ypourgeia";
                }
                default -> System.out.println("Η τιμή δεν είναι 1, 2 ή 3.");
            }
        } while (tablename == null);

        double[] total = manager.getTotal(tablename);
        System.out.println("1: Μεμονωμένο ποσοστό, 2: Όλα τα ποσοστά");
        int answer2 = scanner.nextInt();

        if (answer2 == 1) {
            double percent;
            System.out.println("Για ποιον λογαριασμό;");
            scanner.nextLine();
            String name = scanner.nextLine();
            Search search = new Search(url);
            double amount = search.searchAmountInTable(name, tablename);
            try {
                percent = (amount / total[1]) * PERCENT_MULTIPLIER;
                System.out.printf("%.4f %% %n", percent);
            } catch (ArithmeticException e) {
                System.out.println("Δεν μπορεί να γίνει διαίρεση με 0!");
            }

        } else if (answer2 == 2) {
            processAllPercentages(tablename, total[1]);
        }
    }

    private void processAllPercentages(final String tablename,
                                       final double totalAmount) {
        int rowCount = 0;
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmtCount = conn.createStatement();
             ResultSet rsCount = stmtCount.executeQuery(
                     "SELECT COUNT(*) FROM " + tablename)) {
            if (rsCount.next()) {
                rowCount = rsCount.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Σφάλμα καταμέτρησης: " + e.getMessage());
            return;
        }

        String[] namesArray = new String[rowCount];
        double[] amountsArray = new double[rowCount];

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT name, amount FROM " + tablename)) {
            int index = 0;
            while (rs.next()) {
                namesArray[index] = rs.getString("name");
                amountsArray[index] = rs.getDouble("amount");
                index++;
            }
            System.out.println("Ανάγνωση " + rowCount + " γραμμών.");
        } catch (SQLException e) {
            System.err.println("Σφάλμα ανάγνωσης: " + e.getMessage());
        }

        double[] percentages = new double[amountsArray.length];
        System.out.println("----ΠΟΣΟΣΤΑ ΣΤΟΙΧΕΙΩΝ---");
        for (int i = 0; i < namesArray.length; i++) {
            percentages[i] = (amountsArray[i] / totalAmount);
            double p = percentages[i] * PERCENT_MULTIPLIER;
            System.out.println("ΣΤΟΙΧΕΙΟ: " + namesArray[i]
                    + " ΠΟΣΟΣΤΟ: " + String.format("%.4f", p) + " %");
        }
        EconomicsChart e = new EconomicsChart();
        e.showPieChart(namesArray, percentages);
    }

    /**
     * Διαχειρίζεται την αλληλεπίδραση για τη λήψη εξειδικευμένης συμβουλής AI
     * για μια συγκεκριμένη εγγραφή (Specific Advice).
     * <p>
     * Η μέθοδος καθοδηγεί τον χρήστη στα εξής βήματα:
     * <ol>
     * <li>Επιλογή πίνακα (Έσοδα, Έξοδα, Κράτος, Υπουργεία).</li>
     * <li>Εισαγωγή του μοναδικού κωδικού (ID) της εγγραφής.</li>
     * <li>Ανάκτηση του ονόματος και του τρέχοντος ποσού από τον {@code manager}.</li>
     * <li>Καθορισμός ενός στόχου (goal) από τον χρήστη.</li>
     * <li>Κλήση της κλάσης {@code AiBridge} για τη λήψη της συμβουλής.</li>
     * </ol>
     */

    private void handleAiSpecific() {
        System.out.println("\n--- AI Σύμβουλος (Specific) ---");
        System.out.println("Επιλέξτε πίνακα (1-4):");
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

        String name = manager.getNameById(tableName, idColName, id);
        double amount = manager.getCurrentAmount(tableName, idColName, id);

        if (name == null || amount == -1) {
            System.out.println("Δεν βρέθηκε εγγραφή με αυτό το ID.");
            return;
        }

        System.out.println("Επιλέξατε: " + name);
        System.out.printf("Τρέχον Ποσό: %,.2f €\n", amount);
        System.out.print("Στόχος: ");
        String goal = scanner.nextLine();

        System.out.println("Ο ψηφιακός βοηθός σκέφτεται...");
        AiBridge ai = new AiBridge();
        System.out.println(ai.getSpecificAdvice(
                this.url, name, amount, goal));
    }

    /**
     * Διαχειρίζεται την αλληλεπίδραση για τον καθολικό στρατηγικό σχεδιασμό (Global Strategy).
     * <p>
     * Η μέθοδος ζητά από τον χρήστη έναν γενικό στόχο και ενεργοποιεί την ανάλυση
     * ολόκληρης της βάσης δεδομένων (μέσω του URL) χρησιμοποιώντας την {@code AiBridge},
     * επιστρέφοντας μια συνολική στρατηγική πρόταση.
     */

    private void handleAiGlobal() {
        System.out.println("\n--- AI Στρατηγικός Σχεδιασμός ---");
        System.out.print("Στόχος: ");
        String goal = scanner.nextLine();

        System.out.println("Ανάλυση βάσης δεδομένων...");
        AiBridge ai = new AiBridge();
        System.out.println(ai.getGlobalStrategy(this.url, goal));
    }
    /**
    * Μέθοδος διαχείρισης της διαδικασίας προβλέψεων.
    *
    * Κλήση της collectHistory για την συλλογή των ιστορικών δεδομένων
    * του στοιχείου του προϋπολογισμού που επιλέχθηκε.
    *
    * Μετά, κλήση της performPrediction για την εκτέλεση
    * απλής γραμμικής παλινδρόμισης για την πρόβλεψη.
    */
    private void predictValue() {
        System.out.println("\n--- Πρόβλεψη για το έτος 2027 ---");
        System.out.println("Επιλέξτε πίνακα (1-5):");
        int tableChoice;
        try {
            tableChoice = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Μη έγκυρη είσοδος.");
            scanner.nextLine();
            return;
        }

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

        System.out.print("Δώσε το ID (" + idColName + ") για πρόβλεψη: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Λάθος είσοδος ID.");
            return;
        }
        /*Κλήση του collectHistory, ανάλογα με τα στοιχεία που δόθηκαν. */
        Map<Integer, Double> history = collectHistory(tableName, idColName, id);

        if (history.size() < 2) {
            System.out.println("Δεν υπάρχουν αρκετά δεδομένα για πρόβλεψη.");
            return;
        }

        performPrediction(history);
    }
    /**
     * Συλλέγει ιστορικά οικονομικά δεδομένα για μια συγκεκριμένη εγγραφή
     * σε ένα εύρος ετών (από {@code START_YEAR} έως {@code END_YEAR}).
     *
     * Η μέθοδος λειτουργεί ως εξής για κάθε έτος:
     *
     * Ελέγχει αν υπάρχει ήδη η βάση δεδομένων για το συγκεκριμένο έτος.
     * Αν δεν υπάρχει, την δημιουργεί δυναμικά μετατρέποντας
     * το αντίστοιχο PDF του προϋπολογισμού σε δεδομένα.
     * Ανακτά το ποσό για το ζητούμενο ID χρησιμοποιώντας
     * τον {@link BudgetManager}.
     * Αν η βάση δημιουργήθηκε προσωρινά μόνο για αυτή τη διαδικασία,
     * την διαγράφει
     * στο τέλος για να μην πιάνει χώρο.
     *
     *
     *
     * @param tableName   Το όνομα του πίνακα στη βάση δεδομένων στον
     * οποίο θα γίνει η αναζήτηση.
     * @param idColName   Το όνομα της στήλης που περιέχει το αναγνωριστικό
     * @param id          Ο μοναδικός αριθμός (ID) της εγγραφής που αναζητούμε.
     * @return            Ένα {@link Map} (συγκεκριμένα {@link LinkedHashMap}
     * για διατήρηση της σειράς)
     * που αντιστοιχεί το Έτος (Integer) στο Ποσό (Double).
     * Επιστρέφει μόνο τα έτη για τα οποία βρέθηκαν έγκυρα δεδομένα.
     */
    private Map<Integer, Double> collectHistory(final String tableName,
                                                final String idColName,
                                                final int id) {
        System.out.println("Συλλογή ιστορικών δεδομένων (2023-2026)...");
        Map<Integer, Double> history = new LinkedHashMap<>();
        DatabaseFinder finder = new DatabaseFinder();

        for (int year = START_YEAR; year <= END_YEAR; year++) {
            String dbName = "budget_" + year + ".db";
            String currentDbUrl = "jdbc:sqlite:" + dbName;
            boolean tempCreated = false;

            if (!finder.findYearbase(year)) {
                try {
                    Pdftocsv.run(year);
                    PinakesImporter importer = new PinakesImporter(
                            currentDbUrl);
                    importer.importAll();
                    tempCreated = true;
                } catch (Exception e) {
                    System.out.println("Αδυναμία ανάγνωσης έτους " + year);
                    continue;
                }
            }

            BudgetManager tempManager = new BudgetManager(currentDbUrl);
            double amount = tempManager.getCurrentAmount(
                    tableName, idColName, id);

            if (amount != -1) {
                history.put(year, amount);
                System.out.printf("Έτος %d: %,.2f EUR%n", year, amount);
            } else {
                System.out.printf("Έτος %d: Δεν βρέθηκε εγγραφή.%n", year);
            }

            if (tempCreated) {
                try {
                    new File(dbName).delete();
                } catch (Exception e) {
                    // Ignore deletion errors
                }
            }
        }
        return history;
    }
    /**
     * Εκτελεί οικονομική πρόβλεψη για το έτος {@code PREDICT_YEAR} βασισμένη
     * στα ιστορικά δεδομένα, χρησιμοποιώντας τη μέθοδο της
     * Γραμμικής Παλινδρόμησης.
     *
     *
     * Ο αλγόριθμος υπολογίζει την εξίσωση της ευθείας (y = mx + b)
     * που ταιριάζει
     * καλύτερα στα δεδομένα.
     *
     *
     * Στη συνέχεια, συγκρίνει την προβλεπόμενη τιμή με την τελευταία γνωστή
     * τιμή
     * (του έτους {@code END_YEAR}) για να καθορίσει και να εκτυπώσει την
     * τάση (Αύξηση ή Μείωση).
     *
     * @param history Ένας χάρτης (Map) που περιέχει τα ιστορικά δεδομένα,
     * όπου το κλειδί είναι το Έτος (ανεξάρτητη μεταβλητή x)
     * και η τιμή είναι το Ποσό (εξαρτημένη μεταβλητή y).
     */
    private void performPrediction(final Map<Integer, Double> history) {
        double n = history.size();
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumX2 = 0;

        for (Map.Entry<Integer, Double> entry : history.entrySet()) {
            double x = entry.getKey();
            double y = entry.getValue();
            sumX += x;
            sumY += y;
            sumXY += (x * y);
            sumX2 += (x * x);
        }

        double slope = (n * sumXY - sumX * sumY)
                / (n * sumX2 - sumX * sumX);
        double intercept = (sumY - slope * sumX) / n;
        double predicted = slope * PREDICT_YEAR + intercept;

        System.out.println("----------------------------------------");
        System.out.printf("Εκτίμηση για το 2027: %,.2f EUR%n", predicted);

        double growth = predicted - history.get(END_YEAR);
        if (growth > 0) {
            System.out.printf("Τάση: Αύξηση (+%,.2f EUR)%n", growth);
        } else {
            System.out.printf("Τάση: Μείωση (%,.2f EUR)%n", growth);
        }
        System.out.println("----------------------------------------");
    }
}
