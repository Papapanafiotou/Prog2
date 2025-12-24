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
    public double[] showTotalWeights(double[] a, double w1, double w2, double w3) {
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
            return totalPercentage;
        }
        public void getAllGrades(double[] a) {
            int[] years = {2020, 2021, 2022, 2023, 2024};
            double[] grades = new double[5];
            for (int year : years) {
                DataforGrade d = new DataforGrade();
                double[] data = d.getData(year); 
                int[] elemGrade = new int[10];
                EnvElemGrades e = new EnvElemGrades();
                EconElemGrades ec = new EconElemGrades();
                SocElemGrades s = new SocElemGrades();
                elemGrade[0] = ec.getGDPGrowthGrade(data[0]);
                elemGrade[1] = ec.getpublicDebtGrade(data[1]);
                elemGrade[2] = ec.getSurplusGrade(data[2]);
                elemGrade[3] = e.getRESGrade(data[3]);
                elemGrade[4] = e.getRecycleGrade(data[4]);
                elemGrade[5] = e.getEmmisionGrade(data[5]);
                elemGrade[6] = s.getGINIGrade(data[6]);
                elemGrade[7] = s.getHealthEduGrade(data[7]);
                elemGrade[8] = s.getMentalHealthGrade(data[8]);
                elemGrade[9] = s.getCrimeGrade(data[9]);
                double totalGrade = 0;
                for (int i = 0; i < 10; i++) {
                    totalGrade += (elemGrade[i] * a[i]);
                }
                grades[year] = totalGrade;
            }
            double[] differencies = new double[4];
            differencies[0] = grades[1] - grades[0];
            differencies[1] = grades[2] - grades[1];
            differencies[2] = grades[3] - grades[2];
            differencies[3] = grades[4] - grades[3]; 
        } 
}