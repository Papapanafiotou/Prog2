package mainapp;


public class StateWallet {
    
    public static void main(String[] args) {
            DatabaseChooser chooser = new DatabaseChooser();
            String DATABASE_URL = chooser.getURL();
            BudgetMenu budgetmenu = new BudgetMenu(DATABASE_URL);
            
            // 2. Εκκίνηση της εφαρμογής
            budgetmenu.start();
        }
}


