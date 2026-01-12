package mainapp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.lang.reflect.Field;

/**
 * Test class for LogUi to achieve high JaCoCo line coverage.
 */
class LogUiTest {

    private LogUi logUi;

    @BeforeEach
    void setUp() {
        // Ρύθμιση για να μην πετάει σφάλματα σε περιβάλλοντα χωρίς οθόνη (CI)
        System.setProperty("java.awt.headless", "true");
        logUi = new LogUi();
    }

    @Test
    void testConstructorAndInitialization() {
        assertNotNull(logUi);
        assertEquals("Σύστημα Χρηστών", logUi.getTitle());
    }

    @Test
    void testCreateAccountFlow() {
        // Χρησιμοποιούμε MockedStatic για να προσομοιώσουμε τις απαντήσεις στα παράθυρα διαλόγου
        try (MockedStatic<JOptionPane> mockedPane = mockStatic(JOptionPane.class)) {
            
            // Προσομοίωση: Ο χρήστης δίνει Username "testUser", ID "AB123" και επιλέγει "Τυχαίος κωδικός" (0)
            mockedPane.when(() -> JOptionPane.showInputDialog(anyString())).thenReturn("testUser", "AB123");
            mockedPane.when(() -> JOptionPane.showOptionDialog(any(), any(), anyString(), anyInt(), 
                    anyInt(), any(), any(), any())).thenReturn(0);

            // Εκτέλεση της μεθόδου μέσω Reflection ή απευθείας (αν ήταν public)
            // Εδώ καλούμε τη μέθοδο triggerAction για το κουμπί "Δημιουργία Λογαριασμού"
            invokeButtonAction("btnCreate");

            // Έλεγχος αν εμφανίστηκε το μήνυμα επιτυχίας
            mockedPane.verify(() -> JOptionPane.showMessageDialog(isNull(), contains("Επιτυχής"), 
                    anyString(), anyInt()), atLeastOnce());
        }
    }

    @Test
    void testLoginFlowFailure() {
        try (MockedStatic<JOptionPane> mockedPane = mockStatic(JOptionPane.class)) {
            // Προσομοίωση: Ο χρήστης δίνει username που δεν υπάρχει
            mockedPane.when(() -> JOptionPane.showInputDialog(anyString())).thenReturn("nonExistentUser");

            invokeButtonAction("btnLogin");

            // Έλεγχος αν εμφανίστηκε το μήνυμα σφάλματος
            mockedPane.verify(() -> JOptionPane.showMessageDialog(isNull(), contains("δεν βρέθηκε")));
        }
    }

    @Test
    void testForgotPasswordFlow() {
        try (MockedStatic<JOptionPane> mockedPane = mockStatic(JOptionPane.class)) {
            // Προσομοίωση username και ID
            mockedPane.when(() -> JOptionPane.showInputDialog(anyString())).thenReturn("user1", "ID123");

            invokeButtonAction("btnForgot");

            // Βεβαιωνόμαστε ότι έγινε η προσπάθεια ελέγχου
            mockedPane.verify(() -> JOptionPane.showMessageDialog(eq(null), anyString()), atLeastOnce());
        }
    }

    /**
     * Helper method για να "πατάμε" τα private κουμπιά της κλάσης μέσω Reflection.
     */
    private void invokeButtonAction(String fieldName) {
        try {
            // Αναζήτηση του button στο LogUi
            // Σημείωση: Αν τα buttons είναι τοπικές μεταβλητές στον constructor, 
            // πρέπει να καλέσουμε το ActionListener από το panel.
            // Αν τα κάνατε private fields, χρησιμοποιήστε τον παρακάτω κώδικα:
            Field field = LogUi.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            JButton btn = (JButton) field.get(logUi);
            for (ActionListener al : btn.getActionListeners()) {
                al.actionPerformed(null);
            }
        } catch (Exception e) {
            // Αν τα κουμπιά δεν είναι fields, μπορούμε να τα βρούμε από το περιεχόμενο του frame
            for (java.awt.Component comp : logUi.getContentPane().getComponents()) {
                if (comp instanceof javax.swing.JPanel) {
                    for (java.awt.Component inner : ((javax.swing.JPanel) comp).getComponents()) {
                        if (inner instanceof JButton && ((JButton) inner).getText().contains(getButtonText(fieldName))) {
                            for (ActionListener al : ((JButton) inner).getActionListeners()) {
                                al.actionPerformed(null);
                            }
                        }
                    }
                }
            }
        }
    }

    private String getButtonText(String fieldName) {
        return switch (fieldName) {
            case "btnLogin" -> "Σύνδεση";
            case "btnCreate" -> "Δημιουργία";
            case "btnChange" -> "Αλλαγή";
            case "btnForgot" -> "Ξέχασα";
            default -> "";
        };
    }
}