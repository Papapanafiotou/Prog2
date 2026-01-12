package mainapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.util.Scanner;

/**
 * Test class for Weights to achieve 100% JaCoCo line and branch coverage.
 */
class WeightsTest {

    @BeforeEach
    void setUp() {
        // Απαραίτητο για την EconomicsChart που καλείται στην getAllGrades
        System.setProperty("java.awt.headless", "true");
    }

    private Scanner createScanner(String input) {
        return new Scanner(new ByteArrayInputStream(input.getBytes()));
    }

    @Test
    void testGetWeightValidAndInvalid() {
        /**
         * Σενάριο:
         * 1. 'abc' -> Exception (Catch block)
         * 2. 1.5   -> Εκτός ορίων (>1)
         * 3. -0.5  -> Εκτός ορίων (<0)
         * 4. 0.5   -> Έγκυρο
         */
        Scanner scanner = createScanner("abc\n1.5\n-0.5\n0.5\n");
        Weights weightsObj = new Weights(scanner);
        
        double result = weightsObj.getWeight();
        assertEquals(0.5, result, 0.001);
    }

    @Test
    void testShowTotalWeights() {
        Weights weightsObj = new Weights(new Scanner(""));
        double[] a = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}; // 10 στοιχεία
        double w1 = 0.5, w2 = 0.3, w3 = 0.2;

        double[] result = weightsObj.showTotalWeights(a, w1, w2, w3);

        // Έλεγχος αν υπολογίστηκαν σωστά οι επιδράσεις
        assertEquals(0.5, result[0]); // Econ (idx 0 < 3)
        assertEquals(0.3, result[3]); // Env (idx 3 < 6)
        assertEquals(0.2, result[6]); // Soc (idx 6 < 10)
    }

    @Test
    void testAddWeightsWithRetries() {
        /**
         * Σενάριο για addWeights:
         * Econ: 0.5, 0.1, 0.1 (Σύνολο 0.7 -> Retry) -> 0.4, 0.3, 0.3 (Σύνολο 1.0 -> OK)
         * Env: 0.5, 0.5, 0.5 (Σύνολο 1.5 -> Retry) -> 0.4, 0.3, 0.3 (Σύνολο 1.0 -> OK)
         * Soc: 0.2, 0.2, 0.2, 0.4 (Σύνολο 1.0 -> OK)
         */
        String input = 
            "0.5\n0.1\n0.1\n" + "0.4\n0.3\n0.3\n" + // Econ retry and success
            "0.5\n0.5\n0.5\n" + "0.4\n0.3\n0.3\n" + // Env retry and success
            "0.2\n0.2\n0.2\n0.4\n";              // Soc success
        
        Scanner scanner = createScanner(input);
        Weights weightsObj = new Weights(scanner);
        
        double[] result = weightsObj.addWeights();
        assertEquals(10, result.length);
        assertEquals(0.4, result[0]); // GDP
        assertEquals(0.4, result[9]); // Crime
    }

    @Test
    void testGetAllGrades() {
        /**
         * Η getAllGrades καλεί τις EconElemGrades, EnvElemGrades, SocElemGrades.
         * Χρειάζεται 10 βάρη που αθροίζουν σε 1.0.
         */
        double[] a = {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
        Weights weightsObj = new Weights(new Scanner(""));
        
        // Καλούμε τη μέθοδο. Επειδή τυπώνει πολλά και βγάζει γράφημα,
        // ελέγχουμε απλά ότι δεν πετάει Exception.
        assertDoesNotThrow(() -> weightsObj.getAllGrades(a));
    }
}