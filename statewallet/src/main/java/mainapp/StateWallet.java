package mainapp;


public class StateWallet{

    public static void main(String[] args) {
        final String DATABASE_URL = "jdbc:sqlite:budget.db";
        // 1. Δημιουργία του αντικειμένου που διαχειρίζεται το μενού
        PinakesImporter importer = new PinakesImporter(DATABASE_URL);
        importer.importAll();

        BudgetMenu budgetmenu = new BudgetMenu();
        
        // 2. Εκκίνηση της εφαρμογής
        budgetmenu.start();
    }
}