package mainapp;

public class SocElemGrades {
    
//Βαθμος για τον δείκτη ανισότητας GINI
    public int getGINIGrade(double gini) {
        if (gini <= 26.9) return 10;
        else if (gini <= 28.9) return 9;
        else if (gini <= 31.9) return 8;
        else if (gini <= 33.9) return 7;
        else if (gini <= 35.9) return 6;
        else return 5;
    }
//Βαθμός για την μεταβολή της εγκληματικότητας(σοβαρά αδικήματα)
    public int getCrimeGrade(double percent){
        if (percent <= -0.03) return 10;
        else if (percent < 0.0) return 9;
        else if (percent < 0.03) return 8;
        else if (percent < 0.06) return 7;
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
        if (percent >= 0.13) return 10;
        else if (percent >= 0.11) return 9;
        else if (percent >= 0.095) return 8;
        else if (percent >= 0.08) return 7; 
        else if (percent >= 0.07) return 6;
        else return 5; 
    }
    //Τελικός βαθμός για τον κοινωνικό τομέα
    public double getSocialGrade(double w1, double w2, double w3, double w4,
         double gini, double crimePercent, double mentalHealthPercent,
         double eduHealthExpensesPercent) {
            GradeChar gradeChar = new GradeChar();
            int g1 = getGINIGrade(gini);
            System.out.println("O βαθμός για την τιμή του δείκτη ανισοτήτων "
            + "GINI είναι: " + g1);
            gradeChar.giniChar(g1); //Χαρακτηρισμός τησ επίδοσης για το στοιχείο
            int g2 = getCrimeGrade(crimePercent);
            System.out.println("O βαθμός για την μεταβολή της εγκληματικότητας "
            + "είναι: " + g2);
            gradeChar.crimeRateChar(g2);
            int g3 = getMentalHealthGrade(mentalHealthPercent);
             System.out.println("O βαθμός για την εκτίμηση του ποσοστού " 
            + "ατόμων με προβλήματα ψυχικής υγείας στον πλυθησμό είναι: " 
                + g3);
            int g4 = getHealthEduGrade(eduHealthExpensesPercent);
            System.out.println("O βαθμός για τις δαπάνες σε υγεία και "
            + "παιδεία ως ποσοστό του ΑΕΠ είναι: " + g4);
            gradeChar.edHealthChar(g4);
            double grade = g1 * w1 + g2 * w2 + g3 * w3 + g4 * w4;
            System.out.println("O βαθμός για το κράτος στον κοινωνικό"
                + " τομέα με βάση τα στοιχεία είναι " + grade);
            return grade;
         }
}