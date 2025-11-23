package mainapp;

import java.nio.file.*;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class StateWallet {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Δώσε χρονολογία (π.χ. 2025): ");
        int year = scanner.nextInt();
        scanner.close();
        String path ="Prog2/statewallet/src/main/sources/budget" + year + ".pdf";
        String newPath = "Prog2/statewallet/src/main/sources/budgettouse.pdf";
        try {
            
            Files.move(Paths.get(path), Paths.get(newPath), StandardCopyOption.REPLACE_EXISTING);

            System.out.print("Το αρχείο μετονομάστηκε επιτυχώς σε: ");
            System.out.println(newPath);

        } catch (Exception e) {
            System.out.println("Σφάλμα κατά τη μετονομασία: " + e.getMessage());
        }
        try {
            String scriptPath = "Prog2\\statewallet\\src\\scripts\\pdftocsv.py";

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

            System.out.print("Το αρχείο μετονομάστηκε ξανό στο αρχικό επιτυχώς σε: ");
            System.out.println(path);

        } catch (Exception e) {
            System.out.println("Σφάλμα κατά τη μετονομασία: " + e.getMessage());
        }
    }
}
