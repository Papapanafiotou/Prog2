package mainapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

/**
 * Test class for DatabaseFinder to achieve 100% JaCoCo line coverage.
 */
class DatabaseFinderTest {

    @Test
    void testFindYearbaseReturnsTrueWhenFileExists() throws IOException {
        // Ρυθμίζουμε ένα προσωρινό έτος για το test
        int testYear = 9999;
        String fileName = "budget_" + testYear + ".db";
        File tempFile = new File(fileName);
        
        // Δημιουργούμε ένα πραγματικό κενό αρχείο για να το βρει η μέθοδος
        tempFile.createNewFile();

        try {
            DatabaseFinder finder = new DatabaseFinder();
            boolean exists = finder.findYearbase(testYear);
            
            assertTrue(exists, "Η μέθοδος θα έπρεπε να επιστρέψει true για υπάρχον αρχείο.");
        } finally {
            // Καθαρισμός: Διαγράφουμε το αρχείο μετά το test
            tempFile.delete();
        }
    }

    @Test
    void testFindYearbaseReturnsFalseWhenFileDoesNotExist() {
        // Χρησιμοποιούμε ένα έτος που είναι απίθανο να υπάρχει ως αρχείο
        int nonExistentYear = 1111;
        DatabaseFinder finder = new DatabaseFinder();
        
        // Βεβαιωνόμαστε ότι το αρχείο όντως δεν υπάρχει πριν το test
        File file = new File("budget_" + nonExistentYear + ".db");
        if (file.exists()) {
            file.delete();
        }

        boolean exists = finder.findYearbase(nonExistentYear);
        
        assertFalse(exists, "Η μέθοδος θα έπρεπε να επιστρέψει false για αρχείο που δεν υπάρχει.");
    }
}