package mainapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Accounts {
    public void createTable() {
        String url = "jdbc:sqlite:accounts.db";
        String sql = "CREATE TABLE IF NOT EXISTS accounts (" +
                         "username TEXT NOT NULL," +
                         "password TEXT NOT NULL" +
                         ");";
        try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.prepareStatement(sql)) {
                stmt.execute(sql);
                System.out.println("Ο πίνακας δημιουργήθηκε!");
            } catch (SQLException e) {
            // Χειρισμός σφαλμάτων JDBC.
            System.err.println(e.getMessage());
        }
    }
}