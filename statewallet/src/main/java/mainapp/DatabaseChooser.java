package mainapp;

import java.util.Scanner;

/**
 * Επιτρέπει στον χρήστη να επιλέξει το έτος της βάσης δεδομένων.
 */
public final class DatabaseChooser {

    /** Ελάχιστο επιτρεπτό έτος. */
    private static final int MIN_YEAR = 2023;
    /** Μέγιστο επιτρεπτό έτος. */
    private static final int MAX_YEAR = 2026;

    /**
     * Ζητάει από τον χρήστη έτος και επιστρέφει το URL της βάσης.
     * Αν δεν υπάρχει βάση, εκκινεί τη διαδικασία δημιουργίας.
     *
     * @return Το JDBC URL της βάσης δεδομένων.
     */
    public String getUrl() {
        Scanner scanner = new Scanner(System.in);
        int year;

        do {
            System.out.print("Δώσε χρονολογία (2023 έως 2026): ");
            year = scanner.nextInt();
            scanner.nextLine();
        } while (year < MIN_YEAR || year > MAX_YEAR);

        String databaseUrl = "jdbc:sqlite:budget_" + year + ".db";

        DatabaseFinder finder = new DatabaseFinder();
        boolean databaseExists = finder.findYearbase(year);
        if (!databaseExists) {
            Pdftocsv.run(year);
            PinakesImporter importer = new PinakesImporter(databaseUrl);
            importer.importAll();
        } else {
            System.out.println("Έχει γίνει επεξεργασία. "
                    + "Επανεκκίνηση; (1 για ΝΑΙ - 2 για ΟΧΙ)");
            int answer = scanner.nextInt();
            scanner.nextLine();
            if (answer == 1) {
                System.out.println("Έγινε διαγραφή των παλιών στοιχείων");
                Pdftocsv.run(year);
                PinakesImporter importer = new PinakesImporter(databaseUrl);
                importer.importAll();
            }
        }
        return databaseUrl;
    }
}
