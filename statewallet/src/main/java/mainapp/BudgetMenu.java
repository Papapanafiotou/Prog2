package mainapp;

import java.util.Scanner;

public class BudgetMenu {
    
    private BudgetManager manager;
    private Scanner scanner;

    // Constructor tou BudgetMenu //
    public BudgetMenu() {
        this.manager = new BudgetManager();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        // Επιλογη βασικης λειτουργιας //
        while (true) {
            System.out.println("\n--------------------------------------------------");
            System.out.println("Επιλέξτε μία από τις παρακάτω λειτουργίες");
            System.out.println("1. Εμφάνιση στοιχείων προυπολογισμού");
            System.out.println("2. Αλλαγή στοιχείου προυπολογισμού");
            System.out.println("3. Εμφάνιση αλλαγών");
            System.out.println("4. Έξοδος");
            System.out.print("Επιλογή: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1: 
                    showBudgetSelection(); 
                    break;
                case 2: 
                    ChangeBudget(); 
                    break;
                case 3: 
                    manager.showChanges(); 
                    break;
                case 4:
                    System.out.println("Έξοδος...");
                    scanner.close();
                return;
                    default: System.out.println("Λάθος επιλογή.");
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
        System.out.print("Επιλογή: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1: 
                manager.printTable("esoda", "code"); 
                break;
            case 2: 
                manager.printTable("eksoda", "code"); 
                break;
            case 3: 
                manager.printTable("kratos", "number"); 
                break;
            case 4: 
                manager.printTable("ypourgeia", "number"); 
                break;
            case 5: 
                manager.printTable("apokentromenes", "number"); 
                break;
            case 6:
                manager.printTable("esoda", "code");
                manager.printTable("eksoda", "code");
                manager.printTable("kratos", "number");
                manager.printTable("ypourgeia", "number");
                manager.printTable("apokentromenes", "number");
                break;
            default: 
                System.out.println("Λάθος επιλογή.");
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
            case 1: 
                tableName = "esoda"; idColName = "code"; 
                break;
            case 2: 
                tableName = "eksoda"; idColName = "code"; 
                break;
            case 3: 
                tableName = "kratos"; idColName = "number"; 
                break;
            case 4: 
                tableName = "ypourgeia"; idColName = "number"; 
                break;
            case 5: 
                tableName = "apokentromenes"; idColName = "number"; 
                break;
            default: 
                System.out.println("Άκυρη επιλογή."); 
                return;
        }

        try {
            System.out.print("Δώσε το ID (" + idColName + "): ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Δώσε το νέο ποσό: ");
            double newAmount = Double.parseDouble(scanner.nextLine());
            
            // Ελεγχος και επιστροφη ποσου //

            newAmount = Constrains.negativeAmount(scanner, newAmount);

            // Εδώ καλούμε τη λογική από τον Manager //
            
            boolean success = manager.updateAmount(tableName, idColName, id, newAmount);
            
            if (success) System.out.println("Επιτυχής ενημέρωση!");
            else System.out.println("Αποτυχία: Δεν βρέθηκε το ID.");

        } catch (NumberFormatException e) {
            System.out.println("Λάθος είσοδος (μόνο αριθμοί).");
        }
    }
}