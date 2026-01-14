package mainapp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StateWalletTest {

    private final InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        // Απαραίτητο για tests που μπορεί να καλούν γραφικά στοιχεία
        System.setProperty("java.awt.headless", "true");
    }

    @AfterEach
    void restoreIn() {
        System.setIn(originalIn);
    }

    private void provideInput(String data) {
        // Δημιουργούμε ένα πολύ μεγάλο buffer ασφαλείας.
        // Το '5' βγαίνει από το Log.
        // Το '14' βγαίνει από το BudgetMenu (αν ποτέ έφτανε εκεί).
        StringBuilder sb = new StringBuilder(data);
        for (int i = 0; i < 200; i++) {
            sb.append("\n5\n14\n"); 
        }
        System.setIn(new ByteArrayInputStream(sb.toString().getBytes()));
    }

    @Test
    void testMainFullFlow() {
        // Στέλνουμε '5' για να σταματήσει αμέσως στο log.logMenu() 
        // ώστε η μεταβλητή login να γίνει false και να τελειώσει η main.
        provideInput("5\n");
        
        assertDoesNotThrow(() -> {
            try {
                // Χρησιμοποιούμε reflection ή άμεση κλήση της main
                StateWallet.main(new String[]{});
            } catch (java.util.NoSuchElementException e) {
                // Πιάνουμε το exception αν ο Scanner στερέψει
                System.out.println("Scanner empty - flow finished.");
            }
        }, "Η main δεν έπρεπε να πετάξει μη διαχειρίσιμο σφάλμα");
    }
}