package mainapp;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class ConstrainsTest {

    @Test
    public void testNegativeAmountValidation() {
        // Σενάριο: Ο χρήστης δίνει -50 (λάθος), μετά "abc" (λάθος), μετά 100 (σωστό)
        String simulatedInput = "-50\nabc\n100\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        Scanner scanner = new Scanner(System.in);

        // Καλούμε τη μέθοδο με αρχική τιμή -50
        double result = Constrains.negativeAmount(scanner, -50.0);

        // Πρέπει να επιστρέψει 100.0, που ήταν η πρώτη έγκυρη είσοδος
        assertEquals(100.0, result);
    }

    @Test
    public void testIsReasonableChange() {
        // Έλεγχος ορίου 50% (MAX_CHANGE_LIMIT = 0.5)
        assertTrue(Constrains.isReasonableChange(100, 120)); // 20% αλλαγή - OK
        assertFalse(Constrains.isReasonableChange(100, 160)); // 60% αλλαγή - FAIL
        assertTrue(Constrains.isReasonableChange(0, 500));   // Αρχικό 0 - OK
    }

    @Test
    public void testDeficitLimit() {
        // Έλεγχος ορίου ελλείμματος 3% (MAX_DEFICIT_PERCENT = 3.0)
        assertTrue(Constrains.deficitLimit(1000, 900));  // Πλεόνασμα - OK
        assertTrue(Constrains.deficitLimit(1000, 1020)); // 2% έλλειμμα - OK
        assertFalse(Constrains.deficitLimit(1000, 1050)); // 5% έλλειμμα - FAIL
    }
}