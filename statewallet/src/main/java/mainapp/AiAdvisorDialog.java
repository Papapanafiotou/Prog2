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
 * Η κλάση {@code AiAdvisorDialog} υλοποιεί το γραφικό περιβάλλον για τον AI Οικονομικό Σύμβουλο.
 * <p>
 * <b>Νέα Δυνατότητα:</b> Χρησιμοποιεί {@link JEditorPane} αντί για JTextArea, επιτρέποντας
 * την εμφάνιση των απαντήσεων του AI σε μορφή <b>HTML</b>. Αυτό επιτρέπει πλούσια μορφοποίηση
 * όπως λίστες, έντονα γράμματα και χρωματισμό λέξεων (π.χ. κόκκινο για ελλείμματα).
 * </p>
 */
public class AiAdvisorDialog extends JDialog {

    private final AiBridge aiBridge;
    private final String dbPath;
    
    // Components
    /** Το πεδίο που εμφανίζει την απάντηση (υποστηρίζει HTML). */
    private JEditorPane responseArea; 
    private JTabbedPane tabbedPane;
    
    // Global Tab Input
    private JTextArea globalGoalArea;
    
    // Specific Tab Inputs
    private JTextField idField;
    private JTextField nameField;
    private JTextField amountField;
    private JTextArea specificGoalArea;

    /**
     * Κατασκευάζει το παράθυρο διαλόγου και αρχικοποιεί τα γραφικά συστατικά.
     * * @param parent Το γονικό παράθυρο.
     * @param dbPath Η διαδρομή της βάσης δεδομένων.
     * @param id Το ID της επιλεγμένης εγγραφής (μπορεί να είναι null).
     * @param name Το όνομα της εγγραφής.
     * @param amount Το ποσό της εγγραφής.
     */
    public AiAdvisorDialog(JFrame parent, String dbPath, String id, String name, String amount) {
        super(parent, "AI Οικονομικός Σύμβουλος", true);
        this.dbPath = dbPath;
        this.aiBridge = new AiBridge();

        setSize(850, 750); 
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        initComponents(id, name, amount);
    }

    /**
     * Αρχικοποιεί τη δομή του παραθύρου (Tabs, Inputs) και ρυθμίζει τον HTML Viewer.
     * <p>
     * Ειδική μνεία στο {@link HTMLEditorKit}:
     * Ορίζονται κανόνες CSS (StyleSheet) ώστε η γραμματοσειρά να είναι 'Segoe UI'
     * και να υπάρχει σωστό spacing στις λίστες του HTML που επιστρέφει το AI.
     * </p>
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
            JButton runSpecBtn = new JButton("💡 Λήψη Συμβουλής");
            runSpecBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
            runSpecBtn.addActionListener(e -> runAiTask("specific"));
            specActionPanel.add(runSpecBtn);
            
            specificPanel.add(centerSpecPanel, BorderLayout.CENTER);
            specificPanel.add(specActionPanel, BorderLayout.SOUTH);

        } else {
            JPanel errorPanel = new JPanel();
            errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));
            
            JLabel errorMsg1 = new JLabel("Δεν έχετε επιλέξει λογαριασμό!");
            errorMsg1.setFont(new Font("Segoe UI", Font.BOLD, 16));
            errorMsg1.setForeground(Color.RED);
            errorMsg1.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel errorMsg2 = new JLabel("Για να χρησιμοποιήσετε αυτή τη λειτουργία,");
            errorMsg2.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel errorMsg3 = new JLabel("παρακαλώ κλείστε το παράθυρο και επιλέξτε μια γραμμή από τον πίνακα.");
            errorMsg3.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            errorPanel.add(Box.createVerticalGlue());
            errorPanel.add(errorMsg1);
            errorPanel.add(Box.createVerticalStrut(15));
            errorPanel.add(errorMsg2);
            errorPanel.add(errorMsg3);
            errorPanel.add(Box.createVerticalGlue());
            
            specificPanel.add(errorPanel, BorderLayout.CENTER);
        }

        tabbedPane.addTab("🌍 Γενική Στρατηγική", globalPanel);
        tabbedPane.addTab("🎯 Συγκεκριμένη Ανάλυση", specificPanel);

        // --- ΑΛΛΑΓΗ 4: Ρύθμιση του HTML Viewer ---
        responseArea = new JEditorPane();
        responseArea.setEditable(false);
        responseArea.setContentType("text/html"); // Ενεργοποιεί το HTML
        
        // CSS για όμορφη εμφάνιση (γραμματοσειρές, κενά, λίστες)
        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body { font-family: 'Segoe UI', sans-serif; font-size: 14px; margin: 10px; }");
        styleSheet.addRule("ul { margin-left: 20px; }");
        styleSheet.addRule("li { margin-bottom: 5px; }");
        styleSheet.addRule("b { color: #333; }");
        
        responseArea.setEditorKit(kit);
        responseArea.setDocument(kit.createDefaultDocument());
        
        JScrollPane responseScroll = new JScrollPane(responseArea);
        responseScroll.setBorder(BorderFactory.createTitledBorder("Απάντηση AI"));
        responseScroll.setPreferredSize(new Dimension(600, 300));

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

        if (tabbedPane.getTabCount() > 1) { 
            if (hasSelection) {
                tabbedPane.setSelectedIndex(1);
            } else {
                tabbedPane.setSelectedIndex(0);
            }
        }
    }
    
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
     * Εκτελεί την επικοινωνία με το AI στο παρασκήνιο (Background Thread).
     * <p>
     * Στέλνει το αίτημα μέσω του {@code AiBridge} και περιμένει απάντηση σε μορφή <b>HTML</b>.
     * Το {@code JEditorPane} αναλαμβάνει αυτόματα την απόδοση (render) του HTML κώδικα.
     * </p>
     * * @param mode Η λειτουργία ("global" ή "specific").
     */
    private void runAiTask(String mode) {
        // Χρησιμοποιούμε HTML και στο Loading message για να φαίνεται ωραίο
        responseArea.setText("<html><body><h3 style='color:blue'>⏳ Ο AI οικονομικός σύμβουλος αναλύει το αίτημα σας... Παρακαλώ περιμένετε...</h3></body></html>");
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
                        // Στέλνουμε το αίτημα στο AI (η Python επιστρέφει HTML)
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
                    // Απλά βάζουμε το αποτέλεσμα, το JEditorPane θα το κάνει render αυτόματα
                    responseArea.setText(result);
                    responseArea.setCaretPosition(0); // Scroll στην αρχή
                } catch (Exception e) {
                    responseArea.setText("<html><body style='color:red'>Σφάλμα: " + e.getMessage() + "</body></html>");
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        }.execute();
    }
}