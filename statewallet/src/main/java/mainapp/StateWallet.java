package mainapp;


public class StateWallet{

    public static void main(String[] args) {
        final String DATABASE_URL = "jdbc:sqlite:budget.db";
        PinakesImporter importer = new PinakesImporter(DATABASE_URL);
        importer.importAll();

        BudgetMenu budgetmenu = new BudgetMenu();
        
        // 2. Εκκίνηση της εφαρμογής
        budgetmenu.start();
        

    }
}