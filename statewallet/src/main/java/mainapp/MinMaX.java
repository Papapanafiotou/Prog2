package mainapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Υπολογίζει και εμφανίζει τις ελάχιστες και μέγιστες τιμές από τους
 * πίνακες της βάσης δεδομένων.
 */
public final class MinMaX {

    // Σταθερές Επιλογών
    /** Επιλογή για εύρεση ελαχίστου. */
    private static final int OPTION_MIN = 1;
    /** Επιλογή για εύρεση μεγίστου. */
    private static final int OPTION_MAX = 2;

    // Σταθερές Κατηγοριών
    /** Κατηγορία Εσόδων. */
    private static final int CAT_INCOME = 1;
    /** Κατηγορία Εξόδων. */
    private static final int CAT_EXPENSE = 2;
    /** Κατηγορία Υπουργείων. */
    private static final int CAT_MINISTRY = 3;

    /** Το URL της βάσης δεδομένων. */
    private final String databaseUrl;

    /**
     * Κατασκευαστής.
     *
     * @param url Το URL της βάσης δεδομένων.
     */
    public MinMaX(final String url) {
        this.databaseUrl = url;
    }

    /**
     * Ρωτά τον χρήστη αν θέλει να βρει ελάχιστο ή μέγιστο και για ποια
     * κατηγορία, και εμφανίζει το αποτέλεσμα.
     */
    public void showMinMax() {
        Scanner scan = new Scanner(System.in, "CP737");
        Search s = new Search(databaseUrl);

        int operation = 0;
        boolean validOp = false;

        // 1. Επιλογή Λειτουργίας (Min/Max)
        while (!validOp) {
            System.out.println("Θα θέλατε να υπολογίσετε μέγιστο ή ελάχιστο; "
                    + "(1 για ελάχιστο - 2 για μέγιστο)");
            if (scan.hasNextInt()) {
                operation = scan.nextInt();
                scan.nextLine(); // Clear buffer
                if (operation == OPTION_MIN || operation == OPTION_MAX) {
                    validOp = true;
                } else {
                    System.out.println("Μη έγκυρη επιλογή. Δώστε 1 ή 2.");
                }
            } else {
                System.out.println("Παρακαλώ εισάγετε αριθμό.");
                scan.next(); // Clear invalid input
            }
        }

        int category = 0;
        boolean validCat = false;

        // 2. Επιλογή Κατηγορίας
        while (!validCat) {
            System.out.println("Επιλέξτε κατηγορία:");
            System.out.println("1. Έσοδα");
            System.out.println("2. Έξοδα");
            System.out.println("3. Δαπάνη Υπουργείου");
            System.out.print("Επιλογή: ");

            if (scan.hasNextInt()) {
                category = scan.nextInt();
                scan.nextLine();
                if (category >= CAT_INCOME && category <= CAT_MINISTRY) {
                    validCat = true;
                } else {
                    System.out.println("Μη έγκυρη επιλογή. Δώστε 1, 2 ή 3.");
                }
            } else {
                System.out.println("Παρακαλώ εισάγετε αριθμό.");
                scan.next();
            }
        }

        // 3. Εκτέλεση και Εμφάνιση
        double rawValue = getMinMax(operation, category);
        long value = (long) rawValue;
        String name = s.searchString(rawValue);

        String opText = (operation == OPTION_MIN) ? "ελάχιστο" : "μέγιστο";
        String catText = "";

        if (category == CAT_INCOME) {
            catText = "έσοδο";
        } else if (category == CAT_EXPENSE) {
            catText = "έξοδο";
        } else {
            catText = "ποσό σε υπουργείο";
        }

        System.out.println("Το " + opText + " " + catText + " είναι το "
                + name + " με ποσό " + value);
    }

    /**
     * Εκτελεί το SQL query για την εύρεση min/max.
     *
     * @param opType  Ο τύπος πράξης (1=MIN, 2=MAX).
     * @param catType Η κατηγορία (1=Έσοδα, 2=Έξοδα, 3=Υπουργεία).
     * @return Η τιμή που βρέθηκε.
     */
    public double getMinMax(final int opType, final int catType) {
        String table = "";
        String function = (opType == OPTION_MIN) ? "MIN" : "MAX";

        if (catType == CAT_INCOME) {
            table = "esoda";
        } else if (catType == CAT_EXPENSE) {
            table = "eksoda";
        } else if (catType == CAT_MINISTRY) {
            table = "ypourgeia";
        }

        String sql = "SELECT " + function + "(amount) AS value FROM " + table;
        double value = 0;

        try (Connection conn = DriverManager.getConnection(databaseUrl);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                value = rs.getDouble("value");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }
}
