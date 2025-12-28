package mainapp;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class StateWalletUi {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            StateWalletLauncher launcher = new StateWalletLauncher();
            launcher.setVisible(true);
        });
    }
}
