package mainapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Scanner;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class WeightsTest {

    // Βοηθητική μέθοδος για να "ταΐζουμε" το Scanner με κείμενο
    private void provideInput(String data) {
        InputStream testInput = new ByteArrayInputStream(data.getBytes());
        System.setIn(testInput);
    }

    @Test
    void testGetWeightFullCoverage() {
        // Περιπτώσεις: 1. Μη αριθμός (abc), 2. Εκτός ορίων (1.5), 3. Έγκυρο (0.5)
        provideInput("abc\n1.5\n0.5\n");
        Weights weights = new Weights(new Scanner(System.in));
        
        double result = weights.getWeight();
        assertEquals(0.5, result, 0.001);
    }

    @Test
    void testShowTotalWeights() {
        Weights weightsObj = new Weights(new Scanner(System.in));
        double[] mockData = new double[10];
        for(int i=0; i<10; i++) mockData[i] = 1.0;

        // Έλεγχος αν οι υπολογισμοί για w1, w2, w3 γίνονται σωστά στις σωστές θέσεις
        double[] results = weightsObj.showTotalWeights(mockData, 0.5, 0.3, 0.2);
        
        assertEquals(0.5, results[0]); // Οικονομικό (i < 3)
        assertEquals(0.3, results[4]); // Περιβαλλοντικό (3 <= i < 6)
        assertEquals(0.2, results[9]); // Κοινωνικό (i >= 6)
    }

    @Test
    void testAddWeightsWithRetries() {
        // Προσομοίωση αποτυχίας αθροίσματος (π.χ. 0.1+0.1+0.1 != 1.0) και μετά επιτυχίας
        StringBuilder input = new StringBuilder();
        
        // Οικονομικά: Λάθος άθροισμα (0.1, 0.1, 0.1) -> Επανάληψη -> Σωστό (0.5, 0.3, 0.2)
        input.append("0.1\n0.1\n0.1\n"); 
        input.append("0.5\n0.3\n0.2\n");
        
        // Περιβαλλοντικά: Σωστό (0.4, 0.3, 0.3)
        input.append("0.4\n0.3\n0.3\n");
        
        // Κοινωνικά: Λάθος άθροισμα -> Επανάληψη -> Σωστό (0.25, 0.25, 0.25, 0.25)
        input.append("0.1\n0.1\n0.1\n0.1\n");
        input.append("0.25\n0.25\n0.25\n0.25\n");

        provideInput(input.toString());
        Weights weightsObj = new Weights(new Scanner(System.in));
        
        double[] result = weightsObj.addWeights();
        assertEquals(10, result.length);
        assertEquals(0.5, result[0]);
    }

    @Test
    void testGetAllGrades() {
        // Η μέθοδος αυτή καλεί εξωτερικές κλάσεις (DataforGrade κλπ)
        // Για Line Coverage, αρκεί να εκτελεστεί η ροή.
        Weights weightsObj = new Weights(new Scanner(System.in));
        double[] mockWeights = {0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1};
        
        // Θα χρειαστείς τις κλάσεις DataforGrade, EconElemGrades κλπ να είναι στο classpath
        assertDoesNotThrow(() -> weightsObj.getAllGrades(mockWeights));
    }
}