package mainapp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.io.Serial;

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

/**
 * Η αρχική οθόνη εκκίνησης (Launcher) της εφαρμογής.
 */
public final class StateWalletLauncher extends JFrame {

    /** Serial Version UID. */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Πλάτος παραθύρου. */
    private static final int WINDOW_WIDTH = 450;
    /** Ύψος παραθύρου. */
    private static final int WINDOW_HEIGHT = 350;
    /** Κάθετο περιθώριο. */
    private static final int BORDER_VERT = 30;
    /** Οριζόντιο περιθώριο. */
    private static final int BORDER_HORIZ = 40;

    /** Μέγεθος Emoji. */
    private static final int FONT_SIZE_EMOJI = 70;
    /** Μέγεθος Τίτλου. */
    private static final int FONT_SIZE_TITLE = 24;
    /** Μέγεθος Υπότιτλου. */
    private static final int FONT_SIZE_SUBTITLE = 12;
    /** Μέγεθος Κειμένου. */
    private static final int FONT_SIZE_NORMAL = 14;
    /** Μέγεθος Μικρού Κειμένου. */
    private static final int FONT_SIZE_SMALL = 11;

    /** Πλάτος Combo. */
    private static final int COMBO_WIDTH = 150;
    /** Ύψος Combo. */
    private static final int COMBO_HEIGHT = 30;
    /** Πλάτος κουμπιού. */
    private static final int BTN_WIDTH = 200;
    /** Ύψος κουμπιού. */
    private static final int BTN_HEIGHT = 40;
    /** Πλάτος μπάρας προόδου. */
    private static final int PROGRESS_WIDTH = 200;
    /** Ύψος μπάρας προόδου. */
    private static final int PROGRESS_HEIGHT = 5;

    /** Πολύ μικρό κενό. */
    private static final int GAP_TINY = 5;
    /** Μικρό κενό. */
    private static final int GAP_SMALL = 15;
    /** Μεσαίο κενό. */
    private static final int GAP_MEDIUM = 20;
    /** Μεγάλο κενό. */
    private static final int GAP_LARGE = 30;

    /** Καθυστέρηση. */
    private static final int DELAY_MS = 500;
    /** Μέγιστη πρόοδος. */
    private static final int PROGRESS_MAX = 100;

    /** Βασικό χρώμα. */
    private static final Color PRIMARY = new Color(70, 130, 180);
    /** Χρώμα φόντου. */
    private static final Color BG = new Color(245, 245, 250);

    /** Επιλογέας έτους. */
    private final JComboBox<String> yearSelector;
    /** Κουμπί έναρξης. */
    private final JButton startButton;
    /** Ετικέτα κατάστασης. */
    private final JLabel statusLabel;
    /** Μπάρα προόδου. */
    private final JProgressBar progressBar;

    /**
     * Κατασκευαστής.
     */
    public StateWalletLauncher() {

        setTitle("State Wallet");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BG);
        mainPanel.setBorder(new EmptyBorder(BORDER_VERT, BORDER_HORIZ,
                BORDER_VERT, BORDER_HORIZ));

        addComp(mainPanel, new JLabel("🏛️"),
                new Font("Segoe UI Emoji", Font.PLAIN, FONT_SIZE_EMOJI),
                Color.BLACK, GAP_TINY);

        addComp(mainPanel, new JLabel("State Wallet"),
                new Font("Segoe UI", Font.BOLD, FONT_SIZE_TITLE),
                PRIMARY, GAP_TINY);

        addComp(mainPanel, new JLabel("Επιλέξτε οικονομικό έτος"),
                new Font("Segoe UI", Font.PLAIN, FONT_SIZE_SUBTITLE),
                Color.GRAY, GAP_LARGE);

        yearSelector = new JComboBox<>(
                new String[]{"2023", "2024", "2025", "2026"});
        yearSelector.setMaximumSize(new Dimension(COMBO_WIDTH, COMBO_HEIGHT));
        yearSelector.setBackground(Color.WHITE);
        addComp(mainPanel, yearSelector,
                new Font("Segoe UI", Font.PLAIN, FONT_SIZE_NORMAL),
                Color.BLACK, GAP_SMALL);

        startButton = new JButton("Φόρτωση & Είσοδος");
        startButton.setBackground(PRIMARY);
        startButton.setForeground(Color.BLACK);
        startButton.setFocusPainted(false);
        startButton.setMaximumSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addComp(mainPanel, startButton,
                new Font("Segoe UI", Font.BOLD, FONT_SIZE_NORMAL),
                null, GAP_MEDIUM);

        progressBar = new JProgressBar();
        progressBar.setMaximumSize(
                new Dimension(PROGRESS_WIDTH, PROGRESS_HEIGHT));
        progressBar.setForeground(PRIMARY);
        progressBar.setVisible(false);
        addComp(mainPanel, progressBar, null, null, GAP_TINY);

        statusLabel = new JLabel("Αναμονή...");
        addComp(mainPanel, statusLabel,
                new Font("Segoe UI", Font.ITALIC, FONT_SIZE_SMALL),
                Color.GRAY, 0);

        add(mainPanel);
        startButton.addActionListener(e -> startProcess());
    }

    private void addComp(final JPanel p, final JComponent c,
                         final Font f, final Color col, final int gap) {
        if (f != null) {
            c.setFont(f);
        }
        if (col != null) {
            c.setForeground(col);
        }
        c.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(c);
        if (gap > 0) {
            p.add(Box.createRigidArea(new Dimension(0, gap)));
        }
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
                    int choice = JOptionPane.showConfirmDialog(
                            null,
                            "Η βάση δεδομένων για το έτος " + year
                            + " βρέθηκε.\nΘέλετε να ξεκινήσετε την "
                            + "επεξεργασία από την αρχή;",
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
                    SwingUtilities.invokeLater(() -> statusLabel.setText(
                            "Επεξεργασία δεδομένων (από την αρχή)..."));
                    // Εκτέλεση επεξεργασίας
                    Csvtopdf.run(year);
                    new PinakesImporter("jdbc:sqlite:budget.db").importAll();
                } else {
                    SwingUtilities.invokeLater(() -> statusLabel.setText(
                            "Φόρτωση υπάρχουσας βάσης..."));
                    // Μικρή παύση για την εμπειρία χρήστη
                    Thread.sleep(DELAY_MS);
                }

                SwingUtilities.invokeLater(() -> {
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(PROGRESS_MAX);
                    statusLabel.setText("Ολοκληρώθηκε!");

                    Timer timer = new Timer(DELAY_MS, e -> {
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
                    JOptionPane.showMessageDialog(this,
                            "Σφάλμα: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
    }
}
