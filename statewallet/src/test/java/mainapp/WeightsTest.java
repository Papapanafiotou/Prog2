package mainapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.util.Scanner;

class WeightsTest {

    @Test
    void testGetWeightRetryLogic() {
        // Παρέχουμε: 
        // 1. "abc" (λάθος τύπος) 
        // 2. "\n" (για την nextLine() στο catch)
        // 3. "2.0" (εκτός ορίων)
        // 4. "0.7" (σωστό)
        String input = "abc\n2.0\n0.7\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        Weights weights = new Weights(scanner);

        double result = weights.getWeight();
        assertEquals(0.7, result, 0.001);
    }
}