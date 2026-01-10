package mainapp;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Παράθυρο γραφικής διεπαφής για τον υπολογισμό και την εμφάνιση
 * ποσοστιαίας αναλογίας των εγγραφών ενός πίνακα.
 */
public final class PercentageUI extends JFrame {

    /** Serial Version UID. */
    @Serial
    private static final long serialVersionUID = 1L;

    // Σταθερές Παραθύρου
    /** Πλάτος παραθύρου. */
    private static final int WIN_WIDTH = 500;
    /** Ύψος παραθύρου. */
    private static final int WIN_HEIGHT = 400;
    /** Κενό (gap) στη διάταξη. */
    private static final int LAYOUT_GAP = 10;

    // Σταθερές Γραμματοσειράς
    /** Όνομα γραμματοσειράς αποτελεσμάτων. */
    private static final String FONT_NAME = "Consolas";
    /** Μέγεθος γραμματοσειράς αποτελεσμάτων. */
    private static final int FONT_SIZE = 12;

    // Σταθερές Υπολογισμών
    /** Πολλαπλασιαστής για μετατροπή σε ποσοστό. */
    private static final double PERCENT_MULT = 100.0;
    /** Μέγιστο μήκος ονόματος πριν την αποκοπή. */
    private static final int MAX_NAME_LEN = 27;
    /** Σύμβολο αποκοπής κειμένου. */
    private static final String DOTS = "...";
    /** Αριθμός επαναλήψεων διαχωριστικής γραμμής. */
    private static final int SEPARATOR_REPEAT = 65;

    // Σταθερές Πινάκων Βάσης
    /** Δείκτης στήλης ποσού (αν χρησιμοποιείται). */
    private static final int COL_IDX_AMOUNT = 2;

    /**
     * Κατασκευαστής.
     *
     * @param manager Ο διαχειριστής προϋπολογισμού.
     * @param dbPath  Η διαδρομή της βάσης δεδομένων.
     */
    public PercentageUI(final BudgetManager manager, final String dbPath) {
        setTitle("Ανάλυση Ποσοστών (Κείμενο)");
        setSize(WIN_WIDTH, WIN_HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(LAYOUT_GAP, LAYOUT_GAP));

        // Πάνελ Επιλογών
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(new JLabel("Επιλέξτε Πίνακα:"));

        // Δημιουργία του ComboBox με τα αντικείμενα TableOption
        JComboBox<TableOption> comboTable = new JComboBox<>();

        // Προσθήκη των επιλογών
        comboTable.addItem(new TableOption("Έσοδα", "esoda"));
        comboTable.addItem(new TableOption("Έξοδα", "eksoda"));
        comboTable.addItem(new TableOption("Κράτος", "kratos"));
        comboTable.addItem(new TableOption("Υπουργεία", "ypourgeia"));
        comboTable.addItem(new TableOption(
                "Αποκεντρωμένες Διοικήσεις", "apokentromenes"));

        controlPanel.add(comboTable);

        // Κουμπί υπολογισμού
        JButton btnCalculate = new JButton("Υπολογισμός Ποσοστών");
        controlPanel.add(btnCalculate);

        add(controlPanel, BorderLayout.NORTH);

        // Περιοχή αποτελεσμάτων
        JTextArea resultArea = new JTextArea();
        resultArea.setFont(new Font(FONT_NAME, Font.PLAIN, FONT_SIZE));
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, BorderLayout.CENTER);

        // Action Listener για το κουμπί
        btnCalculate.addActionListener(e -> {
            // 1. Παίρνουμε το αντικείμενο TableOption
            TableOption selectedOption = (TableOption) comboTable
                    .getSelectedItem();

            if (selectedOption == null) {
                return;
            }

            // 2. Παίρνουμε την "κρυφή" τιμή για τη βάση
            String table = selectedOption.getValue();

            double[] totals = manager.getTotal(table);
            double totalAmount = totals[1]; // Τρέχον σύνολο

            if (totalAmount <= 0) {
                resultArea.setText("Σφάλμα: Το συνολικό ποσό του πίνακα "
                        + "είναι 0 ή αρνητικό.");
                return;
            }

            String sql = "SELECT name, amount FROM " + table;

            try (Connection conn = DriverManager.getConnection(dbPath);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                StringBuilder sb = new StringBuilder();
                sb.append(String.format("%-30s | %-15s | %-10s\n",
                        "ΣΤΟΙΧΕΙΟ", "ΠΟΣΟ", "ΠΟΣΟΣΤΟ %"));
                sb.append("-".repeat(SEPARATOR_REPEAT)).append("\n");

                while (rs.next()) {
                    String name = rs.getString("name");
                    double amount = rs.getDouble("amount");
                    double percent = (amount / totalAmount) * PERCENT_MULT;

                    // Κόψιμο ονόματος αν είναι πολύ μεγάλο
                    String displayName;
                    if (name.length() > MAX_NAME_LEN) {
                        displayName = name.substring(0, MAX_NAME_LEN) + DOTS;
                    } else {
                        displayName = name;
                    }

                    sb.append(String.format("%-30s | %15.2f | %9.2f%%\n",
                            displayName, amount, percent));
                }

                resultArea.setText(sb.toString());
                resultArea.setCaretPosition(0);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Σφάλμα κατά την ανάκτηση: " + ex.getMessage());
            }
        });

        setVisible(true);
    }

    /**
     * Εσωτερική κλάση για την αντιστοίχιση ονόματος εμφάνισης και
     * ονόματος πίνακα στη βάση.
     */
    private static final class TableOption {

        /** Το εμφανιζόμενο όνομα (Label). */
        private final String label;
        /** Η τιμή για τη βάση δεδομένων (Value). */
        private final String value;

        /**
         * Κατασκευαστής.
         *
         * @param lbl Το όνομα εμφάνισης.
         * @param val Το όνομα πίνακα στη βάση.
         */
        TableOption(final String lbl, final String val) {
            this.label = lbl;
            this.value = val;
        }

        /**
         * Επιστρέφει την τιμή για τη βάση.
         *
         * @return Το όνομα του πίνακα.
         */
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return label;
        }
    }
}
