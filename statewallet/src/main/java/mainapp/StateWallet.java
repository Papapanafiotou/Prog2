package mainapp;


public class StateWallet{

    public static void main(String[] args) {
        final String DATABASE_URL = "jdbc:sqlite:budget.db";
        PinakesImporter importer = new PinakesImporter(DATABASE_URL);
        importer.importAll();

        BudgetMenu budgetmenu = new BudgetMenu();
        
<<<<<<< HEAD
        // 2. Εκκίνηση της εφαρμογής
        budgetmenu.start();
=======
        menu.start();
>>>>>>> 24ef3551bcfd16022a9cf85cf2c89f105be1dc1f
    }
}