package mainapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class AiAdvisorDialog extends JDialog {

    private final AiBridge aiBridge;
    private final String dbPath;
    
    // Components
    private JTextArea responseArea;
    private JTabbedPane tabbedPane;
    
    // Global Tab Input
    private JTextArea globalGoalArea;
    
    // Specific Tab Inputs (Read-Only)
    private JTextField idField;     // ΝΕΟ: ID
    private JTextField nameField;   // Read-only
    private JTextField amountField; // Read-only
    private JTextArea specificGoalArea; // Εδώ γράφει ο χρήστης

    // Ο Constructor πλέον δέχεται ΚΑΙ το ID
    public AiAdvisorDialog(JFrame parent, String dbPath, String id, String name, String amount) {
        super(parent, "AI Οικονομικός Σύμβουλος", true);
        this.dbPath = dbPath;
        this.aiBridge = new AiBridge();

        setSize(750, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        initComponents(id, name, amount);
        
        // Αν ανοίχτηκε για συγκεκριμένο λογαριασμό, πάμε απευθείας στο 2ο tab
        if (id != null && !id.isEmpty()) {
            tabbedPane.setSelectedIndex(1);
        }
    }

    private void initComponents(String id, String name, String amount) {
        tabbedPane = new JTabbedPane();
        
        // --- TAB 1: Γενική Στρατηγική ---
        JPanel globalPanel = new JPanel(new BorderLayout(10, 10));
        globalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        globalGoalArea = new JTextArea(3, 40);
        globalGoalArea.setLineWrap(true);
        globalGoalArea.setWrapStyleWord(true);
        globalGoalArea.setBorder(BorderFactory.createTitledBorder("Ποιο είναι το όραμά σας για τον προϋπολογισμό;"));
        globalGoalArea.setText("Θέλω να μηδενίσω το έλλειμμα."); 
        
        JPanel globalBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton runGlobalBtn = new JButton("✨ Λήψη Στρατηγικής");
        runGlobalBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        runGlobalBtn.addActionListener(e -> runAiTask("global"));
        globalBtnPanel.add(runGlobalBtn);

        globalPanel.add(new JScrollPane(globalGoalArea), BorderLayout.CENTER);
        globalPanel.add(globalBtnPanel, BorderLayout.SOUTH);

        // --- TAB 2: Συγκεκριμένη Συμβουλή (ΔΙΟΡΘΩΜΕΝΟ) ---
        JPanel specificPanel = new JPanel(new BorderLayout(10, 10));
        specificPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel πληροφοριών (ID, Name, Amount) - ΚΛΕΙΔΩΜΕΝΑ
        JPanel infoPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        
        idField = createReadOnlyField("ID", id);
        nameField = createReadOnlyField("Λογαριασμός", name);
        amountField = createReadOnlyField("Ποσό (€)", amount);
        
        infoPanel.add(idField);
        infoPanel.add(nameField);
        infoPanel.add(amountField);
        
        // Πεδίο Στόχου (Εδώ γράφει ο χρήστης)
        specificGoalArea = new JTextArea(4, 40);
        specificGoalArea.setLineWrap(true);
        specificGoalArea.setWrapStyleWord(true);
        specificGoalArea.setBorder(BorderFactory.createTitledBorder("Τι θέλετε να κάνετε με αυτόν τον λογαριασμό;"));
        
        JPanel centerSpecPanel = new JPanel(new BorderLayout(0, 10));
        centerSpecPanel.add(infoPanel, BorderLayout.NORTH);
        centerSpecPanel.add(new JScrollPane(specificGoalArea), BorderLayout.CENTER);
        
        JPanel specBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton runSpecBtn = new JButton("💡 Λήψη Συμβουλής");
        runSpecBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        runSpecBtn.addActionListener(e -> runAiTask("specific"));
        specBtnPanel.add(runSpecBtn);
        
        specificPanel.add(centerSpecPanel, BorderLayout.CENTER);
        specificPanel.add(specBtnPanel, BorderLayout.SOUTH);

        // --- Tabs Setup ---
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
        responseScroll.setPreferredSize(new java.awt.Dimension(600, 250));

        add(tabbedPane, BorderLayout.CENTER); // Αλλαγή σε CENTER για να πιάνει χώρο
        add(responseScroll, BorderLayout.SOUTH);
    }
    
    // Βοηθητική μέθοδος για πεδία που δεν αλλάζουν
    private JTextField createReadOnlyField(String title, String value) {
        JTextField field = new JTextField();
        field.setBorder(BorderFactory.createTitledBorder(title));
        field.setText(value);
        field.setEditable(false); // Κλειδωμένο
        field.setBackground(new Color(230, 230, 230)); // Γκριζαρισμένο
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return field;
    }

    private void runAiTask(String mode) {
        responseArea.setText("⏳ Επικοινωνία με το AI... Παρακαλώ περιμένετε...");
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
                    String id = idField.getText().trim();
                    String name = nameField.getText().trim();
                    String amountStr = amountField.getText().trim();
                    goal = specificGoalArea.getText().trim();
                    
                    if (id.isEmpty() || name.isEmpty() || amountStr.isEmpty()) {
                        return "Δεν έχει επιλεγεί λογαριασμός. Παρακαλώ επιλέξτε από τον πίνακα.";
                    }
                    if (goal.isEmpty()) {
                        return "Παρακαλώ γράψτε τι θέλετε να κάνετε με τον λογαριασμό.";
                    }
                    
                    try {
                        double amount = Double.parseDouble(amountStr);
                        // Στέλνουμε στο AI το Όνομα, το Ποσό και τον Στόχο
                        return aiBridge.getSpecificAdvice(name, amount, goal);
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