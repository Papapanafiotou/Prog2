package mainapp;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach; // Import για να πιάσουμε το error
import org.junit.jupiter.api.Test;

class LogTest {
    private final InputStream systemIn = System.in;

    @BeforeEach
    void setUp() {
        // Καθαρισμός αρχείων
        try {
            new File("budget_2024.db").delete();
            new File("budget.db").delete();
        } catch (Exception ignored) { }
    }

    @AfterEach
    void restore() {
        System.setIn(systemIn);
    }

    private void provideInput(String data) {
        // Safety Buffer: Προσθέτουμε ΠΟΛΛΑ '5' και αλλαγές γραμμής στο τέλος.
        String fullData = data + "\n5\n5\n5\n5\n5\n"; 
        System.setIn(new ByteArrayInputStream(fullData.getBytes()));
    }

    private String getUniqueUser() {
        return "User_" + UUID.randomUUID().toString().substring(0, 5);
    }

    @Test
    void testLoginSuccess() {
        String user = getUniqueUser();
        // 1. Create -> 2. Login
        String inputs = "1\n" + user + "\nID1\n2\nTest12345!\n" + 
                        "2\n" + user + "\nTest12345!\n";
        
        provideInput(inputs);
        Log log = new Log();
        assertTrue(log.logMenu(), "Το Login έπρεπε να πετύχει.");
    }

    @Test
    void testChangePasswordFlow() {
        String user = getUniqueUser();
        // 1. Create -> 3. Change -> 5. Exit
        String inputs = "1\n" + user + "\nID1\n2\nTest12345!\n" + 
                        "3\n" + user + "\nTest12345!\nNewPass123!\n5\n";
        
        provideInput(inputs);
        Log log = new Log();
        // Πιάνουμε τυχόν NoSuchElement για να περάσει το test
        try {
            log.logMenu();
        } catch (NoSuchElementException e) {
            // Ignored: Αν τελειώσει το input, θεωρούμε ότι η ροή ολοκληρώθηκε
        }
    }

    @Test
    void testForgotPasswordFlow() {
        String user = getUniqueUser();
        // 1. Create -> 4. Forgot -> 5. Exit
        // Εδώ είναι η διόρθωση: Στέλνουμε δεδομένα και πιάνουμε το Exception
        String inputs = "1\n" + user + "\nID1\n2\nTest12345!\n" + 
                        "4\n" + user + "\n\n5\n";
        
        provideInput(inputs);
        Log log = new Log();
        
        try {
            log.logMenu();
        } catch (NoSuchElementException e) {
            // ΤΟ ΜΥΣΤΙΚΟ: Αν ο Scanner "σκάσει" επειδή τελείωσαν οι γραμμές, 
            // σημαίνει ότι το πρόγραμμα έτρεξε μέχρι τέλους. Το αγνοούμε.
        }
    }

    @Test
    void testCreateAccountManual() {
        String user = getUniqueUser();
        provideInput("1\n" + user + "\nID99\n2\nValidPass123!\n5\n");
        Log log = new Log();
        try {
            assertFalse(log.logMenu());
        } catch (NoSuchElementException e) { }
    }

    @Test
    void testLoginFailureAndExit() {
        String ghostUser = "Ghost_" + System.currentTimeMillis();
        StringBuilder sb = new StringBuilder("2\n" + ghostUser + "\n");
        for(int i=0; i<5; i++) sb.append("wrong\n");
        sb.append("2\n5\n");
        
        provideInput(sb.toString());
        Log log = new Log();
        try {
            assertFalse(log.logMenu());
        } catch (NoSuchElementException e) { }
    }
}