package mainapp;

import java.util.Scanner;

public class Weights {
    Scanner scan = new Scanner(System.in);
    //Μέθοδος για την εισαγωγή βαρών για τον υπολογισμο των βαθμών
    public double getWeight() {
        boolean flag = false;
        double weight;
        do {
            weight = scan.nextDouble();
            if (weight < 0 || weight > 1) {
                System.out.println("Παρακαλώ εισάγετε αριθμό"
                + " μεταξύ του 0 και του 1");
            } else {
                flag =true;
            } 
        } while (flag == false);
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
               "MΕΤΑΒΟΛΗ ΑΕΠ",
               "ΔΗΜΟΣΙΟ ΧΡΕΟΣ ΩΣ ΠΟΣΟΣΤΟ ΑΕΠ",
               "ΠΡΩΤΟΓΕΝΕΣ ΠΛΕΟΝΑΣΜΑ",
               "ΑΝΑΝΕΩΣΙΜΕΣ ΠΗΓΕΣ ΕΝΕΡΓΕΙΑΣ",
               "ΠΟΣΟΣΤΟ ΑΝΑΚΥΚΛΩΣΗΣ",
               "ΜΕΤΑΒΟΛΗ ΡΥΠΩΝ",
               "ΔΕΙΚΤΗΣ GINI",
               "ΕΞΟΔΑ ΓΙΑ ΥΓΕΙΑ ΚΑΙ ΠΑΙΔΕΙΑ",
               "ΠΟΣΟΣΤΟ ΑΝΘΡΩΠΩΝ ΜΕ ΠΡΟΒΛΗΜΑΤΑ ΨΥΧΙΚΗΣ ΥΓΕΙΑΣ",
               "ΜΕΤΑΒΟΛΗ ΕΓΚΛΗΜΑΤΙΚΟΤΗΤΑΣ"
            };
            System.out.println("Τα επιμέρους στοιχεία που χρησιμοποιήθηκαν "
                + "για τον υπολογισμό του τελικού βαθμού και οι τελικές " +
                "ποσοστιαίες επιδράσεις τους στον τελικό βαθμό είναι: "
            );
            for (int i = 0; i < 10; i++) {
                System.out.println("Στοιχείο: " + names[i] +
                " Ποσοστό: " + String.format("%.2f", totalPercentage[i]) );
            }
            return totalPercentage;
        }
        public void getAllGrades(double[] a) {
            int[] years = {2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025};
            double[] grades = new double[8];
            int index = 0;
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
                grades[index] = totalGrade;
                index++;
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
        System.out.println("Οι βαθμοί για το κράτος ανά έτος είναι:");
        for (int i =0; i < grades.length; i++) {
            int j = -1; 
            System.out.println("ΧΡΟΝΙΑ: " + years[i] + " ΒΑΘΜΟΣ: " + String.format("%.2f", grades[i]));
            if (j > -1) {
                System.out.print(" ΜΕΤΑΒΟΛΗ ΒΑΘΜΟΥ ΑΠΟ ΤΟ ΠΡΟΗΓΟΥΜΕΝΟ ΕΤΟΣ "
                    +  String.format("%.2f", differences[j])
                );
            }
            j++;
        }        
        System.out.println("--ΑΠΟΤΕΛΕΣΜΑΤΑ ΣΥΓΚΡΙΣΕΙΣ ΒΑΘΜΩΝ--");
        System.out.println("Ο βαθμός του κράτους την τελευταία χρονιά πριν" +
            " αναλάβει η κυβέρνηση ήταν " + grades[0]);
        System.out.println("O πιο πρόσφατος βαθμός για το κράτος (2025) " +
            "με βάσει τις προβλέψεις είναι " + grades[7]);
        if (avg > 0) {
            System.out.println("Κατά μέσο όρο, ο βαθμός του κράτους με βάση"
                + " τα βάρη που δώθηκαν αυξανόταν κατά " + String.format("%.3f", avg) + 
                "την περίοδο 2018-2025. Οι βαθμοί απόδοσης παρουσίασαν"
                + " ανοδική πορεία!"
            );
        } else {
            System.out.println("Κατά μέσο όρο, ο βαθμός του κράτους με βάση"
                + "τα βάρη που δώθηκαν μειωνόταν κατά " + String.format("%.3f", avg) + 
                "την περίοδο 2018-2025. Οι βαθμοί απόδοσης παρουσίασαν" +
                " πτωτική πορεία"
            );
        }
        System.out.println("Η μεγαλύτερη αύξηση βαθμού παρατηρήθηκε τις χρονιές "
            + diffYears[maxIndex] + " ενώ η μεγαλύτερη μείωση τις χρονιές " 
            + diffYears[minIndex] 
        );
        EconomicsChart e = new EconomicsChart();
        String[] xronies = {"2018", "2019", "2020", "2021", "2022", "2023", "2024", "2025"};
        e.displayGraph("ΠΟΡΕΙΑ ΒΑΘΜΩΝ ΚΡΑΤΟΥΣ 2018 - 2025", xronies, grades);        
    }
    
    public double[] addWeights() {
        double[] weights = new double[10]; //KΑΤΑΧΩΡΗΣΗ ΒΑΡΩΝ ΣΤΟΝ ΠΙΝΑΚΑ
        int i = 0;
        System.out.println("ΣΤΟΙΧΕΙΑ ΟΙΚΟΝΟΜΙΚΟΥ ΤΟΜΕΑ");
        double t1 =0;
        do {
        System.out.println("Εισάγετε τo βάρος για τη μεταβολή του ΑΕΠ");
        double wGDP = getWeight();
        t1 += wGDP;
        
        System.out.println("Το βάρος καταχωρήθηκε! Εισάγετε το" 
            + " βάρος για το δημόσιο χρέος ως ποσοστό του ΑΕΠ."
        );
        double wPubDebt = getWeight();
        t1 += wPubDebt;
        weights[i] = wPubDebt;
        i+= 1;
        System.out.println("Το βάρος καταχωρήθηκε! Eισάγετε το βάρος για "
            + "το πρωτογενές πλεόνασμα ως ποσοστό του ΑΕΠ."
        );
        double wSurp = getWeight();
        t1 += wSurp;
        weights[i] = wSurp;
        i+= 1; 
        if (t1 != 1) {
            i =0;
            System.out.println("Το άρθροισμα των βαρών πρέπει να ισούται με 1! Εισάγετε ξανά τα βάρη");
        } 
        } while (t1 != 1);
        System.out.println("Το βάρος καταχωρήθηκε!\n ΠΕΡΙΒΑΛΛΟΝΤΙΚΑ ΣΤΟΙΧΕΙΑ\n "
         + "Eισάγετε το βάρος για το"
            + " ποσοστό αξιοποίησης ανανεώσιμων πηγών ενέργειας."
        );
        double t2 = 0;
        do {
        double wRES = getWeight();
        weights[i] = wRES;
        i+= 1;
        t2 += wRES;
        System.out.println("Το βάρος καταχωρήθηκε! Eισάγετε το βάρος για"
            + " το ποσοστό ανακύκλωσης αστικών αποβλήτων."
        );
        double wRecRate = getWeight();
        weights[i] = wRecRate;
        i+= 1;
        t2 += wRecRate;
        System.out.println("Το βάρος καταχωρήθηκε! Eισάγετε το βάρος για την"
            + " ποσοστίαια μεταβολή τησ εκπομπής ρύπων θερμοκηπίου."
        );
        double wEmm = getWeight();
        weights[i] = wEmm;
        i+= 1;
        t2 += wEmm;
        if (t2 != 1) {
            i = 3;
            System.out.println("Το άρθροισμα των βαρών πρέπει να ισούται με 1! Εισάγετε ξανά τα βάρη"); 
        }
        } while (t2 != 1);
        System.out.println("Το βάρος καταχωρήθηκε!\n KΟΙΝΩΝΙΚΑ ΣΤΟΙΧΕΙΑ\n" 
         + "Eισάγετε το βάρος για τον"
            + " κοινωνικό δείκτη GINI"
        );
        double t3 = 0;
        do {
        double wGini = getWeight();
        weights[i] = wGini;
        i+= 1;
        t3 += wGini;
        System.out.println("Το βάρος καταχωρήθηκε! Eισάγετε το βάρος"
           + " για τις δαπάνες υγείας και παιδείας ως ποσοστό του ΑΕΠ"
        );
        double wEdHealExp = getWeight();
        weights[i] = wEdHealExp;
        i+= 1;
        t3 += wEdHealExp;
        System.out.println("Το βάρος καταχωρήθηκε! Eισάγετε βάρος για "
            + " την εκτίμηση του ποσοστού ανθρώπων με προβλήματα ψυχικής υγείας"
        );
        double wMentHealPer = getWeight();
        weights[i] = wMentHealPer;
        i+= 1;
        t3 += wMentHealPer;
        System.out.println("Το βάρος καταχωρήθηκε! Eισάγετε το βάρος"
            + " για την ποσοστιαία μεταβολή της εγκληματικότητας (σοβαρά αδικήματα ανά 10000)."
        );
        double wCrimeRate = getWeight();
        weights[i] = wCrimeRate;
        t3 += wCrimeRate;
        if (t3 != 1) {
            i = 6;
            System.out.println("Το άρθροισμα των βαρών πρέπει να ισούται με 1! Εισάγετε ξανά τα βάρη");
        }
        } while (t3 != 1);  
        System.out.println("Το βάρος καταχωρήθηκε! Όλα τα βάρη έχουν εισαχθεί");
        return weights;
    }
}