package mainapp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;

public class PinakesImporter {

    private final String dbUrl;

    public PinakesImporter(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public void importAll() {
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            createTables(conn);
            importEsoda(conn, "Prog2\\statewallet\\src\\main\\java\\mainapp\\income.csv");
            importEksoda(conn, "Prog2\\statewallet\\src\\main\\java\\mainapp\\expenses.csv");
            importMinistries(conn, "Prog2\\statewallet\\src\\main\\java\\mainapp\\ministries.csv");
            System.out.println("✔ Όλοι οι πίνακες εισήχθησαν επιτυχώς.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------------------------------------------------
    //  CREATE TABLES
    // ---------------------------------------------------------
    private void createTables(Connection conn) throws SQLException {

        Statement st = conn.createStatement();

        st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS esoda(
                    code INTEGER,
                    name TEXT,
                    amount1 REAL,
                    original_amount REAL
                );
            """);

        st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS eksoda(
                    code INTEGER,
                    name TEXT,
                    amount1 REAL,
                    original_amount REAL
                );
            """);

        st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS ypourgeia(
                    number INTEGER,
                    name TEXT,
                    amount1 REAL,
                    amount2 REAL,
                    amount3 REAL,
                    original_amount REAL
                );
            """);

        st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS kratos(
                    number INTEGER,
                    name TEXT,
                    amount1 REAL,
                    amount2 REAL,
                    amount3 REAL,
                    original_amount REAL
                );
            """);

        st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS apokentromenes(
                    number INTEGER,
                    name TEXT,
                    amount1 REAL,
                    amount2 REAL,
                    amount3 REAL,
                    original_amount REAL
                );
            """);
    }

    // ---------------------------------------------------------
    //  IMPORT ESODA
    // ---------------------------------------------------------
    private void importEsoda(Connection conn, String csvPath) throws Exception {

        String sql = "INSERT INTO esoda (code, name, amount1, original_amount) VALUES (?, ?, ?, ?)";

        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath));
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String line;
            boolean skipHeader = true;

            while ((line = reader.readLine()) != null) {

                if (skipHeader) { skipHeader = false; continue; }

                String[] p = line.split(",");
                if (p.length < 3) continue;

                int code = Integer.parseInt(p[0]);
                String name = p[1];
                double amount1 = Double.parseDouble(p[2]);

                ps.setInt(1, code);
                ps.setString(2, name);
                ps.setDouble(3, amount1);
                ps.setDouble(4, amount1);  // original_amount = amount1
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }

    // ---------------------------------------------------------
    //  IMPORT EKSODA
    // ---------------------------------------------------------
    private void importEksoda(Connection conn, String csvPath) throws Exception {

        String sql = "INSERT INTO eksoda (code, name, amount1, original_amount) VALUES (?, ?, ?, ?)";

        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath));
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String line;
            boolean skipHeader = true;

            while ((line = reader.readLine()) != null) {

                if (skipHeader) { skipHeader = false; continue; }

                String[] p = line.split(",");
                if (p.length < 3) continue;

                int code = Integer.parseInt(p[0]);
                String name = p[1];
                double amount1 = Double.parseDouble(p[2]);

                ps.setInt(1, code);
                ps.setString(2, name);
                ps.setDouble(3, amount1);
                ps.setDouble(4, amount1);  // original_amount = amount1
                ps.addBatch();
            }

            ps.executeBatch();
        }
    }


    // ---------------------------------------------------------
    //  IMPORT MINISTRIES → split into 3 tables
    // ---------------------------------------------------------
    private void importMinistries(Connection conn, String csvPath) throws Exception {

        // --------------------------------------------------------------------------------
        // ministries.csv columns:
        // number, name, amount1, amount2, amount3
        // --------------------------------------------------------------------------------

        String sqlYp = "INSERT INTO ypourgeia VALUES (?, ?, ?, ?, ?, ?)";
        String sqlKr = "INSERT INTO kratos VALUES (?, ?, ?, ?, ?, ?)";
        String sqlAp = "INSERT INTO apokentromenes VALUES (?, ?, ?, ?, ?, ?)";

        PreparedStatement psYp = conn.prepareStatement(sqlYp);
        PreparedStatement psKr = conn.prepareStatement(sqlKr);
        PreparedStatement psAp = conn.prepareStatement(sqlAp);

        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {

            String line;
            boolean skipHeader = true;
            int row = 0;

            while ((line = reader.readLine()) != null) {

                if (skipHeader) { skipHeader = false; continue; }

                row++;

                String[] p = line.split(",");
                if (p.length < 5) continue;

                int number = Integer.parseInt(p[0]);
                String name = p[1];
                double amount1 = Double.parseDouble(p[2]);
                double amount2 = Double.parseDouble(p[3]);
                double amount3 = Double.parseDouble(p[4]); // original_amount

                // --- rows 1–3 → kratos
                if (row >= 1 && row <= 3) {
                    psKr.setInt(1, number);
                    psKr.setString(2, name);
                    psKr.setDouble(3, amount1);
                    psKr.setDouble(4, amount2);
                    psKr.setDouble(5, amount3);
                    psKr.setDouble(6, amount3);  // original_amount
                    psKr.addBatch();
                }

                // --- rows 4–23 → ypourgeia
                else if (row >= 4 && row <= 23) {
                    psYp.setInt(1, number);
                    psYp.setString(2, name);
                    psYp.setDouble(3, amount1);
                    psYp.setDouble(4, amount2);
                    psYp.setDouble(5, amount3);
                    psYp.setDouble(6, amount3);  // original_amount
                    psYp.addBatch();
                }

                // --- rows 24–30 → apokentromenes
                else if (row >= 24 && row <= 30) {
                    psAp.setInt(1, number);
                    psAp.setString(2, name);
                    psAp.setDouble(3, amount1);
                    psAp.setDouble(4, amount2);
                    psAp.setDouble(5, amount3);
                    psAp.setDouble(6, amount3);  // original_amount
                    psAp.addBatch();
                }
            }

            psKr.executeBatch();
            psYp.executeBatch();
            psAp.executeBatch();
        }
    }
}
