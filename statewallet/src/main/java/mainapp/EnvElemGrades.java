public class EnvElemGrades {

//Βαθμός για το ποσοστό χρήσησ ανανεώσιμων πηγών ενέργειας
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
//Βαθμός για το ποσοστό ανακύκλωσης στα αστικά κέντρα
    public int getRecycleGrade(double percent) {
        if (percent >= 0.32) return 10;
        else if (percent >= 0.28) return 9;
        else if (percent >= 0.24) return 8;
        else if (percent >= 0.20) return 7;
        else if (percent >= 0.16) return 6;
        else return 5;
    }
}