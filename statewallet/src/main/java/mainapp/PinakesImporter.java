import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PinakesImporter {

    private static final String url = "jdbc:sqlite:budget.db";

    private final Connection conn;

    public PinakesImporter() throws SQLException {
        try {
        Class.forName("org.sqlite.JDBC"); // <-- προσθήκη αυτής της γραμμής
    } catch (ClassNotFoundException e) {
        throw new SQLException("Δεν βρέθηκε ο driver SQLite JDBC!", e);
    }

    this.conn = DriverManager.getConnection(url);
    createTables();
    }


    public void importAll(String incomeCsv, String expensesCsv, String ministriesCsv) throws IOException, SQLException {
        clearTables(); // καθαρίζουμε τους πίνακες αν υπάρχουν παλιά δεδομένα
        importEsoda(incomeCsv);
        importEksoda(expensesCsv);
        importMinistries(ministriesCsv);
    }

    // === Δημιουργία πινάκων ===
    public void createTables() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS esoda (
                code INTEGER,
                name TEXT,
                original_amount REAL,
                amount REAL
            );

            CREATE TABLE IF NOT EXISTS eksoda (
                code INTEGER,
                name TEXT,
                original_amount REAL,
                amount REAL
            );

            CREATE TABLE IF NOT EXISTS kratos (
                number INTEGER,
                name TEXT,
                amount1 REAL,
                amount2 REAL,
                original_amount REAL,
                amount REAL
            );

            CREATE TABLE IF NOT EXISTS ypourgeia (
                number INTEGER,
                name TEXT,
                amount1 REAL,
                amount2 REAL,
                original_amount REAL,
                amount REAL
            );

            CREATE TABLE IF NOT EXISTS apokentromenes (
                number INTEGER,
                name TEXT,
                amount1 REAL,
                amount2 REAL,
                original_amount REAL,
                amount REAL
            );
            """;
        conn.createStatement().executeUpdate(sql);
    }

    //Καθαρισμός πινάκων
    private void clearTables() throws SQLException {
        String[] tables = {"esoda", "eksoda", "kratos", "ypourgeia", "apokentromenes"};
        for (String t : tables) {
            conn.createStatement().executeUpdate("DELETE FROM " + t);
        }
    }

    //Εισαγωγή CSV για esoda
    private void importEsoda(String csvPath) throws IOException, SQLException {
        importCsvGeneric(csvPath, "esoda");
    }

    // Εισαγωγή CSV για eksoda
    private void importEksoda(String csvPath) throws IOException, SQLException {
        importCsvGeneric(csvPath, "eksoda");
    }

    // === Κοινή μέθοδος για esoda και eksoda ===
    private void importCsvGeneric(String csvPath, String tableName) throws IOException, SQLException {
        String insertSQL = "INSERT INTO " + tableName + " (code, name, original_amount, amount) VALUES (?, ?, ?, ?)";
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath));
             PreparedStatement ps = conn.prepareStatement(insertSQL)) {

            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped && (line.toLowerCase().contains("code") || line.toLowerCase().contains("name"))) {
                    headerSkipped = true;
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length < 3) continue;

                ps.setInt(1, Integer.parseInt(parts[0].trim()));
                ps.setString(2, parts[1].trim());
                ps.setDouble(3, Double.parseDouble(parts[2].trim()));
                ps.setDouble(4, Double.parseDouble(parts[2].trim()));
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    // === Εισαγωγή ministries.csv στους 3 πίνακες ===
    private void importMinistries(String csvPath) throws IOException, SQLException {
        conn.createStatement().executeUpdate("""
            CREATE TEMP TABLE data_temp (
                number INTEGER,
                name TEXT,
                amount1 REAL,
                amount2 REAL,
                original_amount REAL,
                amount REAL
            );
        """);

        String insertSQL = "INSERT INTO data_temp VALUES (?, ?, ?, ?, ? ,?)";
        try (BufferedReader br = new BufferedReader(new FileReader(csvPath));
             PreparedStatement ps = conn.prepareStatement(insertSQL)) {

            String line;
            boolean headerSkipped = false;
            int rowid = 0;

            while ((line = br.readLine()) != null) {
                if (!headerSkipped && (line.toLowerCase().contains("number") || line.toLowerCase().contains("name"))) {
                    headerSkipped = true;
                    continue;
                }
                rowid++;

                String[] parts = line.split(",");
                if (parts.length < 5) continue;

                ps.setInt(1, Integer.parseInt(parts[0].trim()));
                ps.setString(2, parts[1].trim());
                ps.setDouble(3, Double.parseDouble(parts[2].trim()));
                ps.setDouble(4, Double.parseDouble(parts[3].trim()));
                ps.setDouble(5, Double.parseDouble(parts[4].trim()));
                ps.setDouble(6, Double.parseDouble(parts[4].trim()));
                ps.addBatch();
            }
            ps.executeBatch();

            conn.createStatement().executeUpdate("""
                INSERT INTO kratos SELECT * FROM data_temp WHERE rowid BETWEEN 1 AND 3;
                INSERT INTO ypourgeia SELECT * FROM data_temp WHERE rowid BETWEEN 4 AND 23;
                INSERT INTO apokentromenes SELECT * FROM data_temp WHERE rowid BETWEEN 24 AND 30;
                DROP TABLE data_temp;
            """);
        }
    }

    // === Κλείσιμο σύνδεσης ===
    public void close() throws SQLException {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}