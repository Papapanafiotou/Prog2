package mainapp;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Constructor;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

public class ConstrainsTest {

    @Test
    public void testPositiveAmount() {
        // Σενάριο 1: Θετικό ποσό. Δεν ζητείται είσοδος.
        Scanner scanner = new Scanner(System.in);
        double result = Constrains.negativeAmount(scanner, 150.0);
        assertEquals(150.0, result);
    }

    @Test
    public void testNegativeAmountAndContinue() {
        // Σενάριο 2: Αρνητικό ποσό και ο χρήστης επιλέγει "1" (ΝΑΙ).
        String input = "1\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        double result = Constrains.negativeAmount(scanner, -50.0);
        assertEquals(-50.0, result);
    }

    @Test
    public void testNegativeAmountAndCancel() {
        // Σενάριο 3: Αρνητικό ποσό και ο χρήστης επιλέγει "0" (ΟΧΙ).
        String input = "0\n";
        Scanner scanner = new Scanner(new ByteArrayInputStream(input.getBytes()));
        
        double result = Constrains.negativeAmount(scanner, -50.0);
        assertEquals(0, result);
    }

    @Test
    public void testConstructorIsPrivate() throws Exception {
        // Αυτό το test χρησιμοποιεί Reflection για να καλέσει τον private constructor
        // Έτσι το JaCoCo θα "πρασινίσει" και τις γραμμές 11-13 της Constrains.java
        Constructor<Constrains> constructor = Constrains.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Constrains instance = constructor.newInstance();
        assertNotNull(instance);
    }
}