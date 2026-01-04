package mainapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AiBridge {

    // Μέθοδος που ψάχνει παντού για να βρει το αρχείο Python
    private File findScript() {
        String currentDir = System.getProperty("user.dir");
        System.out.println("📂 Φάκελος Εργασίας: " + currentDir);
        
        File folder = new File(currentDir);
        File[] listOfFiles = folder.listFiles();
        
        System.out.println("--- ΤΙ ΒΛΕΠΕΙ Η JAVA ΜΕΣΑ ΣΤΟΝ ΦΑΚΕΛΟ ---");
        boolean found = false;
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                // Τυπώνουμε το όνομα του αρχείου ανάμεσα σε αγκύλες [] για να δούμε αν υπάρχουν κενά
                System.out.println("[" + file.getName() + "]");
                
                // Έλεγχος αν μοιάζει με το δικό μας
                if (file.getName().contains("budget_brain")) {
                    System.out.println("   >>> ΒΡΗΚΑ ΑΥΤΟ: " + file.getName() + " (Είναι αυτό που ψάχνουμε;)");
                }
                
                if (file.getName().equals("budget_brain.py")) {
                    found = true;
                }
            }
        } else {
            System.out.println("ΣΦΑΛΜΑ: Η Java δεν μπορεί να διαβάσει τα περιεχόμενα του φακέλου (null)!");
        }
        System.out.println("-------------------------------------------");

        if (found) {
            return new File(currentDir, "budget_brain.py");
        }
        
        // Αν δεν το βρήκε στο root, ψάχνουμε στα backup locations
        String[] possibleLocations = {
            "src/budget_brain.py",
            "src/main/java/mainapp/budget_brain.py"
        };

        for (String loc : possibleLocations) {
            File f = new File(currentDir, loc);
            if (f.exists()) return f;
        }

        return null; 
    }

    private String runPythonScript(String... args) {
        try {
            // Καλόυμε τον ανιχνευτή
            File scriptFile = findScript();

            if (scriptFile == null) {
                return "ΣΦΑΛΜΑ: Το αρχείο 'budget_brain.py' δεν βρέθηκε πουθενά μέσα στον φάκελο " + System.getProperty("user.dir");
            }

            List<String> command = new ArrayList<>();
            String os = System.getProperty("os.name").toLowerCase();
            command.add(os.contains("win") ? "python" : "python3");
            
            // Δίνουμε την απόλυτη διαδρομή που μόλις βρήκαμε
            command.add(scriptFile.getAbsolutePath()); 
            
            for (String arg : args) {
                command.add(arg);
            }

            ProcessBuilder pb = new ProcessBuilder(command);
            
            // Κρίσιμο για να βρει η Python το api_key.txt:
            // Ορίζουμε ως "φάκελο εργασίας" τον φάκελο που βρίσκεται το script
            pb.directory(scriptFile.getParentFile()); 

            pb.redirectErrorStream(true);
            Process process = pb.start();

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

    public String getSpecificAdvice(String name, double amount, String goal) {
        return runPythonScript("specific", name, String.valueOf(amount), goal);
    }

    public String getGlobalStrategy(String dbUrl, String goal) {
        return runPythonScript("global", dbUrl, goal);
    }
}