package mainapp;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class BudgetImporter {

    private static final String DB_URL = "jdbc:sqlite:budget_data.db";

    public void importData() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            if (conn != null) {
                // Απενεργοποίηση του auto-commit για να διαχειριστούμε εμείς το Transaction
                conn.setAutoCommit(false);

                System.out.println("--> Έναρξη διαδικασίας ενημέρωσης δεδομένων...");

                // 1. Δημιουργία πινάκων (αν δεν υπάρχουν)
                createTables(conn);

                // 2. Καθαρισμός παλιών δεδομένων
                clearOldData(conn);

                // 3. Εισαγωγή νέων δεδομένων
                insertIncomeData(conn, "statewallet\\src\\main\\sources\\income.csv");
                insertMinistriesData(conn, "statewallet\\src\\main\\sources\\ministries.csv");
                insertExpensesData(conn, "statewallet\\src\\main\\sources\\expenses.csv");

                // Αν όλα πήγαν καλά, αποθηκεύουμε τις αλλαγές
                conn.commit();
                System.out.println("--> Η διαδικασία ολοκληρώθηκε επιτυχώς!");
            }
        } catch (SQLException e) {
            System.err.println("Σφάλμα βάσης δεδομένων: " + e.getMessage());
            // Αν συμβεί σφάλμα, κάνουμε rollback για να μην αλλοιωθεί η βάση
            if (conn != null) {
                try {
                    System.err.println("Γίνεται ακύρωση αλλαγών (Rollback)...");
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    public void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS income (code INTEGER, type TEXT, amount INTEGER)");
        stmt.execute("CREATE TABLE IF NOT EXISTS ministries (code INTEGER, entity TEXT, regular_budget INTEGER, pde INTEGER, total INTEGER)");
        stmt.execute("CREATE TABLE IF NOT EXISTS expenses (code INTEGER, type TEXT, amount INTEGER)");
        stmt.close();
    }

    // Διαγράφει τα περιεχόμενα των πινάκων
    public void clearOldData(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        
        int rowsIncome = stmt.executeUpdate("DELETE FROM income");
        int rowsMinistries = stmt.executeUpdate("DELETE FROM ministries");
        int rowsExpenses = stmt.executeUpdate("DELETE FROM expenses");
        
        System.out.println("\tΔιαγράφηκαν παλιά δεδομένα: " + 
                (rowsIncome + rowsMinistries + rowsExpenses) + " εγγραφές.");
        
        stmt.close();
    }

    public void insertIncomeData(Connection conn, String filePath) throws SQLException {
        String sql = "INSERT INTO income(code, type, amount) VALUES(?, ?, ?)";
        processFile(conn, filePath, sql, 3);
    }

    private void insertExpensesData(Connection conn, String filePath) throws SQLException {
        String sql = "INSERT INTO expenses(code, type, amount) VALUES(?, ?, ?)";
        processFile(conn, filePath, sql, 3);
    }

    private void insertMinistriesData(Connection conn, String filePath) throws SQLException {
        String sql = "INSERT INTO ministries(code, entity, regular_budget, pde, total) VALUES(?, ?, ?, ?, ?)";
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); 
                if (data.length >= 5) {
                    pstmt.setInt(1, Integer.parseInt(data[0].trim()));
                    pstmt.setString(2, data[1].trim().replace("\"", ""));
                    pstmt.setLong(3, Long.parseLong(data[2].trim()));
                    pstmt.setLong(4, Long.parseLong(data[3].trim()));
                    pstmt.setLong(5, Long.parseLong(data[4].trim()));
                    pstmt.executeUpdate();
                }
            }
            System.out.println("\tΕισήχθησαν δεδομένα από: " + filePath);

        } catch (IOException | NumberFormatException e) {
            System.err.println("Σφάλμα στο αρχείο " + filePath + ": " + e.getMessage());
            throw new SQLException("Αποτυχία ανάγνωσης αρχείου " + filePath); // Ρίχνουμε το σφάλμα για να γίνει Rollback
        }
    }

    private void processFile(Connection conn, String filePath, String sql, int params) throws SQLException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String line;
            br.readLine(); // Skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= params) {
                    pstmt.setInt(1, Integer.parseInt(data[0].trim()));
                    pstmt.setString(2, data[1].trim());
                    pstmt.setLong(3, Long.parseLong(data[2].trim()));
                    pstmt.executeUpdate();
                }
            }
            System.out.println("\tΕισήχθησαν δεδομένα από: " + filePath);

        } catch (IOException | NumberFormatException e) {
            System.err.println("Σφάλμα στο αρχείο " + filePath + ": " + e.getMessage());
            throw new SQLException("Αποτυχία ανάγνωσης αρχείου " + filePath);
        }
    }
}
