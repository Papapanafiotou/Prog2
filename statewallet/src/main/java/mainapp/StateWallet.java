package mainapp;


import java.io.BufferedReader;
import java.io.InputStreamReader;

public class StateWallet {
    public static void main(String[] args) {
        try {
            String scriptPath = "Prog2\\statewallet\\src\\scripts\\pdftocsv.py";

            ProcessBuilder pb = new ProcessBuilder("python", scriptPath);
            pb.redirectErrorStream(true);

            Process process = pb.start();
            BufferedReader reader = new BufferedReader(
            new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[PYTHON] " + line);
            }
            int exitCode = process.waitFor();
            System.out.println("Python process exited with code: " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
