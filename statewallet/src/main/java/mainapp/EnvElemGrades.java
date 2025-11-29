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
}