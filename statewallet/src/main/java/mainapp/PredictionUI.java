package mainapp;

import java.awt.BorderLayout;
import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class PredictionUI extends JFrame {
    private final String tableName;
    private final String idColName;
    private final int idValue;
    private final String itemName;

    // Ο Constructor δέχεται πλέον τα στοιχεία του επιλεγμένου ID
    public PredictionUI(String dbPath, String tableName, String idColName, int idValue, String itemName) {
        this.tableName = tableName;
        this.idColName = idColName;
        this.idValue = idValue;
        this.itemName = itemName;

        setTitle("Πρόβλεψη 2027 - " + itemName);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setText("Στοιχείο: " + itemName + "\nID: " + idValue + "\n" +
                          "Αναζήτηση ιστορικών δεδομένων (2023-2026)...");
        
        JButton runAnalysisBtn = new JButton("📊 Έναρξη Στατιστικής Ανάλυσης");
        runAnalysisBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        runAnalysisBtn.addActionListener(e -> {
            Map<Integer, Double> history = collectDataFromYears();

            if (history.size() < 2) {
                reportArea.setText("Σφάλμα: Δεν βρέθηκαν επαρκή δεδομένα για το '" + itemName + "'\n" +
                                   "στα αρχεία budget_2023.db έως budget_2026.db.");
                return;
            }

            // Υπολογισμός Γραμμικής Παλινδρόμησης (y = mx + b)
            double n = history.size();
            double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;
            for (Map.Entry<Integer, Double> entry : history.entrySet()) {
                double x = entry.getKey();
                double y = entry.getValue();
                sumX += x; sumY += y;
                sumXY += (x * y); sumX2 += (x * x);
            }

            double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
            double intercept = (sumY - slope * sumX) / n;
            double predicted2027 = slope * 2027 + intercept;

            StringBuilder sb = new StringBuilder();
            sb.append("--- ΣΤΑΤΙΣΤΙΚΗ ΑΝΑΦΟΡΑ ---\n");
            sb.append("Στοιχείο: ").append(itemName).append("\n\n");
            history.forEach((year, val) -> sb.append(String.format("Έτος %d: %,.2f €\n", year, val)));
            sb.append("-----------------------------\n");
            sb.append(String.format("ΠΡΟΒΛΕΨΗ ΓΙΑ ΤΟ 2027: %,.2f €\n", predicted2027));
            sb.append("-----------------------------\n");
            sb.append(String.format("Τάση: %s\n", (slope > 0 ? "Ανοδική ▲" : "Καθοδική ▼")));
            
            reportArea.setText(sb.toString());
        });

        add(new JLabel("Πρόβλεψη βάσει Ιστορικών Στοιχείων", SwingConstants.CENTER), BorderLayout.NORTH);
        add(new JScrollPane(reportArea), BorderLayout.CENTER);
        add(runAnalysisBtn, BorderLayout.SOUTH);
    }

    private Map<Integer, Double> collectDataFromYears() {
        Map<Integer, Double> history = new LinkedHashMap<>();
        for (int year = 2023; year <= 2026; year++) {
            String yearDbUrl = "jdbc:sqlite:budget_" + year + ".db";
            BudgetManager tempManager = new BudgetManager(yearDbUrl);
            // Χρήση της μεθόδου ανάκτησης ποσού
            double amount = tempManager.getCurrentAmount(tableName, idColName, idValue);
            if (amount != -1) {
                history.put(year, amount);
            }
        }
        return history;
    }
}