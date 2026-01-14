package mainapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Test class for PinakesImporter to achieve high JaCoCo line coverage.
 */
class PinakesImporterTest {

    private String testDbUrl = "jdbc:h2:mem:importerDb;DB_CLOSE_DELAY=-1";
    
    @TempDir
    Path tempDir; // JUnit 5 αυτόματη δημιουργία προσωρινού φακέλου

    @BeforeEach
    void setup() throws Exception {
        // Καθαρισμός/Προετοιμασία της H2
        try (Connection conn = DriverManager.getConnection(testDbUrl)) {
            Statement st = conn.createStatement();
            st.execute("DROP ALL OBJECTS");
        }
    }

    @Test
    void testImportAllWithDummyFiles() throws Exception {
        // Δημιουργούμε τη δομή φακέλων που περιμένει η importAll
        // Σημείωση: Η importAll ψάχνει για "src/main/sources"
        // Επειδή η διαδρομή είναι hardcoded, θα ελέγξουμε τις επιμέρους μεθόδους 
        // για να "χτυπήσουμε" τις γραμμές κώδικα.
        
        PinakesImporter importer = new PinakesImporter(testDbUrl);
        
        // 1. Δημιουργία προσωρινού CSV για Έσοδα
        File incomeCsv = new File(tempDir.toFile(), "income.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(incomeCsv))) {
            writer.write("Code,Name,Amount\n"); // Header
            writer.write("100,Test Income,1500.50\n");
            writer.write("10,ShortLine\n"); // Για να τεστάρουμε το skip λόγω p.length < maxlen
        }

        // 2. Δημιουργία προσωρινού CSV για Υπουργεία (με διαφορετικά IDs για όλα τα branches)
        File minCsv = new File(tempDir.toFile(), "ministries.csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(minCsv))) {
            writer.write("Number,Name,A1,A2,A3\n");
            writer.write("1000,Kratos Item,10,20,30\n");      // Κράτος range
            writer.write("1010,Ministry Item,40,50,60\n");    // Υπουργεία range
            writer.write("1900,Apokentromeni,70,80,90\n");    // Αποκεντρωμένες range
            writer.write("5000,Unknown Range,0,0,0\n");       // Εκτός range
        }

        // Εκτέλεση των επιμέρους μεθόδων (καθώς η importAll έχει hardcoded paths)
        try (Connection conn = DriverManager.getConnection(testDbUrl)) {
            // Χρησιμοποιούμε Reflection για να καλέσουμε τις private μεθόδους
            // ή τις ελέγχουμε έμμεσα αν ήταν public. 
            // Εδώ προσομοιώνουμε τη ροή της importAll:
            
            java.lang.reflect.Method createTables = PinakesImporter.class.getDeclaredMethod("createTables", Connection.class);
            createTables.setAccessible(true);
            createTables.invoke(importer, conn);

            java.lang.reflect.Method importEsoda = PinakesImporter.class.getDeclaredMethod("importEsoda", Connection.class, String.class);
            importEsoda.setAccessible(true);
            importEsoda.invoke(importer, conn, incomeCsv.getAbsolutePath());

            java.lang.reflect.Method importMinistries = PinakesImporter.class.getDeclaredMethod("importMinistries", Connection.class, String.class);
            importMinistries.setAccessible(true);
            importMinistries.invoke(importer, conn, minCsv.getAbsolutePath());

            // Έλεγχος αν τα δεδομένα μπήκαν σωστά
            Statement st = conn.createStatement();
            
            // Έλεγχος Εσόδων
            ResultSet rs1 = st.executeQuery("SELECT COUNT(*) FROM esoda");
            rs1.next();
            assertEquals(1, rs1.getInt(1), "Πρέπει να έχει εισαχθεί 1 γραμμή εσόδων (η 2η αγνοήθηκε)");

            // Έλεγχος Κράτους
            ResultSet rs2 = st.executeQuery("SELECT name FROM kratos WHERE number = 1000");
            assertTrue(rs2.next());
            assertEquals("Kratos Item", rs2.getString("name"));
        }
    }

    @Test
    void testImportAllExceptionHandling() {
        // Test με λάθος URL για να ενεργοποιήσουμε το catch block
        PinakesImporter importer = new PinakesImporter("jdbc:invalid:url");
        assertDoesNotThrow(() -> importer.importAll()); 
    }
}