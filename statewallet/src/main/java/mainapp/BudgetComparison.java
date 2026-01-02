package mainapp;

import java.sql.*;
import java.util.Scanner;

public class BudgetComparison {

    // Ορίζουμε τους πίνακες που θέλουμε να ελέγξουμε (όπως στην κλάση Search)
    private static final String[] TABLES = {
        "esoda", "eksoda", "kratos", "ypourgeia", "apokentromenes"
    };

    public void startComparison() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n--- ΕΚΚΙΝΗΣΗ ΛΕΙΤΟΥΡΓΙΑΣ ΣΥΓΚΡΙΣΗΣ ΠΡΟΫΠΟΛΟΓΙΣΜΩΝ ---");

        System.out.print("-> Δώσε το 1ο έτος (π.χ. 2025): ");
        String year1 = scanner.nextLine();
        
        System.out.print("-> Δώσε το 2ο έτος (π.χ. 2026): ");
        String year2 = scanner.nextLine();

        compareDatabases(year1, year2);
    }

    private void compareDatabases(String year1, String year2) {
        String url1 = "jdbc:sqlite:budget_" + year1 + ".db";
        String url2 = "jdbc:sqlite:budget_" + year2 + ".db";

        try (Connection conn1 = DriverManager.getConnection(url1);
             Connection conn2 = DriverManager.getConnection(url2)) {

            // Διατρέχουμε κάθε πίνακα της λίστας TABLES
            for (String table : TABLES) {
                System.out.println("\n======================================================================");
                System.out.println(" ΚΑΤΗΓΟΡΙΑ: " + table.toUpperCase());
                System.out.printf("%-35s | %-12s | %-12s | %-10s\n", "ΟΝΟΜΑΣΙΑ", year1, year2, "ΔΙΑΦΟΡΑ");
                System.out.println("----------------------------------------------------------------------");

                String sqlSelect = "SELECT name, amount FROM " + table;
                
                try (Statement stmt1 = conn1.createStatement();
                     ResultSet rs1 = stmt1.executeQuery(sqlSelect)) {

                    boolean foundData = false;

                    while (rs1.next()) {
                        foundData = true;
                        String name = rs1.getString("name");
                        double val1 = rs1.getDouble("amount");

                        // Αναζήτηση στον ίδιο πίνακα στη βάση του 2ου έτους
                        double val2 = getAmountFromTable(conn2, table, name);

                        double diff = val2 - val1;
                        String sign = (diff > 0) ? "+" : "";

                        System.out.printf("%-35s | %,12.0f | %,12.0f | %s%,10.0f\n", 
                                          name, val1, val2, sign, diff);
                    }

                    if (!foundData) {
                        System.out.println(" (Δεν βρέθηκαν δεδομένα στον πίνακα " + table + ")");
                    }
                } catch (SQLException e) {
                    System.out.println("⚠️  Πρόβλημα κατά την ανάγνωση του πίνακα: " + table);
                }
            }

        } catch (SQLException e) {
            System.out.println("\n❌ ΣΦΑΛΜΑ ΣΥΝΔΕΣΗΣ:");
            System.out.println("   Βεβαιωθείτε ότι υπάρχουν τα αρχεία budget_" + year1 + ".db και budget_" + year2 + ".db");
            System.out.println("   Τεχνικό σφάλμα: " + e.getMessage());
        }
        System.out.println("\n======================================================================\n");
    }

    /**
     * Βοηθητική μέθοδος που ψάχνει ένα ποσό σε συγκεκριμένο πίνακα βάσει ονόματος.
     */
    private double getAmountFromTable(Connection conn, String table, String name) {
        String sql = "SELECT amount FROM " + table + " WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("amount");
                }
            }
        } catch (SQLException e) {
            // Αν δεν βρει τον πίνακα ή το όνομα, επιστρέφει 0
        }
        return 0.0;
    }

    public static void main(String[] args) {
        BudgetComparison comparison = new BudgetComparison();
        comparison.startComparison();
    }
}