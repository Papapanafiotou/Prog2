package mainapp;


import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;

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

  public BudgetImporterGUI() {  // O constructor της κλασης

        super("Budget Importer Tool");

         setDefaultLookAndFeelDecorated(true); // Δείχνει σωστά minimize/maximize
        setResizable(true);                   // Μπορεί να αλλάξει μέγεθος
        setUndecorated(false);                // Ενεργοποιεί minimize/maximize buttons


  }     
}
