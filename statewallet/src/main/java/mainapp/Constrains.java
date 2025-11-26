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

}
