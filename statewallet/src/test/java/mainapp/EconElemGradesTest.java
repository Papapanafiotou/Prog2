package mainapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class EconElemGradesTest {

    @Test
    public void testGDPGrowthGrade() {
        EconElemGrades econ = new EconElemGrades();
        
        // Έλεγχος ορίων (Boundary Testing)
        assertEquals(10, econ.getGDPGrowthGrade(0.025), "0.025 >= 0.022 άρα βαθμός 10");
        assertEquals(9, econ.getGDPGrowthGrade(0.016), "0.016 >= 0.015 άρα βαθμός 9");
        assertEquals(6, econ.getGDPGrowthGrade(0.001), "0.001 >= 0.00 άρα βαθμός 6");
        assertEquals(5, econ.getGDPGrowthGrade(-0.01), "Αρνητικό ΑΕΠ άρα βαθμός 5");
    }

    @Test
    public void testPublicDebtGrade() {
        EconElemGrades econ = new EconElemGrades();
        
        // Χρέος: όσο μικρότερο τόσο καλύτερα
        assertEquals(10, econ.getPublicDebtGrade(130.0), "Χρέος < 141 άρα βαθμός 10");
        assertEquals(8, econ.getPublicDebtGrade(146.0), "146 < 150 άρα βαθμός 8");
        assertEquals(5, econ.getPublicDebtGrade(165.0), "Χρέος > 158 άρα βαθμός 5");
    }

    @Test
    public void testSurplusGrade() {
        EconElemGrades econ = new EconElemGrades();
        
        // Έλεγχος βάσει των νέων ορίων (SURP_LIMIT_10=0.03, SURP_LIMIT_7=0.009)
        assertEquals(10, econ.getSurplusGrade(0.04), "0.04 > 0.03 άρα βαθμός 10");
        
        // Η τιμή 0.01 είναι > 0.009 (Limit 7) αλλά < 0.016 (Limit 8)
        assertEquals(7, econ.getSurplusGrade(0.01), "0.01 >= 0.009 άρα βαθμός 7");
        
        assertEquals(5, econ.getSurplusGrade(-0.02), "Αρνητικό πλεόνασμα άρα βαθμός 5");
    }

    @Test
    public void testEconomicGradeCalculation() {
        EconElemGrades econ = new EconElemGrades();
        
        // w1=0.4, w2=0.3, w3=0.3
        // GDP: 0.025 -> Βαθμός 10
        // Debt: 130.0 -> Βαθμός 10
        // Surplus: 0.04 -> Βαθμός 10
        // Υπολογισμός: 4 + 3 + 3 = 10.0
        double finalGrade = econ.getEconomicGrade(0.4, 0.3, 0.3, 0.04, 130.0, 0.025);
        
        assertEquals(10.0, finalGrade, 0.01);
    }
}