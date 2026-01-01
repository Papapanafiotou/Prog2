package mainapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 * Η γραφική διεπαφή χρήστη (GUI) για την επεξεργασία του προϋπολογισμού.
 */
public final class BudgetGUI extends JFrame {

    /** Serial Version UID. */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Πλάτος παραθύρου. */
    private static final int WINDOW_WIDTH = 950;
    /** Ύψος παραθύρου. */
    private static final int WINDOW_HEIGHT = 600;
    /** Μέγεθος πεδίου ID. */
    private static final int ID_FIELD_SIZE = 8;
    /** Μέγεθος πεδίου ποσού. */
    private static final int AMOUNT_FIELD_SIZE = 10;
    /** Γραμμές περιοχής κειμένου. */
    private static final int TEXT_AREA_ROWS = 8;
    /** Στήλες περιοχής κειμένου. */
    private static final int TEXT_AREA_COLS = 50;
    /** Μέγεθος κενού. */
    private static final int GAP_SIZE = 20;
    /** Δείκτης στήλης ποσού στον πίνακα. */
    private static final int TABLE_COL_AMOUNT = 3;
    /** Μέγεθος γραμματοσειράς κατάστασης. */
    private static final int STATUS_FONT_SIZE = 14;
    /** Μέγεθος γραμματοσειράς κονσόλας. */
    private static final int CONSOLE_FONT_SIZE = 12;
    /** Κενό Border Layout. */
    private static final int BORDER_GAP = 8;
    /** Κενό Panel Layout. */
    private static final int PANEL_GAP = 5;

    /** Χρώμα για θετικό προϋπολογισμό. */
    private static final Color SUCCESS_COLOR = new Color(34, 139, 34);

    /** Διαχειριστής προϋπολογισμού. */
    private final BudgetManager manager;
    /** Διαδρομή βάσης δεδομένων. */
    private final String dbPath;
    /** Dropdown επιλογής πίνακα. */
    private JComboBox<TableInfo> tableSelector;
    /** Κουμπί φόρτωσης. */
    private JButton loadTableButton;
    /** Πίνακας δεδομένων. */
    private JTable dataTable;
    /** Μοντέλο πίνακα. */
    private DefaultTableModel tableModel;
    /** Πεδίο κειμένου ID. */
    private JTextField idField;
    /** Πεδίο κειμένου ποσού. */
    private JTextField amountField;
    /** Κουμπί ενημέρωσης. */
    private JButton updateButton;
    /** Κουμπί επιστροφής. */
    private JButton backButton;
    /** Κουμπί εμφάνισης αλλαγών. */
    private JButton showChangesButton;
    /** Περιοχή κειμένου αλλαγών. */
    private JTextArea changesArea;
    /** Ετικέτα κατάστασης προϋπολογισμού. */
    private JLabel budgetStatusLabel;
    /** Κουμπί εμφάνισης συνόλων. */
    private JButton showTotalsButton;

    /**
     * Κατασκευαστής του BudgetGUI.
     *
     * @param path Η διαδρομή της βάσης δεδομένων.
     */
    public BudgetGUI(final String path) {
        this.dbPath = path;
        this.manager = new BudgetManager(path);

        setTitle("Διαχείριση Προϋπολογισμού");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);

        initComponents();
        initLayout();
        initListeners();
    }

    private void initComponents() {
        backButton = new JButton("⬅ Πίσω");
        showTotalsButton = new JButton("📊 Εμφάνιση Συνόλων");
        tableSelector = new JComboBox<>();
        tableSelector.addItem(new TableInfo("Έσοδα", "esoda", "code"));
        tableSelector.addItem(new TableInfo("Έξοδα", "eksoda", "code"));
        tableSelector.addItem(new TableInfo("Κράτος", "kratos", "number"));
        tableSelector.addItem(new TableInfo(
                "Υπουργεία", "ypourgeia", "number"));
        tableSelector.addItem(new TableInfo(
                "Αποκεντρωμένες Διοικήσεις", "apokentromenes", "number"));

        loadTableButton = new JButton("Εμφάνιση Πίνακα");
        budgetStatusLabel = new JLabel("Χαρακτηρισμός: -");
        budgetStatusLabel.setFont(new Font("Segoe UI", Font.BOLD,
                STATUS_FONT_SIZE));

        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Περιγραφή", "Αρχικό Ποσό", "Τρέχον Ποσό"},
                0
        ) {
            @Override
            public boolean isCellEditable(final int row, final int column) {
                return false;
            }
        };
        dataTable = new JTable(tableModel);
        dataTable.setFillsViewportHeight(true);

        idField = new JTextField(ID_FIELD_SIZE);
        amountField = new JTextField(AMOUNT_FIELD_SIZE);
        updateButton = new JButton("Αλλαγή Ποσού");

        showChangesButton = new JButton("Εμφάνιση Αλλαγών");
        changesArea = new JTextArea(TEXT_AREA_ROWS, TEXT_AREA_COLS);
        changesArea.setEditable(false);
        changesArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN,
                CONSOLE_FONT_SIZE));
    }

    private void initLayout() {
        setLayout(new BorderLayout(BORDER_GAP, BORDER_GAP));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);
        topPanel.add(new JLabel("Πίνακας:"));
        topPanel.add(tableSelector);
        topPanel.add(loadTableButton);
        topPanel.add(showTotalsButton);
        topPanel.add(Box.createRigidArea(new Dimension(GAP_SIZE, 0)));
        topPanel.add(budgetStatusLabel);

        JScrollPane tableScroll = new JScrollPane(dataTable);

        JPanel bottomPanel = new JPanel(new BorderLayout(PANEL_GAP, PANEL_GAP));

        JPanel updatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        updatePanel.setBorder(BorderFactory.createTitledBorder(
                "Αλλαγή στοιχείου προϋπολογισμού"));
        updatePanel.add(new JLabel("ID:"));
        updatePanel.add(idField);
        updatePanel.add(new JLabel("Νέο ποσό:"));
        updatePanel.add(amountField);
        updatePanel.add(updateButton);

        JPanel changesPanel = new JPanel(new BorderLayout());
        changesPanel.setBorder(
                BorderFactory.createTitledBorder("Αλλαγές προϋπολογισμού"));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(showChangesButton);
        changesPanel.add(btnPanel, BorderLayout.NORTH);
        changesPanel.add(new JScrollPane(changesArea), BorderLayout.CENTER);

        bottomPanel.add(updatePanel, BorderLayout.NORTH);
        bottomPanel.add(changesPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void initListeners() {
        backButton.addActionListener(e -> {
            this.dispose();
            new StateWalletLauncher().setVisible(true);
        });

        loadTableButton.addActionListener(e -> loadSelectedTable());

        dataTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = dataTable.getSelectedRow();
                    if (row >= 0) {
                        Object idVal = tableModel.getValueAt(row, 0);
                        Object amountVal = tableModel.getValueAt(
                                row, TABLE_COL_AMOUNT);
                        idField.setText(Objects.toString(idVal, ""));
                        amountField.setText(Objects.toString(amountVal, ""));
                    }
                }
            }
        });

        updateButton.addActionListener(e -> updateAmount());
        showChangesButton.addActionListener(e -> loadChangesFromDb());
        showTotalsButton.addActionListener(e -> {
    TableInfo info = (TableInfo) tableSelector.getSelectedItem();

    if (info == null) {
        JOptionPane.showMessageDialog(this,
                "Δεν έχει επιλεγεί πίνακας.",
                "Προειδοποίηση",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    TotalsPanel panel = new TotalsPanel(manager);
    panel.updateTotals(info.tableName);

    JOptionPane.showMessageDialog(
            this,
            panel,
            "Σύνολα Πίνακα: " + info.displayName,
            JOptionPane.PLAIN_MESSAGE
    );
});
    }

    private void loadSelectedTable() {
        TableInfo info = (TableInfo) tableSelector.getSelectedItem();
        if (info == null) {
            return;
        }
        tableModel.setRowCount(0);
        String sql = "SELECT " + info.idColumnName
                + ", name, original_amount, amount FROM " + info.tableName;

        try (Connection conn = DriverManager.getConnection(dbPath);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getInt(info.idColumnName),
                    rs.getString("name"),
                    rs.getDouble("original_amount"),
                    rs.getDouble("amount")
                };
                tableModel.addRow(row);
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Σφάλμα: " + ex.getMessage(),
                    "Σφάλμα",
                    JOptionPane.ERROR_MESSAGE);
        }
        updateBudgetUI();
    }

    private void updateAmount() {
        TableInfo info = (TableInfo) tableSelector.getSelectedItem();
        if (info == null) {
            return;
        }
        String idText = idField.getText().trim();
        String amountText = amountField.getText().trim();
        if (idText.isEmpty() || amountText.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Συμπλήρωσε ID και νέο ποσό.",
                    "Προειδοποίηση",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int id = Integer.parseInt(idText);
            double newAmount = Double.parseDouble(amountText);
            if (newAmount < 0) {
                int option = JOptionPane.showConfirmDialog(this,
                        "Το νέο ποσό είναι αρνητικό. Συνέχεια;",
                        "Επιβεβαίωση",
                        JOptionPane.YES_NO_OPTION);
                if (option != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            boolean success = manager.updateAmount(
                    info.tableName, info.idColumnName, id, newAmount);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Επιτυχής ενημέρωση!",
                        "Επιτυχία",
                        JOptionPane.INFORMATION_MESSAGE);
                loadSelectedTable();
                updateBudgetUI();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Αποτυχία: Δεν βρέθηκε το ID.",
                        "Αποτυχία",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Λάθος μορφή αριθμών.",
                    "Σφάλμα",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadChangesFromDb() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Αλλαγές Προϋπολογισμού ---\n");
        boolean foundAny = false;

        TableInfo[] tables = new TableInfo[]{
            new TableInfo("Έσοδα", "esoda", "code"),
            new TableInfo("Έξοδα", "eksoda", "code"),
            new TableInfo("Κράτος", "kratos", "number"),
            new TableInfo("Υπουργεία", "ypourgeia", "number"),
            new TableInfo("Αποκεντρωμένες", "apokentromenes", "number")
        };
        for (TableInfo info : tables) {
            String sql = "SELECT " + info.idColumnName
                    + ", name, amount, original_amount FROM "
                    + info.tableName + " WHERE amount != original_amount";

            try (Connection conn = DriverManager.getConnection(dbPath);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                boolean tableHasChanges = false;
                while (rs.next()) {
                    if (!tableHasChanges) {
                        sb.append("\nAλλαγές στον πίνακα: ")
                                .append(info.displayName).append("\n");
                        tableHasChanges = true;
                        foundAny = true;
                    }
                    sb.append(String.format(
                            "ID: %-3d | %-20s | Αρχικό: %.2f | Νέο: %.2f%n",
                            rs.getInt(info.idColumnName),
                            rs.getString("name"),
                            rs.getDouble("original_amount"),
                            rs.getDouble("amount")
                    ));
                }

            } catch (SQLException e) {
                sb.append("Σφάλμα: ").append(e.getMessage()).append("\n");
            }
        }
        if (!foundAny) {
            sb.append("Δεν βρέθηκαν αλλαγές.\n");
        }
        changesArea.setText(sb.toString());
        changesArea.setCaretPosition(0);
    }

    private void updateBudgetUI() {
        double[] rev = manager.getTotal("esoda");
        double[] exp = manager.getTotal("eksoda");
        String statusText = manager.getBudgetCharacterism(rev[1], exp[1]);
        budgetStatusLabel.setText("Χαρακτηρισμός: " + statusText);

        if (rev[1] > exp[1]) {
            budgetStatusLabel.setForeground(SUCCESS_COLOR);
        } else if (rev[1] < exp[1]) {
            budgetStatusLabel.setForeground(Color.RED);
        } else {
            budgetStatusLabel.setForeground(Color.BLUE);
        }
    }

    /**
     * Εσωτερική κλάση που κρατά πληροφορίες για κάθε πίνακα.
     */
    private static class TableInfo {

        /** Το εμφανιζόμενο όνομα. */
        private final String displayName;
        /** Το όνομα του πίνακα στη βάση. */
        private final String tableName;
        /** Το όνομα της στήλης ID. */
        private final String idColumnName;

        /**
         * Κατασκευαστής.
         *
         * @param display Το εμφανιζόμενο όνομα.
         * @param table   Το όνομα πίνακα.
         * @param idCol   Το όνομα στήλης ID.
         */
        TableInfo(final String display, final String table,
                  final String idCol) {
            this.displayName = display;
            this.tableName = table;
            this.idColumnName = idCol;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
}
