package mainapp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class PinakesImporter {

    private final String dbUrl;

    public PinakesImporter(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public void importAll() {
        Path currentWorkingDir = Paths.get(".").toAbsolutePath().normalize();
        Path baseDir;
        if (Files.exists(currentWorkingDir.resolve("statewallet"))) {
            baseDir = currentWorkingDir.resolve("statewallet");
        } else {
            baseDir = currentWorkingDir;
        }
        Path sourcesDir = baseDir.resolve(Paths.get("src", "main", "sources"));
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            createTables(conn);
            importEsoda(conn, sourcesDir.resolve("income.csv").toString());
            importEksoda(conn, sourcesDir.resolve("expenses.csv").toString());
            importMinistries(conn, sourcesDir.resolve("ministries.csv").toString());
            System.out.println(" Όλοι οι πίνακες εισήχθησαν επιτυχώς.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
// ---------------------------------------------------------
    //  CREATE TABLES (ΔΙΟΡΘΩΜΕΝΟ)
    // ---------------------------------------------------------
    private void createTables(Connection conn) throws SQLException {

        Statement st = conn.createStatement();

        // 1. ΠΡΩΤΑ ΣΒΗΝΟΥΜΕ ΤΟΥΣ ΠΑΛΙΟΥΣ ΠΙΝΑΚΕΣ (ΑΝ ΥΠΑΡΧΟΥΝ)
        // Έτσι είμαστε σίγουροι ότι θα ξαναφτιαχτούν με τις ΝΕΕΣ στήλες.
        st.executeUpdate("DROP TABLE IF EXISTS esoda");
        st.executeUpdate("DROP TABLE IF EXISTS eksoda");
        st.executeUpdate("DROP TABLE IF EXISTS ypourgeia");
        st.executeUpdate("DROP TABLE IF EXISTS kratos");
        st.executeUpdate("DROP TABLE IF EXISTS apokentromenes");

        // 2. ΤΩΡΑ ΤΟΥΣ ΔΗΜΙΟΥΡΓΟΥΜΕ ΑΠΟ ΤΗΝ ΑΡΧΗ
        st.executeUpdate("""
                CREATE TABLE esoda(
                    code INTEGER,
                    name TEXT,
                    amount REAL,
                    original_amount REAL
                );
            """);

        st.executeUpdate("""
                CREATE TABLE eksoda(
                    code INTEGER,
                    name TEXT,
                    amount REAL,
                    original_amount REAL
                );
            """);

        st.executeUpdate("""
                CREATE TABLE ypourgeia(
                    number INTEGER,
                    name TEXT,
                    amount1 REAL,
                    amount2 REAL,
                    amount REAL,
                    original_amount1 REAL,
                    original_amount2 REAL,
                    original_amount REAL
                );
            """);

        st.executeUpdate("""
                CREATE TABLE kratos(
                    number INTEGER,
                    name TEXT,
                    amount1 REAL,
                    amount2 REAL,
                    amount REAL,
                    original_amount1 REAL,
                    original_amount2 REAL,
                    original_amount REAL
                );
            """);

        st.executeUpdate("""
                CREATE TABLE apokentromenes(
                    number INTEGER,
                    name TEXT,
                    amount1 REAL,
                    amount2 REAL,
                    amount REAL,
                    original_amount1 REAL,
                    original_amount2 REAL,
                    original_amount REAL
                );
            """);
    }

    // ---------------------------------------------------------
    //  IMPORT ESODA
    // ---------------------------------------------------------
    private void importEsoda(Connection conn, String csvPath) throws Exception {

        String sql = "INSERT INTO esoda (code, name, amount, original_amount) VALUES (?, ?, ?, ?)";

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

        String sql = "INSERT INTO eksoda (code, name, amount, original_amount) VALUES (?, ?, ?, ?)";

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

        String sqlYp = "INSERT INTO ypourgeia VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlKr = "INSERT INTO kratos VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlAp = "INSERT INTO apokentromenes VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement psYp = conn.prepareStatement(sqlYp);
        PreparedStatement psKr = conn.prepareStatement(sqlKr);
        PreparedStatement psAp = conn.prepareStatement(sqlAp);

       try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {

            String line;
            boolean skipHeader = true;


            while ((line = reader.readLine()) != null) {

                if (skipHeader) { skipHeader = false; continue; }



                // Κόβουμε τη γραμμή στα κόμματα
                String[] p = line.split(",");
                
                // Έλεγχος αν έχουμε αρκετά δεδομένα
                if (p.length < 5) continue;

                int len = p.length;

                // 1. Διαβάζουμε τον Κωδικό (πάντα στην αρχή)
                int number = Integer.parseInt(p[0].trim());

                // 2. Διαβάζουμε τα Ποσά (πάντα στο τέλος του πίνακα)
                // Παίρνουμε τα 3 τελευταία στοιχεία ως αριθμούς
                double amount3 = Double.parseDouble(p[len - 1].trim()); // original
                double amount2 = Double.parseDouble(p[len - 2].trim());
                double amount1 = Double.parseDouble(p[len - 3].trim());

                // 3. Φτιάχνουμε το Όνομα
                // Ενώνουμε όλα τα ενδιάμεσα κομμάτια (αν το όνομα είχε κόμματα, έσπασε σε πολλά κομμάτια)
                StringBuilder nameBuilder = new StringBuilder();
                for (int i = 1; i <= len - 4; i++) {
                    nameBuilder.append(p[i]);
                    if (i < len - 4) nameBuilder.append(" "); // Βάζουμε κενό εκεί που ήταν το κόμμα
                }
                String name = nameBuilder.toString().trim();


                // --- rows 1–3 → kratos
                if (number >= 1000 && number <= 1005) {
                    psKr.setInt(1, number);
                    psKr.setString(2, name);
                    psKr.setDouble(3, amount1);
                    psKr.setDouble(4, amount2);
                    psKr.setDouble(5, amount3);
                    psKr.setDouble(6, amount1);
                    psKr.setDouble(7, amount2);
                    psKr.setDouble(8, amount3);
                    psKr.addBatch();
                }

                // --- rows 4–23 → ypourgeia
                else if (number >= 1007 && number <= 1070) {
                    psYp.setInt(1, number);
                    psYp.setString(2, name);
                    psYp.setDouble(3, amount1);
                    psYp.setDouble(4, amount2);
                    psYp.setDouble(5, amount3);
                    psYp.setDouble(6, amount1);
                    psYp.setDouble(7, amount2);
                    psYp.setDouble(8, amount3);
                    psYp.addBatch();
                }

                // --- rows 24–30 → apokentromenes
                else if (number >= 1800 && number <= 2000) {
                    psAp.setInt(1, number);
                    psAp.setString(2, name);
                    psAp.setDouble(3, amount1);
                    psAp.setDouble(4, amount2);
                    psAp.setDouble(5, amount3);
                    psAp.setDouble(6, amount1);
                    psAp.setDouble(7, amount2);
                    psAp.setDouble(8, amount3);
                    psAp.addBatch();
                }
            }

            psKr.executeBatch();
            psYp.executeBatch();
            psAp.executeBatch();
        }
        }
    }
