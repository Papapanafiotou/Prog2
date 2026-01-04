package mainapp;

import java.util.Scanner;

public class BudgetMenu {
    
    DatabaseChooser chooser = new DatabaseChooser();
    String URL;
    private BudgetManager manager;
    private Scanner scanner;

    // Constructor tou BudgetMenu //
    public BudgetMenu(String url) {
        this.URL = url;
        this.manager = new BudgetManager(URL);
        this.scanner = new Scanner(System.in, "CP737");
    }

    public void start() {
        // Επιλογη βασικης λειτουργιας //
        while (true) {
            System.out.println("\n--------------------------------------------------");
            System.out.println("Επιλέξτε μία από τις παρακάτω λειτουργίες");
            System.out.println("1. Εμφάνιση στοιχείων προυπολογισμού");
            System.out.println("2. Αλλαγή στοιχείου προυπολογισμού");
            System.out.println("3. Εμφάνιση αλλαγών");
            System.out.println("4. Εμφάνιση συνόλου");
            System.out.println("5. Αλλαγή έτους προυπολογισμού");
            System.out.println("6. Αναζήτηση στοιχείου");
            System.out.println("7. Χαρακτηρισμός προυπολογισμού");
            System.out.println("8. Έξοδος");
            System.out.print("Επιλογή: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> showBudgetSelection();
                case 2 -> ChangeBudget();
                case 3 -> manager.showChanges();
                case 4 -> showTotalSelection();
                case 5 -> {
                    String newURL = chooser.getURL();
                    this.URL = newURL;
                    manager.URL = newURL;
                }
                case 6 -> {
                    Search s = new Search(URL);
                    System.out.println("Εισάγετε το στοιχείο αναζήτησης");
                    String name = scanner.nextLine().trim();
                    s.searchAmount(name);
                }
                case 7 -> {
                    System.out.println("Θέλετε χαρακτηρισμό στα αρχικά ή στα επεξεργασμένα στοιχεία;");
                    System.out.println("(1 για αρχικά 2 για επεξεργασμένα)");
                    System.out.print("Επιλογή: ");
                    int cho = scanner.nextInt();
                    scanner.nextLine();
                    double[] revenue = manager.getTotal("esoda");
                    double[] expenses = manager.getTotal("eksoda");
                    if(cho == 1) {
                        manager.budgetCharacterism(revenue[0], expenses[0]);
                    } else {
                        manager.budgetCharacterism(revenue[1], expenses[1]);
                    }
                }
                case 8 -> {
                    System.out.println("Έξοδος...");
                    scanner.close();
                    return;
                }
                    default -> System.out.println("Λάθος επιλογή.");
            }
        }
    }

    // μεθοδος επιλογης πινακα //

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
            case 1 -> manager.printTable("esoda", "code");
            case 2 -> manager.printTable("eksoda", "code");
            case 3 -> manager.printTable("kratos", "number");
            case 4 -> manager.printTable("ypourgeia", "number");
            case 5 -> manager.printTable("apokentromenes", "number");
            case 6 -> {
                manager.printTable("esoda", "code");
                manager.printTable("eksoda", "code");
                manager.printTable("kratos", "number");
                manager.printTable("ypourgeia", "number");
                manager.printTable("apokentromenes", "number");
            }
            case 7 -> { return; }
            default -> System.out.println("Λάθος επιλογή.");
        }
    }


    // Μεθοδος αλλαγης ποσου //
    private void ChangeBudget() {
        System.out.println("\nΣε ποιον πίνακα ανήκει το στοιχείο που θέλετε να αλλάξετε;");
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
            case 1 -> {
                tableName = "esoda"; idColName = "code";
            }
            case 2 -> {
                tableName = "eksoda"; idColName = "code";
            }
            case 3 -> {
                tableName = "kratos"; idColName = "number";
            }
            case 4 -> {
                tableName = "ypourgeia"; idColName = "number";
            }
            case 5 -> {
                tableName = "apokentromenes"; idColName = "number";
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
            
            
            
            // Ελεγχος και επιστροφη ποσου //

            newAmount = Constrains.negativeAmount(scanner, newAmount);

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
            
            // Εδώ καλούμε τη λογική από τον Manager //
            
            boolean success = manager.updateAmount(tableName, idColName, id, newAmount);
            
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
        double[] results = new double[2];
        results[0] = 0;
        results[1] = 0;
        int tableChoice = scanner.nextInt();
        scanner.nextLine();
        switch (tableChoice) {
            case 1 -> { results = manager.getTotal("esoda"); }
            case 2 -> { results = manager.getTotal("eksoda"); }
            case 3 -> { results = manager.getTotal("kratos"); }
            case 4 -> { results = manager.getTotal("ypourgeia"); }
            case 5 -> { results = manager.getTotal("apokentromenes"); }
            default -> {
                System.out.println("Άκυρη επιλογή."); 
                return;
            }
        }
            System.out.println("--- Αποτελέσματα για τον πίνακα: " + tableChoice + " ---");
            System.out.printf("Συνολικό Ποσό(αρχικό): %,.2f%n", results[0]);
            System.out.printf("Συνολικό Ποσό(επεξεργασμένο): %,.2f%n", results[1]);
    }
}

