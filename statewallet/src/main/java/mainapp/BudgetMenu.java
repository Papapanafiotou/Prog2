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
    private static final int OPTION_EXIT = 8;

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
            System.out.println("8. Έξοδος");
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

            System.out.print("Δώσε το νέο ποσό: ");
            double newAmount = Double.parseDouble(scanner.nextLine());

            newAmount = Constrains.negativeAmount(scanner, newAmount);
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
}
