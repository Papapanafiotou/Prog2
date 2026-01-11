package mainapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class MinMaXTest {

    private static final String DB_URL = "jdbc:sqlite:test_minmax.db";
    private MinMaX minmax;

    @BeforeEach
    public void setUp() throws Exception {
        minmax = new MinMaX(DB_URL);
        // Δημιουργία πινάκων και δεδομένων για το τεστ
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS esoda");
            stmt.execute("CREATE TABLE esoda (name TEXT, amount DOUBLE)");
            stmt.execute("INSERT INTO esoda VALUES ('IncomeA', 100.0), ('IncomeB', 500.0)");

            stmt.execute("DROP TABLE IF EXISTS eksoda");
            stmt.execute("CREATE TABLE eksoda (name TEXT, amount DOUBLE)");
            stmt.execute("INSERT INTO eksoda VALUES ('ExpenseA', 50.0), ('ExpenseB', 200.0)");

            stmt.execute("DROP TABLE IF EXISTS ypourgeia");
            stmt.execute("CREATE TABLE ypourgeia (name TEXT, amount DOUBLE)");
            stmt.execute("INSERT INTO ypourgeia VALUES ('MinA', 10.0), ('MinB', 1000.0)");
        }
    }

    @Test
    public void testGetMinMaxLogic() {
        // Έλεγχος της μεθόδου getMinMax απευθείας
        assertEquals(100.0, minmax.getMinMax(1, 1)); // Min Income
        assertEquals(500.0, minmax.getMinMax(2, 1)); // Max Income
        assertEquals(50.0, minmax.getMinMax(1, 2));  // Min Expense
        assertEquals(1000.0, minmax.getMinMax(2, 3)); // Max Ministry
    }

    @Test
    public void testShowMinMaxWithFullFlow() {
        // Προσομοίωση εισόδου: 
        // 1. "abc" (λάθος - κείμενο)
        // 2. "5" (λάθος - αριθμός εκτός ορίων)
        // 3. "1" (σωστό - ελάχιστο)
        // 4. "2" (σωστό - έξοδα)
        String simulatedInput = "abc\n5\n1\n2\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Εκτέλεση - δεν πρέπει να πετάξει exception
        assertDoesNotThrow(() -> minmax.showMinMax());
        
        // Επαναφορά του System.in
        System.setIn(System.in);
    }

    @Test
    public void testShowMinMaxMaxIncomeFlow() {
        // Σενάριο: Μέγιστο (2) -> Έσοδα (1)
        String simulatedInput = "2\n1\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        assertDoesNotThrow(() -> minmax.showMinMax());
        System.setIn(System.in);
    }
    
    @Test
    public void testShowMinMaxMinistryFlow() {
        // Σενάριο: Μέγιστο (2) -> Υπουργεία (3)
        String simulatedInput = "2\n3\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        assertDoesNotThrow(() -> minmax.showMinMax());
        System.setIn(System.in);
    }
}
