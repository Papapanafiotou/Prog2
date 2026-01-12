package mainapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Test class for DatabaseChooser to achieve JaCoCo line coverage.
 */
class DatabaseChooserTest {

    private final InputStream systemIn = System.in;
    private ByteArrayInputStream testIn;

    @AfterEach
    void restoreSystemInput() {
        // Επαναφορά του κανονικού System.in μετά από κάθε test
        System.setIn(systemIn);
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }

    @Test
    void testGetUrlWithValidYearAndNewDatabase() {
        // Σενάριο: Ο χρήστης δίνει 2024. 
        // Αν η βάση δεν υπάρχει, θα τρέξει το πρώτο block (Pdftocsv, PinakesImporter).
        provideInput("2024\n");
        
        DatabaseChooser chooser = new DatabaseChooser();
        String url = chooser.getUrl();
        
        assertEquals("jdbc:sqlite:budget_2024.db", url);
    }

    @Test
    void testGetUrlWithInvalidThenValidYear() {
        // Σενάριο: Ο χρήστης δίνει λάθος έτος (2020) και μετά σωστό (2025).
        // Αυτό καλύπτει τις γραμμές του "while" loop.
        provideInput("2020\n2025\n");
        
        DatabaseChooser chooser = new DatabaseChooser();
        String url = chooser.getUrl();
        
        assertEquals("jdbc:sqlite:budget_2025.db", url);
    }

    @Test
    void testGetUrlWhenDatabaseExistsAndUserSaysNo() {
        // Σενάριο: Η βάση υπάρχει και ο χρήστης επιλέγει "2" (ΟΧΙ επανεκκίνηση).
        // Χρησιμοποιούμε το 2023 που πιθανώς υπάρχει ήδη.
        provideInput("2023\n2\n");
        
        DatabaseChooser chooser = new DatabaseChooser();
        String url = chooser.getUrl();
        
        assertNotNull(url);
        assertTrue(url.contains("budget_2023.db"));
    }

    @Test
    void testGetUrlWhenDatabaseExistsAndUserSaysYes() {
        // Σενάριο: Η βάση υπάρχει και ο χρήστης επιλέγει "1" (ΝΑΙ επανεκκίνηση).
        // Καλύπτει τις γραμμές διαγραφής και επανεισαγωγής.
        provideInput("2023\n1\n");
        
        DatabaseChooser chooser = new DatabaseChooser();
        String url = chooser.getUrl();
        
        assertEquals("jdbc:sqlite:budget_2023.db", url);
    }
}