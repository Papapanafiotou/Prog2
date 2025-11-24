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

    // Η διαδρομή για τη βάση δεδομένων
    private static final String DB_URL = "jdbc:sqlite:budget_data.db";

    // Αυτή είναι η μέθοδος που θα καλείς από το άλλο σου πρόγραμμα
    public void importData() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                System.out.println("---------- Έναρξη διαδικασίας εισαγωγής δεδομένων ----------");

                // 1. Δημιουργία πινάκων (αν δεν υπάρχουν)
                createTables(conn);

                // 2. Εισαγωγή δεδομένων
                // Υποθέτουμε ότι τα αρχεία είναι στο root folder του project
                insertIncomeData(conn, "statewallet\\src\\main\\sources\\income.csv");
                insertMinistriesData(conn, "statewallet\\src\\main\\sources\\ministries.csv");
                insertExpensesData(conn, "statewallet\\src\\main\\sources\\expenses.csv");

                System.out.println("-------- Η διαδικασία ολοκληρώθηκε επιτυχώς! --------");
            }
        } catch (SQLException e) {
            System.err.println("Σφάλμα σύνδεσης στη βάση: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Βοηθητικές μέθοδοι (Private) ---

    private void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();

        stmt.execute("CREATE TABLE IF NOT EXISTS income (" +
                     "code INTEGER, type TEXT, amount INTEGER)");

        stmt.execute("CREATE TABLE IF NOT EXISTS ministries (" +
                     "code INTEGER, entity TEXT, regular_budget INTEGER, pde INTEGER, total INTEGER)");

        stmt.execute("CREATE TABLE IF NOT EXISTS expenses (" +
                     "code INTEGER, type TEXT, amount INTEGER)");
    }

    private void insertIncomeData(Connection conn, String filePath) {
        String sql = "INSERT INTO income(code, type, amount) VALUES(?, ?, ?)";
        processFile(conn, filePath, sql, 3);
    }

    private void insertExpensesData(Connection conn, String filePath) {
        String sql = "INSERT INTO expenses(code, type, amount) VALUES(?, ?, ?)";
        processFile(conn, filePath, sql, 3);
    }

    private void insertMinistriesData(Connection conn, String filePath) {
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

        } catch (IOException | SQLException | NumberFormatException e) {
            System.err.println("Σφάλμα στο αρχείο " + filePath + ": " + e.getMessage());
        }
    }

    // Μια γενική μέθοδος για τα απλά αρχεία (Income/Expenses) για να μη γράφουμε διπλό κώδικα
    private void processFile(Connection conn, String filePath, String sql, int params) {
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

        } catch (IOException | SQLException | NumberFormatException e) {
            System.err.println("Σφάλμα στο αρχείο " + filePath + ": " + e.getMessage());
        }
    }
}
