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
 * Η κλάση {@code AiBridge} λειτουργεί ως γέφυρα διασύνδεσης μεταξύ της Java εφαρμογής
 * και του εξωτερικού Python script ({@code budget_brain.py}).
 * <p>
 * Σκοπός της είναι να εκτελεί το Python script ως ξεχωριστή διεργασία του λειτουργικού συστήματος,
 * να μεταφέρει τα δεδομένα και τα ερωτήματα του χρήστη με ασφαλή κωδικοποίηση (UTF-8)
 * και να επιστρέφει την απάντηση του AI στην κονσόλα της Java.
 * </p>
 */
public class AiBridge {

    /**
     * Εντοπίζει το αρχείο του Python script (`budget_brain.py`) ελέγχοντας διάφορες
     * πιθανές τοποθεσίες στο σύστημα αρχείων.
     * <p>
     * Αυτή η μέθοδος εξασφαλίζει ότι η εφαρμογή θα τρέξει σωστά είτε εκτελείται
     * μέσα από ένα IDE είτε ως αυτόνομο πρόγραμμα.
     * </p>
     *
     * @return Το αντικείμενο {@code File} του script αν βρεθεί, διαφορετικά {@code null}.
     */
    private File findScript() {
        String currentDir = System.getProperty("user.dir");
        
        // Λίστα πιθανών τοποθεσιών
        String[] possibleLocations = {
            "budget_brain.py",
            "statewallet/budget_brain.py",
            "src/budget_brain.py",
            "src/main/java/mainapp/budget_brain.py"
        };

        // Έλεγχος στο root
        if (new File(currentDir, "budget_brain.py").exists()) {
             return new File(currentDir, "budget_brain.py");
        }

        // Έλεγχος σε υποφακέλους
        for (String loc : possibleLocations) {
            File f = new File(currentDir, loc);
            if (f.exists()) return f;
        }
        return null; 
    }

    /**
     * Η κεντρική μέθοδος που εκτελεί το Python script και διαχειρίζεται την επικοινωνία I/O.
     * <p>
     * Χρησιμοποιεί την κλάση {@link ProcessBuilder} για να δημιουργήσει τη διεργασία.
     * Δίνει ιδιαίτερη έμφαση στη σωστή κωδικοποίηση χαρακτήρων (UTF-8) τόσο κατά
     * την αποστολή του στόχου (goal) όσο και κατά την ανάγνωση της απάντησης,
     * ώστε να υποστηρίζονται πλήρως τα Ελληνικά.
     * </p>
     *
     * @param goal Το κείμενο με τον στόχο ή το ερώτημα που έθεσε ο χρήστης.
     * @param args Επιπλέον ορίσματα γραμμής εντολών (π.χ. λειτουργία "specific" ή "global", μονοπάτια αρχείων κ.λπ.).
     * @return Το κείμενο της απάντησης που παρήγαγε το Python script (stdout).
     */
    private String runPythonScript(String goal, String... args) {
        try {
            File scriptFile = findScript();
            if (scriptFile == null) return "ΣΦΑΛΜΑ: Δεν βρέθηκε το budget_brain.py";

            List<String> command = new ArrayList<>();
            String os = System.getProperty("os.name").toLowerCase();
            command.add(os.contains("win") ? "python" : "python3");
            
            // Επιβάλλουμε UTF-8 mode στην Python για να μην μπερδευτεί με το output
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
            pb.environment().put("PYTHONIOENCODING", "utf-8");
            
            pb.redirectErrorStream(true);
            
            // Ξεκινάμε την Python
            Process process = pb.start();

            // --- ΣΗΜΑΝΤΙΚΟ ---
            // Στέλνουμε τον στόχο (goal) απευθείας στην Python μέσω "σωλήνα" (OutputStream).
            // Χρησιμοποιούμε UTF-8 εδώ, οπότε αφού η Java έχει το σωστό κείμενο (λόγω CP737),
            // θα φτάσει σωστά και στην Python.
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8))) {
                writer.write(goal);
                writer.flush(); // Στέλνουμε τα δεδομένα τώρα
            }
            // -----------------

            // Διαβάζουμε την απάντηση
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8)
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
     * Ζητάει συμβουλή από το AI για μία συγκεκριμένη εγγραφή του προϋπολογισμού.
     * * @param dbPath Η διαδρομή (path) της βάσης δεδομένων.
     * @param name Το όνομα/περιγραφή της εγγραφής (π.χ. "Φόρος Εισοδήματος").
     * @param amount Το τρέχον ποσό της εγγραφής.
     * @param goal Ο στόχος που θέλει να πετύχει ο χρήστης.
     * @return Η απάντηση του AI ως συμβολοσειρά.
     */
    public String getSpecificAdvice(String dbPath, String name, double amount, String goal) {
        return runPythonScript(goal, "specific", dbPath, name, String.valueOf(amount));
    }

    /**
     * Ζητάει μια συνολική στρατηγική από το AI μελετώντας ολόκληρη τη βάση δεδομένων.
     * * @param dbUrl Το URL σύνδεσης με τη βάση (π.χ. jdbc:sqlite:...).
     * @param goal Το γενικό όραμα ή ο στόχος του χρήστη.
     * @return Η στρατηγική ανάλυση του AI ως συμβολοσειρά.
     */
    public String getGlobalStrategy(String dbUrl, String goal) {
        return runPythonScript(goal, "global", dbUrl);
    }
}