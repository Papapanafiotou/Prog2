package mainapp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.Test;

public class BudgetMenuTest {

    /**
     * Βοηθητική μέθοδος για να προσομοιώνουμε την είσοδο του χρήστη στο Scanner.
     */
    private void simulateInput(String data) {
        InputStream in = new ByteArrayInputStream(data.getBytes());
        System.setIn(in);
    }

    @Test
    public void testMenuExit() {
        // Επιλέγουμε το 10 για έξοδο αμέσως
        simulateInput("10\n");
        
        BudgetMenu menu = new BudgetMenu("jdbc:sqlite:test_budget.db");
        assertDoesNotThrow(menu::start);
    }

    @Test
    public void testMenuNavigation() {
        // Προσομοιώνουμε μια σειρά κινήσεων:
        // 1 (Εμφάνιση) -> 7 (Πίσω)
        // 4 (Σύνολο) -> 1 (Έσοδα)
        // 10 (Έξοδος)
        String inputs = "1\n7\n4\n1\n10\n";
        simulateInput(inputs);

        BudgetMenu menu = new BudgetMenu("jdbc:sqlite:test_budget.db");
        assertDoesNotThrow(menu::start);
    }

    @Test
    public void testHandleCharacterism() {
        // 7 (Χαρακτηρισμός) -> 1 (Αρχικά)
        // 7 (Χαρακτηρισμός) -> 2 (Επεξεργασμένα)
        // 10 (Έξοδος)
        String inputs = "7\n1\n7\n2\n10\n";
        simulateInput(inputs);

        BudgetMenu menu = new BudgetMenu("jdbc:sqlite:test_budget.db");
        assertDoesNotThrow(menu::start);
    }

    @Test
    public void testWrongChoice() {
        // 99 (Λάθος επιλογή) -> 10 (Έξοδος)
        String inputs = "99\n10\n";
        simulateInput(inputs);

        BudgetMenu menu = new BudgetMenu("jdbc:sqlite:test_budget.db");
        assertDoesNotThrow(menu::start);
    }
}