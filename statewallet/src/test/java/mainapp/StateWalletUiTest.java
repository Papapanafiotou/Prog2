package mainapp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Test class for StateWalletUi to achieve 100% JaCoCo line coverage.
 */
class StateWalletUiTest {

    /**
     * Έλεγχος του private constructor μέσω Reflection.
     * Απαραίτητο για να πρασινίσει η γραμμή του constructor στην JaCoCo.
     */
    @Test
    void testPrivateConstructor() throws NoSuchMethodException, InstantiationException, 
            IllegalAccessException, InvocationTargetException {
        Constructor<StateWalletUi> constructor = StateWalletUi.class.getDeclaredConstructor();
        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));
        
        constructor.setAccessible(true);
        StateWalletUi instance = constructor.newInstance();
        assertNotNull(instance);
    }

    /**
     * Έλεγχος της κανονικής ροής της main.
     */
    @Test
    void testMainMethodSuccess() {
        // Ρύθμιση headless για να μην προσπαθήσει να ανοίξει πραγματικό παράθυρο
        System.setProperty("java.awt.headless", "true");
        
        assertDoesNotThrow(() -> StateWalletUi.main(new String[]{}));
    }

    /**
     * Έλεγχος του multi-catch block.
     * Χρησιμοποιούμε Mockito για να προκαλέσουμε μια εξαίρεση κατά το LookAndFeel.
     */
    @Test
    void testMainMethodWithExceptions() {
        try (MockedStatic<UIManager> mockedUIManager = mockStatic(UIManager.class)) {
            // Προσομοίωση σφάλματος (π.χ. ClassNotFoundException)
            mockedUIManager.when(() -> UIManager.setLookAndFeel(anyString()))
                           .thenThrow(new ClassNotFoundException("Mocked Exception"));

            // Εκτέλεση της main - το catch block θα πιάσει το σφάλμα και θα κάνει printStackTrace
            assertDoesNotThrow(() -> StateWalletUi.main(new String[]{}));
        }
    }

    /**
     * Έλεγχος για UnsupportedLookAndFeelException (άλλο branch του catch).
     */
    @Test
    void testMainMethodWithUnsupportedLF() {
        try (MockedStatic<UIManager> mockedUIManager = mockStatic(UIManager.class)) {
            mockedUIManager.when(() -> UIManager.setLookAndFeel(anyString()))
                           .thenThrow(new UnsupportedLookAndFeelException("Not supported"));

            assertDoesNotThrow(() -> StateWalletUi.main(new String[]{}));
        }
    }
}