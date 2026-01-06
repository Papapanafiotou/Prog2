package mainapp;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
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

public class PercentageUI extends JFrame {

    public PercentageUI(BudgetManager manager, String dbPath) {
        setTitle("Ανάλυση Ποσοστών (Κείμενο)");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

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
        comboTable.addItem(new TableOption("Αποκεντρωμένες Διοικήσεις", "apokentromenes"));

        controlPanel.add(comboTable);

        // Κουμπί υπολογισμού
        JButton btnCalculate = new JButton("Υπολογισμός Ποσοστών");
        controlPanel.add(btnCalculate);

        add(controlPanel, BorderLayout.NORTH);

        // Περιοχή αποτελεσμάτων
        JTextArea resultArea = new JTextArea();
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, BorderLayout.CENTER);

        // Action Listener για το κουμπί
        btnCalculate.addActionListener(e -> {
            // 1. Παίρνουμε το αντικείμενο TableOption
            TableOption selectedOption = (TableOption) comboTable.getSelectedItem();

            if (selectedOption == null) return;

            // 2. Παίρνουμε την "κρυφή" τιμή για τη βάση
            String table = selectedOption.getValue();

            double[] totals = manager.getTotal(table);
            double totalAmount = totals[1]; // Τρέχον σύνολο

            if (totalAmount <= 0) {
                resultArea.setText("Σφάλμα: Το συνολικό ποσό του πίνακα είναι 0 ή αρνητικό.");
                return;
            }

            try (Connection conn = DriverManager.getConnection(dbPath);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT name, amount FROM " + table)) {

                StringBuilder sb = new StringBuilder();
                sb.append(String.format("%-30s | %-15s | %-10s\n", "ΣΤΟΙΧΕΙΟ", "ΠΟΣΟ", "ΠΟΣΟΣΤΟ %"));
                sb.append("-".repeat(65) + "\n");

                while (rs.next()) {
                    String name = rs.getString("name");
                    double amount = rs.getDouble("amount");
                    double percent = (amount / totalAmount) * 100;

                    // Κόψιμο ονόματος αν είναι πολύ μεγάλο
                    String displayName = name.length() > 27 ? name.substring(0, 27) + "..." : name;

                    sb.append(String.format("%-30s | %15.2f | %9.2f%%\n",
                            displayName, amount, percent));
                }

                resultArea.setText(sb.toString());
                resultArea.setCaretPosition(0);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Σφάλμα κατά την ανάκτηση: " + ex.getMessage());
            }
        });

        setVisible(true);
    } // Τέλος του Constructor

    // Εσωτερική κλάση (Inner Class) για τις επιλογές
    private static class TableOption {
        private String label;  // Αυτό που βλέπει ο χρήστης
        private String value;  // Αυτό που θέλεις για τη βάση

        public TableOption(String label, String value) {
            this.label = label;
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return label; // Επιστρέφει το Label για να φανεί στο ComboBox
        }
    }
} 