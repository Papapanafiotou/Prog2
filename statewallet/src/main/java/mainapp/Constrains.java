package mainapp;

import java.util.Scanner;

/**
 * Η κλάση {@code Constrains} περιέχει στατικές μεθόδους που επιβάλλουν
 * επιχειρηματικούς κανόνες και ελέγχους εγκυρότητας στα οικονομικά δεδομένα.
 * <p>
 * Χρησιμοποιείται για να διασφαλίσει ότι οι τιμές που εισάγει ο χρήστης
 * είναι λογικές και συμβατές με τους δημοσιονομικούς κανονισμούς.
 * </p>
 */
public final class Constrains {

    /** Μέγιστο επιτρεπτό ποσοστό αλλαγής (50%). */
    private static final double MAX_CHANGE_LIMIT = 0.5;
    /** Μέγιστο επιτρεπτό ποσοστό ελλείμματος (3%). */
    private static final double MAX_DEFICIT_PERCENT = 3.0;
    /** Πολλαπλασιαστής για μετατροπή σε ποσοστό. */
    private static final int PERCENT_MULT = 100;

    /**
     * Ιδιωτικός κατασκευαστής για να αποτραπεί η δημιουργία αντικειμένων
     * αυτής της βοηθητικής κλάσης.
     */
    private Constrains() {
        // Utility class
    }

    /**
     * Επαληθεύει ότι ένα χρηματικό ποσό είναι μη αρνητικό.
     * <p>
     * Αν το αρχικό ποσό είναι αρνητικό, ζητάει από τον χρήστη νέα τιμή.
     * </p>
     *
     * @param scanner Το αντικείμενο {@code Scanner} για την ανάγνωση εισόδου.
     * @param amount  Η αρχική τιμή που πρέπει να ελεγχθεί.
     * @return Ένα έγκυρο, μη αρνητικό ποσό (`double`).
     */
    public static double negativeAmount(final Scanner scanner,
                                        final double amount) {
        double validAmount = amount;
        while (validAmount < 0) {
            System.out.println("ΣΦΑΛΜΑ: Το ποσό δεν μπορεί να είναι αρνητικό.");
            System.out.print("Δώσε το νέο ποσό: ");
            // Ελεγχος αν ο χρηστης εδωσε αριθμο
            try {
                validAmount = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Πρέπει να δώσετε αριθμό.");
                validAmount = -1;
            }
        }
        return validAmount;
    }

    /**
     * Ελέγχει αν η μεταβολή ενός ποσού θεωρείται "λογική" (<= 50%).
     *
     * @param originalAmount Το αρχικό ποσό πριν την αλλαγή.
     * @param newAmount      Το νέο ποσό που προτείνει ο χρήστης.
     * @return {@code true} αν η αλλαγή είναι εντός ορίων.
     */
    public static boolean isReasonableChange(final double originalAmount,
                                             final double newAmount) {
        if (originalAmount == 0) {
            return true;
        }
        double percentChange = Math.abs(
                (originalAmount - newAmount) / originalAmount);

        return percentChange < MAX_CHANGE_LIMIT;
    }

    /**
     * Ελέγχει αν τηρείται το όριο του δημοσιονομικού ελλείμματος (3%).
     *
     * @param esoda  Το συνολικό ποσό των εσόδων.
     * @param eksoda Το συνολικό ποσό των εξόδων.
     * @return {@code true} αν το έλλειμμα είναι εντός του ορίου (<= 3%).
     */
    public static boolean deficitLimit(final double esoda,
                                       final double eksoda) {
        if (esoda >= eksoda) {
            return true;
        }
        double defPerc = ((eksoda - esoda) / esoda) * PERCENT_MULT;

        return defPerc <= MAX_DEFICIT_PERCENT;
    }
}
