package mainapp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BudgetManager {

    private DatabaseHandler dbHandler;
    String URL;
    public BudgetManager(String url) {
        this.dbHandler = new DatabaseHandler();
        this.URL = url;
    }

    // --- Λογική για εμφάνιση (από το παλιό Printtable & ShowBudget) ---
    public void printTable(String tableName, String idColumnName) {
        String sql = "SELECT " + idColumnName + ", name, original_amount, amount FROM " + tableName;

        try (Connection conn = dbHandler.connect(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Πίνακας: " + tableName.toUpperCase() + " ---");
            System.out.printf("%-5s | %-40s | %-15s | %-15s\n", "ID", "Περιγραφή", "Αρχικό Ποσό", "Τρέχον Ποσό");
            System.out.println("---------------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf(
                    "%-5d | %-40s | %12.2f € | %12.2f "+"€\n",
                    rs.getInt(idColumnName),
                    limitString(rs.getString("name"), 40),
                    rs.getDouble("original_amount"),
                    rs.getDouble("amount")
                );
            }
        } catch (SQLException e) {
            System.out.println("Σφάλμα κατά την εμφάνιση του " + tableName + ": " + e.getMessage());
        }
    }

    // --- Λογική για αλλαγή (από το παλιό ChangeBudget) ---
    public boolean updateAmount(String tableName, String idColName, int id, double newAmount) {
        String sql = "UPDATE " + tableName + " SET amount = ? WHERE " + idColName + " = ?";

        try (Connection conn = dbHandler.connect(URL);
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, newAmount);
            pstmt.setInt(2, id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Σφάλμα SQL: " + e.getMessage());
            return false;
        }
    }

    // --- Λογική για εμφάνιση αλλαγών (από το παλιό ShowChanges) ---
    public void showChanges() {
        System.out.println("\n--- Αλλαγές Προϋπολογισμού (σε όλους τους πίνακες) ---");
        boolean foundAnyChange = false;

        foundAnyChange |= checkTableForChanges("esoda", "code");
        foundAnyChange |= checkTableForChanges("eksoda", "code");
        foundAnyChange |= checkTableForChanges("kratos", "number");
        foundAnyChange |= checkTableForChanges("ypourgeia", "number");
        foundAnyChange |= checkTableForChanges("apokentromenes", "number");

        if (!foundAnyChange) {
            System.out.println("Δεν βρέθηκαν αλλαγές σε κανέναν πίνακα.");
        }
    }

    private boolean checkTableForChanges(String tableName, String idColName) {
        String sql = "SELECT " + idColName + ", name, amount, original_amount FROM " + tableName + " WHERE amount != original_amount";
        boolean found = false;

        try (Connection conn = dbHandler.connect(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                if (!found) {
                    System.out.println("\nΑλλαγές στον πίνακα: " + tableName.toUpperCase());
                    found = true;
                }
                System.out.printf(
                    "ID: %-3d | %-30s | Αρχικό: %10.2f | Νέο: %10.2f\n",
                    rs.getInt(idColName),
                    limitString(rs.getString("name"), 30),
                    rs.getDouble("original_amount"),
                    rs.getDouble("amount")
                );
            }
        } catch (SQLException e) {
            System.out.println("Σφάλμα ελέγχου αλλαγών στο " + tableName + ": " + e.getMessage());
        }
        return found;
    }

    // Βοηθητική μέθοδος
    private String limitString(String str, int len) {
        if (str == null) return "";
        if (str.length() > len) {
            return str.substring(0, len - 3) + "...";
        }
        return str;
    }
}