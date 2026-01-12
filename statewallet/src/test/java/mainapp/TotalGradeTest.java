package mainapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Test class for TotalGrade to achieve 100% JaCoCo line and branch coverage.
 */
class TotalGradeTest {

    private final InputStream systemIn = System.in;

    @BeforeEach
    void setUp() {
        // Headless mode για να μην προσπαθήσουν τα charts να ανοίξουν παράθυρα
        System.setProperty("java.awt.headless", "true");
    }

    @AfterEach
    void restoreSystemInput() {
        System.setIn(systemIn);
    }

    private void provideInput(String data) {
        // Χρήση CP737 ή UTF-8 ανάλογα με το περιβάλλον, 
        // εδώ το ByteArrayInputStream λειτουργεί για τα νούμερα
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    @Test
    void testGetTotalGradeSingleYearWithRetry() {
        /**
         * Σενάριο: 
         * 1. Επιλογή 0 (Single Year)
         * 2. Έτος 2024
         * 3. 10 Βάρη (0.1 το καθένα για να αθροίζουν σε 1.0)
         * 4. Βάρη τομέων που ΔΕΝ αθροίζουν σε 1 (π.χ. 0.5, 0.5, 0.5 = 1.5) -> Ενεργοποιεί το Retry
         * 5. Βάρη τομέων που αθροίζουν σε 1 (0.4, 0.3, 0.3)
         */
        StringBuilder sb = new StringBuilder();
        sb.append("0\n"); // Option
        sb.append("2024\n"); // Year (για την DataforGrade)
        // 10 βάρη για την w.addWeights()
        for (int i = 0; i < 10; i++) {
            sb.append("0.1\n");
        }
        // Πρώτη προσπάθεια βαρών τομέων (άθροισμα 1.5)
        sb.append("0.5\n0.5\n0.5\n");
        // Δεύτερη προσπάθεια (άθροισμα 1.0)
        sb.append("0.4\n0.3\n0.3\n");

        provideInput(sb.toString());

        TotalGrade totalGrade = new TotalGrade();
        assertDoesNotThrow(() -> totalGrade.getTotalGrade());
    }

    @Test
    void testGetTotalGradeAllYears() {
        /**
         * Σενάριο:
         * 1. Επιλογή 1 (All Years)
         * 2. 10 Βάρη (0.1)
         * 3. Βάρη τομέων (0.4, 0.3, 0.3)
         */
        StringBuilder sb = new StringBuilder();
        sb.append("1\n"); // Option
        for (int i = 0; i < 10; i++) {
            sb.append("0.1\n");
        }
        sb.append("0.4\n0.3\n0.3\n");

        provideInput(sb.toString());

        TotalGrade totalGrade = new TotalGrade();
        assertDoesNotThrow(() -> totalGrade.getTotalGrade());
    }
}