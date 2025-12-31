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

/**
 * Εισάγει δεδομένα από αρχεία CSV στη βάση δεδομένων.
 */
public final class PinakesImporter {

    /** URL βάσης. */
    private final String dbUrl;
    /** Min ID Κράτους. */
    private static final int MIN_KRATOS = 1000;
    /** Max ID Κράτους. */
    private static final int MAX_KRATOS = 1005;
    /** Min ID Υπουργείων. */
    private static final int MIN_YPOURGEIA = 1007;
    /** Max ID Υπουργείων. */
    private static final int MAX_YPOURGEIA = 1070;
    /** Min ID Αποκεντρωμένων. */
    private static final int MIN_APOK = 1800;
    /** Max ID Αποκεντρωμένων. */
    private static final int MAX_APOK = 2000;

    /**
     * Κατασκευαστής.
     *
     * @param url Το URL της βάσης δεδομένων.
     */
    public PinakesImporter(final String url) {
        this.dbUrl = url;
    }

    /**
     * Εκτελεί την εισαγωγή όλων των πινάκων.
     */
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
            importMinistries(conn,
                    sourcesDir.resolve("ministries.csv").toString());
            System.out.println(" Όλοι οι πίνακες εισήχθησαν επιτυχώς.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTables(final Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate("DROP TABLE IF EXISTS esoda");
        st.executeUpdate("DROP TABLE IF EXISTS eksoda");
        st.executeUpdate("DROP TABLE IF EXISTS ypourgeia");
        st.executeUpdate("DROP TABLE IF EXISTS kratos");
        st.executeUpdate("DROP TABLE IF EXISTS apokentromenes");

        st.executeUpdate("""
                 CREATE TABLE esoda(
                     code INTEGER, name TEXT,
                     amount REAL, original_amount REAL
                 );
             """);
        st.executeUpdate("""
                 CREATE TABLE eksoda(
                     code INTEGER, name TEXT,
                     amount REAL, original_amount REAL
                 );
             """);
        st.executeUpdate("""
                 CREATE TABLE ypourgeia(
                     number INTEGER, name TEXT,
                     amount1 REAL, amount2 REAL, amount REAL,
                     original_amount1 REAL, original_amount2 REAL,
                     original_amount REAL
                 );
             """);
        st.executeUpdate("""
                 CREATE TABLE kratos(
                     number INTEGER, name TEXT,
                     amount1 REAL, amount2 REAL, amount REAL,
                     original_amount1 REAL, original_amount2 REAL,
                     original_amount REAL
                 );
             """);
        st.executeUpdate("""
                 CREATE TABLE apokentromenes(
                     number INTEGER, name TEXT,
                     amount1 REAL, amount2 REAL, amount REAL,
                     original_amount1 REAL, original_amount2 REAL,
                     original_amount REAL
                 );
             """);
    }

    private void importEsoda(final Connection conn, final String csvPath)
            throws Exception {
        String sql = "INSERT INTO esoda VALUES (?, ?, ?, ?)";
        try (BufferedReader reader = new BufferedReader(
                new FileReader(csvPath));
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String line;
            boolean skipHeader = true;

            // Indices
            final int idx1 = 1;
            final int idx2 = 2;
            final int idx3 = 3;
            final int idx4 = 4;
            final int maxlen = 3;

            while ((line = reader.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                String[] p = line.split(",");
                if (p.length < maxlen) {
                    continue;
                }
                int code = Integer.parseInt(p[0]);
                String name = p[1];
                double amount1 = Double.parseDouble(p[2]);

                ps.setInt(idx1, code);
                ps.setString(idx2, name);
                ps.setDouble(idx3, amount1);
                ps.setDouble(idx4, amount1);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void importEksoda(final Connection conn, final String csvPath)
            throws Exception {
        String sql = "INSERT INTO eksoda VALUES (?, ?, ?, ?)";
        try (BufferedReader reader = new BufferedReader(
                new FileReader(csvPath));
             PreparedStatement ps = conn.prepareStatement(sql)) {

            String line;
            boolean skipHeader = true;

            // Indices
            final int idx1 = 1;
            final int idx2 = 2;
            final int idx3 = 3;
            final int idx4 = 4;
            final int maxlen = 3;

            while ((line = reader.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                String[] p = line.split(",");
                if (p.length < maxlen) {
                    continue;
                }
                int code = Integer.parseInt(p[0]);
                String name = p[1];
                double amount1 = Double.parseDouble(p[2]);

                ps.setInt(idx1, code);
                ps.setString(idx2, name);
                ps.setDouble(idx3, amount1);
                ps.setDouble(idx4, amount1);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void importMinistries(final Connection conn, final String csvPath)
            throws Exception {
        String sqlYp = "INSERT INTO ypourgeia VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlKr = "INSERT INTO kratos VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlAp =
        "INSERT INTO apokentromenes VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement psYp = conn.prepareStatement(sqlYp);
        PreparedStatement psKr = conn.prepareStatement(sqlKr);
        PreparedStatement psAp = conn.prepareStatement(sqlAp);

        try (BufferedReader reader = new BufferedReader(
            new FileReader(csvPath))) {
            String line;
            boolean skipHeader = true;
            final int minCols = 5;
            final int offset1 = 3;
            final int offset2 = 2;
            final int offset3 = 1;
            final int offsetName = 4;

            while ((line = reader.readLine()) != null) {
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }
                String[] p = line.split(",");
                if (p.length < minCols) {
                    continue;
                }
                int len = p.length;
                int number = Integer.parseInt(p[0].trim());
                double amount3 = Double.parseDouble(p[len - offset3].trim());
                double amount2 = Double.parseDouble(p[len - offset2].trim());
                double amount1 = Double.parseDouble(p[len - offset1].trim());

                StringBuilder nameBuilder = new StringBuilder();
                for (int i = 1; i <= len - offsetName; i++) {
                    nameBuilder.append(p[i]);
                    if (i < len - offsetName) {
                        nameBuilder.append(" ");
                    }
                }
                String name = nameBuilder.toString().trim();

                if (number >= MIN_KRATOS && number <= MAX_KRATOS) {
                    setParams(psKr, number, name, amount1, amount2, amount3);
                } else if (number >= MIN_YPOURGEIA && number <= MAX_YPOURGEIA) {
                    setParams(psYp, number, name, amount1, amount2, amount3);
                } else if (number >= MIN_APOK && number <= MAX_APOK) {
                    setParams(psAp, number, name, amount1, amount2, amount3);
                }
            }
            psKr.executeBatch();
            psYp.executeBatch();
            psAp.executeBatch();
        }
    }

    private void setParams(final PreparedStatement ps, final int num,
                           final String name, final double a1, final double a2,
                           final double a3) throws SQLException {
        // Indices
        final int idx1 = 1;
        final int idx2 = 2;
        final int idx3 = 3;
        final int idx4 = 4;
        final int idx5 = 5;
        final int idx6 = 6;
        final int idx7 = 7;
        final int idx8 = 8;

        ps.setInt(idx1, num);
        ps.setString(idx2, name);
        ps.setDouble(idx3, a1);
        ps.setDouble(idx4, a2);
        ps.setDouble(idx5, a3);
        ps.setDouble(idx6, a1);
        ps.setDouble(idx7, a2);
        ps.setDouble(idx8, a3);
        ps.addBatch();
    }
}
