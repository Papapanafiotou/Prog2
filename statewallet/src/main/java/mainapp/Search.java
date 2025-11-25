package mainapp;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Search {
    
    private static final String url = "jdbc:sqlite:budget.db";
    private static final String Tables[] = {"esoda", "eksoda", "kratos","ypourgeia", "apokentromenes"};//ονόματα πινάκων
    public double searchAmount(String name) { 
    double amount = 0;
    System.out.println("--> Ψάχνω στη βάση (" + url + ") για: '" + name + "'");
        try (Connection conn = DriverManager.getConnection(url)) {
            for (String table : Tables) { // αναζητεί σε όλους τους πίνακες μέχρι να βρεί τον λογαριασμό
                String sql = "SELECT amount FROM " + table + " WHERE name LIKE ? "; // εντολή SQL
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, "%" + name.trim() + "%"); // σε κάθε επανάληψη αλλάζει το όνομα του πίνακα
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        amount = rs.getDouble("amount"); // εκχωρεί το ποσό όταν βρεθεί
                        System.out.println("✅ ΒΡΕΘΗΚΕ στον πίνακα " + table + "! Ποσό: " + amount);
                        return amount; 
                }
            }
        }
        } catch (SQLException e) {
            System.err.println("Σφάλμα στη βάση: " + e.getMessage());
        }
        return amount; //επιστρέφει το ποσό 
    }

    public String searchString(double amount1) {
        String name = null;
         try (Connection conn = DriverManager.getConnection(url)) {
            for (String table : Tables) { // αναζητεί σε όλους τους πίνακες μέχρι να βρεί τον λογαριασμό
                String sql = "SELECT name FROM " + table + " WHERE amount = ? "; // εντολή SQL
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setDouble(1, amount1); // σε κάθε επανάληψη αλλάζει το όνομα του πίνακα
                    ResultSet rs = stmt.executeQuery();
                    if (rs.next()) { 
                        name = rs.getString("name"); // εκχωρεί το ποσό όταν βρεθεί
                        return name; 
                }
            }
    }
    } catch (SQLException e) {
            System.err.println("Σφάλμα στη βάση: " + e.getMessage());
        }
        return name;
    }

    public String searchTable(String name2) {
        String tab = null;
        try (Connection conn = DriverManager.getConnection(url)) {
            for (String table : Tables) {
                String sql = " SELECT EXISTS ( " +
                         " SELECT 1 FROM " + table +
                         " WHERE logariasmos = ? " +
                         " )";
           try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name2);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 1) {
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
}
