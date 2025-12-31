package mainapp;

import java.io.File;

/**
 * Βοηθητική κλάση για τον εντοπισμό αρχείων βάσης δεδομένων.
 */
public final class DatabaseFinder {

    /**
     * Ελέγχει αν υπάρχει το αρχείο βάσης για το συγκεκριμένο έτος.
     *
     * @param year Το έτος προς έλεγχο.
     * @return true αν υπάρχει το αρχείο, false διαφορετικά.
     */
    public boolean findYearbase(final int year) {
        String dbName = "budget_" + year + ".db";
        File dbFile = new File(dbName);
        return dbFile.exists();
    }
}
