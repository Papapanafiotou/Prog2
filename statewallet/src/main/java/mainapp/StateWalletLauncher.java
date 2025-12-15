package mainapp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class StateWalletLauncher extends JFrame {
    
    private final JComboBox<String> yearSelector;
    private final JButton startButton;
    private final JLabel statusLabel;
    private final JProgressBar progressBar;

    private static final Color PRIMARY = new Color(70, 130, 180);
    private static final Color BG = new Color(245, 245, 250);

    // ο βασικος constructor
    public StateWalletLauncher() {
        setTitle("State Wallet");
        setSize(450, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

}
