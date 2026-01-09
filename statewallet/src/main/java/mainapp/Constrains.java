package mainapp;

import java.util.Scanner;

/**
 * Η κλάση {@code Constrains} περιέχει στατικές μεθόδους που επιβάλλουν
 * επιχειρηματικούς κανόνες και ελέγχους εγκυρότητας στα οικονομικά δεδομένα.
 * <p>
 * Χρησιμοποιείται για να διασφαλίσει ότι οι τιμές που εισάγει ο χρήστης
 * είναι λογικές και συμβατές με τους δημοσιονομικούς κανονισμούς (π.χ. όρια ελλείμματος).
 * </p>
 */
public class Constrains {

    /**
     * Επαληθεύει ότι ένα χρηματικό ποσό είναι μη αρνητικό.
     * <p>
     * Αν το αρχικό ποσό είναι αρνητικό, η μέθοδος εισέρχεται σε έναν βρόχο (loop)
     * ζητώντας από τον χρήστη να εισάγει νέα τιμή μέχρι να δοθεί έγκυρος θετικός αριθμός (ή μηδέν).
     * Διαχειρίζεται επίσης περιπτώσεις λανθασμένης εισόδου (μη αριθμητικοί χαρακτήρες).
     * </p>
     *
     * @param scanner Το αντικείμενο {@code Scanner} για την ανάγνωση νέας εισόδου από τον χρήστη.
     * @param amount Η αρχική τιμή που πρέπει να ελεγχθεί.
     * @return Ένα έγκυρο, μη αρνητικό ποσό (`double`).
     */
    public static double negativeAmount(Scanner scanner, double amount){
        while (amount < 0) {
                System.out.println("ΣΦΑΛΜΑ: Το ποσό δεν μπορεί να είναι αρνητικό.");
                System.out.print("Δώσε το νέο ποσό: ");
                // Ελεγχος αν ο χρηστης εδωσε αριθμο //
                try {
                    amount = Double.parseDouble(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Πρέπει να δώσετε αριθμό.");
                    amount = -1; 
                }
            }
        return amount;
    }

    /**
     * Ελέγχει αν η μεταβολή ενός ποσού θεωρείται "λογική", δηλαδή δεν υπερβαίνει
     * το 50% της αρχικής τιμής.
     * <p>
     * Αυτός ο έλεγχος λειτουργεί ως ασφαλιστική δικλείδα για την αποφυγή ακραίων
     * λαθών κατά την πληκτρολόγηση αλλαγών στον προϋπολογισμό.
     * </p>
     *
     * @param original_amount Το αρχικό ποσό πριν την αλλαγή.
     * @param newAmount Το νέο ποσό που προτείνει ο χρήστης.
     * @return {@code true} αν η αλλαγή είναι μικρότερη του 50% (ή αν το αρχικό ποσό είναι 0),
     * διαφορετικά {@code false}.
     */
    public static boolean isReasonableChange(double original_amount, double newAmount){
        if(original_amount == 0) {
            return true;
        }
        double PercentChange = Math.abs((original_amount - newAmount) / original_amount);     
        if (PercentChange >= 0.5) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Ελέγχει αν τηρείται το όριο του δημοσιονομικού ελλείμματος βάσει των κανονισμών.
     * <p>
     * Συγκεκριμένα, υπολογίζει αν το έλλειμμα (η διαφορά Εξόδων - Εσόδων)
     * υπερβαίνει το 3% των συνολικών εσόδων.
     * </p>
     *
     * @param esoda Το συνολικό ποσό των εσόδων.
     * @param eksoda Το συνολικό ποσό των εξόδων.
     * @return {@code true} αν δεν υπάρχει έλλειμμα ή αν το έλλειμμα είναι εντός του ορίου (<= 3%).
     * {@code false} αν το έλλειμμα υπερβαίνει το 3%.
     */
    public static boolean deficitLimit(double esoda, double eksoda){
        if (esoda >= eksoda) {
            return true;
        }
        double defperc = ((eksoda - esoda) / esoda) * 100;

        if (defperc > 3) {
            return false;
        } else {
            return true;
        }
    
    }
}