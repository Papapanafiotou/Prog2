package mainapp;

import java.sql.*;
import java.util.Scanner;

public class BudgetComparison {

    
    public void startComparison() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n ΕΚΚΙΝΗΣΗ ΛΕΙΤΟΥΡΓΙΑΣ ΣΥΓΚΡΙΣΗΣ");
        System.out.println("----------------------------------");

        System.out.print("-> Δώσε το 1ο έτος (π.χ. 2025): ");
        String year1 = scanner.nextLine();
        
        System.out.print("-> Δώσε το 2ο έτος (π.χ. 2026): ");
        String year2 = scanner.nextLine();

        compareDatabases(year1, year2);
    }

    // Συγκριση των βασεων //
    private void compareDatabases(String year1, String year2) {
       
        String url1 = "jdbc:sqlite:budget_" + year1 + ".db";
        String url2 = "jdbc:sqlite:budget_" + year2 + ".db";
        
        // Αν στη βάση σας ο πίνακας λέγεται "ypourgeia" και οι στήλες "name", "amount"
        // Αν λέγονται αλλιώς, άλλαξέ τα εδώ!
        String sql = "SELECT name, amount FROM ypourgeia"; 

        System.out.println("\n======================================================================");
        System.out.printf("%-30s | %-12s | %-12s | %-10s\n", "ΥΠΟΥΡΓΕΙΟ", year1, year2, "ΔΙΑΦΟΡΑ");
        System.out.println("----------------------------------------------------------------------");

        try (Connection conn1 = DriverManager.getConnection(url1);
             Connection conn2 = DriverManager.getConnection(url2);
             Statement stmt1 = conn1.createStatement();
             Statement stmt2 = conn2.createStatement();
             ResultSet rs1 = stmt1.executeQuery(sql)) {

            boolean foundData = false;

            // Διαβάζουμε γραμμή-γραμμή το 1ο έτος
            while (rs1.next()) {
                foundData = true;
                String name = rs1.getString("name"); 
                double val1 = rs1.getDouble("amount");
                
                // Ψάχνουμε το αντίστοιχο ποσό στο 2ο έτος
                // Χρησιμοποιούμε PreparedStatement για ασφάλεια
                String sql2 = "SELECT amount FROM ypourgeia WHERE name = ?";
                PreparedStatement pstmt2 = conn2.prepareStatement(sql2);
                pstmt2.setString(1, name);
                ResultSet rs2 = pstmt2.executeQuery();
                
                double val2 = 0.0;
                if (rs2.next()) {
                    val2 = rs2.getDouble("amount");
                }
                rs2.close();
                pstmt2.close();

                // Υπολογισμός διαφοράς
                double diff = val2 - val1;
                String sign = (diff > 0) ? "+" : ""; // Αν θετικό, βάλε +

                // Εκτύπωση γραμμής στον πίνακα
                System.out.printf("%-30s | %,12.0f | %,12.0f | %s%,10.0f\n", 
                        name, val1, val2, sign, diff);
            }

            if (!foundData) {
                System.out.println("⚠️  Δεν βρέθηκαν εγγραφές στον πίνακα 'ypourgeia'.");
            }

        } catch (SQLException e) {
            System.out.println("\n❌ ΣΦΑΛΜΑ ΣΥΝΔΕΣΗΣ:");
            System.out.println("   Δεν βρέθηκαν τα αρχεία: budget_" + year1 + ".db ή budget_" + year2 + ".db");
            System.out.println("   ή ο πίνακας/στήλες έχουν άλλο όνομα.");
            System.out.println("   (Τεχνικό μήνυμα: " + e.getMessage() + ")");
        }
        System.out.println("======================================================================\n");
    }

    // --- ΠΡΟΣΩΡΙΝΗ MAIN (ΓΙΑ ΝΑ ΤΟ ΤΡΕΞΕΙΣ ΤΩΡΑ) ---
    public static void main(String[] args) {
        BudgetComparison test = new BudgetComparison();
        test.startComparison();
    }
}