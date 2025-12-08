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
/* Μέθοδος υπολογισμού και εμφάνισης των ποσοστών επίδρασης όλων των στοιχείων
που συμμετέχουν στον υπολογισμό του τελικού βαθμού*/  
    public void showTotalWeights(double[] a, double w1, double w2, double w3) {
        double[] totalPercentage = new double[10];
        for(int i = 0; i < 10; i++) {
            if (i < 3) {
                totalPercentage[i] = w1 * a[i];
            } else if (i < 6) {
                totalPercentage[i] = w2 * a[i];
            } else {
                totalPercentage[i] = w3 * a[i];
            }
        }
            String[] names = {
               "gdpGrowth",
               "publicDebt",
               "surplus",
               "res",
               "recycleRate",
               "emmisionsDiff",
               "gini",
               "eduHealthExp",
               "mentalHealthPer",
               "crimeRateDiff"
            };
            System.out.println("Τα επιμέρους στοιχεία που χρησιμοποιήθηκαν "
                + "για τον υπολογισμό του τελικού βαθμού και οι τελικές " +
                "ποσοστιαίες επιδράσεις τους στον τελικό βαθμό είναι: "
            );
            for (int i = 0; i < 10; i++) {
                System.out.println("Στοιχείο: " + names[i] +
                " Ποσοστό: " + totalPercentage[i] );
            }
    } 
}