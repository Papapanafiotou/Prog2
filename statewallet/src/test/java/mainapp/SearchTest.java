package mainapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Test class for Search to achieve 100% JaCoCo line coverage.
 */
class SearchTest {

    private final String testDbUrl = "jdbc:h2:mem:searchDb;DB_CLOSE_DELAY=-1";
    private Search searcher;

    @BeforeEach
    void setUp() throws Exception {
        searcher = new Search(testDbUrl);
        
        // Προετοιμασία βάσης και πινάκων
        try (Connection conn = DriverManager.getConnection(testDbUrl);
             Statement st = conn.createStatement()) {
            
            st.execute("DROP ALL OBJECTS");
            
            // Δημιουργία των πινάκων που περιλαμβάνονται στο TABLES array της Search
            String[] tables = {"esoda", "eksoda", "kratos", "ypourgeia", "apokentromenes"};
            for (String table : tables) {
                st.execute("CREATE TABLE " + table + " (name VARCHAR(255), amount DOUBLE)");
            }
            
            // Εισαγωγή δεδομένων για δοκιμές
            st.execute("INSERT INTO esoda VALUES ('Ενοίκια', 1500.0)");
            st.execute("INSERT INTO ypourgeia VALUES ('Υπουργείο Παιδείας', 500000.0)");
        }
    }

    @Test
    void testSearchAmountFound() {
        // Αναζήτηση που υπάρχει στον πρώτο πίνακα (esoda)
        double amount = searcher.searchAmount("Ενοίκ");
        assertEquals(1500.0, amount);

        // Αναζήτηση που υπάρχει σε μεταγενέστερο πίνακα (ypourgeia)
        double amount2 = searcher.searchAmount("Παιδείας");
        assertEquals(500000.0, amount2);
    }

    @Test
    void testSearchAmountNotFoundAndInvalid() {
        // Μη υπάρχον όνομα
        assertEquals(0, searcher.searchAmount("Ανύπαρκτο"));

        // Null ή κενό (κάλυψη των validation blocks)
        assertEquals(0, searcher.searchAmount(null));
        assertEquals(0, searcher.searchAmount("   "));
    }

    @Test
    void testSearchStringFoundAndNotFound() {
        // Εύρεση ονόματος βάσει ποσού
        assertEquals("Ενοίκια", searcher.searchString(1500.0));

        // Ποσό που δεν υπάρχει
        assertNull(searcher.searchString(99999.9));
    }

    @Test
    void testSearchTable() {
        // Εύρεση ονόματος πίνακα
        assertEquals("esoda", searcher.searchTable("Ενοίκια"));
        assertEquals("ypourgeia", searcher.searchTable("Υπουργείο Παιδείας"));
        
        // Μη υπάρχον
        assertNull(searcher.searchTable("Κάτι άλλο"));
    }

    @Test
    void testSearchAmountInTable() {
        // Σωστή αναζήτηση σε συγκεκριμένο πίνακα
        double amount = searcher.searchAmountInTable("Ενοίκια", "esoda");
        assertEquals(1500.0, amount);

        // Αναζήτηση σε πίνακα που δεν έχει το record
        assertEquals(0, searcher.searchAmountInTable("Ενοίκια", "kratos"));

        // Validation checks (Null/Empty)
        assertEquals(0, searcher.searchAmountInTable(null, "esoda"));
        assertEquals(0, searcher.searchAmountInTable("Ενοίκια", null));
        assertEquals(0, searcher.searchAmountInTable("", ""));
    }

    @Test
    void testSqlExceptionCatchBlocks() {
        // Χρησιμοποιούμε μια επίτηδες λανθασμένη διεύθυνση για να προκαλέσουμε SQLException
        Search brokenSearcher = new Search("jdbc:invalid:url");
        
        // Κλήση μεθόδων για κάλυψη των catch blocks
        assertDoesNotThrow(() -> {
            brokenSearcher.searchAmount("test");
            brokenSearcher.searchString(100.0);
            brokenSearcher.searchTable("test");
            brokenSearcher.searchAmountInTable("test", "esoda");
        });
    }
}