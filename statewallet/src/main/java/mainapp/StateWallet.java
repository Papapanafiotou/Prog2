package mainapp;
import java.sql.Connection;
import java.sql.DriverManager;
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
            System.out.println("1. Εμφάνιση στοιχείων προυπολογισμού / 2. Αλλαγή στοιχείου προυπολογισμού / 3. Έξοδος");
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
                System.out.println(
                        "ID: " + rs.getInt("id") + "\t" + // \t βάζει ένα TAB για στοίχιση
                        "Υπουργείο: " + rs.getString("ministry") + "\t" +
                        "Κατηγορία: " + rs.getString("category") + "\t" +
                        "Ποσό: " + rs.getDouble("amount") + " $");
            }
        } catch (SQLException e) {
            System.out.println("!! Σφάλμα κατά την εμφάνιση του προϋπολογισμού: " + e.getMessage());
        }
    }
    

    public static void changeBudget(Scanner scanner) {
        
        System.out.println("(makeChange) Εδώ θα γίνει η αλλαγή...");
    }
}


