package mainapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AccountsTest {

    private Accounts accounts;

    @BeforeEach
    public void setUp() {
        accounts = new Accounts();
        // Δημιουργούμε τον πίνακα πριν από κάθε test
        accounts.createTable();
    }

    @Test
    public void testCreateAndGetPassword() {
        accounts.createAccount("testUser", "Pass123!", "ID123");
        String pass = accounts.getPassword("testUser");
        assertEquals("Pass123!", pass);
    }

    @Test
    public void testGetPasswordUserNotFound() {
        String pass = accounts.getPassword("nonExistentUser");
        assertNull(pass);
    }

    @Test
    public void testLogInSuccess() {
        assertTrue(accounts.logIn("myPass", "myPass"));
    }

    @Test
    public void testLogInFailure() {
        assertFalse(accounts.logIn("myPass", "wrongPass"));
        assertFalse(accounts.logIn(null, "anyPass"));
    }

    @Test
    public void testValidatePassword() {
        // Test short password
        assertFalse(Accounts.validatePassword("Short1!"));
        // Test no uppercase
        assertFalse(Accounts.validatePassword("alllower1!"));
        // Test no lowercase
        assertFalse(Accounts.validatePassword("ALLUPPER1!"));
        // Test no digit
        assertFalse(Accounts.validatePassword("NoDigitSpecial!"));
        // Test no special char
        assertFalse(Accounts.validatePassword("NoSpecial123"));
        // Test valid
        assertTrue(Accounts.validatePassword("Valid123#"));
    }

    @Test
    public void testNewPass() {
        accounts.createAccount("user2", "oldPass", "ID2");
        accounts.newPass("newPass123", "user2");
        assertEquals("newPass123", accounts.getPassword("user2"));
    }

    @Test
    public void testGetId() {
        accounts.createAccount("user3", "pass", "ABC_999");
        assertEquals("ABC_999", accounts.getId("user3"));
        assertNull(accounts.getId("ghostUser"));
    }
}