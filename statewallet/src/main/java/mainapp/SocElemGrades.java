public class SocElemGrades {
//Βαθμος για τον δείκτη ανισότητασ GINI
    public int getGINIGrade(double gini) {
        if (gini <= 30.9) return 10;
        else if (gini <= 31.9) return 9;
        else if (gini <= 32.9) return 8;
        else if (gini <= 33.9) return 7;
        else if (gini <= 34.9) return 6;
        else return 5;
    }

}