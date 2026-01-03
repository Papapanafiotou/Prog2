package mainapp;

import java.sql.*;
import java.util.Scanner;

public class BudgetComparison {

    private static final String[] TABLES = {
        "esoda", "eksoda", "kratos", "ypourgeia", "apokentromenes"
    };

    public void startComparison() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n=== ΛΕΙΤΟΥΡΓΙΑ ΣΥΓΚΡΙΣΗΣ ===");

        // 1. Επιλογή Ετών
        System.out.print("-> Έτος 1 (Βάση): ");
        String year1 = scanner.nextLine();
        System.out.print("-> Έτος 2 (Σύγκριση): ");
        String year2 = scanner.nextLine();

        // 2. Μενού Επιλογής Κατηγορίας
        System.out.println("\nΔιαθέσιμες κατηγορίες:");
        for (int i = 0; i < TABLES.length; i++) {
            System.out.println((i + 1) + ". " + TABLES[i]);
        }
        System.out.println((TABLES.length + 1) + ". ΟΛΑ ΤΑ ΠΑΡΑΠΑΝΩ");
        
        System.out.print("Επιλογή (αριθμό): ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (choice >= 1 && choice <= TABLES.length) {
            // Σύγκριση συγκεκριμένου πίνακα
            compareDatabases(year1, year2, TABLES[choice - 1]);
        } else if (choice == TABLES.length + 1) {
            // Σύγκριση όλων
            for (String table : TABLES) {
                compareDatabases(year1, year2, table);
            }
        } else {
            System.out.println("❌ Μη έγκυρη επιλογή.");
        }
    }

    private void compareDatabases(String year1, String year2, String tableName) {
        String url1 = "jdbc:sqlite:budget_" + year1 + ".db";
        String url2 = "jdbc:sqlite:budget_" + year2 + ".db";

        // Εδώ δημιουργούμε ένα αντικείμενο Search για τη βάση του 2ου έτους
        Search searchYear2 = new Search(url2);

        System.out.println("\n>>> ΣΥΓΚΡΙΣΗ ΠΙΝΑΚΑ: " + tableName.toUpperCase());
        System.out.printf("%-35s | %-12s | %-12s | %-10s\n", "ΟΝΟΜΑΣΙΑ", year1, year2, "ΔΙΑΦΟΡΑ");
        System.out.println("----------------------------------------------------------------------");

        try (Connection conn1 = DriverManager.getConnection(url1);
             Statement stmt1 = conn1.createStatement();
             ResultSet rs1 = stmt1.executeQuery("SELECT name, amount FROM " + tableName)) {

            boolean found = false;
            while (rs1.next()) {
                found = true;
                String name = rs1.getString("name");
                double val1 = rs1.getDouble("amount");

                // Χρήση της έτοιμης Search για το 2ο έτος
                // Σημείωση: Η searchAmount της Search επιστρέφει το ποσό αν το βρει σε ΟΠΟΙΟΝΔΗΠΟΤΕ πίνακα
                double val2 = searchYear2.searchAmount(name);

                double diff = val2 - val1;
                String sign = (diff > 0) ? "+" : "";

                System.out.printf("%-35s | %,12.0f | %,12.0f | %s%,10.0f\n", 
                                  name, val1, val2, sign, diff);
            }
            if (!found) System.out.println("Δεν βρέθηκαν δεδομένα.");

        } catch (SQLException e) {
            System.out.println("❌ Σφάλμα στον πίνακα " + tableName + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new BudgetComparison().startComparison();
    }
}