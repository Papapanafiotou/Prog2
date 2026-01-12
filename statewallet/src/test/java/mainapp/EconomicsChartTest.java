package mainapp;

import java.awt.Window;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class EconomicsChartTest {

    @Test
    public void testShowPieChartInitialization() throws Exception {
        // Επειδή η showPieChart δημιουργεί JFrame, την τρέχουμε στο Event Dispatch Thread του Swing
        SwingUtilities.invokeAndWait(() -> {
            EconomicsChart chart = new EconomicsChart();
            String[] names = {"Κατηγορία Α", "Κατηγορία Β"};
            double[] percentages = {0.6, 0.4};

            // Έλεγχος ότι δεν πετάει Exception κατά τη δημιουργία
            assertDoesNotThrow(() -> chart.showPieChart(names, percentages));
            
            // Βρίσκουμε το παράθυρο που δημιουργήθηκε
            Window[] windows = Window.getWindows();
            boolean found = false;
            for (Window w : windows) {
                if (w instanceof JFrame && ((JFrame) w).getTitle().equals("Ανάλυση Οικονομικών Μεγεθών")) {
                    found = true;
                    w.dispose(); // Κλείνουμε το παράθυρο για να μην μείνει ανοιχτό
                }
            }
            assertTrue(found, "Το παράθυρο του Pie Chart πρέπει να έχει δημιουργηθεί");
        });
    }

    @Test
    public void testDisplayGraphCall() {
        EconomicsChart chart = new EconomicsChart();
        String[] years = {"2020", "2021", "2022"};
        double[] grades = {7.5, 8.0, 9.2};

        // Ελέγχουμε αν η μέθοδος εκτελείται χωρίς σφάλμα. 
        // Λόγω Platform.runLater, η σχεδίαση γίνεται σε άλλο thread.
        assertDoesNotThrow(() -> chart.displayGraph("Test Chart", years, grades));
    }
}