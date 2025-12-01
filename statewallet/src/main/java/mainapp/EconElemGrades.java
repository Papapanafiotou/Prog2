public class EconElemGrades {
    //Βαθμός για την μεγένθυση του ΑΕΠ
    public int getGDPGrowthGrade(double percent) {
        if (percent >= 0.022) return 10;
        else if (percent >= 0.015) return 9;
        else if (percent >= 0.012) return 9;
        else if (percent >= 0.009) return 9;
        else if (percent >= 0.005) return 9;
        else return 5;
    }
}