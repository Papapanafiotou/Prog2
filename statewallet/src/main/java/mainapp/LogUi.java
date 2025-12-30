package mainapp;
<<<<<<< HEAD

=======
>>>>>>> eab5d55b586c1d6fadcc0cbd631af542eb09407d
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Random;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

public class LogUi extends JFrame {
<<<<<<< HEAD

    private Accounts acc = new Accounts();

    public LogUi() {
        acc.createTable();

=======
    private Accounts acc = new Accounts();
    public LogUi() {
        acc.createTable();
>>>>>>> eab5d55b586c1d6fadcc0cbd631af542eb09407d
        setTitle("Σύστημα Χρηστών");
        setSize(400, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
<<<<<<< HEAD

=======
>>>>>>> eab5d55b586c1d6fadcc0cbd631af542eb09407d
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));
        panel.setBackground(new Color(245, 245, 250));
<<<<<<< HEAD

        // Εικονίδιο
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60)); // Ελαφρώς μικρότερο εικονίδιο
        userIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(userIcon);

        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Τίτλος
        JLabel welcomeLabel = new JLabel("Καλώς ήρθατε!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20)); // Ελαφρώς μικρότερη γραμματοσειρά
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setForeground(new Color(45, 52, 54));
        panel.add(welcomeLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 35)));

        // Δημιουργία Κουμπιών με το νέο μέγεθος
=======
        // Εικονίδιο
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60)); 
        userIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(userIcon);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        // Τίτλος
        JLabel welcomeLabel = new JLabel("Καλώς ορίσατε!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 20)); 
        welcomeLabel.setForeground(new Color(45, 52, 54));
        panel.add(welcomeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 35)));
        // Δημιουργία Κουμπιών 
>>>>>>> eab5d55b586c1d6fadcc0cbd631af542eb09407d
        JButton btnLogin = createStyledButton("Σύνδεση");
        JButton btnCreate = createStyledButton("Δημιουργία Λογαριασμού");
        JButton btnChange = createStyledButton("Αλλαγή Κωδικού");
        JButton btnForgot = createStyledButton("Ξέχασα Κωδικό");
        JButton btnExit = createStyledButton("Έξοδος");
<<<<<<< HEAD

        // Προσθήκη κουμπιών
=======
>>>>>>> eab5d55b586c1d6fadcc0cbd631af542eb09407d
        panel.add(btnLogin);
        panel.add(Box.createRigidArea(new Dimension(0, 13)));
        panel.add(btnCreate);
        panel.add(Box.createRigidArea(new Dimension(0, 13)));
        panel.add(btnChange);
        panel.add(Box.createRigidArea(new Dimension(0, 13)));
        panel.add(btnForgot);
<<<<<<< HEAD
        panel.add(Box.createRigidArea(new Dimension(0, 40))); 
        panel.add(btnExit);

        add(panel);

=======
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        panel.add(btnExit);
        add(panel);
>>>>>>> eab5d55b586c1d6fadcc0cbd631af542eb09407d
        // Listeners
        btnLogin.addActionListener(e -> login());
        btnCreate.addActionListener(e -> createAccount());
        btnChange.addActionListener(e -> changePassword());
        btnForgot.addActionListener(e -> forgotPassword());
        btnExit.addActionListener(e -> System.exit(0));
    }
<<<<<<< HEAD

    // Η μέθοδος για τα ελαφρώς μικρότερα κουμπιά
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        // Μικρότερο πλάτος και ύψος για πιο κομψή εμφάνιση
        btn.setMaximumSize(new Dimension(230, 35)); 
=======
    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(230, 35));
>>>>>>> eab5d55b586c1d6fadcc0cbd631af542eb09407d
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

<<<<<<< HEAD
    // --- Οι υπόλοιπες μέθοδοι παραμένουν ίδιες ---

    private void createAccount() {
        String username = JOptionPane.showInputDialog("Username:");
        if (username == null || username.trim().isEmpty()) return;

=======
    private void createAccount() {
        String username = JOptionPane.showInputDialog("Username:");
        if (username == null || username.trim().isEmpty()) return;
>>>>>>> eab5d55b586c1d6fadcc0cbd631af542eb09407d
        if (acc.getPassword(username) != null) {
            JOptionPane.showMessageDialog(null, "Το όνομα χρήστη υπάρχει ήδη!", "Σφάλμα", JOptionPane.ERROR_MESSAGE);
            return;
        }
<<<<<<< HEAD

        String id = JOptionPane.showInputDialog("Αριθμός Ταυτότητας:");
        if (id == null) return;

        String password = "";
        String[] options = {"Τυχαίος", "Χειροκίνητος"};
        int choice = JOptionPane.showOptionDialog(null, "Επιλογή κωδικού", "Κωδικός", 0, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

=======
        String id = JOptionPane.showInputDialog("Αριθμός Ταυτότητας:");
        if (id == null) return;
        String password = "";
        String[] options = {"Τυχαίος", "Χειροκίνητος"};
        int choice = JOptionPane.showOptionDialog(null, "Επιλογή κωδικού", "Κωδικός", 0, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
>>>>>>> eab5d55b586c1d6fadcc0cbd631af542eb09407d
        if (choice == 0) {
            String chars = "abcdefghijklmnopqrstuvwxyz0123456789!@#$";
            Random r = new Random();
            for (int i = 0; i < 10; i++) password += chars.charAt(r.nextInt(chars.length()));
            JOptionPane.showMessageDialog(null, "Ο κωδικός σας: " + password);
        } else {
            password = JOptionPane.showInputDialog("Δώστε κωδικό (8+ χαρακτήρες):");
            if (password == null) return;
            if (!acc.validatePassword(password)) {
                JOptionPane.showMessageDialog(null, "Αδύναμος κωδικός!");
                return;
            }
        }
        acc.createAccount(username, password, id);
        JOptionPane.showMessageDialog(null, "Επιτυχής δημιουργία λογαριασμού!");
    }

<<<<<<< HEAD
    private void login() {
=======
     private void login() {
>>>>>>> eab5d55b586c1d6fadcc0cbd631af542eb09407d
        String user = JOptionPane.showInputDialog("Username:");
        if (user == null) return;
        String realPass = acc.getPassword(user);
        if (realPass == null) {
            JOptionPane.showMessageDialog(null, "Ο χρήστης δεν βρέθηκε.");
            return;
        }
        String pass = JOptionPane.showInputDialog("Κωδικός:");
        if (pass == null) return;
<<<<<<< HEAD

=======
>>>>>>> eab5d55b586c1d6fadcc0cbd631af542eb09407d
        if (acc.logIn(realPass, pass)) {
            JOptionPane.showMessageDialog(null, "Επιτυχής σύνδεση!");
            this.dispose();
            new StateWalletLauncher().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Λάθος κωδικός.");
        }
    }

<<<<<<< HEAD
    private void changePassword() {
=======
        private void changePassword() {
>>>>>>> eab5d55b586c1d6fadcc0cbd631af542eb09407d
        String user = JOptionPane.showInputDialog("Username:");
        if (user == null) return;
        String oldP = JOptionPane.showInputDialog("Τρέχων κωδικός:");
        if (oldP == null) return;
<<<<<<< HEAD

=======
>>>>>>> eab5d55b586c1d6fadcc0cbd631af542eb09407d
        if (acc.logIn(acc.getPassword(user), oldP)) {
            String newP = JOptionPane.showInputDialog("Νέος κωδικός:");
            if (newP != null && acc.validatePassword(newP)) {
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
        if (user != null && id != null && id.equals(acc.getID(user))) {
            JOptionPane.showMessageDialog(null, "Ο κωδικός σας είναι: " + acc.getPassword(user));
        } else {
            JOptionPane.showMessageDialog(null, "Τα στοιχεία δεν ταυτίζονται.");
        }
    }
<<<<<<< HEAD

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LogUi().setVisible(true));
    }
}
=======
        public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LogUi().setVisible(true));
    }
}




>>>>>>> eab5d55b586c1d6fadcc0cbd631af542eb09407d
