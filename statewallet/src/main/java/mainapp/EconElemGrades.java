package mainapp;

public class EconElemGrades {
//Βαθμός για την μεγένθυση του ΑΕΠ
    public int getGDPGrowthGrade(double percent) {
        if (percent >= 0.022) return 10;
        else if (percent >= 0.015) return 9;
        else if (percent >= 0.012) return 8;
        else if (percent >= 0.007) return 7;
        else if (percent >= 0.00) return 6;
        else return 5;
    }

//Βαθμός για το δημόσιο χρέος ως ποσοστό του ΑΕΠ
    public int getpublicDebtGrade(double percent) {
        if (percent < 141.0) return 10;
        else if (percent < 145.0) return 9;
        else if (percent < 150.0) return 8;
        else if (percent < 154.0) return 7;
        else if (percent < 158.0) return 6;
        else return 5;
    }

/* Βαθμός για το πρωτογενές πλεόνασμα του κράτους ως ποσοστό του ΑΕΠ
Το πρωτογενές πλεόνασμα ως ποσοστό του ΑΕΠ δείχνει
ι πόσα χρήματα περίσσεψαν ή χάθηκαν από τον Κρατικό Προϋπολογισμό,
 χωρίς να υπολογίζονται οι τόκοι που πληρώνει το κράτος για τα δάνεια*/
    public int getSurplusGrade(double percent) {
        if (percent > 0.03) return 10;
        else if (percent >= 0.023) return 9;
        else if (percent >= 0.016) return 8;
        else if (percent >= 0.09) return 7;
        else if (percent >= 0.0) return 6;
        else return 5;
    }
    //Υπολογισμός του τελικού βαθμού στον οικονομικό τομέα
    public double getEconomicGrade(double w1, double w2, double w3,
        double surplusPercent, double debtPercent, double gdpPercent) {
            int g1 = getGDPGrowthGrade(gdpPercent);
            int g2 = getpublicDebtGrade(debtPercent);
            int g3 = getSurplusGrade(surplusPercent);
            double grade = w1 *g1 + w2 *g2 + w3 *g3;
            System.out.println("O βαθμός για το κράτος στον οικονομικό"
                + " τομέα με βάση τα στοιχεία είναι " + grade);
            return grade;
        }
}