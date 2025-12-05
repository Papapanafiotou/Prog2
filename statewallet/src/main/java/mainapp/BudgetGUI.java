package mainapp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Objects;

public class BudgetGUI extends JFrame {

    private final BudgetManager manager;     // χρησιμοποιούμε τον δικό σου BudgetManager
    private final DatabaseHandler dbHandler; // δικός σου handler για τη βάση

    private JComboBox<TableInfo> tableSelector;
    private JButton loadTableButton;

    private JTable dataTable;
    private DefaultTableModel tableModel;

    private JTextField idField;
    private JTextField amountField;
    private JButton updateButton;

    private JButton showChangesButton;
    private JTextArea changesArea;

    public BudgetGUI() {
        this.manager = new BudgetManager();
        this.dbHandler = new DatabaseHandler();

        setTitle("Διαχείριση Προϋπολογισμού");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);

        initComponents();
        initLayout();
        initListeners();
    }

    // ----------------- Αρχικοποίηση Components -----------------

    private void initComponents() {
        // Επιλογή πίνακα (αντί για μενού στην κονσόλα)
        tableSelector = new JComboBox<>();
        tableSelector.addItem(new TableInfo("Έσοδα", "esoda", "code"));
        tableSelector.addItem(new TableInfo("Έξοδα", "eksoda", "code"));
        tableSelector.addItem(new TableInfo("Κράτος", "kratos", "number"));
        tableSelector.addItem(new TableInfo("Υπουργεία", "ypourgeia", "number"));
        tableSelector.addItem(new TableInfo("Αποκεντρωμένες Διοικήσεις", "apokentromenes", "number"));

        loadTableButton = new JButton("Εμφάνιση Πίνακα");

        // Πίνακας δεδομένων
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Περιγραφή", "Αρχικό Ποσό", "Τρέχον Ποσό"},
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // μόνο ανάγνωση
            }
        };
        dataTable = new JTable(tableModel);
        dataTable.setFillsViewportHeight(true);

        // Πεδία για αλλαγή ποσού
        idField = new JTextField(8);
        amountField = new JTextField(10);
        updateButton = new JButton("Αλλαγή Ποσού");

        // Περιοχή εμφάνισης αλλαγών
        showChangesButton = new JButton("Εμφάνιση Αλλαγών");
        changesArea = new JTextArea(8, 50);
        changesArea.setEditable(false);
        changesArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    }

    private void initLayout() {
        setLayout(new BorderLayout(8, 8));

        // Πάνω: επιλογή πίνακα
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Πίνακας:"));
        topPanel.add(tableSelector);
        topPanel.add(loadTableButton);

        // Κέντρο: πίνακας δεδομένων
        JScrollPane tableScroll = new JScrollPane(dataTable);

        // Κάτω: αλλαγή ποσού + αλλαγές
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));

        JPanel updatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        updatePanel.setBorder(BorderFactory.createTitledBorder("Αλλαγή στοιχείου προϋπολογισμού"));
        updatePanel.add(new JLabel("ID:"));
        updatePanel.add(idField);
        updatePanel.add(new JLabel("Νέο ποσό:"));
        updatePanel.add(amountField);
        updatePanel.add(updateButton);

        JPanel changesPanel = new JPanel(new BorderLayout());
        changesPanel.setBorder(BorderFactory.createTitledBorder("Αλλαγές προϋπολογισμού"));
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
        // Εμφάνιση επιλεγμένου πίνακα
        loadTableButton.addActionListener(e -> loadSelectedTable());

        // Διπλό κλικ σε γραμμή -> γέμισμα ID & ποσού
        dataTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = dataTable.getSelectedRow();
                    if (row >= 0) {
                        Object idVal = tableModel.getValueAt(row, 0);
                        Object amountVal = tableModel.getValueAt(row, 3);
                        idField.setText(Objects.toString(idVal, ""));
                        amountField.setText(Objects.toString(amountVal, ""));
                    }
                }
            }
        });

        // Κουμπί αλλαγής ποσού
        updateButton.addActionListener(e -> updateAmount());

        // Κουμπί εμφάνισης αλλαγών
        showChangesButton.addActionListener(e -> loadChangesFromDb());
        // Αν προτιμάς να χρησιμοποιήσεις την ήδη υπάρχουσα showChanges της κονσόλας,
        // μπορείς απλά να κάνεις: manager.showChanges(); (θα τυπώσει στην κονσόλα)
    }

    // ----------------- ΛΟΓΙΚΗ GUI -----------------

    // Παρόμοιο με printTable, αλλά τα δείχνουμε σε JTable αντί για System.out
    private void loadSelectedTable() {
        TableInfo info = (TableInfo) tableSelector.getSelectedItem();
        if (info == null) return;

        tableModel.setRowCount(0); // καθάρισμα

        String sql = "SELECT " + info.idColumnName + ", name, original_amount, amount FROM " + info.tableName;

        try (Connection conn = dbHandler.connect();
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
                    "Σφάλμα κατά την εμφάνιση του πίνακα " + info.tableName + ":\n" + ex.getMessage(),
                    "Σφάλμα",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Χρησιμοποιούμε τον ΥΠΑΡΧΟΝ BudgetManager.updateAmount
    private void updateAmount() {
        TableInfo info = (TableInfo) tableSelector.getSelectedItem();
        if (info == null) return;

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
                        "Το νέο ποσό είναι αρνητικό. Θέλεις να συνεχίσεις;",
                        "Επιβεβαίωση",
                        JOptionPane.YES_NO_OPTION);
                if (option != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            boolean success = manager.updateAmount(info.tableName, info.idColumnName, id, newAmount);
            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Επιτυχής ενημέρωση!",
                        "Επιτυχία",
                        JOptionPane.INFORMATION_MESSAGE);
                loadSelectedTable(); // ανανέωση πίνακα
            } else {
                JOptionPane.showMessageDialog(this,
                        "Αποτυχία: Δεν βρέθηκε το ID.",
                        "Αποτυχία",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Λάθος μορφή αριθμών. To ID πρέπει να είναι ακέραιος και το ποσό αριθμός.",
                    "Σφάλμα",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Παρόμοιο με showChanges/checkTableForChanges, αλλά τα δείχνουμε σε JTextArea
    private void loadChangesFromDb() {
        StringBuilder sb = new StringBuilder();
        sb.append("--- Αλλαγές Προϋπολογισμού (σε όλους τους πίνακες) ---\n");
        boolean foundAny = false;

        TableInfo[] tables = new TableInfo[]{
                new TableInfo("Έσοδα", "esoda", "code"),
                new TableInfo("Έξοδα", "eksoda", "code"),
                new TableInfo("Κράτος", "kratos", "number"),
                new TableInfo("Υπουργεία", "ypourgeia", "number"),
                new TableInfo("Αποκεντρωμένες Διοικήσεις", "apokentromenes", "number")
        };

        for (TableInfo info : tables) {
            String sql = "SELECT " + info.idColumnName + ", name, amount, original_amount FROM "
                    + info.tableName + " WHERE amount != original_amount";

            try (Connection conn = dbHandler.connect();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                boolean tableHasChanges = false;
                while (rs.next()) {
                    if (!tableHasChanges) {
                        sb.append("\nΑλλαγές στον πίνακα: ").append(info.displayName).append("\n");
                        tableHasChanges = true;
                        foundAny = true;
                    }
                    sb.append(String.format(
                            "ID: %-3d | %-30s | Αρχικό: %10.2f | Νέο: %10.2f%n",
                            rs.getInt(info.idColumnName),
                            rs.getString("name"),
                            rs.getDouble("original_amount"),
                            rs.getDouble("amount")
                    ));
                }

            } catch (SQLException e) {
                sb.append("Σφάλμα ελέγχου αλλαγών στο ")
                  .append(info.displayName).append(": ")
                  .append(e.getMessage()).append("\n");
            }
        }

        if (!foundAny) {
            sb.append("Δεν βρέθηκαν αλλαγές σε κανέναν πίνακα.\n");
        }

        changesArea.setText(sb.toString());
        changesArea.setCaretPosition(0);
    }

    // Βοηθητική κλάση για το comboBox
    private static class TableInfo {
        final String displayName;
        final String tableName;
        final String idColumnName;

        TableInfo(String displayName, String tableName, String idColumnName) {
            this.displayName = displayName;
            this.tableName = tableName;
            this.idColumnName = idColumnName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }
    public static void main(String[] args) {
       try {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
    e.printStackTrace();
}
        javax.swing.SwingUtilities.invokeLater(() -> {
        BudgetGUI gui = new BudgetGUI();
        gui.setVisible(true);
    });
}
}