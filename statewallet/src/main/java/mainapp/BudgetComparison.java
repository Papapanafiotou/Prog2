package mainapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class MinistryData {
    String name;
    double amount2025;
    double amount2026;

    public MinistryData(String name, double amount2025, double amount2026) {
        this.name = name;
        this.amount2025 = amount2025;
        this.amount2026 = amount2026;
    }
}

public class BudgetComparison {

    public static void main(String[] args) {
        // 1. Φόρτωση Mock Δεδομένων (Προσομοίωση ανάγνωσης από τη βάση για 2 έτη)
        // Χρησιμοποιώ δεδομένα από το PDF που ανέβασες για το 2026 και τυχαία για το 2025
        List<MinistryData> ministries = new ArrayList<>();
        
        ministries.add(new MinistryData("Υπ. Παιδείας", 5500000000.00, 6763933000.00));
        ministries.add(new MinistryData("Υπ. Υγείας", 7200000000.00, 7841945000.00));
        ministries.add(new MinistryData("Υπ. Άμυνας", 6800000000.00, 7063272000.00));
        ministries.add(new MinistryData("Υπ. Πολιτισμού", 600000000.00, 653109000.00));
        ministries.add(new MinistryData("Προεδρία Δημ.", 5200000.00, 4951000.00)); // Παράδειγμα μείωσης

        // 2. Εκτέλεση Σύγκρισης
        System.out.println("==================================================================================");
        System.out.println("                         ΣΥΓΚΡΙΣΗ ΠΡΟΥΠΟΛΟΓΙΣΜΩΝ (2025 vs 2026)                   ");
        System.out.println("==================================================================================");
        
        System.out.printf("%-20s | %-15s | %-15s | %-12s | %-8s%n", 
                "ΦΟΡΕΑΣ", "ΠΟΣΟ 2025 ($)", "ΠΟΣΟ 2026 ($)", "ΔΙΑΦΟΡΑ ($)", "ΜΕΤΑΒΟΛΗ");
        System.out.println("----------------------------------------------------------------------------------");

        double totalDiff = 0;

        for (MinistryData min : ministries) {
            double diff = min.amount2026 - min.amount2025;
            double percent = (diff / min.amount2025) * 100;
            
            totalDiff += diff;

            // Επιλογή προσήμου για εμφάνιση
            String sign = (diff > 0) ? "+" : "";
            
            System.out.printf("%-20s | %,15.0f | %,15.0f | %s%,11.0f | %s%.2f%%%n", 
                    min.name, 
                    min.amount2025, 
                    min.amount2026, 
                    sign, diff, 
                    sign, percent);
        }

        System.out.println("----------------------------------------------------------------------------------");
        System.out.printf("%-20s                                         ΣΥΝΟΛΙΚΗ ΑΥΞΗΣΗ: %,15.0f $%n", "ΣΥΝΟΛΟ", totalDiff);
    }
}