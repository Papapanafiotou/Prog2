package mainapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class StateWallet {

    // Αλλαγή στο σωστό όνομα της βάσης δεδομένων του συμφοιτητή σου
    private static final String DATABASE_URL = "jdbc:sqlite:budget.db";

    public static void main(String[] args) {
        StateWallet app = new StateWallet();
        app.run();
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--------------------------------------------------");
            System.out.println("Επιλέξτε μία από τις παρακάτω λειτουργίες");
            System.out.println("1. Εμφάνιση στοιχείων προυπολογισμού");
            System.out.println("2. Αλλαγή στοιχείου προυπολογισμού");
            System.out.println("3. Εμφάνιση αλλαγών");
            System.out.println("4. Έξοδος");
            System.out.print("Επιλογή: ");

            int choice = -1;
            if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                scanner.nextLine(); // καθαρισμός buffer
            } else {
                scanner.nextLine(); // καθαρισμός λάθος εισόδου
            }

            switch (choice) {
                case 1:
                    showBudgetMenu(scanner);
                    break;
                case 2:
                    changeBudget(scanner);
                    break;
                case 3:
                    showChanges();
                    break;
                case 4:
                    System.out.println("Έξοδος από την εφαρμογή.");
                    scanner.close();
                    System.exit(0);
                    return;
                default:
                    System.out.println("Λάθος επιλογή. Παρακαλώ επιλέξτε ξανά.");
            }
        }
    }

    private Connection connect() {
        Connection conn = null;
        try {
            // Φόρτωση driver (προαιρετικό σε νεότερες Java, αλλά καλό για σιγουριά)
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DATABASE_URL);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Σφάλμα σύνδεσης με τη βάση: " + e.getMessage());
        }
        return conn;
    }

    // === Λειτουργία 1: Εμφάνιση ===
    // Επειδή υπάρχουν πολλοί πίνακες, ρωτάμε τον χρήστη ποιον θέλει να δει ή τους δείχνουμε όλους.
    public void showBudgetMenu(Scanner scanner) {
        System.out.println("\nΠοιον πίνακα θέλετε να δείτε;");
        System.out.println("1. Έσοδα (esoda)");
        System.out.println("2. Έξοδα (eksoda)");
        System.out.println("3. Κράτος (kratos)");
        System.out.println("4. Υπουργεία (ypourgeia)");
        System.out.println("5. Αποκεντρωμένες Διοικήσεις (apokentromenes)");
        System.out.println("6. Όλα");
        System.out.print("Επιλογή: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1: printTable("esoda", "code"); break;
            case 2: printTable("eksoda", "code"); break;
            case 3: printTable("kratos", "number"); break;
            case 4: printTable("ypourgeia", "number"); break;
            case 5: printTable("apokentromenes", "number"); break;
            case 6:
                printTable("esoda", "code");
                printTable("eksoda", "code");
                printTable("kratos", "number");
                printTable("ypourgeia", "number");
                printTable("apokentromenes", "number");
                break;
            default: System.out.println("Λάθος επιλογή.");
        }
    }

    // Βοηθητική μέθοδος για εμφάνιση πίνακα
    private void printTable(String tableName, String idColumnName) {
        String sql = "SELECT " + idColumnName + ", name, original_amount, amount FROM " + tableName;

        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Πίνακας: " + tableName.toUpperCase() + " ---");
            System.out.printf("%-5s | %-40s | %-15s | %-15s\n", "ID", "Περιγραφή", "Αρχικό Ποσό", "Τρέχον Ποσό");
            System.out.println("---------------------------------------------------------------------------------");

            while (rs.next()) {
                System.out.printf(
                    "%-5d | %-40s | %12.2f € | %12.2f €\n",
                    rs.getInt(idColumnName),
                    limitString(rs.getString("name"), 40), // Κόβουμε το όνομα αν είναι πολύ μεγάλο
                    rs.getDouble("original_amount"),
                    rs.getDouble("amount")
                );
            }
        } catch (SQLException e) {
            System.out.println("Σφάλμα κατά την εμφάνιση του " + tableName + ": " + e.getMessage());
        }
    }

    // === Λειτουργία 2: Αλλαγή ===
    public void changeBudget(Scanner scanner) {
        System.out.println("\nΣε ποιον πίνακα ανήκει το στοιχείο που θέλετε να αλλάξετε;");
        System.out.println("1. Έσοδα (esoda)");
        System.out.println("2. Έξοδα (eksoda)");
        System.out.println("3. Κράτος (kratos)");
        System.out.println("4. Υπουργεία (ypourgeia)");
        System.out.println("5. Αποκεντρωμένες Διοικήσεις (apokentromenes)");
        System.out.print("Επιλογή: ");

        int tableChoice = scanner.nextInt();
        scanner.nextLine();

        String tableName;
        String idColName;

        // Αντιστοίχιση επιλογής σε όνομα πίνακα και όνομα στήλης ID
        switch (tableChoice) {
            case 1: tableName = "esoda"; idColName = "code"; break;
            case 2: tableName = "eksoda"; idColName = "code"; break;
            case 3: tableName = "kratos"; idColName = "number"; break;
            case 4: tableName = "ypourgeia"; idColName = "number"; break;
            case 5: tableName = "apokentromenes"; idColName = "number"; break;
            default: System.out.println("Άκυρη επιλογή."); return;
        }

        try {
            System.out.print("Δώσε το ID (" + idColName + ") του στοιχείου στο " + tableName + ": ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Δώσε το νέο ποσό: ");
            double newAmount = Double.parseDouble(scanner.nextLine());

            // Έλεγχος περιορισμών (υποθέτω ότι η κλάση Constrains υπάρχει στο package)
            // Αν δεν υπάρχει η κλάση Constrains, σχολίασε τις επόμενες 3 γραμμές
            if (Constrains.negativeAmount(newAmount)) {
                return; 
            }

            String sql = "UPDATE " + tableName + " SET amount = ? WHERE " + idColName + " = ?";

            try (Connection conn = this.connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setDouble(1, newAmount);
                pstmt.setInt(2, id);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("ΕΠΙΤΥΧΙΑ! Το ποσό στον πίνακα " + tableName + " για ID " + id + " ενημερώθηκε.");
                } else {
                    System.out.println("ΑΠΟΤΥΧΙΑ: Δεν βρέθηκε εγγραφή με ID " + id + " στον πίνακα " + tableName);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("ΣΦΑΛΜΑ: Μη έγκυρη είσοδος. Παρακαλώ δώστε μόνο αριθμούς.");
        } catch (SQLException e) {
            System.out.println("Σφάλμα SQL: " + e.getMessage());
        }
    }

    // === Λειτουργία 3: Εμφάνιση Αλλαγών ===
    public void showChanges() {
        System.out.println("\n--- Αλλαγές Προϋπολογισμού (σε όλους τους πίνακες) ---");
        boolean foundAnyChange = false;

        // Πίνακες που έχουν στήλη ID = 'code'
        foundAnyChange |= checkTableForChanges("esoda", "code");
        foundAnyChange |= checkTableForChanges("eksoda", "code");

        // Πίνακες που έχουν στήλη ID = 'number'
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

        try (Connection conn = this.connect();
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

    // Βοηθητική μέθοδος για να μην χαλάει η στοίχιση αν το κείμενο είναι τεράστιο
    private String limitString(String str, int len) {
        if (str == null) return "";
        if (str.length() > len) {
            return str.substring(0, len - 3) + "...";
        }
        return str;
    }
}