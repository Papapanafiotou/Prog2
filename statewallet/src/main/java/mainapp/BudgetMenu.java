package mainapp;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    private static final int OPTION_EXIT = 10;


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
        this.scanner = new Scanner(System.in, "CP737");
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
            System.out.println("9. Εμφάνιση Ποσοστών σε σχέση με το σύνολο.");
            System.out.println("10. Έξοδος");
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

        public void getPrecentage() {
            Scanner scan = new Scanner(System.in);
            System.out.println("Για έσοδα πατήστε 1, για έξοδα πατήστε 2,"
            + "για υπουργεία 3.");
            String tablename = null;
            do {
               int answer = scan.nextInt();
               switch (answer) {
                case 1:
                tablename = "esoda";
                break;
                case 2:
                tablename = "eksoda";
                break;
                case 3:
                tablename = "ypourgeia";
                break;
                default:
                System.out.println("Η τιμή δεν είναι 1, 2 ή 3.");
                break;
                }
            } while (tablename == null);
            double[] total = manager.getTotal(tablename);
            System.out.println("Για υπολογισμό μεμονωμένου ποσοστού "
                + "πατήστε 1, για τον υπολογισμό όλων των ποσοστών πατήστε 2"
            );
            int answer2 = scan.nextInt();
            if (answer2 == 1) { 
            double precent = 0.0;
            System.out.println("Για ποιον λογαριασμό θέλετε να υπολογίσετε " +
             "το ποσοστό;"); 
            String name = scanner.nextLine();
            Search search = new Search(url);
            double amount = search.searchAmountInTable(name, tablename);
            try {
                precent = (amount / total[1]) * 100; 
            } catch (ArithmeticException e) {
                System.out.println("Σφάλμα! Δεν μπορεί να γίνει διαίρεση με 0!");
            }
            System.out.println("Το ποσοστό του " + name + "είναι " 
            + precent + " %" );
        } else if (answer2 == 2) {
           int rowCount = 0;
           try (Connection conn = DriverManager.getConnection(url);
               Statement stmtCount = conn.createStatement();
               ResultSet rsCount = stmtCount.executeQuery("SELECT COUNT(*) FROM " + tablename)) {
                if (rsCount.next()) {
                  rowCount = rsCount.getInt(1);
                }
            } catch (SQLException e) {
            System.err.println("Σφάλμα κατά την καταμέτρηση: " + e.getMessage());
            return; 
            }
            String[] namesArray = new String[rowCount];
            double[] amountsArray = new double[rowCount];
            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT name, amount FROM " + tablename)) {
                int index = 0;
                while (rs.next()) {
                   namesArray[index] = rs.getString("name");
                   amountsArray[index] = rs.getDouble("amount");
                   index++;
                }
            
            System.out.println("Επιτυχής ανάγνωση " + rowCount + " γραμμών.");
            } catch (SQLException e) {
               System.err.println("Σφάλμα κατά την ανάγνωση δεδομένων: " + e.getMessage());
            }
            double[] percentages = new double[amountsArray.length];

            System.out.println("----ΠΟΣΟΣΤΑ ΣΤΟΙΧΕΙΩΝ---");
            for (int i = 0; i < namesArray.length; i++) {
                percentages[i] = (amountsArray[i] / total[1]) ;
                double p = percentages[i] * 100;
                System.out.println("ΣΤΟΙΧΕΙΟ: " + namesArray[i] +
                    " ΠΟΣΟΣΤΟ: " + String.format("%.4f",p) + " %"
                );
            }
            EconomicsChart e = new EconomicsChart();
            e.showPieChart(namesArray, percentages);
        } 
    }
}
