package mainapp;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BudgetMenuTest {
    private static final String DB_URL = "jdbc:sqlite:test_menu.db";

    @BeforeEach
    public void setUp() throws Exception {
        // Δημιουργία βάσης για να μην κρασάρουν οι μέθοδοι που κάνουν queries
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS esoda (code INTEGER, name TEXT, amount DOUBLE)");
            stmt.execute("INSERT INTO esoda VALUES (101, 'TestIncome', 1000.0)");
            stmt.execute("CREATE TABLE IF NOT EXISTS eksoda (code INTEGER, name TEXT, amount DOUBLE)");
            stmt.execute("INSERT INTO eksoda VALUES (201, 'TestExpense', 500.0)");
        }
    }

    @Test
    public void testMenuFullFlow() {
        // Προσομοίωση ροής:
        // 1. Επιλογή 1 (Show) -> Επιλογή 7 (Back)
        // 2. Επιλογή 4 (Total) -> Επιλογή 1 (Esoda)
        // 3. Επιλογή 14 (Exit)
        String input = "1\n7\n4\n1\n14\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        BudgetMenu menu = new BudgetMenu(DB_URL);
        assertDoesNotThrow(() -> menu.start());
    }

    @Test
    public void testInvalidInputRecovery() {
        // Προσομοίωση: "abc" (λάθος) -> 14 (έξοδος)
        String input = "abc\n14\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        BudgetMenu menu = new BudgetMenu(DB_URL);
        assertDoesNotThrow(() -> menu.start());
    }

    @Test
    public void testAiSpecificFlowWithMissingData() {
        // Προσομοίωση: 10 (AI) -> 1 (Esoda) -> 999 (ID που δεν υπάρχει) -> 14 (Exit)
        String input = "10\n1\n999\n14\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        BudgetMenu menu = new BudgetMenu(DB_URL);
        assertDoesNotThrow(() -> menu.start());
    }
}