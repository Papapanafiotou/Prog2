package mainapp;

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
}