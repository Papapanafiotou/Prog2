package mainapp;

import java.util.Scanner;

public class TotalGrade {
//Μέθοδος υπολογισμού του συνολικού βαθμού για το κράτος
    public void getTotalGrade() {
        Scanner scan = new Scanner(System.in);
        EconElemGrades ec = new EconElemGrades();
        SocElemGrades soc = new SocElemGrades();
        EnvElemGrades env = new EnvElemGrades();
        Weights w = new Weights();
        DataforGrade d = new DataforGrade();
        //Εύρεση δεδομένων για το έτος που ζητάει ο χρήστης
        boolean flag = true;
        int year;
        do {
        System.out.println("Ποιο έτος θέλετε να χρησιμοποιήσετε");
        year = scan.nextInt();
        if (year > 2025 || year < 2018) {
            System.out.println("Επιλέξτε ένα έτος ανάμεσα στο 2018 και 2025");
        } else {
            flag = false;
        }
        } while (flag);
        //Προειδοποίησησ στον χρήστη για το 2020
        if (year == 2020) {
            System.out.println("Κάποια στοιχεία είναι πολύ εππηρεαμσένα "
                + "από το ξέσπασμα της πανδημίας, όπως ο ρυθμός αύξησης του "
                + "ΑΕΠ και η μεταβολή των ρύπων, οπότε ο τελικός βαθμος "
                + "δεν είναι 100% αξιόπιστος!"
            );
        }
        if (year == 2025) {
            System.out.println("Κάποιες τιμές δεδομένων προέρχονται από"
                + " προβλέψεις. Ενδεχεται να υπάρχουν αποκλίσεις από τα πραγματικά"
            );
        }
        //Εισαγωγή των δεδομένων στις μεταβλητές
        double[] yearsData = d.getData(year);
        double gdpGrowth = yearsData[0];  //ΕΛΣΤΑΤ
        double publicDebt = yearsData[1];  //ΕΛΣΤΑΤ
        double surplus = yearsData[2]; //ΕΛΣΤΑΤ
        double res = yearsData[3]; //EUROSTAT
        double recycleRate = yearsData[4];  //EUROSTAT
        double emmisionsDiff = yearsData[5];  //EUROSTAT
        double gini = yearsData[6];  //EΛΣΤΑΤ
        double eduHealthExp = yearsData[7]; //EUROSTAT
        double mentalHealthPer = yearsData[8]; //ΕΚΤΕΠΝ(ΠΡΟΣΕΓΓΙΣΤΙΚΑ)
        double crimeRateDiff = yearsData[9]; //ΕΛ. ΑΣ.
        //EIΣΑΓΩΓΗ ΤΩΝ ΒΑΡΩΝ ΓΙΑ ΤΟΥΣ ΥΠΟΛΟΓΙΣΜΟΎΣ ΑΠΟ ΤΟΝ ΧΡΗΣΤΗ
        double[] weights = new double[10]; //KΑΤΑΧΩΡΗΣΗ ΒΑΡΩΝ ΣΤΟΝ ΠΙΝΑΚΑ
        int i = 0;
        System.out.println("Εισάγετε τα βάρη για τα οικονομικά στοιχεία ");
        double wGDP = w.getWeight();
        weights[i] = wGDP;
        i+= 1;
        System.out.println("Το βάρος για τον βαθμό της μεταβολής του ΑΕΠ" 
        + " καταχωρήθηκε! Eισάγετε το επόμενο βάρος!");
        double wPubDebt = w.getWeight();
        weights[i] = wPubDebt;
        i+= 1;
        System.out.println("Το βάρος για τον βαθμό του δημοσίου χρέους ως ποσοστό του ΑΕΠ" 
        +" καταχωρήθηκε! Eισάγετε το επόμενο βάρος!");
        double wSurp = w.getWeight();
        weights[i] = wSurp;
        i+= 1;
        System.out.println("Το βάρος για τον βαθμό του πρωτογενούς πλεονάσματος ως ποσοστό του ΑΕΠ" 
        +" καταχωρήθηκε! Eισάγετε το επόμενο βάρος!");
        System.out.println("Εισάγετε τα βάρη για τα περιβαλλοντικά στοιχεία ");
        double wRES = w.getWeight();
        weights[i] = wRES;
        i+= 1;
        System.out.println("Το βάρος για τον βαθμό της αξιοποίησης ανανεώσιμων πηγών ενέργειας" 
        + " καταχωρήθηκε! Eισάγετε το επόμενο βάρος!");
        double wRecRate = w.getWeight();
        weights[i] = wRecRate;
        i+= 1;
        System.out.println("Το βάρος για τον βαθμό του ποσοστόυ ανακύκλωσης αστικών"
         + " αποβλήτων καταχωρήθηκε! Eισάγετε το επόμενο βάρος!");
        double wEmm = w.getWeight();
        weights[i] = wEmm;
        i+= 1;
        System.out.println("Το βάρος για τον βαθμό της μεταβολής των ρύπων"
         +  " θερμοκηπίου καταχωρήθηκε! Eισάγετε το επόμενο βάρος!");
        System.out.println("Εισάγετε τα βάρη για τα κοινωνικά στοιχεία");
        double wGini = w.getWeight();
        weights[i] = wGini;
        i+= 1;
        System.out.println("Το βάρος για τον βαθμό του δείκτη GINI" 
        + " καταχωρήθηκε! Eισάγετε το επόμενο βάρος!");
        double wEdHealExp = w.getWeight();
        weights[i] = wEdHealExp;
        i+= 1;
        System.out.println("Το βάρος για τον βαθμό των δαπανών για υγεία" + 
        " και παιδεία καταχωρήθηκε! Eισάγετε το επόμενο βάρος!");
        double wMentHealPer = w.getWeight();
        weights[i] = wMentHealPer;
        i+= 1;
        System.out.println("Το βάρος για τον βαθμό του εκτιμώμενου ποσοστού ατόμων με" 
        + " προβλήματα ψυχικής υγείας καταχωρήθηκε! Eισάγετε το επόμενο βάρος!");
        double wCrimeRate = w.getWeight();
        weights[i] = wCrimeRate;
        i+= 1;
        System.out.println("Το βάρος για τον βαθμό της ποσοστιαίας μεταβολής"
         + " εγκληματικότητας(σοβαρά αδικήματα ανά 10000 αιδκήματα) καταχωρήθηκε!");
        //Υπολογισμός των βαθμών για τις 3 κατηγορίες
        double econGrade = ec.getEconomicGrade(wGDP, wPubDebt, wSurp,
             surplus, publicDebt, gdpGrowth);
        double envGrade = env.getEnviromentalGrade(wRES, wEmm, wRecRate,
             res, emmisionsDiff, recycleRate);
        double socGrade = soc.getSocialGrade(wGini, wCrimeRate, wMentHealPer, wEdHealExp,
            gini, crimeRateDiff, mentalHealthPer, eduHealthExp);
        //Εισαγωγή των βαρών για τις 3 κατηγορίες
        System.out.println("Εισάγετε τα βάρη του τελικού βαθμού για τους "
            + " τρεις τομείς");
        System.out.println("Οικονομικός τομέας");    
        double wEcon = w.getWeight();
        System.out.println("Περιβαλλοντικός τομέας");    
        double wEnv = w.getWeight();
        System.out.println("Κοινωνικός τομέας");    
        double wSoc = w.getWeight();
        double finalGrade = (wEcon * econGrade) + (wEnv * envGrade) + (wSoc * socGrade);
        System.out.println("O τελικός βαθμός για το κράτος για το έτος" + year 
        + " με βάση τα στοιχεία είναι " + finalGrade);
        double[] totalWeights = w.showTotalWeights(weights, wEcon, wEnv, wSoc);
        System.out.println("Για εμφάνιση των βαθμών του κράτους τα "
            + "τελευταία 5 έτη με βάση τα βάρη που δώσατε πατήστε 1."
        );
        int anwser = scan.nextInt();
        if (anwser == 1) {
            w.getAllGrades(totalWeights);
        }    
    }
}
