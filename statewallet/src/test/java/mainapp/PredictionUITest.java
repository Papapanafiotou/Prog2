package mainapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.io.File;

class PredictionUITest {

    private PredictionUI predictionUI;
    private final String testTable = "esoda";
    private final String testIdCol = "code";
    private final int testIdVal = 100;

    @BeforeEach
    void setUp() throws Exception {
        System.setProperty("java.awt.headless", "true");

        // Δημιουργία των 4 βάσεων (2023-2026) με τη σωστή δομή πινάκων
        for (int year = 2023; year <= 2026; year++) {
            String dbName = "budget_" + year + ".db";
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
                 Statement st = conn.createStatement()) {
                st.execute("DROP TABLE IF EXISTS " + testTable);
                // Δημιουργία πίνακα με 4 στήλες όπως ορίζει ο PinakesImporter
                st.execute("CREATE TABLE " + testTable + 
                           " (code INTEGER, name TEXT, amount REAL, original_amount REAL)");
                
                // Εισαγωγή 4 τιμών: ID, Name, Current Amount, Original Amount
                double amount = 1000.0 * (year / 2023.0);
                st.execute("INSERT INTO " + testTable + " VALUES (" + 
                           testIdVal + ", 'TestItem', " + amount + ", 1000.0)");
            }
        }

        predictionUI = new PredictionUI("jdbc:sqlite:budget_2026.db", 
                                        testTable, testIdCol, testIdVal, "Test Item");
    }

    @Test
    void testAnalysisFlow() {
        JButton runBtn = findButton(predictionUI);
        JTextArea reportArea = findTextArea(predictionUI);

        assertNotNull(runBtn);
        runBtn.doClick();

        String text = reportArea.getText();
        assertTrue(text.contains("ΣΤΑΤΙΣΤΙΚΗ ΑΝΑΦΟΡΑ"));
        // Έλεγχος αν ο υπολογισμός y = mx + b παρήγαγε αποτέλεσμα
        assertTrue(text.contains("ΠΡΟΒΛΕΨΗ ΓΙΑ ΤΟ 2027"));
    }

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