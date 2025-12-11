package mainapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Csvtopdf {
    public static void run(int year) {
        String path ="statewallet/src/main/sources/budget" + year + ".pdf";
        String newPath = "statewallet/src/main/sources/budgettouse.pdf";
        
        try {
            
            Files.move(Paths.get(path), Paths.get(newPath), StandardCopyOption.REPLACE_EXISTING);

            System.out.print("Το αρχείο μετονομάστηκε επιτυχώς σε: ");
            System.out.println(newPath);

        } catch (Exception e) {
            System.out.println("Σφάλμα κατά τη μετονομασία: " + e.getMessage());
        }
        try {
            String scriptPath = "statewallet\\src\\scripts\\pdftocsv.py";

            ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
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
            Files.move(Paths.get(newPath), Paths.get(path), StandardCopyOption.REPLACE_EXISTING);

            System.out.print("Το αρχείο μετονομάστηκε ξανά στο αρχικό επιτυχώς σε: ");
            System.out.println(path);

        } catch (Exception e) {
            System.out.println("Σφάλμα κατά τη μετονομασία: " + e.getMessage());
        }
    }
}
