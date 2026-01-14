package mainapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

class DatabaseChooserTest {

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
    void testGetUrlWithValidYearAndNewDatabase() {
        // Δίνουμε το έτος ΚΑΙ μια απάντηση "2" (ΟΧΙ) για την περίπτωση που η βάση υπάρχει ήδη
        provideInput("2024\n2\n");
        
        DatabaseChooser chooser = new DatabaseChooser();
        String url = chooser.getUrl();
        
        assertEquals("jdbc:sqlite:budget_2024.db", url);
    }

    @Test
    void testGetUrlWithInvalidThenValidYear() {
        // Λάθος έτος, σωστό έτος και απάντηση "2"
        provideInput("2020\n2025\n2\n");
        
        DatabaseChooser chooser = new DatabaseChooser();
        String url = chooser.getUrl();
        
        assertEquals("jdbc:sqlite:budget_2025.db", url);
    }

    @Test
    void testGetUrlWhenDatabaseExistsAndUserSaysNo() {
        provideInput("2023\n2\n");
        
        DatabaseChooser chooser = new DatabaseChooser();
        String url = chooser.getUrl();
        
        assertNotNull(url);
        assertTrue(url.contains("budget_2023.db"));
    }
}