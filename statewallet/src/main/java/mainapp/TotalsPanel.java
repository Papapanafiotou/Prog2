package mainapp;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.Serial;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Πάνελ που εμφανίζει τα οικονομικά σύνολα (αρχικό, τρέχον, διαφορά)
 * για έναν επιλεγμένο πίνακα.
 */
public final class TotalsPanel extends JPanel {

    @Serial
    private static final long serialVersionUID = 1L;

    // Σταθερές Γραμματοσειράς
    /** Όνομα γραμματοσειράς UI. */
    private static final String FONT_NAME = "Segoe UI";
    /** Μέγεθος γραμματοσειράς UI. */
    private static final int FONT_SIZE = 13;

    // Σταθερές Κειμένου
    /** Τίτλος περιγράμματος (Border). */
    private static final String TITLE_BORDER = "Σύνολα Πίνακα";
    /** Αρχικό κείμενο ετικέτας αρχικού ποσού. */
    private static final String TEXT_INIT_ORIG = "Αρχικό Σύνολο: -";
    /** Αρχικό κείμενο ετικέτας τρέχοντος ποσού. */
    private static final String TEXT_INIT_CURR = "Τρέχον Σύνολο: -";
    /** Αρχικό κείμενο ετικέτας διαφοράς. */
    private static final String TEXT_INIT_DIFF = "Διαφορά: -";

    // Σταθερές Μορφοποίησης (Formats)
    /** Format string για το αρχικό σύνολο. */
    private static final String FMT_ORIG = "Αρχικό Σύνολο: %.2f €";
    /** Format string για το τρέχον σύνολο. */
    private static final String FMT_CURR = "Τρέχον Σύνολο: %.2f €";
    /** Format string για τη διαφορά. */
    private static final String FMT_DIFF = "Διαφορά: %+,.2f €";

    // Δείκτες πίνακα αποτελεσμάτων
    /** Δείκτης για το αρχικό ποσό στον πίνακα αποτελεσμάτων. */
    private static final int IDX_ORIGINAL = 0;
    /** Δείκτης για το τρέχον ποσό στον πίνακα αποτελεσμάτων. */
    private static final int IDX_CURRENT = 1;

    // Χρώματα
    /** Χρώμα για θετική διαφορά (Σκούρο Πράσινο). */
    private static final Color COLOR_POSITIVE = new Color(34, 139, 34);

    /** Ο διαχειριστής προϋπολογισμού. */
    private final BudgetManager manager;

    /** Ετικέτα αρχικού ποσού. */
    private final JLabel originalLabel;
    /** Ετικέτα τρέχοντος ποσού. */
    private final JLabel currentLabel;
    /** Ετικέτα διαφοράς. */
    private final JLabel diffLabel;

    /**
     * Κατασκευαστής.
     *
     * @param budgetManager Ο διαχειριστής προϋπολογισμού.
     */
    public TotalsPanel(final BudgetManager budgetManager) {
        this.manager = budgetManager;

        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder(TITLE_BORDER));

        Font font = new Font(FONT_NAME, Font.BOLD, FONT_SIZE);
        originalLabel = new JLabel(TEXT_INIT_ORIG);
        currentLabel = new JLabel(TEXT_INIT_CURR);
        diffLabel = new JLabel(TEXT_INIT_DIFF);

        originalLabel.setFont(font);
        currentLabel.setFont(font);
        diffLabel.setFont(font);

        add(originalLabel);
        add(currentLabel);
        add(diffLabel);
    }

    /**
     * Ανανεώνει τα σύνολα για τον δοσμένο πίνακα.
     *
     * @param tableName Το όνομα του πίνακα (π.χ. "esoda", "eksoda").
     */
    public void updateTotals(final String tableName) {
        double[] totals = manager.getTotal(tableName);

        double original = totals[IDX_ORIGINAL];
        double current = totals[IDX_CURRENT];
        double diff = current - original;

        originalLabel.setText(
                String.format(FMT_ORIG, original)
        );
        currentLabel.setText(
                String.format(FMT_CURR, current)
        );
        diffLabel.setText(
                String.format(FMT_DIFF, diff)
        );

        if (diff > 0) {
            diffLabel.setForeground(COLOR_POSITIVE);
        } else if (diff < 0) {
            diffLabel.setForeground(Color.RED);
        } else {
            diffLabel.setForeground(Color.BLUE);
        }
    }
}
