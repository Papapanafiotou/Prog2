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
    
}
