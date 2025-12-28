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
            int[] years = {2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025};
            double[] grades = new double[8];
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
            double[] differences = new double[7];
            differences[0] = grades[1] - grades[0];
            differences[1] = grades[2] - grades[1];
            differences[2] = grades[3] - grades[2];
            differences[3] = grades[4] - grades[3];
            differences[4] = grades[5] - grades[4];
            differences[5] = grades[6] - grades[5];
            differences[6] = grades[7] - grades[6];
            String diffYears[] = {"2019-2018","2020-2019", "2021-2020",
            "2022-2021", "2023-2022", "2024-2023", "2025-2024"};
            double maxDiff = differences[0];
            double minDiff = differences[0];
            int maxIndex = 0;
            int minIndex = 0;
            for (int i = 1; i < differences.length; i++) {
            if (differences[i] > maxDiff) {
                maxDiff = Math.max(maxDiff, differences[i]);
                maxIndex = i;
            }
            if (differences[i] < minDiff) {
                minDiff = Math.min(minDiff, differences[i]);
                minIndex = i; 
            }
        }
        double totaldiff = 0;
        for (int i =0; i < differences.length; i++) {
            totaldiff += differences[i];
        }
        double avg = totaldiff / 7;
        System.out.println("--ΑΠΟΤΕΛΕΣΜΑΤΑ ΣΥΓΚΡΙΣΕΙΣ ΒΑΘΜΩΝ");
        System.out.println("Ο βαθμός του κράτους την τελευταία χρονιά πριν" +
            " αναλάβει η κυβέρνηση ήταν " + grades[0]);
        System.out.println("O πιο πρόσφατος βαθμός για το κράτος (2025) " +
            "με βάσει τις προβλέψεις είναι " + grades[7]);
        if (avg > 0) {
            System.out.println("Κατά μέσο όρο, ο βαθμός του κράτους με βάση"
                + "τα βάρη που δώθηκαν αυξανόταν κατά " + avg + 
                "την περίοδο 2018-2025. Οι βαθμοί απόδοσης παρουσίασαν"
                + " ανοδική πορεία!"
            );
        } else {
            System.out.println("Κατά μέσο όρο, ο βαθμός του κράτους με βάση"
                + "τα βάρη που δώθηκαν μειωνόταν κατά " + avg + 
                "την περίοδο 2018-2025. Οι βαθμοί απόδοσης παρουσίασαν" +
                " πτωτική πορεία"
            );
        }
        System.out.println("Η μεγαλύτερη αύξηση βαθμού παρατηρήθηκε τις χρονιές"
            + diffYears[maxIndex] + " ενώ η μεγαλύτερη μείωση τις χρονιές" 
            + diffYears[minIndex] 
        );        
    } 
}