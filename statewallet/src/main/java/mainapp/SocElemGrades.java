package mainapp;

public class SocElemGrades {
    
//Βαθμος για τον δείκτη ανισότητας GINI
    public int getGINIGrade(double gini) {
        if (gini <= 30.9) return 10;
        else if (gini <= 31.9) return 9;
        else if (gini <= 32.9) return 8;
        else if (gini <= 33.9) return 7;
        else if (gini <= 34.9) return 6;
        else return 5;
    }
//Βαθμός για την μεταβολή της εγκληματικότητας(σοβαρά αδικήματα)
    public int getCrimeGrade(double percent){
        if (percent <= -0.01) return 10;
        else if (percent < 0.01) return 9;
        else if (percent < 0.03) return 8;
        else if (percent < 0.05) return 7;
        else if (percent < 0.09) return 6;
        else return 5;
    }
//Βαθμός για το ποσοστό ατόμων με δυνητικά προβλήματα ψυχικής υγείας(άγχος, κατάθλιψη κλπ)
    public int getMentalHealthGrade(double percent) {
        if (percent <= 0.15) return 10;
        else if (percent <= 0.16) return 9;
        else if (percent <= 0.18) return 8;
        else if (percent <= 0.21) return 7;
        else if (percent <= 0.24) return 6;
        else return 5;
    }
//Βαθμός για το άρθροισμα των ποσοστών δαπανών υγείας και παιδείας στο ΑΕΠ
    public int getHealthEduGrade(double percent) {
        if (percent >= 0.12) return 10;
        else if (percent >= 0.11) return 9;
        else if (percent >= 0.095) return 8;
        else if (percent >= 0.08) return 7; // 10.1% -> 7/10
        else if (percent >= 0.07) return 6;
        else return 5; 
    }
    //Τελικός βαθμός για τον κοινωνικό τομέα
    public double getSocialGrade(double w1, double w2, double w3, double w4,
         double gini, double crimePercent, double mentalHealthPercent,
         double eduHealthExpensesPercent) {
            int g1 = getGINIGrade(gini);
            int g2 = getCrimeGrade(crimePercent);
            int g3 = getMentalHealthGrade(mentalHealthPercent);
            int g4 = getHealthEduGrade(eduHealthExpensesPercent);
            double grade = g1 * w1 + g2 * w2 + g3 * w3 + g4 * w4;
            System.out.println("O βαθμός για το κράτος στον κοινωνικό"
                + " τομέα με βάση τα στοιχεία είναι " + grade);
            return grade;
         }
}