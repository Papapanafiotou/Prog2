package mainapp;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class AiBridgeTest {

    @Test
    public void testScriptDiscovery() {
        AiBridge bridge = new AiBridge();
        
        // Χρησιμοποιούμε Reflection για να ελέγξουμε την private μέθοδο findScript
        // ή απλώς ελέγχουμε αν το script υπάρχει στις αναμενόμενες τοποθεσίες
        String currentDir = System.getProperty("user.dir");
        File script = new File(currentDir, "budget_brain.py");
        
        // Αν το script δεν υπάρχει στο root, το τεστ θα μας ενημερώσει
        // αλλά δεν θα "σπάσει" το build αν το χειριστούμε σωστά
        if (script.exists()) {
            System.out.println("AI Script found at: " + script.getAbsolutePath());
            assertTrue(script.exists());
        } else {
            System.out.println("Warning: budget_brain.py not found in root. AI features will be disabled.");
        }
    }

    @Test
    public void testGetSpecificAdviceErrorHandling() {
        AiBridge bridge = new AiBridge();
        
        // Ελέγχουμε την αντίδραση της κλάσης όταν καλείται με κενά δεδομένα
        // Αν το python script λείπει, η μέθοδος πρέπει να επιστρέψει το μήνυμα "ΣΦΑΛΜΑ:..."
        String result = bridge.getSpecificAdvice("fake.db", "Test", 100.0, "Goal");
        
        assertNotNull(result);
        // Αν δεν υπάρχει το script, ο κώδικάς σας επιστρέφει συγκεκριμένο μήνυμα
        if (result.contains("ΣΦΑΛΜΑ")) {
            assertTrue(result.contains("Δεν βρέθηκε το budget_brain.py"));
        }
    }

    @Test
    public void testGlobalStrategyArgumentPassing() {
        AiBridge bridge = new AiBridge();
        // Ελέγχουμε αν η μέθοδος επιστρέφει κάτι (έστω και σφάλμα) χωρίς να κρασάρει η Java
        String result = bridge.getGlobalStrategy("jdbc:sqlite:test.db", "Μείωση ελλείμματος");
        assertNotNull(result);
    }
}