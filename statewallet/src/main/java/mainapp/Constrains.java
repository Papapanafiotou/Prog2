package mainapp;

import java.util.Scanner;

public class Constrains {
    // Έλεγχος αν ο αριθμός είναι θετικός //
    public static double negativeAmount(Scanner scanner, double amount){
        while (amount < 0) {
                System.out.println("ΣΦΑΛΜΑ: Το ποσό δεν μπορεί να είναι αρνητικό.");
                System.out.print("Δώσε το νέο ποσό: ");
                // Ελεγχος αν ο χρηστης εδωσε αριθμο //
                try {
                    amount = Double.parseDouble(scanner.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Πρέπει να δώσετε αριθμό.");
                    amount = -1; 
                }
            }
        return amount;
    }

    public static boolean isReasonableChange(double original_amount, double newAmount){
        if(original_amount == 0) {
            return true;
        }
        double PercentChange = Math.abs((original_amount - newAmount) / original_amount);     
        if (PercentChange >= 0.5) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean deficitLimit(double esoda, double eksoda){
        double defperc = ((esoda - eksoda) / esoda) * 100;

        if (defperc >= 3){
            return false;
        } else {
            return true;
        }
        
    }
}
