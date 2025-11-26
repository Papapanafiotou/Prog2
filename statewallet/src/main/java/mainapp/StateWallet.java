package mainapp;


import java.util.Scanner;



public class StateWallet {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        int year;

        do {
        System.out.print("Δώσε χρονολογία (2023 έως 2026): ");
        year = scanner.nextInt(); 
        } while(year <2023 || year > 2026);
        final String DATABASE_URL = "jdbc:sqlite:budget.db";
        PinakesImporter importer = new PinakesImporter(DATABASE_URL);
        Csvtopdf.run(year);
        importer.importAll();

        BudgetMenu budgetmenu = new BudgetMenu();
        
        // 2. Εκκίνηση της εφαρμογής
        budgetmenu.start();
        

    }
}
