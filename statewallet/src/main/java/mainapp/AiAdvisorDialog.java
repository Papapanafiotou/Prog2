package mainapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

/**
 * Η κλάση {@code AiAdvisorDialog} υλοποιεί το γραφικό περιβάλλον (GUI) για την αλληλεπίδραση
 * του χρήστη με τον AI Οικονομικό Σύμβουλο.
 * <p>
 * Πρόκειται για ένα παράθυρο διαλόγου (Modal Dialog) που παρέχει δύο βασικές λειτουργίες
 * οργανωμένες σε καρτέλες (Tabs):
 * <ol>
 * <li><b>Γενική Στρατηγική:</b> Ανάλυση συνολικών δεδομένων βάσει ενός οράματος.</li>
 * <li><b>Συγκεκριμένη Ανάλυση:</b> Συμβουλές για μια συγκεκριμένη εγγραφή που επέλεξε ο χρήστης.</li>
 * </ol>
 * Η κλάση διαχειρίζεται επίσης την ασύγχρονη επικοινωνία με το AI ώστε να μην "παγώνει"
 * το περιβάλλον κατά την αναμονή της απάντησης.
 * </p>
 */
public class AiAdvisorDialog extends JDialog {

    private final AiBridge aiBridge;
    private final String dbPath;
    
    // Components
    private JTextArea responseArea;
    private JTabbedPane tabbedPane;
    
    // Global Tab Input
    private JTextArea globalGoalArea;
    
    // Specific Tab Inputs
    private JTextField idField;
    private JTextField nameField;
    private JTextField amountField;
    private JTextArea specificGoalArea;

    /**
     * Κατασκευάζει και εμφανίζει το παράθυρο του AI Συμβούλου.
     * * @param parent Το γονικό παράθυρο (JFrame) της εφαρμογής, ώστε ο διάλογος να κεντραριστεί σωστά.
     * @param dbPath Η διαδρομή της βάσης δεδομένων για να την διαβάσει το Python script.
     * @param id Το ID της επιλεγμένης εγγραφής (αν υπάρχει, αλλιώς null/κενό).
     * @param name Το όνομα της επιλεγμένης εγγραφής.
     * @param amount Το ποσό της επιλεγμένης εγγραφής.
     */
    public AiAdvisorDialog(JFrame parent, String dbPath, String id, String name, String amount) {
        super(parent, "AI Οικονομικός Σύμβουλος", true);
        this.dbPath = dbPath;
        this.aiBridge = new AiBridge();

        setSize(800, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Χτίσιμο UI
        initComponents(id, name, amount);
    }

    /**
     * Αρχικοποιεί όλα τα γραφικά συστατικά (buttons, text fields, tabs) του παραθύρου.
     * <p>
     * Ελέγχει αν ο χρήστης έχει επιλέξει κάποια εγγραφή πριν ανοίξει το παράθυρο:
     * <ul>
     * <li>Αν <b>έχει επιλέξει</b>, συμπληρώνει αυτόματα τα πεδία στην καρτέλα "Συγκεκριμένη Ανάλυση".</li>
     * <li>Αν <b>δεν έχει επιλέξει</b>, εμφανίζει μήνυμα λάθους στη δεύτερη καρτέλα και οδηγεί τον χρήστη στην πρώτη.</li>
     * </ul>
     * </p>
     *
     * @param id Το ID της εγγραφής.
     * @param name Το όνομα της εγγραφής.
     * @param amount Το ποσό της εγγραφής.
     */
    private void initComponents(String id, String name, String amount) {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // --- TAB 1: Γενική Στρατηγική ---
        JPanel globalPanel = new JPanel(new BorderLayout(10, 10));
        globalPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        globalGoalArea = new JTextArea(4, 40);
        globalGoalArea.setLineWrap(true);
        globalGoalArea.setWrapStyleWord(true);
        globalGoalArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        globalGoalArea.setBorder(BorderFactory.createTitledBorder("Ποιο είναι το όραμά σας για τον προϋπολογισμό;"));
        globalGoalArea.setText("π.χ. Θέλω να μειώσω το έλλειμμα κατά 3% χωρίς να πειράξω την Υγεία."); 
        
        JPanel globalActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        // Κλασικό κουμπί χωρίς styling
        JButton runGlobalBtn = new JButton("✨ Λήψη Στρατηγικής");
        runGlobalBtn.setFont(new Font("Segoe UI", Font.BOLD, 12)); 
        runGlobalBtn.addActionListener(e -> runAiTask("global"));
        globalActionPanel.add(runGlobalBtn);

        globalPanel.add(new JScrollPane(globalGoalArea), BorderLayout.CENTER);
        globalPanel.add(globalActionPanel, BorderLayout.SOUTH);

        // --- TAB 2: Συγκεκριμένη Συμβουλή ---
        JPanel specificPanel = new JPanel(new BorderLayout(10, 10));
        specificPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        boolean hasSelection = (id != null && !id.isEmpty());

        if (hasSelection) {
            // ΠΕΡΙΠΤΩΣΗ Α: ΕΧΕΙ ΕΠΙΛΕΓΕΙ ΛΟΓΑΡΙΑΣΜΟΣ
            JPanel infoPanel = new JPanel(new GridLayout(1, 3, 10, 0));
            idField = createReadOnlyField("ID", id);
            nameField = createReadOnlyField("Λογαριασμός", name);
            amountField = createReadOnlyField("Ποσό (€)", amount);
            
            infoPanel.add(idField);
            infoPanel.add(nameField);
            infoPanel.add(amountField);
            
            specificGoalArea = new JTextArea(4, 40);
            specificGoalArea.setLineWrap(true);
            specificGoalArea.setWrapStyleWord(true);
            specificGoalArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            specificGoalArea.setBorder(BorderFactory.createTitledBorder("Τι θέλετε να κάνετε με αυτόν τον λογαριασμό;"));
            
            JPanel centerSpecPanel = new JPanel(new BorderLayout(0, 15));
            centerSpecPanel.add(infoPanel, BorderLayout.NORTH);
            centerSpecPanel.add(new JScrollPane(specificGoalArea), BorderLayout.CENTER);
            
            JPanel specActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            // Κλασικό κουμπί χωρίς styling
            JButton runSpecBtn = new JButton("💡 Λήψη Συμβουλής");
            runSpecBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            runSpecBtn.addActionListener(e -> runAiTask("specific"));
            specActionPanel.add(runSpecBtn);
            
            specificPanel.add(centerSpecPanel, BorderLayout.CENTER);
            specificPanel.add(specActionPanel, BorderLayout.SOUTH);

        } else {
            // ΠΕΡΙΠΤΩΣΗ Β: ΔΕΝ ΕΧΕΙ ΕΠΙΛΕΓΕΙ ΛΟΓΑΡΙΑΣΜΟΣ
            JPanel errorPanel = new JPanel();
            errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));
            // Αφαιρέθηκε το κόκκινο χρώμα φόντου, είναι κλασικό γκρι του panel
            
            // --- ΑΦΑΙΡΕΣΗ ΤΟΥ ΕΙΚΟΝΙΔΙΟΥ ⚠️ ---
            
            JLabel errorMsg1 = new JLabel("Δεν έχετε επιλέξει λογαριασμό!");
            errorMsg1.setFont(new Font("Segoe UI", Font.BOLD, 16));
            errorMsg1.setForeground(Color.RED); // Κρατάμε το κόκκινο γράμμα για προσοχή
            errorMsg1.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel errorMsg2 = new JLabel("Για να χρησιμοποιήσετε αυτή τη λειτουργία,");
            errorMsg2.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel errorMsg3 = new JLabel("παρακαλώ κλείστε το παράθυρο και επιλέξτε μια γραμμή από τον πίνακα.");
            errorMsg3.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Στοίχιση στο κέντρο
            errorPanel.add(Box.createVerticalGlue());
            errorPanel.add(errorMsg1);
            errorPanel.add(Box.createVerticalStrut(15));
            errorPanel.add(errorMsg2);
            errorPanel.add(errorMsg3);
            errorPanel.add(Box.createVerticalGlue());
            
            specificPanel.add(errorPanel, BorderLayout.CENTER);
        }

        // --- ΠΡΟΣΘΗΚΗ TABS ---
        tabbedPane.addTab("🌍 Γενική Στρατηγική", globalPanel);
        tabbedPane.addTab("🎯 Συγκεκριμένη Ανάλυση", specificPanel);

        // --- Output Area ---
        responseArea = new JTextArea();
        responseArea.setEditable(false);
        responseArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        responseArea.setLineWrap(true);
        responseArea.setWrapStyleWord(true);
        responseArea.setBackground(new Color(245, 245, 250));
        
        JScrollPane responseScroll = new JScrollPane(responseArea);
        responseScroll.setBorder(BorderFactory.createTitledBorder("Απάντηση AI"));
        responseScroll.setPreferredSize(new Dimension(600, 250));

        // --- Bottom Panel ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(responseScroll, BorderLayout.CENTER);
        
        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("← Πίσω");
        closeButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        closeButton.addActionListener(e -> dispose());
        closePanel.add(closeButton);
        
        bottomPanel.add(closePanel, BorderLayout.SOUTH);

        add(tabbedPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // --- ΑΣΦΑΛΗΣ ΕΠΙΛΟΓΗ TAB ---
        if (tabbedPane.getTabCount() > 1) { 
            if (hasSelection) {
                tabbedPane.setSelectedIndex(1);
            } else {
                tabbedPane.setSelectedIndex(0);
            }
        }
    }
    
    // --- Βοηθητική Μέθοδος ---
    
    private JTextField createReadOnlyField(String title, String value) {
        JTextField field = new JTextField();
        field.setBorder(BorderFactory.createTitledBorder(title));
        field.setText(value);
        field.setEditable(false);
        field.setBackground(new Color(230, 230, 230));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return field;
    }

    /**
     * Εκτελεί την εργασία επικοινωνίας με το AI σε ξεχωριστό νήμα (background thread)
     * χρησιμοποιώντας την κλάση {@link SwingWorker}.
     * <p>
     * Αυτό είναι απαραίτητο διότι η κλήση στο Python script μπορεί να καθυστερήσει αρκετά δευτερόλεπτα.
     * Αν γινόταν στο κεντρικό νήμα (Event Dispatch Thread), η εφαρμογή θα φαινόταν "κολλημένη".
     * </p>
     * <p>
     * Η μέθοδος:
     * <ol>
     * <li>Αλλάζει τον κέρσορα σε "Wait Cursor" για ένδειξη φόρτωσης.</li>
     * <li>Εκτελεί το {@code doInBackground} για να πάρει την απάντηση από το {@code AiBridge}.</li>
     * <li>Όταν τελειώσει, ενημερώνει το {@code responseArea} μέσω της μεθόδου {@code done}.</li>
     * </ol>
     * </p>
     *
     * @param mode Η λειτουργία που ζητήθηκε ("global" ή "specific").
     */
    private void runAiTask(String mode) {
        responseArea.setText("⏳ Ο AI οικονομικός σύμβουλος αναλύει το αίτημα σας... Παρακαλώ περιμένετε...");
        responseArea.setForeground(Color.BLUE);
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                String goal;
                if (mode.equals("global")) {
                    goal = globalGoalArea.getText().trim();
                    if (goal.isEmpty()) return "Παρακαλώ εισάγετε έναν στόχο.";
                    return aiBridge.getGlobalStrategy(dbPath, goal);
                } else {
                    String id = idField != null ? idField.getText().trim() : "";
                    String name = nameField != null ? nameField.getText().trim() : "";
                    String amountStr = amountField != null ? amountField.getText().trim() : "";
                    goal = specificGoalArea != null ? specificGoalArea.getText().trim() : "";
                    
                    if (goal.isEmpty()) {
                        return "Παρακαλώ γράψτε τι θέλετε να κάνετε με τον λογαριασμό.";
                    }
                    try {
                        double amount = Double.parseDouble(amountStr);
                        return aiBridge.getSpecificAdvice(dbPath, name, amount, goal);
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
                    responseArea.setForeground(Color.BLACK);
                } catch (Exception e) {
                    responseArea.setText("Σφάλμα: " + e.getMessage());
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        }.execute();
    }
}