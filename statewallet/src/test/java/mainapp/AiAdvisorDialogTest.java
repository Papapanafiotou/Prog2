package mainapp;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

public class AiAdvisorDialogTest {

    private static final String DB_PATH = "jdbc:sqlite:test_ai.db";

    @Test
    public void testInitializationWithSelection() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JFrame frame = new JFrame();
            // Προσομοίωση επιλογής εγγραφής
            AiAdvisorDialog dialog = new AiAdvisorDialog(frame, DB_PATH, "101", "Υγεία", "500000");

            // Έλεγχος τίτλου και διαστάσεων
            assertEquals("AI Οικονομικός Σύμβουλος", dialog.getTitle());
            assertEquals(850, dialog.getWidth());

            // Έλεγχος αν επιλέχθηκε η σωστή καρτέλα (TabIndex 1 για Specific Analysis)
            JTabbedPane tabs = findComponent(dialog, JTabbedPane.class);
            assertNotNull(tabs);
            assertEquals(1, tabs.getSelectedIndex(), "Όταν υπάρχει επιλογή, πρέπει να ανοίγει η 2η καρτέλα");

            dialog.dispose();
            frame.dispose();
        });
    }

    @Test
    public void testInitializationWithoutSelection() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JFrame frame = new JFrame();
            // Προσομοίωση χωρίς επιλογή (null τιμές)
            AiAdvisorDialog dialog = new AiAdvisorDialog(frame, DB_PATH, null, null, null);

            JTabbedPane tabs = findComponent(dialog, JTabbedPane.class);
            assertEquals(0, tabs.getSelectedIndex(), "Χωρίς επιλογή, πρέπει να ανοίγει η 1η καρτέλα (Global)");

            // Έλεγχος αν η περιοχή απάντησης είναι κενή/αρχικοποιημένη
            JEditorPane editor = findComponent(dialog, JEditorPane.class);
            assertNotNull(editor);
            assertEquals("text/html", editor.getContentType());

            dialog.dispose();
            frame.dispose();
        });
    }

    /**
     * Βοηθητική μέθοδος για την εύρεση Swing components μέσα στο δέντρο του παραθύρου.
     */
    private <T> T findComponent(Container container, Class<T> type) {
        for (Component c : container.getComponents()) {
            if (type.isInstance(c)) {
                return type.cast(c);
            } else if (c instanceof Container) {
                T result = findComponent((Container) c, type);
                if (result != null) return result;
            }
        }
        return null;
    }
}