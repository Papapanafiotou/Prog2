package mainapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.io.File;

/**
 * Test class for PredictionUI to achieve high JaCoCo line coverage.
 */
class PredictionUITest {

    private PredictionUI predictionUI;
    private final String testTable = "esoda";
    private final String testIdCol = "code";
    private final int testIdVal = 100;
    private final String testName = "Test Item";

    @BeforeEach
    void setUp() throws Exception {
        // Ενεργοποίηση headless mode για Swing tests
        System.setProperty("java.awt.headless", "true");

        // Δημιουργία εικονικών βάσεων δεδομένων για τα έτη 2023-2026
        // ώστε η collectDataFromYears να βρει δεδομένα.
        for (int year = 2023; year <= 2026; year++) {
            String dbName = "budget_" + year + ".db";
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbName);
                 Statement st = conn.createStatement()) {
                st.execute("CREATE TABLE IF NOT EXISTS " + testTable + 
                           " (" + testIdCol + " INTEGER, amount REAL)");
                st.execute("INSERT INTO " + testTable + " VALUES (" + testIdVal + ", " + (1000 * year / 2023.0) + ")");
            }
        }

        predictionUI = new PredictionUI("jdbc:sqlite:budget_2026.db", 
                                        testTable, testIdCol, testIdVal, testName);
    }

    @Test
    void testConstructorAndInit() {
        assertNotNull(predictionUI);
        assertEquals("Πρόβλεψη 2027 - " + testName, predictionUI.getTitle());
    }

    @Test
    void testAnalysisFlow() {
        // Εύρεση του κουμπιού στο UI
        JButton runBtn = null;
        JTextArea reportArea = null;

        for (Component comp : predictionUI.getContentPane().getComponents()) {
            if (comp instanceof JButton) {
                runBtn = (JButton) comp;
            } else if (comp instanceof JScrollPane) {
                Component inner = ((JScrollPane) comp).getViewport().getView();
                if (inner instanceof JTextArea) {
                    reportArea = (JTextArea) inner;
                }
            }
        }

        assertNotNull(runBtn, "Το κουμπί ανάλυσης δεν βρέθηκε.");
        assertNotNull(reportArea, "Η περιοχή κειμένου δεν βρέθηκε.");

        // Προσομοίωση πατήματος κουμπιού
        runBtn.doClick();

        // Έλεγχος αν το reportArea περιέχει τα αποτελέσματα της παλινδρόμησης
        String text = reportArea.getText();
        assertTrue(text.contains("ΣΤΑΤΙΣΤΙΚΗ ΑΝΑΦΟΡΑ"));
        assertTrue(text.contains("ΠΡΟΒΛΕΨΗ ΓΙΑ ΤΟ 2027"));
        assertTrue(text.contains("Τάση:"));
    }

    @Test
    void testInsufficientData() throws Exception {
        // Διαγραφή των βάσεων για να προκαλέσουμε σφάλμα έλλειψης δεδομένων
        for (int year = 2023; year <= 2026; year++) {
            File dbFile = new File("budget_" + year + ".db");
            if (dbFile.exists()) {
                dbFile.delete();
            }
        }

        // Ξανά-εκτέλεση της ανάλυσης
        JButton runBtn = findButton(predictionUI);
        runBtn.doClick();

        JTextArea area = findTextArea(predictionUI);
        assertTrue(area.getText().contains("Σφάλμα: Δεν βρέθηκαν επαρκή δεδομένα"));
    }

    // Helper methods για αναζήτηση components
    private JButton findButton(PredictionUI ui) {
        for (Component c : ui.getContentPane().getComponents()) {
            if (c instanceof JButton) return (JButton) c;
        }
        return null;
    }

    private JTextArea findTextArea(PredictionUI ui) {
        for (Component c : ui.getContentPane().getComponents()) {
            if (c instanceof JScrollPane) {
                return (JTextArea) ((JScrollPane) c).getViewport().getView();
            }
        }
        return null;
    }
}