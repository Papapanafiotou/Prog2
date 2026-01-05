package mainapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane; // <--- ΝΕΟ
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.html.HTMLEditorKit; // <--- ΝΕΟ
import javax.swing.text.html.StyleSheet; // <--- ΝΕΟ

public class AiAdvisorDialog extends JDialog {

    private final AiBridge aiBridge;
    private final String dbPath;
    
    private static final Color BG_DARK = new Color(30, 30, 35);
    private static final Color BG_PANEL = new Color(45, 45, 50);
    private static final Color TEXT_WHITE = new Color(240, 240, 240);
    private static final Color ACCENT_BLUE = new Color(50, 120, 220);
    private static final Color ACCENT_GREEN = new Color(40, 180, 100);
    private static final Color TERMINAL_BG = new Color(20, 20, 20);
    
    // Components
    private JEditorPane responseArea; // <--- ΑΛΛΑΓΗ ΣΕ JEditorPane
    private JTabbedPane tabbedPane;
    private JTextArea globalGoalArea;
    private JTextField idField;
    private JTextField nameField;
    private JTextField amountField;
    private JTextArea specificGoalArea;

    public AiAdvisorDialog(JFrame parent, String dbPath, String id, String name, String amount) {
        super(parent, "AI Οικονομικός Σύμβουλος", true);
        this.dbPath = dbPath;
        this.aiBridge = new AiBridge();

        setSize(900, 750); // Λίγο πιο φαρδύ για το HTML
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_DARK);

        initComponents(id, name, amount);
    }

    private void initComponents(String id, String name, String amount) {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabbedPane.setBackground(BG_PANEL);
        tabbedPane.setForeground(Color.BLACK);
        
        // --- TAB 1: Global ---
        JPanel globalPanel = new JPanel(new BorderLayout(20, 20));
        globalPanel.setBackground(BG_DARK);
        globalPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        JLabel globalLabel = new JLabel("Ποιο είναι το όραμά σας;");
        globalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        globalLabel.setForeground(TEXT_WHITE);
        
        globalGoalArea = createModernTextArea();
        globalGoalArea.setText("Θέλω να μειώσω το έλλειμμα κατά 3% χωρίς να πειράξω την Υγεία."); 
        
        JPanel globalCenter = new JPanel(new BorderLayout(0, 10));
        globalCenter.setBackground(BG_DARK);
        globalCenter.add(globalLabel, BorderLayout.NORTH);
        globalCenter.add(new JScrollPane(globalGoalArea), BorderLayout.CENTER);

        JPanel globalActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        globalActionPanel.setBackground(BG_DARK);
        ModernButton runGlobalBtn = new ModernButton("✨ Λήψη Στρατηγικής", ACCENT_BLUE);
        runGlobalBtn.addActionListener(e -> runAiTask("global"));
        globalActionPanel.add(runGlobalBtn);

        globalPanel.add(globalCenter, BorderLayout.CENTER);
        globalPanel.add(globalActionPanel, BorderLayout.SOUTH);

        // --- TAB 2: Specific ---
        JPanel specificPanel = new JPanel(new BorderLayout(20, 20));
        specificPanel.setBackground(BG_DARK);
        specificPanel.setBorder(new EmptyBorder(25, 25, 25, 25));
        
        boolean hasSelection = (id != null && !id.isEmpty());

        if (hasSelection) {
            JPanel infoPanel = new JPanel(new GridLayout(1, 3, 15, 0));
            infoPanel.setBackground(BG_DARK);
            infoPanel.add(createModernInfoField("ID", id));
            infoPanel.add(createModernInfoField("Λογαριασμός", name));
            infoPanel.add(createModernInfoField("Ποσό (€)", amount));
            
            JLabel specLabel = new JLabel("Στόχος για τον λογαριασμό:");
            specLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
            specLabel.setForeground(TEXT_WHITE);
            specificGoalArea = createModernTextArea();
            
            JPanel centerSpecPanel = new JPanel(new BorderLayout(0, 15));
            centerSpecPanel.setBackground(BG_DARK);
            centerSpecPanel.add(infoPanel, BorderLayout.NORTH);
            JPanel textWrapper = new JPanel(new BorderLayout(0, 5));
            textWrapper.setBackground(BG_DARK);
            textWrapper.add(specLabel, BorderLayout.NORTH);
            textWrapper.add(new JScrollPane(specificGoalArea), BorderLayout.CENTER);
            centerSpecPanel.add(textWrapper, BorderLayout.CENTER);
            
            JPanel specActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            specActionPanel.setBackground(BG_DARK);
            ModernButton runSpecBtn = new ModernButton("💡 Λήψη Συμβουλής", ACCENT_GREEN);
            runSpecBtn.addActionListener(e -> runAiTask("specific"));
            specActionPanel.add(runSpecBtn);
            
            specificPanel.add(centerSpecPanel, BorderLayout.CENTER);
            specificPanel.add(specActionPanel, BorderLayout.SOUTH);

        } else {
            JPanel errorPanel = new JPanel();
            errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));
            errorPanel.setBackground(BG_DARK);
            
            JLabel iconLabel = new JLabel("⚠️");
            iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            iconLabel.setForeground(Color.ORANGE);
            
            JLabel errorMsg1 = new JLabel("Δεν έχετε επιλέξει λογαριασμό!");
            errorMsg1.setFont(new Font("Segoe UI", Font.BOLD, 20));
            errorMsg1.setForeground(new Color(220, 60, 80));
            errorMsg1.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            errorPanel.add(Box.createVerticalGlue());
            errorPanel.add(iconLabel);
            errorPanel.add(Box.createVerticalStrut(15));
            errorPanel.add(errorMsg1);
            errorPanel.add(Box.createVerticalGlue());
            specificPanel.add(errorPanel, BorderLayout.CENTER);
        }

        tabbedPane.addTab("🌍 Γενική Στρατηγική", globalPanel);
        tabbedPane.addTab("🎯 Συγκεκριμένη Ανάλυση", specificPanel);

        // --- OUTPUT AREA (HTML ENABLED) ---
        responseArea = new JEditorPane();
        responseArea.setEditable(false);
        responseArea.setContentType("text/html"); // <--- Ενεργοποίηση HTML
        responseArea.setBackground(TERMINAL_BG);
        
        // CSS Styling για να φαίνεται ωραίο το HTML στο Dark Mode
        HTMLEditorKit kit = new HTMLEditorKit();
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body { font-family: 'Segoe UI', sans-serif; font-size: 14px; color: #e0e0e0; background-color: #141414; padding: 10px; }");
        styleSheet.addRule("h3 { color: #8be9fd; margin-top: 15px; }"); // Μπλε ανοιχτό για τίτλους
        styleSheet.addRule("b { color: #ffffff; }"); // Λευκό για bold
        styleSheet.addRule("li { margin-bottom: 5px; }");
        responseArea.setEditorKit(kit);
        responseArea.setDocument(kit.createDefaultDocument());
        
        JScrollPane responseScroll = new JScrollPane(responseArea);
        responseScroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 60)));
        responseScroll.setPreferredSize(new Dimension(600, 300));
        
        // --- BOTTOM PANEL ---
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(BG_DARK);
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JLabel terminalLabel = new JLabel(" > AI ANALYTICS REPORT:");
        terminalLabel.setFont(new Font("Consolas", Font.BOLD, 12));
        terminalLabel.setForeground(Color.GRAY);
        
        JPanel outputWrapper = new JPanel(new BorderLayout(0, 5));
        outputWrapper.setBackground(BG_DARK);
        outputWrapper.add(terminalLabel, BorderLayout.NORTH);
        outputWrapper.add(responseScroll, BorderLayout.CENTER);
        
        bottomPanel.add(outputWrapper, BorderLayout.CENTER);
        
        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closePanel.setBackground(BG_DARK);
        ModernButton closeButton = new ModernButton("❌ Κλείσιμο", new Color(100, 100, 100));
        closeButton.addActionListener(e -> dispose());
        closePanel.add(closeButton);
        bottomPanel.add(closePanel, BorderLayout.SOUTH);

        add(tabbedPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        if (tabbedPane.getTabCount() > 1) { 
            if (hasSelection) tabbedPane.setSelectedIndex(1);
            else tabbedPane.setSelectedIndex(0);
        }
    }
    
    // --- HELPER COMPONENTS ---
    private JTextArea createModernTextArea() {
        JTextArea area = new JTextArea(4, 40);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setBackground(BG_PANEL);
        area.setForeground(TEXT_WHITE);
        area.setCaretColor(TEXT_WHITE);
        area.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(60, 60, 70), 1),
            new EmptyBorder(10, 10, 10, 10)
        ));
        return area;
    }
    
    private JPanel createModernInfoField(String labelText, String valueText) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(BG_DARK);
        
        JLabel label = new JLabel(labelText);
        label.setForeground(Color.GRAY);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JTextField field = new JTextField(valueText);
        field.setEditable(false);
        field.setBackground(BG_PANEL);
        field.setForeground(ACCENT_BLUE);
        field.setFont(new Font("Segoe UI", Font.BOLD, 14));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(60, 60, 70), 1),
            new EmptyBorder(8, 5, 8, 5)
        ));
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        
        if (labelText.equals("ID")) idField = field;
        if (labelText.equals("Λογαριασμός")) nameField = field;
        if (labelText.equals("Ποσό (€)")) amountField = field;
        
        return panel;
    }

    private void runAiTask(String mode) {
        // Εδώ βάζουμε HTML μήνυμα φόρτωσης!
        responseArea.setText("<html><body style='color:#8be9fd; font-family:Segoe UI'><h3>⏳ Επικοινωνία με AI...</h3><p>Ανάλυση δεδομένων, παρακαλώ περιμένετε...</p></body></html>");
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                String goal;
                if (mode.equals("global")) {
                    goal = globalGoalArea.getText().trim();
                    if (goal.isEmpty()) return "Error: No goal specified.";
                    return aiBridge.getGlobalStrategy(dbPath, goal);
                } else {
                    String id = idField != null ? idField.getText().trim() : "";
                    String name = nameField != null ? nameField.getText().trim() : "";
                    String amountStr = amountField != null ? amountField.getText().trim() : "";
                    goal = specificGoalArea != null ? specificGoalArea.getText().trim() : "";
                    
                    if (goal.isEmpty()) return "Error: Input goal is empty.";
                    try {
                        double amount = Double.parseDouble(amountStr);
                        return aiBridge.getSpecificAdvice(dbPath, name, amount, goal);
                    } catch (NumberFormatException e) {
                        return "Error: Invalid amount format.";
                    }
                }
            }

            @Override
            protected void done() {
                try {
                    String result = get();
                    // Το αποτέλεσμα είναι ήδη HTML από την Python
                    responseArea.setText("<html><body>" + result + "</body></html>");
                    responseArea.setCaretPosition(0); // Scroll to top
                } catch (Exception e) {
                    responseArea.setText("<html><body style='color:red'>Critical Error: " + e.getMessage() + "</body></html>");
                } finally {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        }.execute();
    }
    
    // --- MODERN BUTTON CLASS ---
    private static class ModernButton extends JButton {
        private Color baseColor;
        private Color hoverColor;

        public ModernButton(String text, Color color) {
            super(text);
            this.baseColor = color;
            this.hoverColor = color.brighter();
            
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor);
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(baseColor);
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            if (getModel().isRollover()) g2.setColor(hoverColor);
            else g2.setColor(baseColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}