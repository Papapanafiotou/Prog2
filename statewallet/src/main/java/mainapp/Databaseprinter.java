package mainapp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Locale;

public class Databaseprinter {

    private static final String DB_URL = "jdbc:sqlite:budget_data.db";

    // Αυτή είναι η μέθοδος που θα καλείς από την main σου
    public void printAllData() {
        // Ρύθμιση για να φαίνονται τα νούμερα με τελείες (π.χ. 1.000.000)
        NumberFormat nf = NumberFormat.getInstance(new Locale("el", "GR"));

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                System.out.println("\n=== ΕΚΤΥΠΩΣΗ ΠΕΡΙΕΧΟΜΕΝΩΝ ΒΑΣΗΣ ΔΕΔΟΜΕΝΩΝ ===");
                
                printIncome(conn, nf);
                System.out.println("\n--------------------------------------------------\n");
                
                printMinistries(conn, nf);
                System.out.println("\n--------------------------------------------------\n");
                
                printExpenses(conn, nf);
                
                System.out.println("\n=== ΤΕΛΟΣ ΕΚΤΥΠΩΣΗΣ ===\n");
            }
        } catch (SQLException e) {
            System.err.println("Σφάλμα κατά την ανάγνωση της βάσης: " + e.getMessage());
        }
    }

    // --- Βοηθητικές μέθοδοι (Private) ---

    private void printIncome(Connection conn, NumberFormat nf) throws SQLException {
        System.out.println(">>> ΠΙΝΑΚΑΣ: ΕΣΟΔΑ (INCOME)");
        System.out.printf("%-10s %-50s %15s%n", "ΚΩΔΙΚΟΣ", "ΤΥΠΟΣ", "ΠΟΣΟ");
        System.out.println(".......... .................................................. ...............");

        String sql = "SELECT * FROM income";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int code = rs.getInt("code");
                String type = rs.getString("type");
                long amount = rs.getLong("amount");
                System.out.printf("%-10d %-50s %15s%n", code, truncate(type, 50), nf.format(amount));
            }
        }
    }

    private void printMinistries(Connection conn, NumberFormat nf) throws SQLException {
        System.out.println(">>> ΠΙΝΑΚΑΣ: ΥΠΟΥΡΓΕΙΑ (MINISTRIES)");
        System.out.printf("%-8s %-45s %15s %15s %15s%n", "ΚΩΔΙΚΟΣ", "ΦΟΡΕΑΣ", "ΤΑΚΤΙΚΟΣ", "ΠΔΕ", "ΣΥΝΟΛΟ");
        System.out.println("........ ............................................. ............... ............... ...............");

        String sql = "SELECT * FROM ministries";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int code = rs.getInt("code");
                String entity = rs.getString("entity");
                long reg = rs.getLong("regular_budget");
                long pde = rs.getLong("pde");
                long total = rs.getLong("total");
                System.out.printf("%-8d %-45s %15s %15s %15s%n", 
                        code, truncate(entity, 45), nf.format(reg), nf.format(pde), nf.format(total));
            }
        }
    }

    private void printExpenses(Connection conn, NumberFormat nf) throws SQLException {
        System.out.println(">>> ΠΙΝΑΚΑΣ: ΕΞΟΔΑ (EXPENSES)");
        System.out.printf("%-10s %-50s %15s%n", "ΚΩΔΙΚΟΣ", "ΤΥΠΟΣ", "ΠΟΣΟ");
        System.out.println(".......... .................................................. ...............");

        String sql = "SELECT * FROM expenses";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int code = rs.getInt("code");
                String type = rs.getString("type");
                long amount = rs.getLong("amount");
                System.out.printf("%-10d %-50s %15s%n", code, truncate(type, 50), nf.format(amount));
            }
        }
    }

    private String truncate(String str, int width) {
        if (str.length() > width) {
            return str.substring(0, width - 3) + "...";
        }
        return str;
    }
}
