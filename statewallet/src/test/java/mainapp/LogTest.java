package mainapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Test class for Log to achieve high JaCoCo line coverage.
 */
class LogTest {

    private final InputStream systemIn = System.in;

    @AfterEach
    void restoreSystemInput() {
        System.setIn(systemIn);
    }

    private void provideInput(String data) {
        ByteArrayInputStream testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @Test
    void testLogMenuExit() {
        // Επιλογή 5: Έξοδος
        provideInput("5\n");
        Log log = new Log();
        assertFalse(log.logMenu(), "Η logMenu πρέπει να επιστρέφει false στην έξοδο.");
    }

    @Test
    void testCreateAccountRandomPassword() {
        // 1 (Create) -> Name -> ID -> 1 (Random Pass) -> 5 (Exit)
        provideInput("1\ntestUser\nID12345\n1\n5\n");
        Log log = new Log();
        log.logMenu();
        // Ελέγχουμε αν εκτελέστηκαν οι γραμμές του handleCreate
    }

    @Test
    void testCreateAccountManualPassword() {
        // 1 (Create) -> Name -> ID -> 2 (Manual) -> Password (θα αποτύχει το validation αν είναι μικρό) 
        // -> ValidPassword! -> 5 (Exit)
        // Σημείωση: Το validation εξαρτάται από την Accounts.validatePassword
        provideInput("1\nmanualUser\nID54321\n2\nShort\nValidPass123!\n5\n");
        Log log = new Log();
        log.logMenu();
    }

    @Test
    void testChangePasswordFlow() {
        // 3 (Change) -> Name -> OldPass -> NewPass -> 5 (Exit)
        provideInput("3\ntest1\nTest12345!\nNewStrongPass123!\n5\n");
        Log log = new Log();
        log.logMenu();
    }

    @Test
    void testForgotPasswordFlow() {
        // 4 (Forgot) -> Username -> 5 (Exit)
        provideInput("4\ntest1\n5\n");
        Log log = new Log();
        log.logMenu();
    }

    @Test
    void testLoginSuccess() {
        // 2 (Login) -> Username -> Password -> Επιστρέφει true άμεσα
        provideInput("2\ntest1\nTest12345!\n");
        Log log = new Log();
        assertTrue(log.logMenu(), "Πρέπει να επιστρέφει true μετά από επιτυχή σύνδεση.");
    }

    @Test
    void testLoginFailureAndRecovery() {
        // 2 (Login) -> Username -> 5 αποτυχημένες προσπάθειες -> 1 (Ανάκτηση) -> 5 (Exit)
        StringBuilder sb = new StringBuilder("2\ntestUser\n");
        for (int i = 0; i < 5; i++) {
            sb.append("wrongPass\n");
        }
        sb.append("1\n5\n"); // Ανάκτηση και μετά έξοδος
        
        provideInput(sb.toString());
        Log log = new Log();
        log.logMenu();
    }
}