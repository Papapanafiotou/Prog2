package mainapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MockDataBase {

    // Εδώ θα δημιουργηθεί η βάση (στο φάκελο του project)
    private static final String DATABASE_URL = "jdbc:sqlite:test_budget.db";

    public static void main(String[] args) {
        // Αυτό θα δημιουργήσει το αρχείο test_budget.db αν δεν υπάρχει
        try (Connection conn = DriverManager.getConnection(DATABASE_URL);
             Statement stmt = conn.createStatement()) {

            System.out.println("Η σύνδεση με τη SQLite πέτυχε.");

            // 1. Σβήνουμε τον πίνακα αν υπάρχει ήδη, για να είναι καθαρό
            stmt.executeUpdate("DROP TABLE IF EXISTS budget;");
            System.out.println("Ο παλιός πίνακας 'budget' διαγράφηκε (αν υπήρχε).");

            // 2. Δημιουργούμε τον πίνακα (ΑΥΤΟ ΕΙΝΑΙ ΤΟ ΣΥΜΒΟΛΑΙΟ ΣΑΣ)
            String createTableSql = "CREATE TABLE budget (" +
                                    " id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                    " ministry TEXT NOT NULL," +
                                    " category TEXT NOT NULL," +
                                    " amount REAL NOT NULL" +
                                    ");";
            stmt.executeUpdate(createTableSql);
            System.out.println("Ο νέος πίνακας 'budget' δημιουργήθηκε.");

            // 3. Βάζουμε τα ψεύτικα (mock) δεδομένα
            System.out.println("Εισαγωγή ψεύτικων δεδομένων...");
            stmt.addBatch("INSERT INTO budget (ministry, category, amount) VALUES ('Υπουργείο Παιδείας', 'Μισθοί', 1200000.50);");
            stmt.addBatch("INSERT INTO budget (ministry, category, amount) VALUES ('Υπουργείο Υγείας', 'Εξοπλισμός', 750000.00);");
            stmt.addBatch("INSERT INTO budget (ministry, category, amount) VALUES ('Υπουργείο Άμυνας', 'Συντήρηση', 500000.25);");
            stmt.addBatch("INSERT INTO budget (ministry, category, amount) VALUES ('Υπουργείο Πολιτισμού', 'Εκδηλώσεις', 150000.00);");
            
            stmt.executeBatch(); // Εκτέλεση όλων των εντολών μαζί

            System.out.println("-------------------------------------------------");
            System.out.println("Η βάση 'test_budget.db' είναι ΕΤΟΙΜΗ για χρήση!");
            System.out.println("-------------------------------------------------");

        } catch (SQLException e) {
            System.out.println("ΣΦΑΛΜΑ: " + e.getMessage());
        }
    }
}
