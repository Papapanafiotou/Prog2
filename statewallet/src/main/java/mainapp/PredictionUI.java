package mainapp;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.Serial;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * Παρέχει γραφική διεπαφή για την πρόβλεψη τιμών μελλοντικών ετών βάσει
 * ιστορικών δεδομένων (2023-2026).
 */
public final class PredictionUI extends JFrame {

    /** Serial Version UID. */
    @Serial
    private static final long serialVersionUID = 1L;

    // Σταθερές παραθύρου
    /** Πλάτος παραθύρου. */
    private static final int WIN_WIDTH = 500;
    /** Ύψος παραθύρου. */
    private static final int WIN_HEIGHT = 450;
    /** Κενό (gap) στη διάταξη. */
    private static final int LAYOUT_GAP = 10;

    // Σταθερές γραμματοσειρών
    /** Μέγεθος γραμματοσειράς περιοχής κειμένου. */
    private static final int FONT_SIZE_AREA = 13;
    /** Μέγεθος γραμματοσειράς κουμπιού. */
    private static final int FONT_SIZE_BTN = 14;

    // Σταθερές ετών
    /** Έτος έναρξης ιστορικών δεδομένων. */
    private static final int START_YEAR = 2023;
    /** Έτος λήξης ιστορικών δεδομένων. */
    private static final int END_YEAR = 2026;
    /** Έτος στόχος πρόβλεψης. */
    private static final int TARGET_YEAR = 2027;

    // Πεδία δεδομένων
    /** Το όνομα του πίνακα στη βάση. */
    private final String tableName;
    /** Το όνομα της στήλης ID. */
    private final String idColName;
    /** Η τιμή του ID της εγγραφής. */
    private final int idValue;
    /** Το όνομα/περιγραφή της εγγραφής. */
    private final String itemName;

    /**
     * Κατασκευαστής της διεπαφής πρόβλεψης.
     *
     * @param dbPath    Η διαδρομή της βάσης δεδομένων (δεν χρησιμοποιείται
     * άμεσα εδώ, αλλά για συμβατότητα).
     * @param table     Το όνομα του πίνακα στη βάση.
     * @param idCol     Το όνομα της στήλης ID.
     * @param idVal     Η τιμή του ID της εγγραφής.
     * @param name      Το όνομα/περιγραφή της εγγραφής.
     */
    public PredictionUI(final String dbPath, final String table,
                        final String idCol, final int idVal,
                        final String name) {
        this.tableName = table;
        this.idColName = idCol;
        this.idValue = idVal;
        this.itemName = name;

        setTitle("Πρόβλεψη 2027 - " + itemName);
        setSize(WIN_WIDTH, WIN_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(LAYOUT_GAP, LAYOUT_GAP));

        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, FONT_SIZE_AREA));
        reportArea.setText("Στοιχείο: " + itemName + "\nID: " + idValue
                + "\n" + "Αναζήτηση ιστορικών δεδομένων (2023-2026)...");

        JButton runAnalysisBtn = new JButton("📊 Έναρξη Στατιστικής Ανάλυσης");
        runAnalysisBtn.setFont(new Font("Segoe UI", Font.BOLD, FONT_SIZE_BTN));

        runAnalysisBtn.addActionListener(e -> {
            Map<Integer, Double> history = collectDataFromYears();

            if (history.size() < 2) {
                reportArea.setText("Σφάλμα: Δεν βρέθηκαν επαρκή δεδομένα για "
                        + "'" + itemName + "'\n"
                        + "στα αρχεία budget_2023.db έως budget_2026.db.");
                return;
            }

            // Υπολογισμός Γραμμικής Παλινδρόμησης (y = mx + b)
            double n = history.size();
            double sumX = 0;
            double sumY = 0;
            double sumXY = 0;
            double sumX2 = 0;

            for (Map.Entry<Integer, Double> entry : history.entrySet()) {
                double x = entry.getKey();
                double y = entry.getValue();
                sumX += x;
                sumY += y;
                sumXY += (x * y);
                sumX2 += (x * x);
            }

            double slope = (n * sumXY - sumX * sumY)
                    / (n * sumX2 - sumX * sumX);
            double intercept = (sumY - slope * sumX) / n;
            double predicted = slope * TARGET_YEAR + intercept;

            StringBuilder sb = new StringBuilder();
            sb.append("--- ΣΤΑΤΙΣΤΙΚΗ ΑΝΑΦΟΡΑ ---\n");
            sb.append("Στοιχείο: ").append(itemName).append("\n\n");
            history.forEach((year, val) -> sb.append(
                    String.format("Έτος %d: %,.2f €\n", year, val)));
            sb.append("-----------------------------\n");
            sb.append(String.format("ΠΡΟΒΛΕΨΗ ΓΙΑ ΤΟ 2027: %,.2f €\n",
                    predicted));
            sb.append("-----------------------------\n");
            sb.append(String.format("Τάση: %s\n",
                    (slope > 0 ? "Ανοδική ▲" : "Καθοδική ▼")));

            reportArea.setText(sb.toString());
        });

        add(new JLabel("Πρόβλεψη βάσει Ιστορικών Στοιχείων",
                SwingConstants.CENTER), BorderLayout.NORTH);
        add(new JScrollPane(reportArea), BorderLayout.CENTER);
        add(runAnalysisBtn, BorderLayout.SOUTH);
    }

    private Map<Integer, Double> collectDataFromYears() {
        Map<Integer, Double> history = new LinkedHashMap<>();
        for (int year = START_YEAR; year <= END_YEAR; year++) {
            String yearDbUrl = "jdbc:sqlite:budget_" + year + ".db";
            BudgetManager tempManager = new BudgetManager(yearDbUrl);
            // Χρήση της μεθόδου ανάκτησης ποσού
            double amount = tempManager.getCurrentAmount(
                    tableName, idColName, idValue);
            if (amount != -1) {
                history.put(year, amount);
            }
        }
        return history;
    }
}
