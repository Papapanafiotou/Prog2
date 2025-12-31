package mainapp;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.io.Serial;
import java.util.Random;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * Η γραφική διεπαφή για τη σύνδεση χρηστών (Login Screen).
 */
public final class LogUi extends JFrame {

    /** Serial Version UID. */
    @Serial
    private static final long serialVersionUID = 1L;

    /** Πλάτος παραθύρου. */
    private static final int WINDOW_WIDTH = 400;
    /** Ύψος παραθύρου. */
    private static final int WINDOW_HEIGHT = 550;
    /** Περιθώριο. */
    private static final int BORDER_PADDING = 30;
    /** Πλάτος κουμπιού. */
    private static final int BTN_WIDTH = 230;
    /** Ύψος κουμπιού. */
    private static final int BTN_HEIGHT = 35;
    /** Padding Οριζόντιο. */
    private static final int PAD_HOR = 50;

    /** Red component φόντου. */
    private static final int BG_R = 245;
    /** Green component φόντου. */
    private static final int BG_G = 245;
    /** Blue component φόντου. */
    private static final int BG_B = 250;

    /** Red component κειμένου. */
    private static final int TXT_R = 45;
    /** Green component κειμένου. */
    private static final int TXT_G = 52;
    /** Blue component κειμένου. */
    private static final int TXT_B = 54;

    /** Μέγεθος εικονιδίου. */
    private static final int ICON_SIZE = 60;
    /** Κενό μετά το εικονίδιο. */
    private static final int GAP_ICON = 8;
    /** Μέγεθος τίτλου. */
    private static final int TITLE_SIZE = 20;
    /** Κενό μετά τον τίτλο. */
    private static final int GAP_TITLE = 35;
    /** Κενό μεταξύ κουμπιών. */
    private static final int GAP_BTN = 13;
    /** Κενό πριν την έξοδο. */
    private static final int GAP_EXIT = 40;
    /** Μέγεθος γραμματοσειράς κουμπιών. */
    private static final int BTN_FONT = 13;
    /** Μήκος τυχαίου κωδικού. */
    private static final int RND_PASS_LEN = 10;

    /** Αντικείμενο λογαριασμών. */
    private final Accounts acc = new Accounts();

    /**
     * Κατασκευαστής.
     */
    public LogUi() {
        acc.createTable();
        setTitle("Σύστημα Χρηστών");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(
                BORDER_PADDING, PAD_HOR, BORDER_PADDING, PAD_HOR));
        panel.setBackground(new Color(BG_R, BG_G, BG_B));

        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, ICON_SIZE));
        userIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(userIcon);
        panel.add(Box.createRigidArea(new Dimension(0, GAP_ICON)));

        JLabel welcomeLabel = new JLabel("Καλώς ορίσατε!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, TITLE_SIZE));
        welcomeLabel.setForeground(new Color(TXT_R, TXT_G, TXT_B));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(welcomeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, GAP_TITLE)));

        JButton btnLogin = createStyledButton("Σύνδεση");
        JButton btnCreate = createStyledButton("Δημιουργία Λογαριασμού");
        JButton btnChange = createStyledButton("Αλλαγή Κωδικού");
        JButton btnForgot = createStyledButton("Ξέχασα τον Κωδικό");
        JButton btnExit = createStyledButton("Έξοδος");

        panel.add(btnLogin);
        panel.add(Box.createRigidArea(new Dimension(0, GAP_BTN)));
        panel.add(btnCreate);
        panel.add(Box.createRigidArea(new Dimension(0, GAP_BTN)));
        panel.add(btnChange);
        panel.add(Box.createRigidArea(new Dimension(0, GAP_BTN)));
        panel.add(btnForgot);
        panel.add(Box.createRigidArea(new Dimension(0, GAP_EXIT)));
        panel.add(btnExit);

        add(panel);

        btnLogin.addActionListener(e -> login());
        btnCreate.addActionListener(e -> createAccount());
        btnChange.addActionListener(e -> changePassword());
        btnForgot.addActionListener(e -> forgotPassword());
        btnExit.addActionListener(e -> System.exit(0));
    }

    private JButton createStyledButton(final String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, BTN_FONT));
        btn.setBackground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void createAccount() {
        String username = JOptionPane.showInputDialog("Username:");
        if (username == null || username.trim().isEmpty()) {
            return;
        }
        if (acc.getPassword(username) != null) {
            JOptionPane.showMessageDialog(null, "Το όνομα υπάρχει!",
                    "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String id = JOptionPane.showInputDialog("Αριθμός Ταυτότητας:");
        if (id == null) {
            return;
        }
        String password = "";
        String[] options = {"Τυχαίος", "Χειροκίνητος"};
        int choice = JOptionPane.showOptionDialog(null, "Επιλογή κωδικού",
                "Κωδικός", 0, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]);

        if (choice == 0) {
            String chars = "abcdefghijklmnopqrstuvwxyz0123456789!@#$";
            Random r = new Random();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < RND_PASS_LEN; i++) {
                sb.append(chars.charAt(r.nextInt(chars.length())));
            }
            password = sb.toString();
            JOptionPane.showMessageDialog(null, "Ο κωδικός σας: " + password);
        } else {
            password = JOptionPane.showInputDialog("Δώστε κωδικό (8+ chars):");
            if (password == null) {
                return;
            }
            if (!Accounts.validatePassword(password)) {
                JOptionPane.showMessageDialog(null, "Αδύναμος κωδικός!");
                return;
            }
        }
        acc.createAccount(username, password, id);
        JOptionPane.showMessageDialog(null, "Επιτυχής δημιουργία!");
    }

    private void login() {
        String user = JOptionPane.showInputDialog("Username:");
        if (user == null) {
            return;
        }
        String realPass = acc.getPassword(user);
        if (realPass == null) {
            JOptionPane.showMessageDialog(null, "Ο χρήστης δεν βρέθηκε.");
            return;
        }
        String pass = JOptionPane.showInputDialog("Κωδικός:");
        if (pass == null) {
            return;
        }
        if (acc.logIn(realPass, pass)) {
            JOptionPane.showMessageDialog(null, "Επιτυχής σύνδεση!");
            this.dispose();
            new StateWalletLauncher().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Λάθος κωδικός.");
        }
    }

    private void changePassword() {
        String user = JOptionPane.showInputDialog("Username:");
        if (user == null) {
            return;
        }
        String oldP = JOptionPane.showInputDialog("Τρέχων κωδικός:");
        if (oldP == null) {
            return;
        }
        if (acc.logIn(acc.getPassword(user), oldP)) {
            String newP = JOptionPane.showInputDialog("Νέος κωδικός:");
            if (newP != null && Accounts.validatePassword(newP)) {
                acc.newPass(newP, user);
                JOptionPane.showMessageDialog(null, "Ο κωδικός ενημερώθηκε!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Λάθος στοιχεία!");
        }
    }

    private void forgotPassword() {
        String user = JOptionPane.showInputDialog("Username:");
        String id = JOptionPane.showInputDialog("Αριθμός Ταυτότητας:");
        if (user != null && id != null && id.equals(acc.getId(user))) {
            JOptionPane.showMessageDialog(null,
                    "Ο κωδικός σας είναι: " + acc.getPassword(user));
        } else {
            JOptionPane.showMessageDialog(null, "Τα στοιχεία δεν ταυτίζονται.");
        }
    }
}
