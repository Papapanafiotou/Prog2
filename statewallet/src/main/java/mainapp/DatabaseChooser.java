package mainapp;

import java.util.Scanner;

public class DatabaseChooser {
    public String getURL() { 
        Scanner scanner = new Scanner(System.in);
        int year;
                
        do {
            System.out.print("Δώσε χρονολογία (2023 έως 2026): ");
            year = scanner.nextInt();
            scanner.nextLine();
            } while(year <2023 || year > 2026);
                String DATABASE_URL = "jdbc:sqlite:budget_" + year + ".db";

                DatabaseFinder finder = new DatabaseFinder();
                boolean DatabaseExists = finder.findYearbase(year);
                if (!DatabaseExists) {
                    Csvtopdf.run(year);
                    PinakesImporter importer = new PinakesImporter(DATABASE_URL);
                    importer.importAll(); 
                } else {
                    System.out.println("Έχει γίνει επεξεργασία του συγκεκριμένου έτους στο παρελθόν. Θέλετε να ξεκινήσετε από την αρχή; (1 για ΝΑΙ --- 2 για ΟΧΙ)");
                    int answer = scanner.nextInt();
                    scanner.nextLine();
                    if (answer == 1) {
                        System.out.println("Έγινε διαγραφή των παλιών στοιχείων");
                        Csvtopdf.run(year);
                        PinakesImporter importer = new PinakesImporter(DATABASE_URL);
                        importer.importAll();
                    }
            }
        return DATABASE_URL;
    }
}
