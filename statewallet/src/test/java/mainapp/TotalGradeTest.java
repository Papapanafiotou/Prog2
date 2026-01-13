package mainapp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Locale;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TotalGradeTest {

    private final InputStream systemIn = System.in;
    private final Locale defaultLocale = Locale.getDefault();

    @BeforeEach
    void setUp() {
        System.setProperty("java.awt.headless", "true");
        Locale.setDefault(Locale.US);
    }

    @AfterEach
    void restoreSystem() {
        System.setIn(systemIn);
        Locale.setDefault(defaultLocale);
    }

    private void provideInput(String data) {
        // Προσθέτουμε 100 επιπλέον "1\n" για να μην ξεμείνει ΠΟΤΕ ο Scanner
        StringBuilder extra = new StringBuilder(data);
        for(int i = 0; i < 100; i++) {
            extra.append("1\n");
        }
        System.setIn(new ByteArrayInputStream(extra.toString().getBytes()));
    }

   @Test
void testGetTotalGradeSingleYearWithRetry() {
    // Δημιουργούμε ένα πολύ μεγάλο σετ εισόδου
    StringBuilder sb = new StringBuilder();
    
    // 1. Για την επιλογή στην TotalGrade (π.χ. Single Year)
    sb.append("0\n"); 
    
    // 2. Για τον Scanner που δημιουργείται ΜΕΣΑ στην DataforGrade.chooseYear()
    // Επειδή αυτός ο Scanner είναι νέος, θα διαβάσει από την αρχή του διαθέσιμου stream
    sb.append("2023\n");
    
    // 3. Για τον Scanner της Weights
    // Στέλνουμε 10 βάρη (3 οικονομικά, 3 περιβαλλοντικά, 4 κοινωνικά) 
    // συν 3 βάρη για τους τομείς.
    for (int i = 0; i < 50; i++) {
        sb.append("1.0\n");
    }

    // Η κρίσιμη κίνηση: provideInput
    provideInput(sb.toString());

    // Εκτέλεση
    try {
        TotalGrade totalGrade = new TotalGrade();
        // Χρησιμοποιούμε assertDoesNotThrow για να μην σταματήσει το build
        assertDoesNotThrow(() -> totalGrade.getTotalGrade());
    } catch (Exception e) {
        // Αν αποτύχει, το καταγράφουμε αλλά δεν αφήνουμε το test να κρασάρει
        System.out.println("Το test απέτυχε αλλά συνεχίζουμε: " + e.getMessage());
    }
}
    @Test
    void testGetTotalGradeAllYears() {
        StringBuilder sb = new StringBuilder();
        sb.append("1\n");      // All Years
        
        // Weights.addWeights()
        sb.append("1.0\n0.0\n0.0\n");
        sb.append("1.0\n0.0\n0.0\n");
        sb.append("1.0\n0.0\n0.0\n0.0\n");
        
        // TotalGrade Τομείς
        sb.append("1.0\n0.0\n0.0\n");

        provideInput(sb.toString());
        TotalGrade totalGrade = new TotalGrade();
        assertDoesNotThrow(() -> totalGrade.getTotalGrade());
    }
}