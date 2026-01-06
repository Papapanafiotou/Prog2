package mainapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AiBridge {

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

    public String getSpecificAdvice(String dbPath, String name, double amount, String goal) {
        return runPythonScript(goal, "specific", dbPath, name, String.valueOf(amount));
    }

    public String getGlobalStrategy(String dbUrl, String goal) {
        return runPythonScript(goal, "global", dbUrl);
    }
}