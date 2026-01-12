package mainapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Test class for Pdftocsv to achieve high JaCoCo line coverage.
 */
class PdftocsvTest {

    @TempDir
    Path tempDir;

    /**
     * Coverage για τον private constructor.
     */
    @Test
    void testPrivateConstructor() throws NoSuchMethodException, InstantiationException, 
            IllegalAccessException, InvocationTargetException {
        Constructor<Pdftocsv> constructor = Pdftocsv.class.getDeclaredConstructor();
        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        Pdftocsv instance = constructor.newInstance();
        assertNotNull(instance);
    }

    /**
     * Δοκιμή της μεθόδου run. 
     * Επειδή η κλάση χρησιμοποιεί σκληρά κωδικοποιημένα paths (src/main/sources),
     * θα προσομοιώσουμε τη δομή φακέλων.
     */
    @Test
    void testRunFlowWithMissingFiles() {
        // Σενάριο: Το αρχείο PDF λείπει.
        // Αυτό θα ενεργοποιήσει το πρώτο catch block (Σφάλμα κατά τη μετονομασία).
        assertDoesNotThrow(() -> Pdftocsv.run(2026));
    }

    @Test
    void testRunProcessException() throws IOException {
        /**
         * Για να καλύψουμε το catch block της εκτέλεσης Python, 
         * δημιουργούμε τη δομή φακέλων αλλά χωρίς το script, 
         * ή με ένα αρχείο που δεν είναι εκτελέσιμο.
         */
        Path sources = Files.createDirectories(tempDir.resolve("src/main/sources"));
        Path scripts = Files.createDirectories(tempDir.resolve("src/scripts"));
        
        // Δημιουργία dummy PDF
        Path dummyPdf = sources.resolve("budget2026.pdf");
        Files.createFile(dummyPdf);

        // Αλλαγή του user.dir για να "κοροϊδέψουμε" την Pdftocsv να κοιτάξει στον tempDir
        String originalDir = System.getProperty("user.dir");
        System.setProperty("user.dir", tempDir.toString());

        try {
            // Θα προσπαθήσει να μετονομάσει το αρχείο (επιτυχία)
            // Αλλά θα αποτύχει στην εκτέλεση της Python (επειδή δεν υπάρχει το script)
            Pdftocsv.run(2026);
            
            // Έλεγχος αν το αρχείο επέστρεψε στο αρχικό του όνομα μετά το δεύτερο try-catch
            assertTrue(Files.exists(dummyPdf));
        } finally {
            System.setProperty("user.dir", originalDir);
        }
    }
}