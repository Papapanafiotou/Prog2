package mainapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BudgetManagerTest {

    private static final String TEST_DB_URL = "jdbc:sqlite:test_budget.db";
    private BudgetManager manager;

    @BeforeEach
    public void setUp() throws Exception {
        manager = new BudgetManager(TEST_DB_URL);
        
        // Δημιουργούμε έναν πίνακα για τις δοκιμές
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS test_table");
            stmt.execute("CREATE TABLE test_table (" +
                         "id INTEGER PRIMARY KEY, " +
                         "name TEXT, " +
                         "original_amount DOUBLE, " +
                         "amount DOUBLE)");
            
            // Εισάγουμε μερικά δεδομένα
            stmt.execute("INSERT INTO test_table (id, name, original_amount, amount) " +
                         "VALUES (1, 'Test Item Long Name for Padding', 100.0, 80.0)");
            
            // Δημιουργούμε και έναν πίνακα για το showChanges (π.χ. esoda)
            stmt.execute("DROP TABLE IF EXISTS esoda");
            stmt.execute("CREATE TABLE esoda (code INTEGER, name TEXT, original_amount DOUBLE, amount DOUBLE)");
            stmt.execute("INSERT INTO esoda VALUES (10, 'Income', 500.0, 600.0)");
        }
    }

    @Test
    public void testSetUrl() {
        manager.setUrl("jdbc:sqlite:new_path.db");
        // Δεν μπορούμε να ελέγξουμε το πεδίο url απευθείας γιατί είναι private, 
        // αλλά η μέθοδος εκτελέστηκε.
    }

    @Test
    public void testUpdateAmount() {
        boolean updated = manager.updateAmount("test_table", "id", 1, 150.0);
        assertTrue(updated);
        
        double[] totals = manager.getTotal("test_table");
        assertEquals(150.0, totals[1]); // Το τρέχον σύνολο πρέπει να είναι 150
    }

    @Test
    public void testGetTotal() {
        double[] totals = manager.getTotal("test_table");
        assertEquals(100.0, totals[0]); // original_amount
        assertEquals(80.0, totals[1]);  // amount
    }

    @Test
    public void testGetBudgetCharacterism() {
        // Περίπτωση Πλεονάσματος
        String res1 = manager.getBudgetCharacterism(1000, 500);
        assertTrue(res1.contains("Πλεονασματικός"));
        
        // Περίπτωση Ελλείμματος
        String res2 = manager.getBudgetCharacterism(500, 1000);
        assertTrue(res2.contains("Ελλειμματικός"));
        
        // Περίπτωση Ισοσκελισμένου
        String res3 = manager.getBudgetCharacterism(500, 500);
        assertEquals("Ισοσκελισμένος", res3);
    }

    @Test
    public void testPrintTableAndShowChanges() {
        // Αυτές οι μέθοδοι εκτυπώνουν στην κονσόλα (void). 
        // Τις καλούμε για να σιγουρευτούμε ότι δεν πετάνε Exception 
        // και για να πάρουν "πράσινο" χρώμα στο JaCoCo.
        assertDoesNotThrow(() -> {
            manager.printTable("test_table", "id");
            manager.showChanges();
        });
    }

    @Test
    public void testUpdateAmountFail() {
        // Δοκιμή σε πίνακα που δεν υπάρχει για να πιάσουμε το catch (SQLException)
        boolean result = manager.updateAmount("non_existent", "id", 1, 100.0);
        assertFalse(result);
    }
}