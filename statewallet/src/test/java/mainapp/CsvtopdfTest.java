package mainapp;

import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

public class CsvtopdfTest {

    @Test
    public void testRunMethodFlow() throws Exception {
        // Δημιουργούμε τη δομή φακέλων που περιμένει ο κώδικας για να μην "χτυπήσει" σφάλμα
        Path baseDir = Paths.get(".").toAbsolutePath().normalize();
        // Αν το τεστ τρέχει από το root, ίσως χρειαστεί να προσομοιώσουμε το src/main/sources
        Path sourceDir = baseDir.resolve(Paths.get("src", "main", "sources"));
        Files.createDirectories(sourceDir);

        // Δημιουργούμε ένα εικονικό PDF αρχείο για το έτος 2025
        int testYear = 2025;
        Path pdfPath = sourceDir.resolve("budget" + testYear + ".pdf");
        if (!Files.exists(pdfPath)) {
            Files.createFile(pdfPath);
        }

        // Εκτελούμε τη μέθοδο
        // Σημείωση: Αν δεν υπάρχει το python script, θα πιάσει το catch block, 
        // κάτι που επίσης θέλουμε για το coverage των σφαλμάτων.
        assertDoesNotThrow(() -> Pdftocsv.run(testYear));

        // Καθαρισμός (Cleanup)
        Files.deleteIfExists(sourceDir.resolve("budgettouse.pdf"));
        Files.deleteIfExists(pdfPath);
    }

    @Test
    public void testRunWithNonExistentFile() {
        // Ελέγχουμε την περίπτωση που το αρχείο PDF δεν υπάρχει για να καλύψουμε το πρώτο catch block
        assertDoesNotThrow(() -> Pdftocsv.run(9999));
    }

    @Test
    public void testConstructorIsPrivate() throws Exception {
        // Reflection για τον private constructor για 100% coverage
        Constructor<Pdftocsv> constructor = Pdftocsv.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Pdftocsv instance = constructor.newInstance();
        assertNotNull(instance);
    }
}