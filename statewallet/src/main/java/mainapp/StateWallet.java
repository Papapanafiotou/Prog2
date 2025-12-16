package mainapp;


import java.util.Scanner;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;



public class StateWallet {
    
    @SuppressWarnings({"CallToPrintStackTrace", "UseSpecificCatch"})
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {   // τρεχουμε τη statewalletlauncher
            StateWalletLauncher launcher = new StateWalletLauncher();
            launcher.setVisible(true);
        });

        try (Scanner scanner = new Scanner(System.in)) { // χρηση try-with για να κλεισει αυτοματα το scanner //
            int year;
            
            do {
                System.out.print("Δώσε χρονολογία (2023 έως 2026): ");
                year = scanner.nextInt();
            } while(year <2023 || year > 2026);
            final String DATABASE_URL = "jdbc:sqlite:budget.db";
            PinakesImporter importer = new PinakesImporter(DATABASE_URL);
            Csvtopdf.run(year);
            importer.importAll();
            
            BudgetMenu budgetmenu = new BudgetMenu();
            
            // 2. Εκκίνηση της εφαρμογής
            budgetmenu.start();
        }
    }
}

