package mainapp;

public class EnvElemGrades {

//Βαθμός για το ποσοστό χρήσης ανανεώσιμων πηγών ενέργειας
    public int getRESGrade(double percent) {
        if (percent >= 30.0) return 10;
        else if (percent >= 28.0) return 9;
        else if (percent >= 26.0) return 8;
        else if (percent >= 24.0) return 7;
        else if (percent >= 22.0) return 6;
        else return 5;
    }

//Βαθμός για την ετήσια ποσοστιαία μεταβολή ρύπων θερμοκηπίου
    public int getEmmisionGrade(double percent) {
        if (percent <= -0.04) return 10;
        else if (percent <= -0.02) return 9;
        else if (percent <= 0) return 8;
        else if (percent <= 1.9) return 7;
        else if (percent <= 3.9) return 6;
        else return 5;
    }
//Βαθμός για το ποσοστό ανακύκλωσης αστικών αποβλήτων
    public int getRecycleGrade(double percent) {
        if (percent >= 0.32) return 10;
        else if (percent >= 0.28) return 9;
        else if (percent >= 0.24) return 8;
        else if (percent >= 0.20) return 7;
        else if (percent >= 0.16) return 6;
        else return 5;
    }

//Τελικός βαθμός για τον περιβαλλοντικό τομέα
     public double getEconomicGrade(double w1, double w2, double w3,
        double resPercent, double emmisionPercent, double recyclePercent) {
            int g1 = getRESGrade(resPercent);
            int g2 = getEmmisionGrade(emmisionPercent);
            int g3 = getRecycleGrade(recyclePercent);
            double grade = w1 *g1 + w2 *g2 + w3 *g3;
            return grade;
        }
}
