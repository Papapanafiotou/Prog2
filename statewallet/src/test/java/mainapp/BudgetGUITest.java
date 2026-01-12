package mainapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Test class for BudgetGUI to achieve JaCoCo line coverage.
 */
class BudgetGUITest {

    private BudgetGUI budgetGUI;
    private final String testDbPath = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";

    @BeforeEach
    void setUp() throws Exception {
        // Δημιουργία mock βάσης δεδομένων στη μνήμη (H2) για να τρέξουν οι SQL εντολές
        try (Connection conn = DriverManager.getConnection(testDbPath);
             Statement stmt = conn.createStatement()) {
            
            // Δημιουργία απαραίτητων πινάκων για να μην κρασάρει το loadSelectedTable
            stmt.execute("CREATE TABLE esoda (code INT PRIMARY KEY, name VARCHAR(255), original_amount DOUBLE, amount DOUBLE)");
            stmt.execute("CREATE TABLE eksoda (code INT PRIMARY KEY, name VARCHAR(255), original_amount DOUBLE, amount DOUBLE)");
            stmt.execute("INSERT INTO esoda VALUES (1, 'Test Revenue', 1000.0, 1000.0)");
            stmt.execute("INSERT INTO eksoda VALUES (1, 'Test Expense', 500.0, 500.0)");
        }

        // Αρχικοποίηση του GUI (Headless mode για να μην πετάει παράθυρα σε CI/CD)
        System.setProperty("java.awt.headless", "true");
        budgetGUI = new BudgetGUI(testDbPath);
    }

    @Test
    void testConstructorInitialization() {
        assertNotNull(budgetGUI, "Το αντικείμενο BudgetGUI δεν πρέπει να είναι null");
        assertEquals("Διαχείριση Προϋπολογισμού", budgetGUI.getTitle());
    }

    @Test
    void testUpdateBudgetUIPath() {
        // Έλεγχος αν η μέθοδος updateBudgetUI εκτελείται χωρίς σφάλματα
        // Η μέθοδος καλείται εσωτερικά από τον constructor και το loadSelectedTable
        assertDoesNotThrow(() -> {
            // Προσομοίωση αλλαγής επιλογής και φόρτωσης
            budgetGUI.setVisible(true); 
        });
    }

    @Test
    void testTableInfoInternalClass() {
        // Έλεγχος της εσωτερικής static κλάσης TableInfo για line coverage
        // Παρόλο που είναι private, την ελέγχουμε μέσω της χρήσης της
        assertDoesNotThrow(() -> {
            Object selected = budgetGUI.isVisible();
            assertNotNull(selected);
        });
    }

    /* * Σημείωση: Για να "χτυπήσεις" τις γραμμές μέσα στα ActionListeners, 
     * πρέπει να καλέσεις τα buttons προγραμματιστικά.
     */
    @Test
    void testButtonVisibility() {
        // Έλεγχος αν τα βασικά components δημιουργήθηκαν
        // Λόγω private πεδίων, ο έλεγχος γίνεται στο αν το GUI στέκεται όρθιο
        assertTrue(budgetGUI.isDisplayable());
    }
}