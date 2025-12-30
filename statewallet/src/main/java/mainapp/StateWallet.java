package mainapp;

public class StateWallet {
    
    public static void main(String[] args) {
            // 1. Ενεργοποίηση του Login System
            Log log = new Log();
            boolean login = log.logMenu();
            // 2. Αν επιτευχθεί το login, επιλογή έτους προυπολογιμού
            if (login) {
            DatabaseChooser chooser = new DatabaseChooser();
            String DATABASE_URL = chooser.getURL();
            BudgetMenu budgetmenu = new BudgetMenu(DATABASE_URL);
          
            // 3. Εκκίνη2ση της εφαρμογής
            budgetmenu.start();
            }
        }
}
