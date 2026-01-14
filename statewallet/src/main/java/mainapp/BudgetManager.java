package mainapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Διαχειρίζεται τις λειτουργίες του προϋπολογισμού στη βάση δεδομένων.
 */
public final class BudgetManager {

    /** Μέγιστο μήκος ονόματος για εμφάνιση. */
    private static final int NAME_LIMIT = 40;
    /** Μέγιστο μήκος ονόματος για εμφάνιση αλλαγών. */
    private static final int CHANGE_NAME_LIMIT = 30;
    /** Μήκος κατάληξης "..." (3 χαρακτήρες). */
    private static final int DOTS_LEN = 3;
    /** Δείκτης SQL 1. */
    private static final int IDX_1 = 1;
    /** Δείκτης SQL 2. */
    private static final int IDX_2 = 2;
    /** Τιμή επιστροφής όταν δεν βρεθεί ποσό. */
    private static final double AMOUNT_NOT_FOUND = -1.0;

    // Σταθερές Format
    /** Format string για εκτύπωση γραμμής πίνακα. */
    private static final String FMT_TABLE_ROW
            = "%-5d | %-40s | %12.2f EUR | %12.2f EUR\n";
    /** Format string για εκτύπωση αλλαγών. */
    private static final String FMT_CHANGE_ROW
            = "ID: %-3d | %-30s | Αρχικό: %10.2f | Νέο: %10.2f\n";

    /** Το URL σύνδεσης στη βάση. */
    private String url;

    /**
     * Κατασκευαστής.
     *
     * @param dbUrl Το URL της βάσης δεδομένων.
     */
    public BudgetManager(final String dbUrl) {
        this.url = dbUrl;
    }

    /**
     * Ορίζει νέο URL βάσης δεδομένων.
     *
     * @param dbUrl Το νέο URL.
     */
    public void setUrl(final String dbUrl) {
        this.url = dbUrl;
    }

    /**
     * Εκτυπώνει τα περιεχόμενα ενός πίνακα στην κονσόλα.
     *
     * @param tableName    Το όνομα του πίνακα.
     * @param idColumnName Το όνομα της στήλης ID.
     */
    public void printTable(final String tableName, final String idColumnName) {
        String sql = "SELECT " + idColumnName
                + ", name, original_amount, amount FROM " + tableName;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Πίνακας: " + tableName.toUpperCase()
                    + " ---");
            System.out.printf("%-5s | %-40s | %-15s | %-15s\n",
                    "ID", "Περιγραφή", "Αρχικό Ποσό", "Τρέχον Ποσό");
            System.out.println(
                    "-------------------------------------------------------");

            while (rs.next()) {
                System.out.printf(FMT_TABLE_ROW,
                        rs.getInt(idColumnName),
                        limitString(rs.getString("name"), NAME_LIMIT),
                        rs.getDouble("original_amount"),
                        rs.getDouble("amount")
                );
            }
        } catch (SQLException e) {
            System.out.println("Σφάλμα: " + e.getMessage());
        }
    }

    /**
     * Ενημερώνει το ποσό μιας εγγραφής.
     *
     * @param tableName Το όνομα του πίνακα.
     * @param idColName Το όνομα στήλης ID.
     * @param id        Το ID της εγγραφής.
     * @param newAmount Το νέο ποσό.
     * @return true αν έγινε η ενημέρωση, false διαφορετικά.
     */
    public boolean updateAmount(final String tableName, final String idColName,
                                final int id, final double newAmount) {
        String sql = "UPDATE " + tableName + " SET amount = ? WHERE "
                + idColName + " = ?";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(IDX_1, newAmount);
            pstmt.setInt(IDX_2, id);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.out.println("Σφάλμα SQL: " + e.getMessage());
            return false;
        }
    }

    /**
     * Εμφανίζει τις αλλαγές που έχουν γίνει σε όλους τους πίνακες.
     */
    public void showChanges() {
        System.out.println("\n--- Αλλαγές Προϋπολογισμού ---");
        boolean c1 = checkTableForChanges("esoda", "code");
        boolean c2 = checkTableForChanges("eksoda", "code");
        boolean c3 = checkTableForChanges("kratos", "number");
        boolean c4 = checkTableForChanges("ypourgeia", "number");
        boolean c5 = checkTableForChanges("apokentromenes", "number");

        if (!(c1 || c2 || c3 || c4 || c5)) {
            System.out.println("Δεν βρέθηκαν αλλαγές σε κανέναν πίνακα.");
        }
    }

    private boolean checkTableForChanges(final String tableName,
                                         final String idColName) {
        String sql = "SELECT " + idColName
                + ", name, amount, original_amount FROM "
                + tableName + " WHERE amount != original_amount";
        boolean found = false;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                if (!found) {
                    System.out.println("\nΑλλαγές στον πίνακα: "
                            + tableName.toUpperCase());
                    found = true;
                }
                System.out.printf(FMT_CHANGE_ROW,
                        rs.getInt(idColName),
                        limitString(rs.getString("name"), CHANGE_NAME_LIMIT),
                        rs.getDouble("original_amount"),
                        rs.getDouble("amount")
                );
            }
        } catch (SQLException e) {
            System.out.println("Σφάλμα: " + e.getMessage());
        }
        return found;
    }

    private String limitString(final String str, final int len) {
        if (str == null) {
            return "";
        }
        if (str.length() > len) {
            return str.substring(0, len - DOTS_LEN) + "...";
        }
        return str;
    }

    /**
     * Υπολογίζει τα σύνολα για έναν πίνακα.
     *
     * @param tablename Το όνομα του πίνακα.
     * @return Πίνακας double με [αρχικό σύνολο, τρέχον σύνολο].
     */
    public double[] getTotal(final String tablename) {
        String sql = "SELECT SUM(amount) AS total_amount, "
                + "SUM(original_amount) AS total_original FROM " + tablename;
        double[] results = new double[2];
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                results[0] = rs.getDouble("total_original");
                results[1] = rs.getDouble("total_amount");
            }

        } catch (SQLException e) {
            System.err.println("Σφάλμα σύνδεσης: " + e.getMessage());
        }
        return results;
    }

    /**
     * Επιστρέφει τον χαρακτηρισμό του προϋπολογισμού.
     *
     * @param revenue  Τα έσοδα.
     * @param expenses Τα έξοδα.
     * @return String με τον χαρακτηρισμό (Πλεονασματικός/Ελλειμματικός/Ισος).
     */
    public String getBudgetCharacterism(final double revenue,
                                        final double expenses) {
        if (revenue > expenses) {
            return "Πλεονασματικός (+"
                    + (long) (revenue - expenses) + " EUR)";
        } else if (revenue < expenses) {
            return "Ελλειμματικός (-"
                    + (long) (expenses - revenue) + " EUR)";
        } else {
            return "Ισοσκελισμένος";
        }
    }

    /**
     * Ανακτά το τρέχον ποσό μιας εγγραφής βάσει ID.
     *
     * @param tableName Το όνομα του πίνακα.
     * @param idColName Το όνομα της στήλης ID.
     * @param id        Το ID της εγγραφής.
     * @return Το ποσό της εγγραφής, ή -1 αν δεν βρεθεί.
     */
    public double getCurrentAmount(final String tableName,
                                   final String idColName, final int id) {
        String sql = "SELECT amount FROM " + tableName
                + " WHERE " + idColName + " = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(IDX_1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("amount");
            }
        } catch (SQLException e) {
            System.out.println("Σφάλμα κατά την ανάκτηση του ποσού: "
                    + e.getMessage());
        }
        return AMOUNT_NOT_FOUND;
    }

    /**
     * Βρίσκει το όνομα μιας εγγραφής βάσει ID.
     *
     * @param tableName Το όνομα του πίνακα.
     * @param idColName Το όνομα της στήλης ID.
     * @param id        Το ID της εγγραφής.
     * @return Το όνομα της εγγραφής, ή null αν δεν βρεθεί.
     */
    public String getNameById(final String tableName,
                              final String idColName, final int id) {
        String sql = "SELECT name FROM " + tableName
                + " WHERE " + idColName + " = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(IDX_1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            System.out.println("Σφάλμα ανάκτησης ονόματος: " + e.getMessage());
        }
        return null;
    }
}
