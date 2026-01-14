package mainapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.Serial;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

/**
 * Η κλάση {@code AiAdvisorDialog} υλοποιεί το γραφικό περιβάλλον για τον AI
 * Οικονομικό Σύμβουλο.
 * <p>
 * Χρησιμοποιεί {@link JEditorPane} για την εμφάνιση των απαντήσεων του AI σε
 * μορφή HTML.
 * </p>
 */
public final class AiAdvisorDialog extends JDialog {

    @Serial
    private static final long serialVersionUID = 1L;

    // Διαστάσεις παραθύρου
    /** Πλάτος παραθύρου. */
    private static final int WIN_WIDTH = 850;
    /** Ύψος παραθύρου. */
    private static final int WIN_HEIGHT = 750;

    // Layout Constants
    /** Κενό περιθωρίων (Border Gap). */
    private static final int GAP_BORDER = 10;
    /** Εσωτερικό περιθώριο πάνελ (Padding). */
    private static final int PADDING_PANEL = 15;
    /** Κενό πλέγματος (Grid Gap). */
    private static final int GAP_GRID = 10;
    /** Αριθμός στηλών στο πλέγμα πληροφοριών. */
    private static final int INFO_COLS = 3;

    // Text Area Constants
    /** Αριθμός γραμμών περιοχής κειμένου. */
    private static final int TXT_ROWS = 4;
    /** Αριθμός στηλών περιοχής κειμένου. */
    private static final int TXT_COLS = 40;

    // Font Sizes
    /** Μέγεθος κανονικής γραμματοσειράς. */
    private static final int FONT_SIZE_NORMAL = 14;
    /** Μέγεθος γραμματοσειράς κουμπιών. */
    private static final int FONT_SIZE_BTN = 12;
    /** Μέγεθος γραμματοσειράς σφαλμάτων. */
    private static final int FONT_SIZE_ERR = 16;

    // Scroll Pane Dimensions
    /** Πλάτος περιοχής κύλισης. */
    private static final int SCROLL_W = 600;
    /** Ύψος περιοχής κύλισης. */
    private static final int SCROLL_H = 300;

    // Colors
    /** Τιμή γκρι χρώματος (RGB). */
    private static final int COL_GRAY = 230;

    // Λειτουργίες AI
    /** Λειτουργία γενικής στρατηγικής. */
    private static final String MODE_GLOBAL = "global";
    /** Λειτουργία συγκεκριμένης συμβουλής. */
    private static final String MODE_SPECIFIC = "specific";

    /** Η γέφυρα επικοινωνίας με το AI. */
    private final AiBridge aiBridge;
    /** Η διαδρομή της βάσης δεδομένων. */
    private final String dbPath;

    // Components
    /** Το πεδίο που εμφανίζει την απάντηση (υποστηρίζει HTML). */
    private JEditorPane responseArea;
    /** Το πάνελ καρτελών (Tabs). */
    private JTabbedPane tabbedPane;

    // Global Tab Input
    /** Πεδίο εισαγωγής στόχου για γενική στρατηγική. */
    private JTextArea globalGoalArea;

    // Specific Tab Inputs
    /** Πεδίο ID εγγραφής. */
    private JTextField idField;
    /** Πεδίο ονόματος εγγραφής. */
    private JTextField nameField;
    /** Πεδίο ποσού εγγραφής. */
    private JTextField amountField;
    /** Πεδίο εισαγωγής στόχου για συγκεκριμένη εγγραφή. */
    private JTextArea specificGoalArea;

    /**
     * Κατασκευάζει το παράθυρο διαλόγου και αρχικοποιεί τα γραφικά συστατικά.
     *
     * @param parent       Το γονικό παράθυρο.
     * @param databasePath Η διαδρομή της βάσης δεδομένων.
     * @param recId        Το ID της επιλεγμένης εγγραφής (μπορεί να είναι
     * null).
     * @param recName      Το όνομα της εγγραφής.
     * @param recAmount    Το ποσό της εγγραφής.
     */
    public AiAdvisorDialog(final JFrame parent, final String databasePath,
                           final String recId, final String recName,
                           final String recAmount) {
        super(parent, "AI Οικονομικός Σύμβουλος", true);
        this.dbPath = databasePath;
        this.aiBridge = new AiBridge();

        setSize(WIN_WIDTH, WIN_HEIGHT);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(GAP_BORDER, GAP_BORDER));

        initComponents(recId, recName, recAmount);
    }

    /**
     * Αρχικοποιεί τη δομή του παραθύρου (Tabs, Inputs) και ρυθμίζει τον HTML
     * Viewer.
     *
     * @param id     Το ID εγγραφής.
     * @param name   Το όνομα εγγραφής.
     * @param amount Το ποσό εγγραφής.
     */
    private void initComponents(final String id, final String name,
                                final String amount) {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, FONT_SIZE_NORMAL));

        // --- TAB 1: Γενική Στρατηγική ---
        JPanel globalPanel = new JPanel(new BorderLayout(GAP_BORDER,
                GAP_BORDER));
        globalPanel.setBorder(BorderFactory.createEmptyBorder(
                PADDING_PANEL, PADDING_PANEL, PADDING_PANEL, PADDING_PANEL));

        globalGoalArea = new JTextArea(TXT_ROWS, TXT_COLS);
        globalGoalArea.setLineWrap(true);
        globalGoalArea.setWrapStyleWord(true);
        globalGoalArea.setFont(new Font("Segoe UI", Font.PLAIN,
                FONT_SIZE_NORMAL));
        globalGoalArea.setBorder(BorderFactory.createTitledBorder(
                "Ποιο είναι το όραμά σας για τον προϋπολογισμό;"));
        globalGoalArea.setText("π.χ. Θέλω να μειώσω το έλλειμμα κατά 3% "
                + "χωρίς να πειράξω την Υγεία.");

        JPanel globalActionPanel = new JPanel(
                new FlowLayout(FlowLayout.RIGHT));
        JButton runGlobalBtn = new JButton("✨ Λήψη Στρατηγικής");
        runGlobalBtn.setFont(new Font("Segoe UI", Font.BOLD, FONT_SIZE_BTN));
        runGlobalBtn.addActionListener(e -> runAiTask(MODE_GLOBAL));
        globalActionPanel.add(runGlobalBtn);

        globalPanel.add(new JScrollPane(globalGoalArea), BorderLayout.CENTER);
        globalPanel.add(globalActionPanel, BorderLayout.SOUTH);

        // --- TAB 2: Συγκεκριμένη Συμβουλή ---
        JPanel specificPanel = new JPanel(new BorderLayout(GAP_BORDER,
                GAP_BORDER));
        specificPanel.setBorder(BorderFactory.createEmptyBorder(
                PADDING_PANEL, PADDING_PANEL, PADDING_PANEL, PADDING_PANEL));

        boolean hasSelection = (id != null && !id.isEmpty());

        if (hasSelection) {
            JPanel infoPanel = new JPanel(
                    new GridLayout(1, INFO_COLS, GAP_GRID, 0));
            idField = createReadOnlyField("ID", id);
            nameField = createReadOnlyField("Λογαριασμός", name);
            amountField = createReadOnlyField("Ποσό (€)", amount);

            infoPanel.add(idField);
            infoPanel.add(nameField);
            infoPanel.add(amountField);

            specificGoalArea = new JTextArea(TXT_ROWS, TXT_COLS);
            specificGoalArea.setLineWrap(true);
            specificGoalArea.setWrapStyleWord(true);
            specificGoalArea.setFont(new Font("Segoe UI", Font.PLAIN,
                    FONT_SIZE_NORMAL));
            specificGoalArea.setBorder(BorderFactory.createTitledBorder(
                    "Τι θέλετε να κάνετε με αυτόν τον λογαριασμό;"));

            JPanel centerSpecPanel = new JPanel(new BorderLayout(0,
                    PADDING_PANEL));
            centerSpecPanel.add(infoPanel, BorderLayout.NORTH);
            centerSpecPanel.add(new JScrollPane(specificGoalArea),
                    BorderLayout.CENTER);

            JPanel specActionPanel = new JPanel(
                    new FlowLayout(FlowLayout.RIGHT));
            JButton runSpecBtn = new JButton("💡 Λήψη Συμβουλής");
            runSpecBtn.setFont(new Font("Segoe UI", Font.BOLD, FONT_SIZE_BTN));
            runSpecBtn.addActionListener(e -> runAiTask(MODE_SPECIFIC));
            specActionPanel.add(runSpecBtn);

            specificPanel.add(centerSpecPanel, BorderLayout.CENTER);
            specificPanel.add(specActionPanel, BorderLayout.SOUTH);

        } else {
            JPanel errorPanel = new JPanel();
            errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));

            JLabel errorMsg1 = new JLabel("Δεν έχετε επιλέξει λογαριασμό!");
            errorMsg1.setFont(new Font("Segoe UI", Font.BOLD, FONT_SIZE_ERR));
            errorMsg1.setForeground(Color.RED);
            errorMsg1.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel errorMsg2 = new JLabel(
                    "Για να χρησιμοποιήσετε αυτή τη λειτουργία,");
            errorMsg2.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel errorMsg3 = new JLabel("παρακαλώ κλείστε το παράθυρο "
                    + "και επιλέξτε μια γραμμή από τον πίνακα.");
            errorMsg3.setAlignmentX(Component.CENTER_ALIGNMENT);

            errorPanel.add(Box.createVerticalGlue());
            errorPanel.add(errorMsg1);
            errorPanel.add(Box.createVerticalStrut(PADDING_PANEL));
            errorPanel.add(errorMsg2);
            errorPanel.add(errorMsg3);
            errorPanel.add(Box.createVerticalGlue());

            specificPanel.add(errorPanel, BorderLayout.CENTER);
        }

        tabbedPane.addTab("🌍 Γενική Στρατηγική", globalPanel);
        tabbedPane.addTab("🎯 Συγκεκριμένη Ανάλυση", specificPanel);

        // --- HTML Viewer Setup ---
        responseArea = new JEditorPane();
        responseArea.setEditable(false);
        responseArea.setContentType("text/html");

        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body { font-family: 'Segoe UI', sans-serif; "
                + "font-size: 14px; margin: 10px; }");
        styleSheet.addRule("ul { margin-left: 20px; }");
        styleSheet.addRule("li { margin-bottom: 5px; }");
        styleSheet.addRule("b { color: #333; }");

        responseArea.setEditorKit(kit);
        responseArea.setDocument(kit.createDefaultDocument());

        JScrollPane responseScroll = new JScrollPane(responseArea);
        responseScroll.setBorder(BorderFactory.createTitledBorder(
                "Απάντηση AI"));
        responseScroll.setPreferredSize(new Dimension(SCROLL_W, SCROLL_H));

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(responseScroll, BorderLayout.CENTER);

        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("← Πίσω");
        closeButton.setFont(new Font("Segoe UI", Font.PLAIN, FONT_SIZE_BTN));
        closeButton.addActionListener(e -> dispose());
        closePanel.add(closeButton);

        bottomPanel.add(closePanel, BorderLayout.SOUTH);

        add(tabbedPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        if (tabbedPane.getTabCount() > 1) {
            if (hasSelection) {
                tabbedPane.setSelectedIndex(1);
            } else {
                tabbedPane.setSelectedIndex(0);
            }
        }
    }

    private JTextField createReadOnlyField(final String title,
                                           final String value) {
        JTextField field = new JTextField();
        field.setBorder(BorderFactory.createTitledBorder(title));
        field.setText(value);
        field.setEditable(false);
        field.setBackground(new Color(COL_GRAY, COL_GRAY, COL_GRAY));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setFont(new Font("Segoe UI", Font.BOLD, FONT_SIZE_BTN));
        return field;
    }

    /**
     * Εκτελεί την επικοινωνία με το AI στο παρασκήνιο (Background Thread).
     *
     * @param mode Η λειτουργία ("global" ή "specific").
     */
    private void runAiTask(final String mode) {
        responseArea.setText("<html><body><h3 style='color:blue'>"
                + "⏳ Ο AI οικονομικός σύμβουλος αναλύει το αίτημα σας..."
                + " Παρακαλώ περιμένετε...</h3></body></html>");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                String goal;
                if (MODE_GLOBAL.equals(mode)) {
                    goal = globalGoalArea.getText().trim();
                    if (goal.isEmpty()) {
                        return "Παρακαλώ εισάγετε έναν στόχο.";
                    }
                    return aiBridge.getGlobalStrategy(dbPath, goal);
                } else {
                    String name = nameField != null
                            ? nameField.getText().trim() : "";
                    String amountStr = amountField != null
                            ? amountField.getText().trim() : "";
                    goal = specificGoalArea != null
                            ? specificGoalArea.getText().trim() : "";

                    if (goal.isEmpty()) {
                        return "Παρακαλώ γράψτε τι θέλετε να κάνετε "
                                + "με τον λογαριασμό.";
                    }
                    try {
                        double amount = Double.parseDouble(amountStr);
                        return aiBridge.getSpecificAdvice(
                                dbPath, name, amount, goal);
                    } catch (NumberFormatException e) {
                        return "Σφάλμα ανάγνωσης ποσού.";
                    }
                }
            }

            @Override
            protected void done() {
                try {
                    String result = get();
                    responseArea.setText(result);
                    responseArea.setCaretPosition(0);
                } catch (Exception e) {
                    responseArea.setText("<html><body style='color:red'>"
                            + "Σφάλμα: " + e.getMessage() + "</body></html>");
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        }.execute();
    }
}
