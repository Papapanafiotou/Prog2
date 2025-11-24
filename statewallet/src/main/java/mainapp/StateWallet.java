package mainapp;


import java.io.BufferedInputStream;
import java.util.Scanner;



public class StateWallet {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int year;

        do {
        System.out.print("Δώσε χρονολογία (2023 έως 2026): ");
        year = scanner.nextInt(); 
        } while(year <2023 || year > 2026);

        scanner.close();
        Csvtopdf.run(year);

        BudgetImporter importer = new BudgetImporter();
        importer.importData();
    }
}
