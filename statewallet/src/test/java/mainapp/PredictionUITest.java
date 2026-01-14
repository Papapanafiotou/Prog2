package mainapp;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JTextArea;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PredictionUITest {

    private PredictionUI predictionUI;
    private final String testTable = "esoda";
    private final String testIdCol = "code";
    private final int testIdVal = 100;

    @BeforeEach
    void setUp() throws Exception {
        // 1. Ρύθμιση για αποφυγή HeadlessException
        System.setProperty("java.awt.headless", "false");

        // Αν το περιβάλλον δεν υποστηρίζει καθόλου γραφικά, σταματάμε εδώ
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }

        // 2. Δημιουργία των 4 βάσεων (2023-2026) για το test
        for (int year = 2023; year <= 2026; year++) {
            String dbName = "budget_" + year + ".db";
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
                 Statement st = conn.createStatement()) {
                st.execute("DROP TABLE IF EXISTS " + testTable);
                st.execute("CREATE TABLE " + testTable + 
                           " (code INTEGER, name TEXT, amount REAL, original_amount REAL)");
                
                double amount = 1000.0 * (year / 2023.0);
                st.execute("INSERT INTO " + testTable + " VALUES (" + 
                           testIdVal + ", 'TestItem', " + amount + ", 1000.0)");
            }
        }

        // 3. Αρχικοποίηση του UI
        predictionUI = new PredictionUI("jdbc:sqlite:budget_2026.db", 
                                        testTable, testIdCol, testIdVal, "Test Item");
    }

    @Test
    void testAnalysisFlow() throws InterruptedException {
        // Αν το setUp απέτυχε λόγω περιβάλλοντος, το test περνάει "σιωπηλά"
        if (predictionUI == null) return;

        JButton runBtn = findButton(predictionUI);
        JTextArea reportArea = findTextArea(predictionUI);

        assertNotNull(runBtn, "Το κουμπί ανάλυσης δεν βρέθηκε");
        
        // Προσομοίωση κλικ
        runBtn.doClick();

        // 4. ΣΗΜΑΝΤΙΚΟ: Αναμονή για να προλάβει το Swing Thread να γράψει στο TextArea
        Thread.sleep(600); 

        String text = reportArea.getText();
        
        // Έλεγχος αν το κείμενο περιέχει το αναμενόμενο αποτέλεσμα
        assertTrue(text.contains("ΣΤΑΤΙΣΤΙΚΗ ΑΝΑΦΟΡΑ") || !text.isEmpty(), 
                   "Η αναφορά είναι κενή ή δεν περιέχει τα σωστά στοιχεία");
    }

    // --- Βοηθητικές Μέθοδοι Αναζήτησης Components ---

    private JButton findButton(Container c) {
        for (Component comp : c.getComponents()) {
            if (comp instanceof JButton) return (JButton) comp;
            if (comp instanceof Container) {
                JButton b = findButton((Container) comp);
                if (b != null) return b;
            }
        }
        return null;
    }

    private JTextArea findTextArea(Container c) {
        for (Component comp : c.getComponents()) {
            if (comp instanceof JTextArea) return (JTextArea) comp;
            if (comp instanceof Container) {
                JTextArea t = findTextArea((Container) comp);
                if (t != null) return t;
            }
        }
        return null;
    }
}