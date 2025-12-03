package mainapp;


import java.util.Scanner;



public class StateWallet {
    
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) { // χρηση try-with για να κλεισει αυτοματα το scanner //
            int year;
            
            do {
                System.out.print("Δώσε χρονολογία (2023 έως 2026): ");
                year = scanner.nextInt();
                scanner.nextLine();
            } while(year <2023 || year > 2026);
            String DATABASE_URL = "jdbc:sqlite:budget_" + year + ".db";

            DatabaseFinder finder = new DatabaseFinder();
            boolean DatabaseExists = finder.findYearbase(year);
            if (!DatabaseExists) {
                Csvtopdf.run(year);
                PinakesImporter importer = new PinakesImporter(DATABASE_URL);
                importer.importAll(); 
            } else {
                System.out.println("Έχει γίνει επεξεργασία του συγκεκριμένου έτους στο παρελθόν. Θέλετε να ξεκινήσετε από την αρχή; (1 για ΝΑΙ --- 2 για ΟΧΙ)");
                int answer = scanner.nextInt();
                if (answer == 1) {
                    System.out.println("Έγινε διαγραφή των παλιών στοιχείων");
                    Csvtopdf.run(year);
                    PinakesImporter importer = new PinakesImporter(DATABASE_URL);
                    importer.importAll();
                }
            }
            
            BudgetMenu budgetmenu = new BudgetMenu(DATABASE_URL);
            
            // 2. Εκκίνηση της εφαρμογής
            budgetmenu.start();
        }
    }
}

