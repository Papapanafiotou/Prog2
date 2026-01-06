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
    
    public PredictionUI(String dbPath) {
        setTitle("Οικονομική Πρόβλεψη 2027");
        setSize(500, 450);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLayout(new BorderLayout(10, 10));

        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setText("--- Οδηγίες ---\nΠατήστε το κουμπί 'Έναρξη' για να υπολογιστεί\n" +
                          "η πρόβλεψη βάσει των ετών 2023-2026.");
        
        JButton runAnalysisBtn = new JButton("📊 Έναρξη Στατιστικής Ανάλυσης");
        runAnalysisBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        runAnalysisBtn.addActionListener(e -> {
            // 1. Ιστορικά δεδομένα 
            Map<Integer, Double> history = new LinkedHashMap<>();
            history.put(2023, 100000.0);
            history.put(2024, 115000.0);
            history.put(2025, 128000.0);
            history.put(2026, 142000.0);

            // 2. Υπολογισμός Γραμμικής Παλινδρόμησης (y = mx + b)
            double n = history.size();
            double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

            for (Map.Entry<Integer, Double> entry : history.entrySet()) {
                double x = entry.getKey();
                double y = entry.getValue();
                sumX += x;
                sumY += y;
                sumXY += (x * y);
                sumX2 += (x * x);
            }
            double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
            double intercept = (sumY - slope * sumX) / n;
            
            // Πρόβλεψη για το 2027
            double predicted2027 = slope * 2027 + intercept;

            //  Εμφάνιση του πραγματικού αποτελέσματος
            StringBuilder sb = new StringBuilder();
            sb.append("--- ΑΠΟΤΕΛΕΣΜΑΤΑ ΑΝΑΛΥΣΗΣ ---\n\n");
            history.forEach((year, val) -> sb.append(String.format("Έτος %d: %,.2f €\n", year, val)));
            sb.append("-----------------------------\n");
            sb.append(String.format("ΠΡΟΒΛΕΨΗ ΓΙΑ ΤΟ 2027: %,.2f €\n", predicted2027));
            sb.append("-----------------------------\n");
            sb.append(String.format("Τάση: %s\n", (slope > 0 ? "Ανοδική ▲" : "Καθοδική ▼")));
            
            reportArea.setText(sb.toString());
        });

        add(new JLabel("Στατιστικό Μοντέλο Πρόβλεψης", SwingConstants.CENTER), BorderLayout.NORTH);
        add(new JScrollPane(reportArea), BorderLayout.CENTER);
        add(runAnalysisBtn, BorderLayout.SOUTH);
    }
}
