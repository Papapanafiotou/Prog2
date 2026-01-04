package mainapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Υλοποιεί τη λειτουργία αναζήτησης σε όλους τους πίνακες.
 */
public final class Search {

    /** Λίστα πινάκων για αναζήτηση. */
    private static final String[] TABLES = {
        "esoda", "eksoda", "kratos", "ypourgeia", "apokentromenes"
    };

    /** Το URL της βάσης. */
    private final String url;

    /**
     * Κατασκευαστής.
     *
     * @param databaseUrl Το URL της βάσης δεδομένων.
     */
    public Search(final String databaseUrl) {
        this.url = databaseUrl;
    }

    /**
     * Αναζητά το ποσό βάσει ονόματος.
     *
     * @param name Το όνομα προς αναζήτηση.
     * @return Το ποσό που βρέθηκε ή 0.
     */
    public double searchAmount(final String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Σφάλμα: Δεν δόθηκε όνομα για αναζήτηση.");
            return 0;
        }
        double amount = 0;
        try (Connection conn = DriverManager.getConnection(url)) {
            for (String table : TABLES) {
                String sql = "SELECT amount FROM " + table
                        + " WHERE name LIKE ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, "%" + name.trim() + "%");
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            amount = rs.getDouble("amount");
                            System.out.println(" ΒΡΕΘΗΚΕ στον πίνακα "
                                    + table + "! Ποσό: " + (long) amount);
                            return amount;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Σφάλμα στη βάση: " + e.getMessage());
        }
        return amount;
    }

    /**
     * Αναζητά το όνομα βάσει ποσού.
     *
     * @param amount1 Το ποσό προς αναζήτηση.
     * @return Το όνομα που βρέθηκε ή null.
     */
    public String searchString(final double amount1) {
        String name = null;
        try (Connection conn = DriverManager.getConnection(url)) {
            for (String table : TABLES) {
                String sql = "SELECT name FROM " + table + " WHERE amount = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setDouble(1, amount1);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            name = rs.getString("name");
                            return name;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Σφάλμα στη βάση: " + e.getMessage());
        }
        return name;
    }

    /**
     * Επιστρέφει το όνομα του πίνακα που περιέχει το όνομα αναζήτησης.
     *
     * @param name2 Το όνομα προς αναζήτηση.
     * @return Το όνομα του πίνακα ή null.
     */
    public String searchTable(final String name2) {
        String tab = null;
        try (Connection conn = DriverManager.getConnection(url)) {
            for (String table : TABLES) {
                String sql = "SELECT 1 FROM " + table + " WHERE name = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, name2);
                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            tab = table;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Σφάλμα στη βάση: " + e.getMessage());
        }
        return tab;
    }

    public double searchAmountInTable(final String name, final String tableName) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Σφάλμα: Δεν δόθηκε όνομα για αναζήτηση.");
            return 0;
        }
        if (tableName == null || tableName.trim().isEmpty()) {
            System.out.println("Σφάλμα: Δεν δόθηκε όνομα πίνακα.");
            return 0;
        }
        double amount = 0;
        try (Connection conn = DriverManager.getConnection(url)) {

         String sql = "SELECT amount FROM " + tableName + " WHERE name LIKE ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, "%" + name.trim() + "%");
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        amount = rs.getDouble("amount");
                        System.out.println(" ΒΡΕΘΗΚΕ στον πίνακα " 
                            + tableName + "! Ποσό: " + (long) amount);
                        return amount;
                    } else {
                        System.out.println(" Δεν βρέθηκε εγγραφή στον πίνακα " + tableName);
                    }
            }
        }
        } catch (SQLException e) {
            System.err.println("Σφάλμα στη βάση: " + e.getMessage());
        }
        return amount;
    }
}
