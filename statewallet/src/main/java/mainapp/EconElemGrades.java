public class EconElemGrades {
    //Βαθμός για την μεγένθυση του ΑΕΠ
    public int getGDPGrowthGrade(double percent) {
        if (percent >= 0.022) return 10;
        else if (percent >= 0.015) return 9;
        else if (percent >= 0.012) return 8;
        else if (percent >= 0.009) return 7;
        else if (percent >= 0.005) return 6;
        else return 5;
    }

    //Βαθμός για το ποσοστό του δημοσίου χρέους στο ΑΕΠ
     public int getpublicDebtGrade(double percent) {
        if (percent < 141) return 10;
        else if (percent < 145) return 9;
        else if (percent < 150) return 8;
        else if (percent < 154) return 7;
        else if (percent < 158) return 6;
        else return 5;
    }
}