package mainapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Csvtopdf {
    public static void run(int year) {
        Path currentWorkingDir = Paths.get(".").toAbsolutePath().normalize();
        Path baseDir;
        if (Files.exists(currentWorkingDir.resolve("statewallet"))) {
            baseDir = currentWorkingDir.resolve("statewallet");
        } else {
            baseDir = currentWorkingDir;
        }

        Path sourceDir = baseDir.resolve(Paths.get("src", "main", "sources"));
        Path scriptsDir = baseDir.resolve(Paths.get("src", "scripts"));
        Path path = sourceDir.resolve("budget" + year + ".pdf");
        Path newpath = sourceDir.resolve("budgettouse.pdf");
        
        try {
            
            Files.move(path, newpath, StandardCopyOption.REPLACE_EXISTING);

            System.out.print("Το αρχείο μετονομάστηκε επιτυχώς σε: ");
            System.out.println(newpath);

        } catch (Exception e) {
            System.out.println("Σφάλμα κατά τη μετονομασία: " + e.getMessage());
        }
        try {
            Path scriptPath = scriptsDir.resolve("pdftocsv.py").toAbsolutePath();

            ProcessBuilder pb = new ProcessBuilder("python", scriptPath.toAbsolutePath().toString());
            pb.redirectErrorStream(true);

            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[PYTHON] " + line);
            }
            int exitCode = process.waitFor();
            System.out.println("Python process exited with code: " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Files.move(newpath, path, StandardCopyOption.REPLACE_EXISTING);

            System.out.print("Το αρχείο μετονομάστηκε ξανά στο αρχικό επιτυχώς σε: ");
            System.out.println(path);

        } catch (Exception e) {
            System.out.println("Σφάλμα κατά τη μετονομασία: " + e.getMessage());
        }
    }
}
