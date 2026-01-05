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
        
        JComboBox<String> comboTable = new JComboBox<>(new String[]{
            "Έσοδα", "έξοδα", "Κράτος", "Υπουργεία", "Αποκεντρωμένες"
        });
        controlPanel.add(comboTable);

        JButton btnCalculate = new JButton("Υπολογισμός Ποσοστών");
        controlPanel.add(btnCalculate);

        add(controlPanel, BorderLayout.NORTH);

        JTextArea resultArea = new JTextArea();
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, BorderLayout.CENTER);

        
        btnCalculate.addActionListener(e -> {
            String table = (String) comboTable.getSelectedItem();
            double[] totals = manager.getTotal(table);
            double totalAmount = totals[1]; // Χρησιμοποιούμε το τρέχον  σύνολο

            if (totalAmount <= 0) {
                resultArea.setText("Σφάλμα: Το συνολικό ποσό του πίνακα είναι 0 ή αρνητικό.");
                return;
            }

            try (Connection conn = DriverManager.getConnection(dbPath);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT name, amount FROM " + table)) {

                StringBuilder sb = new StringBuilder();
                sb.append(String.format("%-30s | %-15s | %-10s\n", "ΣΤΟΙΧΕΙΟ", "ΠΟΣΟ", "ΠΟΔΟΣΤΟ %"));
                sb.append("-".repeat(65) + "\n");

                while (rs.next()) {
                    String name = rs.getString("name");
                    double amount = rs.getDouble("amount");
                    double percent = (amount / totalAmount) * 100;

                    // Μορφοποίηση ονόματος αν είναι πολύ μεγάλο για την ευθυγράμμιση
                    String displayName = name.length() > 27 ? name.substring(0, 27) + "..." : name;

                    sb.append(String.format("%-30s | %15.2f | %9.2f%%\n", 
                        displayName, amount, percent));
                }

                resultArea.setText(sb.toString());
                resultArea.setCaretPosition(0); // Επαναφορά του scroll στην κορυφή

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Σφάλμα κατά την ανάκτηση: " + ex.getMessage());
            }
        });

        setVisible(true);
    }
}