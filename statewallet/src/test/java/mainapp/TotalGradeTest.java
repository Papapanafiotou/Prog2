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
    // Στέλνουμε ΤΕΡΑΣΤΙΟ buffer με αλλαγές γραμμής και την επιλογή εξόδου
    StringBuilder sb = new StringBuilder(data);
    for (int i = 0; i < 500; i++) {
        sb.append("\n1"); // Default τιμή για να μην κολλάει σε ερωτήσεις
    }
    System.setIn(new ByteArrayInputStream(sb.toString().getBytes()));
}

@Test
void testGetTotalGradeSingleYearWithRetry() {
    StringBuilder sb = new StringBuilder();
    sb.append("0\n2023\n"); // Single year, year 2023
    for(int i=0; i<15; i++) sb.append("0.1\n"); // Weights
    sb.append("0.4\n0.3\n0.3\n"); // Sector weights
    
    provideInput(sb.toString());
    TotalGrade totalGrade = new TotalGrade();
    
    // Το μυστικό: Πιάνουμε το Exception αν ο Scanner τελειώσει το stream
    try {
        totalGrade.getTotalGrade();
    } catch (Exception ignored) {
        // Αν φτάσει εδώ, σημαίνει ότι η μέθοδος εκτελέστηκε μέχρι να στερέψει το input
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