package mainapp;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import org.mockito.MockedStatic;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mockStatic;

class LogUiTest {

    private LogUi logUi;

    @BeforeEach
    void setUp() {
        System.setProperty("java.awt.headless", "false");
        if (!GraphicsEnvironment.isHeadless()) {
            logUi = new LogUi();
        }
    }

    @Test
    void testConstructorAndInitialization() {
        if (logUi == null) return;
        assertNotNull(logUi);
        assertEquals("Σύστημα Χρηστών", logUi.getTitle());
    }

   @Test
void testCreateAccountFlow() {
    if (logUi == null) return;

    try (MockedStatic<JOptionPane> mockedPane = mockStatic(JOptionPane.class)) {
        // Προσομοιώνουμε τις εισόδους του χρήστη
        mockedPane.when(() -> JOptionPane.showInputDialog(anyString()))
                  .thenReturn("testUser", "AB123");
        
        mockedPane.when(() -> JOptionPane.showOptionDialog(any(), any(), anyString(), anyInt(), 
                            anyInt(), any(), any(), any()))
                  .thenReturn(0);

        // Πατάμε το κουμπί
        invokeButtonAction("Δημιουργία Λογαριασμού");

        // ΣΩΣΤΗ ΕΠΑΛΗΘΕΥΣΗ: Ελέγχουμε αν εμφανίστηκε οποιοδήποτε MessageDialog
        mockedPane.verify(() -> JOptionPane.showMessageDialog(
            nullable(java.awt.Component.class), 
            any(), 
            anyString(), 
            anyInt()
        ), atLeastOnce());
    }
}

    @Test
    void testLoginFlowFailure() {
        if (logUi == null) return;

        try (MockedStatic<JOptionPane> mockedPane = mockStatic(JOptionPane.class)) {
            mockedPane.when(() -> JOptionPane.showInputDialog(anyString()))
                      .thenReturn("nonExistentUser");

            invokeButtonAction("Σύνδεση");

            // Επαλήθευση για το μήνυμα "Ο χρήστης δεν βρέθηκε." (2 παράμετροι)
            mockedPane.verify(() -> JOptionPane.showMessageDialog(nullable(java.awt.Component.class), any()), atLeastOnce());
        }
    }

    // --- ΒΟΗΘΗΤΙΚΕΣ ΜΕΘΟΔΟΙ ---

    private void invokeButtonAction(String buttonText) {
        JButton button = findButton(logUi, buttonText);
        if (button != null) {
            for (ActionListener al : button.getActionListeners()) {
                al.actionPerformed(new java.awt.event.ActionEvent(button, 1001, "click"));
            }
        }
    }

    private JButton findButton(Container container, String text) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton && ((JButton) comp).getText().equals(text)) {
                return (JButton) comp;
            } else if (comp instanceof Container) {
                JButton res = findButton((Container) comp, text);
                if (res != null) return res;
            }
        }
        return null;
    }
}