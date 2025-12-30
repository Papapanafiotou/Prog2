package mainapp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class StateWalletLauncher extends JFrame {
    
    private final JComboBox<String> yearSelector;
    private final JButton startButton;
    private final JLabel statusLabel;
    private final JProgressBar progressBar;

    private static final Color PRIMARY = new Color(70, 130, 180);
    private static final Color BG = new Color(245, 245, 250);

    // ο βασικος constructor
    public StateWalletLauncher() {

        setTitle("State Wallet");
        setSize(450, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BG);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        addComp(mainPanel, new JLabel("🏛️"), new Font("Segoe UI Emoji", Font.PLAIN, 70), Color.BLACK, 5);
        addComp(mainPanel, new JLabel("State Wallet"), new Font("Segoe UI", Font.BOLD, 24), PRIMARY, 5);
        addComp(mainPanel, new JLabel("Επιλέξτε οικονομικό έτος"), new Font("Segoe UI", Font.PLAIN, 12), Color.GRAY, 30);

        yearSelector = new JComboBox<>(new String[]{"2023", "2024", "2025", "2026"});
        yearSelector.setMaximumSize(new Dimension(150, 30));
        yearSelector.setBackground(Color.WHITE);
        addComp(mainPanel, yearSelector, new Font("Segoe UI", Font.PLAIN, 14), Color.BLACK, 15);

       startButton = new JButton("Φόρτωση & Είσοδος");
        startButton.setBackground(PRIMARY);
        startButton.setForeground(Color.BLACK); // Λευκά γράμματα για αντίθεση
        startButton.setFocusPainted(false);
        startButton.setMaximumSize(new Dimension(200, 40));
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addComp(mainPanel, startButton, new Font("Segoe UI", Font.BOLD, 14), null, 20);

        progressBar = new JProgressBar();
        progressBar.setMaximumSize(new Dimension(200, 5));
        progressBar.setForeground(PRIMARY);
        progressBar.setVisible(false);
        addComp(mainPanel, progressBar, null, null, 5);

        statusLabel = new JLabel("Αναμονή...");
        addComp(mainPanel, statusLabel, new Font("Segoe UI", Font.ITALIC, 11), Color.GRAY, 0);
        add(mainPanel);
        startButton.addActionListener(e -> startProcess());
    }
    private void addComp(JPanel p, JComponent c, Font f, Color col, int gap) {
        if (f != null) c.setFont(f);
        if (col != null) c.setForeground(col);
        c.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(c);
        if (gap > 0) p.add(Box.createRigidArea(new Dimension(0, gap)));
    }
private void startProcess() {
    int year = Integer.parseInt((String) yearSelector.getSelectedItem());
    DatabaseFinder finder = new DatabaseFinder();
    
    startButton.setEnabled(false);
    progressBar.setVisible(true);
    progressBar.setIndeterminate(true);

    new Thread(() -> {
        try {
            boolean reProcess = false;

            // Έλεγχος αν η βάση υπάρχει ήδη
            if (finder.findYearbase(year)) {
                // Ερώτηση στο χρήστη (Πρέπει να γίνει στο UI Thread)
                int choice = JOptionPane.showConfirmDialog(
                    null, 
                    "Η βάση δεδομένων για το έτος " + year + " βρέθηκε.\nΘέλετε να ξεκινήσετε την επεξεργασία από την αρχή;", 
                    "Υπάρχουσα Βάση", 
                    JOptionPane.YES_NO_OPTION
                );

                if (choice == JOptionPane.YES_OPTION) {
                    reProcess = true;
                }
            } else {
                // Αν δεν υπάρχει η βάση, η επεξεργασία είναι υποχρεωτική
                reProcess = true;
            }

            if (reProcess) {
                SwingUtilities.invokeLater(() -> statusLabel.setText("Επεξεργασία δεδομένων (από την αρχή)..."));
                // Εκτέλεση επεξεργασίας
                Csvtopdf.run(year);
                new PinakesImporter("jdbc:sqlite:budget.db").importAll(); 
            } else {
                SwingUtilities.invokeLater(() -> statusLabel.setText("Φόρτωση υπάρχουσας βάσης..."));
                Thread.sleep(500); // Μικρή παύση για την εμπειρία χρήστη
            }

            SwingUtilities.invokeLater(() -> {
                progressBar.setIndeterminate(false);
                progressBar.setValue(100);
                statusLabel.setText("Ολοκληρώθηκε!");

                Timer timer = new Timer(500, e -> {
                    dispose();
                    String dbPath = "jdbc:sqlite:budget_" + year + ".db";
                    new BudgetGUI(dbPath).setVisible(true);
                });
                timer.setRepeats(false);
                timer.start();
            });

        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> {
                progressBar.setVisible(false);
                statusLabel.setText("Σφάλμα!");
                startButton.setEnabled(true);
                JOptionPane.showMessageDialog(this, "Σφάλμα: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            });
        }
    }).start();
}
}

   





