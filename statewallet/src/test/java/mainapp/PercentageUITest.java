package mainapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Test class for PercentageUI to achieve high JaCoCo line coverage.
 */
class PercentageUITest {

    private final String testDbUrl = "jdbc:sqlite:test_percentage.db";
    private BudgetManager manager;
    private PercentageUI ui;

    @BeforeEach
    void setUp() throws Exception {
        System.setProperty("java.awt.headless", "true");
        manager = new BudgetManager(testDbUrl);

        // Προετοιμασία δεδομένων
        try (Connection conn = DriverManager.getConnection(testDbUrl);
             Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS esoda");
            st.execute("CREATE TABLE esoda (name TEXT, amount REAL)");
            
            // 1. Κανονική εγγραφή
            st.execute("INSERT INTO esoda VALUES ('Μισθός', 1000.0)");
            // 2. Εγγραφή με πολύ μεγάλο όνομα (> 27 χαρακτήρες) για κάλυψη του truncation logic
            st.execute("INSERT INTO esoda VALUES ('Αυτή είναι μια πολύ μεγάλη περιγραφή για έσοδα κράτους', 500.0)");
        }
        
        ui = new PercentageUI(manager, testDbUrl);
    }

    @Test
    void testConstructorAndInitialization() {
        assertNotNull(ui);
        assertEquals("Ανάλυση Ποσοστών (Κείμενο)", ui.getTitle());
    }

    @Test
    void testSuccessfulCalculationFlow() {
        // Εύρεση components
        JComboBox<?> combo = findComponent(ui, JComboBox.class);
        JButton btn = findComponent(ui, JButton.class);
        JTextArea area = findComponent(ui, JTextArea.class);

        assertNotNull(combo);
        assertNotNull(btn);

        // Επιλογή "Έσοδα" (είναι το πρώτο στοιχείο)
        combo.setSelectedIndex(0);

        // Κλικ στο κουμπί
        btn.doClick();

        String result = area.getText();
        
        // Έλεγχος αν περιλαμβάνει τα headers
        assertTrue(result.contains("ΣΤΟΙΧΕΙΟ"));
        assertTrue(result.contains("ΠΟΣΟΣΤΟ %"));
        
        // Έλεγχος αν έγινε το truncation (οι τρεις τελείες ...)
        assertTrue(result.contains("...")); 
        
        // Έλεγχος υπολογισμού (1000 / 1500 = ~66.67%)
        assertTrue(result.contains("66.67%"));
    }

    @Test
    void testZeroTotalError() throws Exception {
        // Δημιουργία πίνακα με μηδενικό σύνολο
        try (Connection conn = DriverManager.getConnection(testDbUrl);
             Statement st = conn.createStatement()) {
            st.execute("DROP TABLE IF EXISTS kratos");
            st.execute("CREATE TABLE kratos (name TEXT, amount REAL)");
            // Δεν βάζουμε δεδομένα ή βάζουμε 0
        }

        JComboBox<?> combo = findComponent(ui, JComboBox.class);
        JButton btn = findComponent(ui, JButton.class);
        JTextArea area = findComponent(ui, JTextArea.class);

        // Επιλογή "Κράτος" (δείκτης 2)
        combo.setSelectedIndex(2);
        btn.doClick();

        assertTrue(area.getText().contains("Σφάλμα: Το συνολικό ποσό"));
    }

    @Test
    void testTableOptionInternalClass() {
        // Έλεγχος της εσωτερικής κλάσης για 100% coverage
        // Παρόλο που είναι private, ελέγχουμε τις public μεθόδους της μέσω του combo
        JComboBox<Object> combo = (JComboBox<Object>) findComponent(ui, JComboBox.class);
        Object item = combo.getItemAt(0);
        
        assertNotNull(item.toString()); // Καλύπτει την toString()
        assertTrue(item.toString().length() > 0);
    }

    // --- Helper Methods για αναζήτηση components στο UI ---
    
    private <T> T findComponent(Container container, Class<T> clazz) {
        for (Component comp : container.getComponents()) {
            if (clazz.isInstance(comp)) {
                return clazz.cast(comp);
            } else if (comp instanceof Container) {
                T result = findComponent((Container) comp, clazz);
                if (result != null) return result;
            }
        }
        return null;
    }
}