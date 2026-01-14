package mainapp;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Η κλάση εκκίνησης της εφαρμογής με GUI (Launcher).
 */
public final class StateWalletUi {

    /**
     * Private constructor to prevent instantiation.
     */
    private StateWalletUi() {
        // Utility class
    }

    /**
     * Η μέθοδος main.
     *
     * @param args Τα ορίσματα της γραμμής εντολών.
     */
    public static void main(final String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException
                 | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new LogUi().setVisible(true));
    }
}

