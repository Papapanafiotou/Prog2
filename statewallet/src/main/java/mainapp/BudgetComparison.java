package mainapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BudgetComparison {

    private static final String[] TABLES = {
        "esoda", "eksoda", "kratos", "ypourgeia", "apokentromenes"
    };

    public void startComparison() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== ΛΕΙΤΟΥΡΓΙΑ ΣΥΓΚΡΙΣΗΣ ===");

        System.out.print("-> Έτος 1 (Βάση): ");
        String year1 = scanner.nextLine();
        System.out.print("-> Έτος 2 (Σύγκριση): ");
        String year2 = scanner.nextLine();

        // 1. Επιλογή Κατηγορίας
        System.out.println("\nΔιαθέσιμες κατηγορίες:");
        for (int i = 0; i < TABLES.length; i++) {
            System.out.println((i + 1) + ". " + TABLES[i]);
        }
        System.out.print("Επιλογή κατηγορίας (αριθμό): ");
        int catChoice = scanner.nextInt();
        scanner.nextLine();

        if (catChoice < 1 || catChoice > TABLES.length) {
            System.out.println("❌ Μη έγκυρη επιλογή.");
            return;
        }

        String selectedTable = TABLES[catChoice - 1];

        // 2. Ανάκτηση και Εμφάνιση Λίστας Στοιχείων
        List<String> items = getItemsFromTable(year1, selectedTable);
        
        if (items.isEmpty()) {
            System.out.println("⚠️ Δεν βρέθηκαν δεδομένα στη βάση του " + year1);
            return;
        }

        System.out.println("\nΣτοιχεία στην κατηγορία " + selectedTable.toUpperCase() + ":");
        System.out.println("0. ΟΛΑ ΤΑ ΣΤΟΙΧΕΙΑ");
        for (int i = 0; i < items.size(); i++) {
            System.out.println((i + 1) + ". " + items.get(i));
        }

        System.out.print("Επιλογή στοιχείου (αριθμό): ");
        int itemChoice = scanner.nextInt();
        scanner.nextLine();

        // 3. Εκτέλεση Σύγκρισης
        if (itemChoice == 0) {
            compareDatabases(year1, year2, selectedTable, null);
        } else if (itemChoice > 0 && itemChoice <= items.size()) {
            compareDatabases(year1, year2, selectedTable, items.get(itemChoice - 1));
        } else {
            System.out.println("❌ Μη έγκυρη επιλογή στοιχείου.");
        }
    }

    // Βοηθητική μέθοδος για να φέρουμε τα ονόματα από τη βάση
    private List<String> getItemsFromTable(String year, String tableName) {
        List<String> names = new ArrayList<>();
        String url = "jdbc:sqlite:budget_" + year + ".db";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM " + tableName + " ORDER BY name ASC")) {
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Σφάλμα κατά την ανάγνωση ονομάτων: " + e.getMessage());
        }
        return names;
    }

    private void compareDatabases(String year1, String year2, String tableName, String specificName) {
        String url1 = "jdbc:sqlite:budget_" + year1 + ".db";
        String url2 = "jdbc:sqlite:budget_" + year2 + ".db";

        Search searchYear2 = new Search(url2);

        System.out.println("\n>>> ΣΥΓΚΡΙΣΗ: " + tableName.toUpperCase());
        System.out.printf("%-45s | %-12s | %-12s | %-10s\n", "ΟΝΟΜΑΣΙΑ", year1, year2, "ΔΙΑΦΟΡΑ");
        System.out.println("-----------------------------------------------------------------------------------------");

        String sql = "SELECT name, amount FROM " + tableName;
        if (specificName != null) {
            sql += " WHERE name = ?";
        }

        try (Connection conn1 = DriverManager.getConnection(url1);
             PreparedStatement pstmt1 = conn1.prepareStatement(sql)) {

            if (specificName != null) {
                pstmt1.setString(1, specificName);
            }

            try (ResultSet rs1 = pstmt1.executeQuery()) {
                boolean found = false;
                double totalYear1 = 0;
                double totalYear2 = 0;

                while (rs1.next()) {
                    found = true;
                    String name = rs1.getString("name");
                    double val1 = rs1.getDouble("amount");
                    
                    // Εδώ καλούμε τη "σιωπηλή" searchAmount(name, true) που φτιάξαμε
                    double val2 = searchYear2.searchAmount(name, true);

                    totalYear1 += val1;
                    totalYear2 += val2;
                    double diff = val2 - val1;
                    String sign = (diff > 0) ? "+" : "";

                    System.out.printf("%-45s | %,12.0f | %,12.0f | %s%,10.0f\n", 
                                      name, val1, val2, sign, diff);
                }

                if (found) {
                    double totalDiff = totalYear2 - totalYear1;
                    String tSign = (totalDiff > 0) ? "+" : "";
                    System.out.println("-----------------------------------------------------------------------------------------");
                    System.out.printf("%-45s | %,12.0f | %,12.0f | %s%,10.0f\n", 
                                      "ΣΥΝΟΛΑ", totalYear1, totalYear2, tSign, totalDiff);
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Σφάλμα: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new BudgetComparison().startComparison();
    }
}