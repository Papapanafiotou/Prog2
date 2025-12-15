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

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BG);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        addComp(mainPanel, new JLabel("🏛️"), new Font("Segoe UI Emoji", Font.PLAIN, 70), Color.BLACK, 5);
        addComp(mainPanel, new JLabel("State Wallet"), new Font("Segoe UI", Font.BOLD, 24), PRIMARY, 5);
        addComp(mainPanel, new JLabel("Επιλέξτε οικονομικό έτος"), new Font("Segoe UI", Font.PLAIN, 12), Color.GRAY, 30);

        yearSelector = new JComboBox<>(new String[]{"2023", "2024", "2025", "2026"});
        yearSelector.setMaximumSize(new Dimension(150, 30));
        yearSelector.setBackground(Color.WHITE);
        addComp(mainPanel, yearSelector, new Font("Segoe UI", Font.PLAIN, 14), Color.BLACK, 15);

       startButton = new JButton("Φόρτωση & Είσοδος");
        startButton.setBackground(PRIMARY);
        startButton.setForeground(Color.BLACK); // Λευκά γράμματα για αντίθεση
        startButton.setFocusPainted(false);
        startButton.setMaximumSize(new Dimension(200, 40));
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addComp(mainPanel, startButton, new Font("Segoe UI", Font.BOLD, 14), null, 20);

}
