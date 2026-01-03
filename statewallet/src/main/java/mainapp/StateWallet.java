package mainapp;

/**
 * Η κύρια κλάση εκκίνησης της εφαρμογής (Console version).
 */
public final class StateWallet {

    /**
     * Private constructor to prevent instantiation.
     */
    private StateWallet() {
        // Utility class
    }

    /**
     * Η μέθοδος main που ξεκινάει την εφαρμογή.
     *
     * @param args Τα ορίσματα της γραμμής εντολών.
     */
    public static void main(final String[] args) {
        // 1. Ενεργοποίηση του Login System
        Log log = new Log();
        boolean login = log.logMenu();

        // 2. Αν επιτευχθεί το login, επιλογή έτους προϋπολογισμού
        if (login) {
            DatabaseChooser chooser = new DatabaseChooser();
            String databaseUrl = chooser.getUrl();
            BudgetMenu budgetMenu = new BudgetMenu(databaseUrl);

            // 3. Εκκίνηση της εφαρμογής
            budgetMenu.start();
        }
    }
}
