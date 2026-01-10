package mainapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Η κλάση {@code AiBridge} λειτουργεί ως γέφυρα διασύνδεσης μεταξύ της Java
 * εφαρμογής και του εξωτερικού Python script ({@code budget_brain.py}).
 * <p>
 * Σκοπός της είναι να εκτελεί το Python script ως ξεχωριστή διεργασία του
 * λειτουργικού συστήματος, να μεταφέρει τα δεδομένα και τα ερωτήματα του χρήστη
 * με ασφαλή κωδικοποίηση (UTF-8) και να επιστρέφει την απάντηση του AI στην
 * κονσόλα της Java.
 * </p>
 */
public final class AiBridge {

    // Ονόματα αρχείων και εντολών
    /** Το όνομα του αρχείου Python script. */
    private static final String SCRIPT_NAME = "budget_brain.py";
    /** Η εντολή εκτέλεσης Python στα Windows. */
    private static final String PYTHON_CMD_WIN = "python";
    /** Η εντολή εκτέλεσης Python σε Unix/Linux/Mac. */
    private static final String PYTHON_CMD_UNIX = "python3";
    /** Η κωδικοποίηση χαρακτήρων για την επικοινωνία (UTF-8). */
    private static final String ENCODING_UTF8 = "utf-8";
    /** Λειτουργία για συγκεκριμένο λογαριασμό. */
    private static final String MODE_SPECIFIC = "specific";
    /** Λειτουργία για γενική στρατηγική. */
    private static final String MODE_GLOBAL = "global";

    /**
     * Εντοπίζει το αρχείο του Python script (`budget_brain.py`) ελέγχοντας
     * διάφορες πιθανές τοποθεσίες στο σύστημα αρχείων.
     * <p>
     * Αυτή η μέθοδος εξασφαλίζει ότι η εφαρμογή θα τρέξει σωστά είτε εκτελείται
     * μέσα από ένα IDE είτε ως αυτόνομο πρόγραμμα.
     * </p>
     *
     * @return Το αντικείμενο {@code File} του script αν βρεθεί, διαφορετικά
     * {@code null}.
     */
    private File findScript() {
        String currentDir = System.getProperty("user.dir");

        // Λίστα πιθανών τοποθεσιών
        String[] possibleLocations = {
            SCRIPT_NAME,
            "statewallet/" + SCRIPT_NAME,
            "src/" + SCRIPT_NAME,
            "src/main/java/mainapp/" + SCRIPT_NAME
        };

        // Έλεγχος στο root
        if (new File(currentDir, SCRIPT_NAME).exists()) {
            return new File(currentDir, SCRIPT_NAME);
        }

        // Έλεγχος σε υποφακέλους
        for (String loc : possibleLocations) {
            File f = new File(currentDir, loc);
            if (f.exists()) {
                return f;
            }
        }
        return null;
    }

    /**
     * Η κεντρική μέθοδος που εκτελεί το Python script και διαχειρίζεται την
     * επικοινωνία I/O.
     * <p>
     * Χρησιμοποιεί την κλάση {@link ProcessBuilder} για να δημιουργήσει τη
     * διεργασία. Δίνει ιδιαίτερη έμφαση στη σωστή κωδικοποίηση χαρακτήρων
     * (UTF-8) τόσο κατά την αποστολή του στόχου (goal) όσο και κατά την
     * ανάγνωση της απάντησης, ώστε να υποστηρίζονται πλήρως τα Ελληνικά.
     * </p>
     *
     * @param goal Το κείμενο με τον στόχο ή το ερώτημα που έθεσε ο χρήστης.
     * @param args Επιπλέον ορίσματα γραμμής εντολών.
     * @return Το κείμενο της απάντησης που παρήγαγε το Python script (stdout).
     */
    private String runPythonScript(final String goal, final String... args) {
        try {
            File scriptFile = findScript();
            if (scriptFile == null) {
                return "ΣΦΑΛΜΑ: Δεν βρέθηκε το " + SCRIPT_NAME;
            }

            List<String> command = new ArrayList<>();
            String os = System.getProperty("os.name").toLowerCase();
            command.add(os.contains("win") ? PYTHON_CMD_WIN : PYTHON_CMD_UNIX);

            // Επιβάλλουμε UTF-8 mode στην Python
            command.add("-X");
            command.add("utf8");

            command.add(scriptFile.getAbsolutePath());

            for (String arg : args) {
                command.add(arg);
            }

            ProcessBuilder pb = new ProcessBuilder(command);

            if (scriptFile.getParentFile() != null) {
                pb.directory(scriptFile.getParentFile());
            }

            // Environment variables για πλήρη υποστήριξη UTF-8
            pb.environment().put("PYTHONIOENCODING", ENCODING_UTF8);

            pb.redirectErrorStream(true);

            // Ξεκινάμε την Python
            Process process = pb.start();

            // --- ΣΗΜΑΝΤΙΚΟ ---
            // Στέλνουμε τον στόχο (goal) απευθείας στην Python μέσω
            // "σωλήνα" (OutputStream). Χρησιμοποιούμε UTF-8 εδώ.
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(process.getOutputStream(),
                            StandardCharsets.UTF_8))) {
                writer.write(goal);
                writer.flush(); // Στέλνουμε τα δεδομένα τώρα
            }
            // -----------------

            // Διαβάζουμε την απάντηση
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(),
                            StandardCharsets.UTF_8)
            );

            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            process.waitFor();
            return output.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Εξαίρεση Java: " + e.getMessage();
        }
    }

    /**
     * Ζητάει συμβουλή από το AI για μία συγκεκριμένη εγγραφή του
     * προϋπολογισμού.
     *
     * @param dbPath Η διαδρομή (path) της βάσης δεδομένων.
     * @param name   Το όνομα/περιγραφή της εγγραφής (π.χ. "Φόρος Εισοδήματος").
     * @param amount Το τρέχον ποσό της εγγραφής.
     * @param goal   Ο στόχος που θέλει να πετύχει ο χρήστης.
     * @return Η απάντηση του AI ως συμβολοσειρά.
     */
    public String getSpecificAdvice(final String dbPath, final String name,
                                    final double amount, final String goal) {
        return runPythonScript(goal, MODE_SPECIFIC, dbPath, name,
                String.valueOf(amount));
    }

    /**
     * Ζητάει μια συνολική στρατηγική από το AI μελετώντας ολόκληρη τη βάση
     * δεδομένων.
     *
     * @param dbUrl Το URL σύνδεσης με τη βάση (π.χ. jdbc:sqlite:...).
     * @param goal  Το γενικό όραμα ή ο στόχος του χρήστη.
     * @return Η στρατηγική ανάλυση του AI ως συμβολοσειρά.
     */
    public String getGlobalStrategy(final String dbUrl, final String goal) {
        return runPythonScript(goal, MODE_GLOBAL, dbUrl);
    }
}
