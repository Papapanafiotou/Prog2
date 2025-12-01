package mainapp;

import java.util.Scanner;

public class Weights {
    Scanner scan = new Scanner(System.in);
    //Μέθοδος για την εισαγωγή βαρών για τον υπολογισμο των βαθμών
    public double getWeight() {
        boolean flag = false;
        double weight;
        do {
            System.out.println("Παρακαλώ εισάγετε το"
             + " ποσοστό επίδρασης στον βαθμό");
            weight = scan.nextDouble();
            if (weight < 0 || weight > 1) {
                System.out.println("Παρακαλώ εισάγετε αριθμό"
                + " μεταξύ του 0 και του 1");
            } else {
                flag =true;
            } 
        } while (flag = false);
        return weight;
    }
}