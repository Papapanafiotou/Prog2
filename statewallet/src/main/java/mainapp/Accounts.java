package mainapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

public class Accounts {
    public void createTable() {
        String url = "jdbc:sqlite:accounts.db";
        String sql = "CREATE TABLE IF NOT EXISTS accounts (" +
                         "username TEXT NOT NULL," +
                         "password TEXT NOT NULL" +
                         ");";
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.prepareStatement(sql)) {
                stmt.execute(sql);
                System.out.println("Ο πίνακας δημιουργήθηκε!");
            } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    public void createAccount(String name, String pass) {
        String url = "jdbc:sqlite:accounts.db";
        String sql = "INSERT INTO accounts(username, password) VALUES(?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, pass);
                pstmt.executeUpdate();
            } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
    //εύρεση κωδικού με το username
    public String getPassword(String username) {
        String url = "jdbc:sqlite:accounts.db";
        String sql = "SELECT password FROM accounts WHERE username = " + username;
        String password = null;
         try (
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
                try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    password = rs.getString("password");
                    System.out.println("Βρέθηκε χρήστης: " + username);
                } else {
                    System.out.println("Ο χρήστης " + username + " δεν βρέθηκε.");
                }
            } catch (SQLException e) {
            System.err.println(e.getMessage());
            }
            } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return password;
    }
    //έλεγχος για την σύνδεση
    public boolean logIn(String pass1, String pass2) {
        if (pass1.equals(pass2)) {
            System.out.println("Επιτυχής σύνδεση! Καλωσορίσατε!");
            return true;
        } else {
            System.out.println("Λάθος κωδικός! Δοκιμάστε ξανά");
            return false;
        }
    }
    //μέθοδος για αλλαγή κωδικού
    public void newPass(String password, String name) {
        String url = "jdbc:sqlite:accounts.db";
        String sql = "UPDATE accounts SET password = ? WHERE usermame = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, password); 
            pstmt.setString(2, name); 
            pstmt.executeUpdate();
            System.out.println("Ο νέος κωδικός είναι " + password);
        } catch (SQLException e) {
            System.err.println("Σφάλμα στη βάση δεδομένων: " + e.getMessage());
        }
    }

    //μεθοδος για έλεγχο εγκυρότητας κωδικού
    public static boolean validatePassword(String password) {
        // Έλεγχος μήκους (τουλάχιστον 8 χαρακτήρες)
        if (password.length() < 8) {
            System.out.println("Σφάλμα: Ο κωδικός πρέπει να έχει τουλάχιστον 8 χαρακτήρες.");
            return false;
        }

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        String specialChars = "#@$!%^&*()-_=+";

        // Έλεγχος κάθε χαρακτήρα
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (specialChars.contains(String.valueOf(c))) hasSpecial = true;
        }

        // Έλεγχος αν πληρούνται όλα τα κριτήρια
        if (!hasUpper) {
            System.out.println("Σφάλμα: Ο κωδικός πρέπει να περιέχει τουλάχιστον ένα κεφαλαίο γράμμα.");
            return false;
        }
        if (!hasLower) {
            System.out.println("Σφάλμα: Ο κωδικός πρέπει να περιέχει τουλάχιστον ένα πεζό γράμμα.");
            return false;
        }
        if (!hasDigit) {
            System.out.println("Σφάλμα: Ο κωδικός πρέπει να περιέχει τουλάχιστον έναν αριθμό.");
            return false;
        }
        if (!hasSpecial) {
            System.out.println("Σφάλμα: Ο κωδικός πρέπει να περιέχει τουλάχιστον έναν ειδικό χαρακτήρα (#@$).");
            return false;
        }

        System.out.println("Ο κωδικός είναι έγκυρος!");
        return true;
    }
}

