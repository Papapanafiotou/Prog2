package mainapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseHandler {
    
    private static final String DATABASE_URL = "jdbc:sqlite:budget.db";

    public Connection connect() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DATABASE_URL);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Σφάλμα σύνδεσης με τη βάση: " + e.getMessage());
        }
        return conn;
    }
}