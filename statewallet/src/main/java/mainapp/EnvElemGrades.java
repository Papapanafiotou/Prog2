package mainapp;

public class EnvElemGrades {

//Βαθμός για το ποσοστό χρήσης ανανεώσιμων πηγών ενέργειας
    public int getRESGrade(double percent) {
        if (percent >= 0.30) return 10;
        else if (percent >= 0.28) return 9;
        else if (percent >= 0.26) return 8;
        else if (percent >= 0.24) return 7;
        else if (percent >= 0.22) return 6;
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
     public double getEnviromentalGrade(double w1, double w2, double w3,
        double resPercent, double emmisionPercent, double recyclePercent) {
            GradeChar gradeChar = new GradeChar();
            int g1 = getRESGrade(resPercent);
            System.out.println("O βαθμός για τo ποσοστό αξιοποίησης "
            + "ανανεώσιμων πηγών ενέργειας είναι : " + g1);
            gradeChar.resChar(g1);
            int g2 = getEmmisionGrade(emmisionPercent);
            System.out.println("O βαθμός για την μεταβολή των ρύπων είναι: " 
            + g1);
            gradeChar.emmisionChar(g2);
            int g3 = getRecycleGrade(recyclePercent);
             System.out.println("O βαθμός για τo ποσοστό ανακύκλωσης" 
            + " των αστικών αποβλήτων είναι: " + g3);
            double grade = w1 *g1 + w2 *g2 + w3 *g3;
            System.out.println("O βαθμός για το κράτος στον περιβαλλοντικό"
                + " τομέα με βάση τα στοιχεία είναι " + grade);
            return grade;
        }
}
