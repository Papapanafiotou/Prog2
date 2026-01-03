package mainapp;

import java.util.Scanner;

/**
 * Βοηθητική κλάση για ελέγχους περιορισμών (constraints).
 */
public final class Constrains {

    private Constrains() {
        // Utility class
    }

    /**
     * Ελέγχει αν ένα ποσό είναι αρνητικό και ζητά επιβεβαίωση.
     *
     * @param scanner Το αντικείμενο Scanner για είσοδο.
     * @param amount  Το ποσό προς έλεγχο.
     * @return Το τελικό ποσό (ή το παλιό αν ακυρωθεί).
     */
    public static double negativeAmount(final Scanner scanner,
                                        final double amount) {
        if (amount < 0) {
            System.out.println("Το ποσό είναι αρνητικό. Συνέχεια; "
                    + "(1 για ΝΑΙ, άλλο για ΟΧΙ)");
            int answer = scanner.nextInt();
            scanner.nextLine();
            if (answer != 1) {
                System.out.println("Ακύρωση αλλαγής.");
                return 0; // Ή κάποια άλλη λογική ακύρωσης
            }
        }
        return amount;
    }
}
