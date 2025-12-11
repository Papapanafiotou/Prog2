package mainapp;
import java.io.File;

public class DatabaseFinder {
    public boolean findYearbase(int year) {
        String dbName = "budget_" + year + ".db";
        File dbFile = new File(dbName);
        boolean dbExists = dbFile.exists();
        if (dbExists) {
            System.out.println("Η βάση δεδομένων '" + dbName + "' βρέθηκε.");
            return true;
        } else {
            System.out.println(">> Η βάση δεδομένων δεν υπάρχει. Δημιουργία νέας: '" + dbName + "'...");
            return false;
        }
    }
}
