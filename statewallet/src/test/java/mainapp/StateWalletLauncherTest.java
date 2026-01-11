package mainapp;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class StateWalletLauncherTest {

    @Test
    public void testLauncherInitialization() throws Exception {
        // Τρέχουμε το GUI test στο Event Dispatch Thread της Swing
        SwingUtilities.invokeAndWait(() -> {
            StateWalletLauncher launcher = new StateWalletLauncher();

            // 1. Έλεγχος βασικών ιδιοτήτων παραθύρου
            assertEquals("State Wallet", launcher.getTitle());
            assertFalse(launcher.isResizable());
            assertEquals(WindowConstants.EXIT_ON_CLOSE, launcher.getDefaultCloseOperation());

            // 2. Έλεγχος αν υπάρχουν τα απαραίτητα συστατικά
            // Ψάχνουμε το panel και τα περιεχόμενά του
            JPanel contentPane = (JPanel) launcher.getContentPane().getComponent(0);
            assertNotNull(contentPane);

            // 3. Έλεγχος αρχικής κατάστασης ProgressBar και Button
            // Χρησιμοποιούμε reflection ή ψάχνουμε τα components αν δεν είναι public
            boolean foundProgressBar = false;
            boolean foundButton = false;

            for (Component comp : contentPane.getComponents()) {
                if (comp instanceof JProgressBar) {
                    assertFalse(comp.isVisible(), "Η μπάρα προόδου πρέπει να είναι κρυφή στην αρχή");
                    foundProgressBar = true;
                }
                if (comp instanceof JButton) {
                    assertTrue(comp.isEnabled(), "Το κουμπί έναρξης πρέπει να είναι ενεργό");
                    foundButton = true;
                }
            }

            assertTrue(foundProgressBar, "Δεν βρέθηκε JProgressBar");
            assertTrue(foundButton, "Δεν βρέθηκε JButton");
            
            launcher.dispose(); // Κλείσιμο παραθύρου μετά το τεστ
        });
    }

    @Test
    public void testYearSelectorOptions() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            StateWalletLauncher launcher = new StateWalletLauncher();
            
            // Ψάχνουμε το JComboBox
            JComboBox<String> yearCombo = null;
            JPanel contentPane = (JPanel) launcher.getContentPane().getComponent(0);
            
            for (Component comp : contentPane.getComponents()) {
                if (comp instanceof JComboBox) {
                    yearCombo = (JComboBox<String>) comp;
                    break;
                }
            }

            assertNotNull(yearCombo, "Δεν βρέθηκε ο επιλογέας έτους");
            assertEquals(4, yearCombo.getItemCount(), "Πρέπει να υπάρχουν 4 επιλογές ετών (2023-2026)");
            assertEquals("2023", yearCombo.getItemAt(0));
            assertEquals("2026", yearCombo.getItemAt(3));
            
            launcher.dispose();
        });
    }
}