package mainapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * Διαχειρίζεται τους λογαριασμούς χρηστών (δημιουργία, σύνδεση, ανάκτηση).
 */
public final class Accounts {

    /** Το URL σύνδεσης με τη βάση δεδομένων. */
    private static final String DB_URL = "jdbc:sqlite:accounts.db";
    /** Ελάχιστο μήκος κωδικού πρόσβασης. */
    private static final int MIN_PASS_LENGTH = 8;
    /** Δείκτης παραμέτρου SQL 1. */
    private static final int IDX_1 = 1;
    /** Δείκτης παραμέτρου SQL 2. */
    private static final int IDX_2 = 2;
    /** Δείκτης παραμέτρου SQL 3. */
    private static final int IDX_3 = 3;

    /**
     * Δημιουργεί τον πίνακα account στη βάση δεδομένων αν δεν υπάρχει.
     */
    public void createTable() {
        String sql = "CREATE TABLE account ("
                + "username TEXT NOT NULL, "
                + "password TEXT NOT NULL, "
                + "numID TEXT NOT NULL"
                + ");";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Ο πίνακας δημιουργήθηκε!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Δημιουργεί έναν νέο λογαριασμό χρήστη.
     *
     * @param name   Το όνομα χρήστη.
     * @param pass   Ο κωδικός πρόσβασης.
     * @param numID  Ο αριθμός ταυτότητας.
     */
    public void createAccount(final String name, final String pass,
                              final String numID) {
        String sql = "INSERT INTO account(username, password, numID) "
                + "VALUES(?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(IDX_1, name);
            pstmt.setString(IDX_2, pass);
            pstmt.setString(IDX_3, numID);
            pstmt.executeUpdate();
            System.out.println("Ο λογαριασμός σας δημιουργήθηκε!");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Ανακτά τον κωδικό πρόσβασης για ένα συγκεκριμένο όνομα χρήστη.
     *
     * @param username Το όνομα χρήστη.
     * @return Ο κωδικός πρόσβασης ή null αν δεν βρεθεί.
     */
    public String getPassword(final String username) {
        String sql = "SELECT password FROM account WHERE username = ?";
        String password = null;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(IDX_1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    password = rs.getString("password");
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return password;
    }

    /**
     * Ελέγχει αν τα διαπιστευτήρια σύνδεσης είναι σωστά.
     *
     * @param pass1 Ο αποθηκευμένος κωδικός (από τη βάση).
     * @param pass2 Ο κωδικός που έδωσε ο χρήστης.
     * @return true αν ταιριάζουν, false διαφορετικά.
     */
    public boolean logIn(final String pass1, final String pass2) {
        if (pass1 != null && pass1.equals(pass2)) {
            System.out.println("Επιτυχής σύνδεση! Καλωσορίσατε!");
            return true;
        } else {
            System.out.println("Λάθος κωδικός ή username! Δοκιμάστε ξανά");
            return false;
        }
    }

    /**
     * Ενημερώνει τον κωδικό πρόσβασης ενός χρήστη.
     *
     * @param password Ο νέος κωδικός.
     * @param name     Το όνομα χρήστη.
     */
    public void newPass(final String password, final String name) {
        String sql = "UPDATE account SET password = ? WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(IDX_1, password);
            pstmt.setString(IDX_2, name);
            pstmt.executeUpdate();
            System.out.println("Ο νέος κωδικός είναι " + password);
        } catch (SQLException e) {
            System.err.println("Σφάλμα στη βάση δεδομένων: " + e.getMessage());
        }
    }

    /**
     * Ελέγχει την εγκυρότητα ενός κωδικού πρόσβασης.
     *
     * @param password Ο κωδικός προς έλεγχο.
     * @return true αν ο κωδικός πληροί τα κριτήρια ασφαλείας.
     */
    public static boolean validatePassword(final String password) {
        if (password.length() < MIN_PASS_LENGTH) {
            System.out.println("Σφάλμα: Ο κωδικός πρέπει να έχει "
                    + "τουλάχιστον 8 χαρακτήρες.");
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        String specialChars = "#@$!%^&*()-_=+";

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (specialChars.indexOf(c) >= 0) {
                hasSpecial = true;
            }
        }

        if (!hasUpper) {
            System.out.println("Σφάλμα: Πρέπει να περιέχει κεφαλαίο γράμμα.");
            return false;
        }
        if (!hasLower) {
            System.out.println("Σφάλμα: Πρέπει να περιέχει πεζό γράμμα.");
            return false;
        }
        if (!hasDigit) {
            System.out.println("Σφάλμα: Πρέπει να περιέχει αριθμό.");
            return false;
        }
        if (!hasSpecial) {
            System.out.println("Σφάλμα: Πρέπει να περιέχει ειδικό χαρακτήρα.");
            return false;
        }

        System.out.println("Ο κωδικός είναι έγκυρος!");
        return true;
    }

    /**
     * Διαδικασία ανάκτησης κωδικού μέσω ταυτοποίησης με αριθμό ταυτότητας.
     *
     * @param username Το όνομα χρήστη που ξέχασε τον κωδικό.
     */
    public void forgotPass(final String username) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Για την ανάκτηση του κωδικού σας απαιτείται "
                + "ταυτοποίηση μέσω του αριθμού ταυτότητας."
                + " Εισάγετε τον αριθμό ταυτότητάς σας!"
        );
        String numID = scanner.nextLine();
        String realID = getId(username);
        if (realID != null && realID.equals(numID)) {
            String pass = getPassword(username);
            System.out.println("Επιτυχής ανάκτηση! Ο κωδικός είναι: " + pass);
        } else {
            System.out.println("Αποτυχημένη προσπάθεια ανάκτησης!");
        }
    }

    /**
     * Ανακτά τον αριθμό ταυτότητας που συνδέεται με ένα όνομα χρήστη.
     *
     * @param username Το όνομα χρήστη.
     * @return Ο αριθμός ταυτότητας ή null αν δεν βρεθεί.
     */
    public String getId(final String username) {
        String sql = "SELECT numID FROM account WHERE username = ?";
        String numID = null;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(IDX_1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    numID = rs.getString("numID");
                } else {
                    System.out.println("Ο χρήστης " + username
                            + " δεν βρέθηκε.");
                }
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return numID;
    }
}
