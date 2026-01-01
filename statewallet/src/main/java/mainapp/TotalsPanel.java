package mainapp;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class TotalsPanel extends JPanel {
     private final BudgetManager manager;

    private final JLabel originalLabel;
    private final JLabel currentLabel;
    private final JLabel diffLabel;

    private static final Color POSITIVE = new Color(34, 139, 34);

    public TotalsPanel(final BudgetManager manager) {
        this.manager = manager;

        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder("Σύνολα Πίνακα"));

        Font font = new Font("Segoe UI", Font.BOLD, 13);
         originalLabel = new JLabel("Αρχικό Σύνολο: -");
        currentLabel  = new JLabel("Τρέχον Σύνολο: -");
        diffLabel     = new JLabel("Διαφορά: -");

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
     * @param tableName όνομα πίνακα (π.χ. esoda, eksoda)
     */
    public void updateTotals(final String tableName) {
        double[] totals = manager.getTotal(tableName);

        double original = totals[0];
        double current  = totals[1];
        double diff     = current - original;

        originalLabel.setText(
            String.format("Αρχικό Σύνολο: %.2f €", original)
        );
        currentLabel.setText(
            String.format("Τρέχον Σύνολο: %.2f €", current)
        );
        diffLabel.setText(
            String.format("Διαφορά: %+,.2f €", diff)
        );

        if (diff > 0) {
            diffLabel.setForeground(POSITIVE);
        } else if (diff < 0) {
            diffLabel.setForeground(Color.RED);
        } else {
            diffLabel.setForeground(Color.BLUE);
        }
    }
}
