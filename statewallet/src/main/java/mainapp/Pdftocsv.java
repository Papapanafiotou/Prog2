package mainapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Διαχειρίζεται τη μετατροπή PDF σε CSV καλώντας εξωτερικό script Python.
 */
public final class Pdftocsv {

    private Pdftocsv() {
        // Utility class
    }

    /**
     * Εκτελεί τη διαδικασία μετατροπής για το επιλεγμένο έτος.
     *
     * Επειδή χρησιμοποιούμε python script, δεν μπορεί να πάρει ορίσμτα.
     * Για τον λόγο αυτό, μετονομάζουμε το αντίστοιχο PDF σε budgettouse.
     * Έτσι η python πάντα επεξεργάζεται το PDF με το συγκεκριμένο όνομα.
     *
     * @param year Το έτος προϋπολογισμού.
     */
    public static void run(final int year) {
        /*  Εξέταση των Paths ωστε το πρόγραμμα να τρέχει
            είτε απο το root, είτε απο άλλο module. */
        Path currentWorkingDir = Paths.get(".").toAbsolutePath().normalize();
        Path baseDir;
        if (Files.exists(currentWorkingDir.resolve("statewallet"))) {
            baseDir = currentWorkingDir.resolve("statewallet");
        } else {
            baseDir = currentWorkingDir;
        }
        /* Δημιουργία τελικής διαδρομής για τους φακέλους */
        Path sourceDir = baseDir.resolve(Paths.get("src", "main", "sources"));
        Path scriptsDir = baseDir.resolve(Paths.get("src", "scripts"));
        Path path = sourceDir.resolve("budget" + year + ".pdf");
        Path newpath = sourceDir.resolve("budgettouse.pdf");
        /* Μετονομασία του αντίστοιχου PDF ωστε να περάσει στην python */
        try {
            Files.move(path, newpath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Το αρχείο μετονομάστηκε επιτυχώς σε: "
                    + newpath);
        } catch (Exception e) {
            System.out.println("Σφάλμα κατά τη μετονομασία: "
                    + e.getMessage());
        }
        /* Εκτέλεση του Python script. */
        try {
            Path scriptPath = scriptsDir.resolve("pdftocsv.py")
                    .toAbsolutePath();
            ProcessBuilder pb = new ProcessBuilder(
                    "python", scriptPath.toAbsolutePath().toString());
            pb.redirectErrorStream(true);

            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("[PYTHON] " + line);
                }
            }
            int exitCode = process.waitFor();
            System.out.println("Python process exited with code: " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /* Επιστροφή PDF στην αρχική του ονομασία. */
        try {
            Files.move(newpath, path, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Το αρχείο μετονομάστηκε ξανά επιτυχώς σε: "
                    + path);
        } catch (Exception e) {
            System.out.println("Σφάλμα κατά τη μετονομασία: "
                    + e.getMessage());
        }
    }
}
