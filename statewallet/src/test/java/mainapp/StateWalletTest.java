package mainapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Test class for StateWallet to achieve 100% JaCoCo line coverage.
 */
class StateWalletTest {

    /**
     * Επειδή ο κατασκευαστής είναι private (Utility Class), 
     * χρησιμοποιούμε Reflection για να τον καλέσουμε και να πάρουμε 100% coverage.
     */
    @Test
    void testConstructorIsPrivate() throws NoSuchMethodException, InstantiationException, 
            IllegalAccessException, InvocationTargetException {
        Constructor<StateWallet> constructor = StateWallet.class.getDeclaredConstructor();
        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()), 
                "Ο κατασκευαστής πρέπει να είναι private");
        
        constructor.setAccessible(true);
        StateWallet instance = constructor.newInstance();
        assertNotNull(instance);
    }

    @Test
    void testMainLoginFailure() {
        // Σενάριο: Ο χρήστης επιλέγει "5" (Έξοδος) στο πρώτο μενού (Log).
        // Η main θα σταματήσει αμέσως μετά το log.logMenu().
        String input = "5\n"; 
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Κλήση της main
        assertDoesNotThrow(() -> StateWallet.main(new String[]{}));
    }

    @Test
    void testMainFullFlow() {
        // Προσθέτουμε πολλά \n στο τέλος για ασφάλεια και καλύπτουμε όλα τα πιθανά prompts
        // 2 (Login), username, password, 2024 (Year), 2 (No Redo), 0 (Exit)
        // Προσθέτουμε επιπλέον "0" και "\n" σε περίπτωση που το μενού επαναλαμβάνεται
        String input = "2\ntest1\nTest12345!\n2024\n2\n0\n0\n0\n\n\n\n\n"; 
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

    assertDoesNotThrow(() -> {
        // Καλούμε τη main. Αν υπάρχουν έξτρα reads, τα \n θα τα καλύψουν.
        StateWallet.main(new String[]{});
    });
  }
}