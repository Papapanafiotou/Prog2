package mainapp;

//βιβλιοθηκες για τα στοιχεια του UI
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Objects;

public class BudgetGUI extends JFrame {

private final BudgetManager manager;     // χρηση του manager που φτιαξαμε
    private final DatabaseHandler dbHandler; // συνδεση μεταξυ βασης δεδομενων και UI

    //επιλογη πινακα
    private JComboBox<TableInfo> tableSelector; 
    private JButton loadTableButton;

    //πινακας που θα εμφανιστει
    private JTable dataTable;
    private DefaultTableModel tableModel;

    //κουμπι και πεδιο εισαγωγης αλλαγων
    private JTextField idField;
    private JTextField amountField;
    private JButton updateButton;

    //εμφανιση αλλαγών
    private JButton showChangesButton;
    private JTextArea changesArea;

    public BudgetGUI() {
        this.manager = new BudgetManager();
        this.dbHandler = new DatabaseHandler();

        setTitle("Διαχείριση Προϋπολογισμού");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //κλεινει το προγραμμα με το Χ
        setSize(950, 600); //ορισμος του αρχικου μεγεθους του παραθυρου
        setLocationRelativeTo(null); // τοποθετηση του παραθυρου στο κεντρο της οθονης

        initComponents();
        initLayout();
        initListeners();
    }
    private void initComponents() {
        // Μενου επιλογης πινακα και συνδεση με τη βαση δεδομενων (αντιστοιχια ονοματων στηλων)
        tableSelector = new JComboBox<>();
        tableSelector.addItem(new TableInfo("Έσοδα", "esoda", "code"));
        tableSelector.addItem(new TableInfo("Έξοδα", "eksoda", "code"));
        tableSelector.addItem(new TableInfo("Κράτος", "kratos", "number"));
        tableSelector.addItem(new TableInfo("Υπουργεία", "ypourgeia", "number"));
        tableSelector.addItem(new TableInfo("Αποκεντρωμένες Διοικήσεις", "apokentromenes", "number"));

       loadTableButton = new JButton("Εμφάνιση Πίνακα");  //κουμπι εμφανισης πινακα

         // Πίνακας δεδομένων (δομη)
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Περιγραφή", "Αρχικό Ποσό", "Τρέχον Ποσό"}, //ονοματα στηλων της δομης
                0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // ο πινακας ειναι μονο για αναγνωση
            }
        };
        dataTable = new JTable(tableModel); //δεδομενα που θα μπουν στο μοντελο πινακα που φτιαξαμε
        dataTable.setFillsViewportHeight(true);

         // Πεδία για αλλαγή ποσού
        idField = new JTextField(8); //id που θα αλλαξουμε
        amountField = new JTextField(10); // νεο ποσο
        updateButton = new JButton("Αλλαγή Ποσού"); //πραγματοποιηση αλλαγης

         // Περιοχή εμφάνισης αλλαγών
        showChangesButton = new JButton("Εμφάνιση Αλλαγών");
        changesArea = new JTextArea(8, 50);
        changesArea.setEditable(false);
        changesArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
    }

    private void initLayout() {
        setLayout(new BorderLayout(8, 8));  //χωριζει την περιοχη σε τμηματα
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Πίνακας:"));
        topPanel.add(tableSelector);
        topPanel.add(loadTableButton);

    
        JScrollPane tableScroll = new JScrollPane(dataTable); //προσθηκη scroll bar

        // Κάτω: αλλαγή ποσού + αλλαγές
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));

        //ενημερωση στοιχειων
        JPanel updatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        updatePanel.setBorder(BorderFactory.createTitledBorder("Αλλαγή στοιχείου προϋπολογισμού"));
        updatePanel.add(new JLabel("ID:"));
        updatePanel.add(idField);
        updatePanel.add(new JLabel("Νέο ποσό:"));
        updatePanel.add(amountField);
        updatePanel.add(updateButton);

        JPanel changesPanel = new JPanel(new BorderLayout());
        changesPanel.setBorder(BorderFactory.createTitledBorder("Αλλαγές προϋπολογισμού"));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(showChangesButton);
        changesPanel.add(btnPanel, BorderLayout.NORTH);
        changesPanel.add(new JScrollPane(changesArea), BorderLayout.CENTER);

        bottomPanel.add(updatePanel, BorderLayout.NORTH);
        bottomPanel.add(changesPanel, BorderLayout.CENTER); 
        add(topPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    private void initListeners() {
        // Εμφάνιση επιλεγμένου πίνακα
        loadTableButton.addActionListener(e -> loadSelectedTable());
        // Διπλό κλικ σε γραμμή -> γέμισμα ID & ποσού
        dataTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int row = dataTable.getSelectedRow();
                    if (row >= 0) {
                        Object idVal = tableModel.getValueAt(row, 0);
                        Object amountVal = tableModel.getValueAt(row, 3);
                        idField.setText(Objects.toString(idVal, ""));
                        amountField.setText(Objects.toString(amountVal, ""));
                    }
                }
            }
        });
        // Κουμπί αλλαγής ποσού
        updateButton.addActionListener(e -> updateAmount());

        // Κουμπί εμφάνισης αλλαγών
        showChangesButton.addActionListener(e -> loadChangesFromDb());
    }
    // Μέθοδος που φορτώνει τον επιλεγμένο πίνακα από τη βάση και τον δείχνει στο JTable
private void loadSelectedTable() { 
    TableInfo info = (TableInfo) tableSelector.getSelectedItem(); // Παίρνουμε ποιο στοιχείο έχει επιλεγεί από το ComboBox
    if (info == null) return;                                    // Αν για κάποιο λόγο δεν έχει επιλεγεί τίποτα, βγαίνουμε από τη μέθοδο

    tableModel.setRowCount(0);     // Σβήνουμε όλες τις υπάρχουσες γραμμές από το JTable

    String sql = "SELECT " + info.idColumnName + ", name, original_amount, amount FROM " + info.tableName;
    // Φτιάχνουμε το SQL query: παίρνουμε ID στήλη, όνομα, αρχικό ποσό και τρέχον ποσό από τον σωστό πίνακα
    try (Connection conn = dbHandler.connect();                   // Ανοίγουμε σύνδεση με τη βάση μέσω του DatabaseHandler
         Statement stmt = conn.createStatement();                 // Δημιουργούμε Statement για να εκτελέσουμε το SQL
         ResultSet rs = stmt.executeQuery(sql)) 
         {                
        while (rs.next()) {                                       // Επαναλαμβάνουμε για κάθε γραμμή που επιστρέφει η βάση
            Object[] row = new Object[]{                          // Δημιουργούμε ένα object array που αντιπροσωπεύει μια γραμμή του πίνακα
                    rs.getInt(info.idColumnName),                 
                    rs.getString("name"),                         
                    rs.getDouble("original_amount"),              
                    rs.getDouble("amount")                       
            };
            tableModel.addRow(row);                               // Προσθέτουμε αυτή τη γραμμή στο JTable
        }

        }  
        catch (SQLException ex) {                                   // Αν συμβεί κάποιο SQL σφάλμα
        JOptionPane.showMessageDialog(this,                       // Εμφανίζουμε ένα μήνυμα λάθους
                "Σφάλμα κατά την εμφάνιση του πίνακα " + info.tableName + ":\n" + ex.getMessage(),
                "Σφάλμα",
                JOptionPane.ERROR_MESSAGE);
        }
}
// Χρησιμοποιούμε τον ΥΠΑΡΧΟΝ BudgetManager.updateAmount για να αλλάξουμε ποσό σε γραμμή
private void updateAmount() {
    TableInfo info = (TableInfo) tableSelector.getSelectedItem(); // Ξαναπαίρνουμε τον επιλεγμένο πίνακα από το ComboBox
    if (info == null) return;                                     // Αν δεν υπάρχει επιλογή, σταματάμε

    String idText = idField.getText().trim();                     // Διαβάζουμε το κείμενο που έγραψε ο χρήστης στο πεδίο ID
    String amountText = amountField.getText().trim();             // Διαβάζουμε το κείμενο που έγραψε ο χρήστης στο πεδίο Νέο Ποσό

    if (idText.isEmpty() || amountText.isEmpty()) {               // Αν κάποιο από τα δύο πεδία είναι άδειο 
        JOptionPane.showMessageDialog(this,                       // Εμφανίζουμε προειδοποίηση ότι πρέπει να τα συμπληρώσει
                "Συμπλήρωσε ID και νέο ποσό.",
                "Προειδοποίηση",
                JOptionPane.WARNING_MESSAGE);
        return;                                                   // Και σταματάμε τη μέθοδο
    }
    try {
        int id = Integer.parseInt(idText);                        // Μετατρέπουμε το κείμενο του ID σε ακέραιο
        double newAmount = Double.parseDouble(amountText);        // Μετατρέπουμε το κείμενο του ποσού σε double 

        if (newAmount < 0) {                                      // Αν το νέο ποσό είναι αρνητικό
            int option = JOptionPane.showConfirmDialog(this,      // Ρωτάμε τον χρήστη αν θέλει σίγουρα να συνεχίσει
                    "Το νέο ποσό είναι αρνητικό. Θέλεις να συνεχίσεις;",
                    "Επιβεβαίωση",
                    JOptionPane.YES_NO_OPTION);
            if (option != JOptionPane.YES_OPTION) {               // Αν απαντήσει όχι
                return;                                           // Σταματάμε χωρίς να κάνουμε την αλλαγή
            }
        }
        boolean success = manager.updateAmount(info.tableName, info.idColumnName, id, newAmount);                                       
        if (success) {                                             // Αν η μέθοδος επέστρεψε true
            JOptionPane.showMessageDialog(this,                   // Εμφανίζουμε μήνυμα επιτυχίας
                    "Επιτυχής ενημέρωση!",
                    "Επιτυχία",
                    JOptionPane.INFORMATION_MESSAGE);
            loadSelectedTable();                                  // Ξαναφορτώνουμε τον πίνακα για να δούμε τα νέα ποσά
        } else {                                                  // Αν η ενημέρωση δεν πέτυχε 
            JOptionPane.showMessageDialog(this,                   // Εμφανίζουμε προειδοποίηση στον χρήστη
                    "Αποτυχία: Δεν βρέθηκε το ID.",
                    "Αποτυχία",
                    JOptionPane.WARNING_MESSAGE);
        }
    } 
    catch (NumberFormatException ex) {                          // Αν γίνει λάθος στη μετατροπή string → αριθμό (ID ή ποσό)
        JOptionPane.showMessageDialog(this,                       // Εμφανίζουμε μήνυμα λάθους για λάθος μορφή αριθμών
                "Λάθος μορφή αριθμών. To ID πρέπει να είναι ακέραιος και το ποσό αριθμός.",
                "Σφάλμα",
                JOptionPane.ERROR_MESSAGE);
    }
}

}
