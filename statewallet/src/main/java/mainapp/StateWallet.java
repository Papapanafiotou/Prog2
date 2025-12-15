package mainapp;


import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;



public class StateWallet {
    
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // 2. Εκκίνηση του Launcher (Το πρώτο παράθυρο)
        SwingUtilities.invokeLater(() -> {
            StateWalletLauncher launcher = new StateWalletLauncher();
            launcher.setVisible(true);
        });
    
       
        }
    }


