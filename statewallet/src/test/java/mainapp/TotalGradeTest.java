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
        StringBuilder sb = new StringBuilder();
        
        // 1. Επιλογή Single Year (διαβάζεται από τον Scanner της TotalGrade)
        sb.append("0\n"); 

        // 2. Είσοδος για τον ΚΑΙΝΟΥΡΓΙΟ Scanner της DataforGrade.chooseYear()
        // Δίνουμε πολλά έτη σε περίπτωση που ο νέος Scanner χάσει την πρώτη γραμμή
        sb.append("2023\n"); 
        sb.append("2023\n");
        sb.append("2023\n");
        sb.append("2023\n");
        
        // 3. Βάρη για Weights.addWeights() (διαβάζονται από τον Scanner της Weights)
        // Οικονομικά (1.0, 0.0, 0.0)
        sb.append("1.0\n0.0\n0.0\n");
        // Περιβάλλον (1.0, 0.0, 0.0)
        sb.append("1.0\n0.0\n0.0\n");
        // Κοινωνία (1.0, 0.0, 0.0, 0.0)
        sb.append("1.0\n0.0\n0.0\n0.0\n");
        
        // 4. Τελικά βάρη τομέων στην TotalGrade
        sb.append("1.0\n0.0\n0.0\n");

        provideInput(sb.toString());
        TotalGrade totalGrade = new TotalGrade();
        assertDoesNotThrow(() -> totalGrade.getTotalGrade());
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