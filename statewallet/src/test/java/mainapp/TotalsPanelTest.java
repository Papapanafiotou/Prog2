package mainapp;

import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TotalsPanelTest {

    private static final String DB_URL = "jdbc:sqlite:test_totals.db";
    private BudgetManager manager;
    private TotalsPanel panel;

    @BeforeEach
    public void setUp() throws Exception {
        // 1. Προετοιμασία βάσης δεδομένων
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS esoda");
            stmt.execute("CREATE TABLE esoda (code INTEGER, name TEXT, original_amount DOUBLE, amount DOUBLE)");
            // Εισαγωγή δεδομένων: Αρχικό 1000, Τρέχον 1200 (Διαφορά +200)
            stmt.execute("INSERT INTO esoda VALUES (1, 'Test', 1000.0, 1200.0)");
        }
        
        manager = new BudgetManager(DB_URL);
        panel = new TotalsPanel(manager);
    }

    @Test
    public void testUpdateTotalsPositive() {
        panel.updateTotals("esoda");

        // Έλεγχος αν το κείμενο περιέχει τις σωστές τιμές
        // Σημείωση: originalLabel κλπ είναι private, αλλά επειδή το TotalsPanel 
        // είναι JPanel, μπορούμε να βρούμε τα Components.
        // Εναλλακτικά, αν οι ετικέτες ήταν package-private θα ήταν πιο εύκολο.
        
        // Έλεγχος χρωμάτων (Logic check)
        // Εφόσον 1200 > 1000, η διαφορά είναι θετική
        java.awt.Component[] components = panel.getComponents();
        javax.swing.JLabel diffLabel = (javax.swing.JLabel) components[2];
        
        assertEquals(new Color(34, 139, 34), diffLabel.getForeground(), "Το χρώμα πρέπει να είναι πράσινο για θετική διαφορά");
        assertTrue(diffLabel.getText().contains("+200"), "Το κείμενο πρέπει να δείχνει τη διαφορά");
    }

    @Test
    public void testUpdateTotalsNegative() throws Exception {
        // Αλλαγή δεδομένων σε αρνητική διαφορά
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("UPDATE esoda SET amount = 800.0 WHERE code = 1");
        }

        panel.updateTotals("esoda");
        
        java.awt.Component[] components = panel.getComponents();
        javax.swing.JLabel diffLabel = (javax.swing.JLabel) components[2];

        assertEquals(Color.RED, diffLabel.getForeground(), "Το χρώμα πρέπει να είναι κόκκινο για αρνητική διαφορά");
    }
}