package mainapp;


import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.DriverManager;

public class BudgetImporterGui extends JFrame {
    
// 1.Περιοχή κειμένου όπου εμφανίζονται όλα τα μηνύματα και τα logs της εφαρμογής
    private JTextArea logArea;


// 2.Μπάρα προόδου που δείχνει το ποσοστό ολοκλήρωσης κατά τη διάρκεια λειτουργιών
    private JProgressBar progressBar;

// 3.Κουμπί που όταν πατηθεί ξεκινά τη διαδικασία δημιουργίας των πινάκων της βάσης
    private JButton createTablesBtn;

// 4.Κουμπί που όταν πατηθεί διαγράφει όλα τα παλιά δεδομένα από τη βάση
    private JButton clearDataBtn;

// 5.Κουμπί που όταν πατηθεί εισάγει όλα τα δεδομένα από τα CSV στη βάση
    private JButton importDataBtn;  

  public BudgetImporterGui() {  // O constructor της κλασης

        super("Budget Importer Tool");

        setDefaultLookAndFeelDecorated(true); // Δείχνει σωστά minimize/maximize
        setResizable(true);                   // Μπορεί να αλλάξει μέγεθος το παραθυρο απο αριστερα η δεξια
        setUndecorated(false);                // Ενεργοποιεί minimize/maximize buttons
        setSize(700, 500);              // Ορίζει το βασικό μέγεθος του παραθυρου
    
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // Κλεινει το παραθυρο
        setLayout(new BorderLayout());

        logArea = new JTextArea();  // Δημιουργία περιοχής κειμένου για εμφάνιση logs και μηνυμάτων
        logArea.setEditable(false);  // Κάνει την περιοχή κειμένου μη-επεξεργάσιμη (μόνο για ανάγνωση)
        JScrollPane scrollPane = new JScrollPane(logArea);  // Προσθέτει scrollbars γύρω από το JTextArea ώστε να μπορεί να κάνει scroll ο χρηστης
        add(scrollPane, BorderLayout.CENTER);  // Τοποθετεί το scroll pane στο κέντρο του παραθύρου

        progressBar = new JProgressBar(0, 100); //Δημιουργει progress bar με τιμές από 0-100
        progressBar.setStringPainted(true); // χρωματίζει το ποσοστό που έχει φορτώσει
        progressBar.setValue(0); // Θέτει στη μπάρα αρχική τιμή 0
        add(progressBar, BorderLayout.NORTH); //Τοποθετεί τη μπάρα στο πάνω μέρος του layout

            JPanel panel = new JPanel(); // panel με ολα τα κουμπιά
        panel.setLayout(new GridLayout(1, 3, 10, 10)); //φτιαχνει layout για 3 κουμπιά

        createTablesBtn = new JButton("Δημιουργία πινάκων");
        clearDataBtn = new JButton("Εκκαθάριση Δεδομένων");
        importDataBtn = new JButton(" Εισαγωγή δεδομένων");

        //Τοποθετεί τα κουμπιά στο grid layout που φτιαξαμε πιο πάνω
        panel.add(createTablesBtn);
        panel.add(clearDataBtn);
        panel.add(importDataBtn);

        add(panel, BorderLayout.SOUTH); // Τοποθετεί τα κουμπιά στο κάτω μέρος της οθόνης
        
      
        createTablesBtn.addActionListener(this::handleCreateTables);
        clearDataBtn.addActionListener(this::handleClearData);
        importDataBtn.addActionListener(this::handleImportData);

        setVisible(true);
    }
        /**
     * Δημιουργεί μόνο τους πίνακες στη βάση δεδομένων.
     * Καλεί την createTables(conn) από το BudgetImporter.
     */       
   private void handleCreateTables(ActionEvent e) {
    logArea.append("Creating tables...\n");

    SwingWorker<Void, Integer> worker = new SwingWorker<>() {

        @Override
        protected Void doInBackground() throws Exception {

            // FAKE progress 
            for (int i = 0; i <= 100; i++) {
                Thread.sleep(20);   // μικρή παύση για να φαίνεται η κίνηση
                publish(i);         // στέλνουμε την τιμή στο process()
            }

            //  δημιουργία πινάκων
            try (Connection conn= DriverManager.getConnection("jdbc:sqlite:budget_data.db")) {
                conn.setAutoCommit(false);
                BudgetImporter importer = new BudgetImporter();
                importer.createTables(conn);

                conn.commit();
            }

            return null;
        }

        @Override
        protected void process(java.util.List<Integer> chunks) {
            // παίρνουμε την τελευταία τιμή που στάλθηκε από το publish()
            int value = chunks.get(chunks.size() - 1);
            progressBar.setValue(value);   // ενημέρωση της μπάρας
        }

        @Override
        protected void done() {
            progressBar.setValue(100);     // σιγουρεύουμε ότι είναι στο 100%
            logArea.append("Tables created successfully.\n");
        }
    };

    worker.execute();
}
    private void handleClearData(ActionEvent e) {
        logArea.append("Clearing data...\n");
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {

                try (Connection conn = DriverManager.getConnection("jdbc:sqlite:budget_data.db")) {
                    conn.setAutoCommit(false);

                    BudgetImporter importer = new BudgetImporter();
                    importer.clearOldData(conn);  // καλούμε την clearOldData

                    conn.commit();
                }
                return null;
            }           
            @Override
            protected void done() {
                logArea.append("Data cleared successfully.\n");
            }
            };
            worker.execute();
    } 
    private void handleImportData(ActionEvent e) {

        logArea.append("Importing data...\n");

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            
            @Override
            protected Void doInBackground() throws Exception {

                BudgetImporter importer = new BudgetImporter();
                importer.importData();   // περιλαμβάνει όλα τα στάδια

                return null;
            }

            @Override
            protected void done() {
                logArea.append("Data imported successfully.\n");
            }
        };

        worker.execute();
    }
    }