package mainapp;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class DataforGradeTest {

    @Test
    public void testGetDataValidYear() {
        DataforGrade dfg = new DataforGrade();
        double[] data2018 = dfg.getData(2018);
        
        assertEquals(10, data2018.length, "Ο πίνακας δεδομένων πρέπει να έχει 10 στοιχεία");
        assertEquals(0.019, data2018[0], 0.0001);
        assertEquals(-0.01, data2018[9], 0.0001);
    }

    @Test
    public void testGetDataInvalidYear() {
        DataforGrade dfg = new DataforGrade();
        double[] data = dfg.getData(1990); // Έτος που δεν υπάρχει
        
        assertEquals(0, data.length, "Πρέπει να επιστρέφει κενό πίνακα για μη έγκυρο έτος");
    }

    @Test
    public void testChooseYearLoop() {
        // Προσομοίωση: Ο χρήστης δίνει 2010 (λάθος) και μετά 2022 (σωστό)
        String simulatedInput = "2010\n2022\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        
        DataforGrade dfg = new DataforGrade();
        int selectedYear = dfg.chooseYear();
        
        assertEquals(2022, selectedYear, "Η μέθοδος πρέπει να επιστρέψει το πρώτο έγκυρο έτος");
    }
}