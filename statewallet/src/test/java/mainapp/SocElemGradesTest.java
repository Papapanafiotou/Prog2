package mainapp;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for SocElemGrades to achieve 100% JaCoCo line and branch coverage.
 */
class SocElemGradesTest {

    private SocElemGrades grader;

    @BeforeEach
    void setUp() {
        grader = new SocElemGrades();
    }

    @Test
    void testGetGINIGrade() {
        // Έλεγχος όλων των ορίων (Limits: 26.9, 28.9, 31.9, 33.9, 35.9)
        assertEquals(10, grader.getGINIGrade(25.0));   // <= 26.9
        assertEquals(9, grader.getGINIGrade(27.0));    // <= 28.9
        assertEquals(8, grader.getGINIGrade(30.0));    // <= 31.9
        assertEquals(7, grader.getGINIGrade(33.0));    // <= 33.9
        assertEquals(6, grader.getGINIGrade(35.0));    // <= 35.9
        assertEquals(5, grader.getGINIGrade(40.0));    // > 35.9
    }

    @Test
    void testGetCrimeGrade() {
        // Έλεγχος όλων των ορίων (Limits: -0.03, -0.01, 0.03, 0.06, 0.09)
        assertEquals(10, grader.getCrimeGrade(-0.05)); // <= -0.03
        assertEquals(9, grader.getCrimeGrade(-0.02));  // <= -0.01
        assertEquals(8, grader.getCrimeGrade(0.01));   // <= 0.03
        assertEquals(7, grader.getCrimeGrade(0.05));   // <= 0.06
        assertEquals(6, grader.getCrimeGrade(0.08));   // <= 0.09
        assertEquals(5, grader.getCrimeGrade(0.15));   // > 0.09
    }

    @Test
    void testGetMentalHealthGrade() {
        // Έλεγχος όλων των ορίων (Limits: 0.15, 0.16, 0.18, 0.21, 0.24)
        assertEquals(10, grader.getMentalHealthGrade(0.14)); // <= 0.15
        assertEquals(9, grader.getMentalHealthGrade(0.155)); // <= 0.16
        assertEquals(8, grader.getMentalHealthGrade(0.17));  // <= 0.18
        assertEquals(7, grader.getMentalHealthGrade(0.20));  // <= 0.21
        assertEquals(6, grader.getMentalHealthGrade(0.23));  // <= 0.24
        assertEquals(5, grader.getMentalHealthGrade(0.30));  // > 0.24
    }

    @Test
    void testGetHealthEduGrade() {
        // ΠΡΟΣΟΧΗ: Εδώ η λογική είναι αντίστροφη (>=) - Όσο μεγαλύτερο, τόσο καλύτερο
        // Limits: 0.13, 0.11, 0.095, 0.08, 0.07
        assertEquals(10, grader.getHealthEduGrade(0.15));  // >= 0.13
        assertEquals(9, grader.getHealthEduGrade(0.12));   // >= 0.11
        assertEquals(8, grader.getHealthEduGrade(0.10));   // >= 0.095
        assertEquals(7, grader.getHealthEduGrade(0.085));  // >= 0.08
        assertEquals(6, grader.getHealthEduGrade(0.075));  // >= 0.07
        assertEquals(5, grader.getHealthEduGrade(0.05));   // < 0.07
    }

    @Test
    void testGetSocialGrade() {
        // w1, w2, w3, w4
        double[] weights = {0.25, 0.25, 0.25, 0.25};
        
        // Εισαγωγή τιμών που θα δώσουν συγκεκριμένους βαθμούς
        // GINI: 25.0 -> 10
        // Crime: 0.01 -> 8
        // Mental: 0.14 -> 10
        // EduHealth: 0.075 -> 6
        // Σύνολο: (0.25*10) + (0.25*8) + (0.25*10) + (0.25*6) = 2.5 + 2.0 + 2.5 + 1.5 = 8.5
        
        double result = grader.getSocialGrade(weights, 25.0, 0.01, 0.14, 0.075);
        
        assertEquals(8.5, result, 0.001);
    }
}