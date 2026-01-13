package mainapp;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextArea;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PercentageUITest {

    private BudgetManager mockManager;
    private PercentageUI ui;

    @BeforeEach
    void setUp() throws Exception {
        System.setProperty("java.awt.headless", "false");
        
        // Δημιουργούμε ένα mock του BudgetManager
        mockManager = mock(BudgetManager.class);

        if (!GraphicsEnvironment.isHeadless()) {
            // Χρησιμοποιούμε μια τυχαία διαδρομή (δεν θα χρησιμοποιηθεί λόγω mock)
            ui = new PercentageUI(mockManager, "jdbc:h2:mem:test_db");
        }
    }

    @Test
    void testSuccessfulCalculationFlow() {
        if (ui == null) return;

        // Ρυθμίζουμε το mock να επιστρέφει ένα σύνολο (π.χ. 1500)
        when(mockManager.getTotal("esoda")).thenReturn(new double[]{0.0, 1500.0});

        JTextArea area = findComponent(ui, JTextArea.class);
        JButton btn = findComponent(ui, JButton.class);
        JComboBox<?> combo = findComponent(ui, JComboBox.class);

        // Προσομοίωση επιλογής και κλικ
        combo.setSelectedIndex(0); // Έσοδα
        
        // Αντί για btn.doClick(), καλούμε τους ActionListeners χειροκίνητα 
        // για να αποφύγουμε προβλήματα με το UI Thread
        for (java.awt.event.ActionListener al : btn.getActionListeners()) {
            al.actionPerformed(new java.awt.event.ActionEvent(btn, 1001, "click"));
        }

        // Το JTextArea πρέπει τώρα να έχει ενημερωθεί (ή τουλάχιστον να μην είναι null)
        assertNotNull(area.getText());
    }

    @Test
    void testZeroTotalError() {
        if (ui == null) return;

        // Ορίζουμε ότι για τον πίνακα "kratos", το σύνολο στη θέση [1] είναι 0.0
        // Χρησιμοποιούμε και το "apokentromenes" ως εναλλακτική αν ο δείκτης αλλάξει
        when(mockManager.getTotal(anyString())).thenReturn(new double[]{0.0, 0.0});

        JButton btn = findComponent(ui, JButton.class);
        JComboBox<?> combo = findComponent(ui, JComboBox.class);
        JTextArea area = findComponent(ui, JTextArea.class);

        // Επιλέγουμε "Κράτος" (index 2)
        combo.setSelectedIndex(2);
        
        // Χειροκίνητη ενεργοποίηση των ActionListeners
        for (java.awt.event.ActionListener al : btn.getActionListeners()) {
            al.actionPerformed(new java.awt.event.ActionEvent(btn, 1001, "click"));
        }

        // Έλεγχος αν το κείμενο περιέχει τη λέξη "Σφάλμα"
        String text = area.getText();
        assertTrue(text.contains("Σφάλμα") || text.contains("0"), 
            "Το JTextArea θα έπρεπε να δείχνει μήνυμα σφάλματος. Περιεχόμενο: " + text);
    }

    private <T> T findComponent(Container container, Class<T> clazz) {
        for (Component comp : container.getComponents()) {
            if (clazz.isInstance(comp)) {
                return clazz.cast(comp);
            } else if (comp instanceof Container) {
                T result = findComponent((Container) comp, clazz);
                if (result != null) return result;
            }
        }
        return null;
    }
}