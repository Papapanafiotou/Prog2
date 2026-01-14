package mainapp;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GradeCharTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private GradeChar gradeChar;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent));
        gradeChar = new GradeChar();
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testGdpGrowthCharExcellent() {
        gradeChar.gdpGrowthChar(10);
        assertTrue(outContent.toString().contains("Μεγάλη αύξηση του ΑΕΠ"));
    }

    @Test
    public void testSurplusCharLowest() {
        gradeChar.surplusChar(5);
        assertTrue(outContent.toString().contains("αρνητική τιμή"));
    }

    @Test
    public void testGiniCharLow() {
        gradeChar.giniChar(6);
        assertTrue(outContent.toString().contains("μεγάλες εισοδηματικές ανισότητες"));
    }

    @Test
    public void testCrimeRateCharExcellent() {
        gradeChar.crimeRateChar(9);
        assertTrue(outContent.toString().contains("μειώθηκε σημαντικά"));
    }

    @Test
    public void testEdHealthCharAcceptable() {
        gradeChar.edHealthChar(7);
        assertTrue(outContent.toString().contains("επιτρεπτό επίπεδο"));
    }

    @Test
    public void testResCharLow() {
        gradeChar.resChar(5);
        assertTrue(outContent.toString().contains("ποσοστό χρήσης των ανανεώσιμων"));
    }

    @Test
    public void testEmissionCharGood() {
        gradeChar.emissionChar(8);
        assertTrue(outContent.toString().contains("Μικρή μείωση των ρύπων"));
    }
}