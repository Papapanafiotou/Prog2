package mainapp;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

public class PredictionUI extends JFrame {
    
    public PredictionUI(String dbPath) {
        setTitle("Οικονομική Πρόβλεψη 2027");
        setSize(500, 400);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout(10, 10));

        // Περιοχή αποτελεσμάτων
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setText("--- Οδηγίες ---\nΠατήστε το κουμπί 'Ανάλυση' για να υπολογιστεί\n" +
                          "η πρόβλεψη βάσει των ετών 2023-2026.");
        
        JButton runAnalysisBtn = new JButton("📊 Έναρξη Στατιστικής Ανάλυσης");
        runAnalysisBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        runAnalysisBtn.addActionListener(e -> {
            reportArea.setText("Εκτελείται υπολογισμός y = mx + b...\nΑνάλυση ιστορικών δεδομένων...\n\nΠρόβλεψη για το 2027: [Αποτέλεσμα]");
        });

        add(new JLabel("Στατιστικό Μοντέλο Πρόβλεψης", SwingConstants.CENTER), BorderLayout.NORTH);
        add(new JScrollPane(reportArea), BorderLayout.CENTER);
        add(runAnalysisBtn, BorderLayout.SOUTH);
    }
}