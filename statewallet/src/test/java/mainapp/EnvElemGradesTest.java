package mainapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class EnvElemGradesTest {

    @Test
    public void testResGrade() {
        EnvElemGrades env = new EnvElemGrades();
        
        // Όρια ΑΠΕ (RES_LIMIT_10 = 0.30, RES_LIMIT_7 = 0.24)
        assertEquals(10, env.getResGrade(0.35), "0.35 >= 0.30 άρα βαθμός 10");
        assertEquals(7, env.getResGrade(0.25), "0.25 >= 0.24 άρα βαθμός 7");
        assertEquals(5, env.getResGrade(0.10), "0.10 < 0.22 άρα βαθμός 5");
    }

    @Test
    public void testEmissionGrade() {
        EnvElemGrades env = new EnvElemGrades();
        
        // Όρια Ρύπων (EMM_LIMIT_10 = -0.04, EMM_LIMIT_8 = 0.0)
        // Εδώ το μικρότερο είναι καλύτερο
        assertEquals(10, env.getEmissionGrade(-0.05), "-0.05 <= -0.04 άρα βαθμός 10");
        assertEquals(8, env.getEmissionGrade(-0.01), "-0.01 <= 0.0 άρα βαθμός 8");
        assertEquals(6, env.getEmissionGrade(3.0), "3.0 <= 3.9 άρα βαθμός 6");
        assertEquals(5, env.getEmissionGrade(5.0), "5.0 > 3.9 άρα βαθμός 5");
    }

    @Test
    public void testRecycleGrade() {
        EnvElemGrades env = new EnvElemGrades();
        
        // Όρια Ανακύκλωσης (REC_LIMIT_10 = 0.32, REC_LIMIT_6 = 0.16)
        assertEquals(10, env.getRecycleGrade(0.40), "0.40 >= 0.32 άρα βαθμός 10");
        assertEquals(6, env.getRecycleGrade(0.17), "0.17 >= 0.16 άρα βαθμός 6");
        assertEquals(5, env.getRecycleGrade(0.05), "0.05 < 0.16 άρα βαθμός 5");
    }

    @Test
    public void testEnvironmentalGradeCalculation() {
        EnvElemGrades env = new EnvElemGrades();
        
        // w1=0.4, w2=0.3, w3=0.3
        // RES: 0.35 -> Βαθμός 10
        // Emission: -0.05 -> Βαθμός 10
        // Recycle: 0.40 -> Βαθμός 10
        // Σύνολο: (0.4*10) + (0.3*10) + (0.3*10) = 10.0
        double finalGrade = env.getEnvironmentalGrade(0.4, 0.3, 0.3, 0.35, -0.05, 0.40);
        
        assertEquals(10.0, finalGrade, 0.01);
    }
}