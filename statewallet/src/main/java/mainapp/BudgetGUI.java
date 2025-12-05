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

}
