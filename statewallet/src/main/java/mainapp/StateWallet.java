package mainapp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;



public class StateWallet {

    private static final String DATABASE_URL = "jdbc:sqlite:test_budget.db";

    public static void main(String[] args) {

        StateWallet app = new StateWallet();
        
        app.run();
    }    
        
    public void run(){   
        Scanner scanner = new Scanner(System.in);

        while(true){
            // main menu //
            System.out.println("Επιλέξτε μία από τις παρακάτω λειτουργίες");
            System.out.println("1. Εμφάνιση στοιχείων προυπολογισμού / 2. Αλλαγή στοιχείου προυπολογισμού / 3. Εμφάνιση αλλαγών / 4. Έξοδος");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
            case 1:
                showBudget();
                break;
            case 2:
                changeBudget(scanner);
                break;
            case 3:
                showChanges();
                break;
            case 4:
                System.exit(0);
                scanner.close();
                return;
            default:
                System.out.println("Λάθος επιλογή. Παρακαλώ επιλέξτε ξανά");
                break;
        }
        
        }
        
    }

    private Connection connect() {
        // method for connection with db //
        
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            System.out.println("Σφάλμα σύνδεσης με τη βάση: " + e.getMessage());
        }
        return conn;
    }

    public void showBudget() {
        
        String sql = "SELECT id, ministry, category, amount FROM budget";

        try (Connection conn = this.connect(); // Παίρνουμε τη σύνδεση //
             Statement stmt = conn.createStatement(); // Φτιάχνουμε το κανάλι εντολής //
             ResultSet rs = stmt.executeQuery(sql)) { // Εκτελούμε και παίρνουμε τα data //

            System.out.println("\n--- Τρέχων Προϋπολογισμός ---");
            // Loop σε όσα αποτελέσματα βρήκαμε //
            while (rs.next()) {
                System.out.printf(
                    "ID: %-3d | Υπουργείο: %-22s | Κατηγορία: %-15s | Ποσό: %12.2f $\n",
                    rs.getInt("id"),
                    rs.getString("ministry"),
                    rs.getString("category"),
                    rs.getDouble("amount")
                );
            }
        } catch (SQLException e) {
            System.out.println("!! Σφάλμα κατά την εμφάνιση του προϋπολογισμού: " + e.getMessage());
        }
    }
    

    
        
    
    // Αλλαγή ποσού, και ενημέρωση της βάσης δεδομένων //
    
    public void changeBudget(Scanner scanner) {
        
        try {
            // --- 1. ΣΥΛΛΟΓΗ ΔΕΔΟΜΕΝΩΝ ---
            // Ρωτάμε για το ID
            System.out.print("Δώσε το ID του στοιχείου που θες να αλλάξεις: ");
            String idString = scanner.nextLine();
            int id = Integer.parseInt(idString); // Μετατροπή κειμένου σε αριθμό

            // Ρωτάμε για το Ποσό
            System.out.print("Δώσε το νέο ποσό: ");
            String amountString = scanner.nextLine();
            double newAmount = Double.parseDouble(amountString); // Μετατροπή σε double

            // --- 2. ΕΛΕΓΧΟΣ ΠΕΡΙΟΡΙΣΜΩΝ ---
            // Αυτή είναι η απαίτηση #4 της εργασίας
            
            boolean answer =Constrains.negativeAmount(newAmount);
            
            if (answer == true){
                return;
            }
            // --- 3. ΕΚΤΕΛΕΣΗ (DATABASE UPDATE) ---
            // Η SQL εντολή με "placeholders" (τα '?')
            String sql = "UPDATE budget SET amount = ? WHERE id = ?";

            // Χρησιμοποιούμε try-with-resources ξανά, αυτή τη φορά με PreparedStatement
            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                // "Γεμίζουμε" τα ερωτηματικά με ασφάλεια
                pstmt.setDouble(1, newAmount); // 1ο ερωτηματικό (?) είναι το ποσό
                pstmt.setInt(2, id);         // 2ο ερωτηματικό (?) είναι το id

                // Εκτελούμε το update. 
                // Αυτό επιστρέφει το πόσες γραμμές άλλαξαν.
                int rowsAffected = pstmt.executeUpdate();

                // 4. ΕΛΕΓΧΟΣ ΑΠΟΤΕΛΕΣΜΑΤΟΣ
                if (rowsAffected > 0) {
                    // Αν άλλαξε 1 (ή παραπάνω) γραμμές, πέτυχε
                    System.out.println("ΕΠΙΤΥΧΙΑ! Το ποσό για το ID " + id + " ενημερώθηκε.");
                } else {
                    // Αν άλλαξε 0 γραμμές, σημαίνει ότι δεν βρήκε το ID
                    System.out.println("!! ΑΠΟΤΥΧΙΑ: Δεν βρέθηκε εγγραφή με ID " + id);
                }
            }

        } catch (NumberFormatException e) {
            // Αυτό θα τρέξει αν ο χρήστης γράψει "abc" αντί για αριθμό
            System.out.println("!! ΣΦΑΛΜΑ: Μη έγκυρη είσοδος. Παρακαλώ δώστε μόνο αριθμούς.");
        } catch (SQLException e) {
            System.out.println("!! Σφάλμα κατά την αλλαγή του προϋπολογισμού: " + e.getMessage());
        }

    }

    /**
     * Εμφανίζει ΜΟΝΟ τις εγγραφές που έχουν αλλάξει.
     */
    public void showChanges() {
        // Η SQL που συγκρίνει τις δύο στήλες
        String sql = "SELECT id, ministry, category, amount, original_amount FROM budget WHERE amount != original_amount";

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Αλλαγές Προϋπολογισμού ---");
            boolean foundChanges = false;
            while (rs.next()) {
                foundChanges = true;
                System.out.printf(
                    "ID: %-3d | Υπουργείο: %-22s | Κατηγορία: %-15s | Αρχικό Ποσό: %12.2f $ | Νέο Ποσό: %12.2f $\n",
                    rs.getInt("id"),
                    rs.getString("ministry"),
                    rs.getString("category"),
                    rs.getDouble("original_amount"),
                    rs.getDouble("amount")
                );
            }
            if (!foundChanges) {
                System.out.println("Δεν έχουν γίνει αλλαγές.");
            }
        } catch (SQLException e) {
            System.out.println("!! Σφάλμα κατά την εμφάνιση αλλαγών: " + e.getMessage());
        }
    }
    
}


