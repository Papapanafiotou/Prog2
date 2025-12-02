package mainapp;

import java.util.Scanner;

public class TotalGrade {
//Μέθοδος υπολογισμού του συνολικού βαθμού για το κράτος
    public double getTotalGrade() {
        Scanner scan = new Scanner(System.in);
        EconElemGrades ec = new EconElemGrades();
        SocElemGrades soc = new SocElemGrades();
        EnvElemGrades env = new EnvElemGrades();
        Weights w = new Weights();
        //ΣΤΟΙΧΕΙΑ ΤΟΥ 2024 ΓΙΑ ΤΗΝ ΑΠΟΔΟΤΙΚΟΤΗΤΑ
        double gdpGrowth = 0.028;  //ΕΛΣΤΑΤ
        double publicDebt = 161.9;  //ΕΛΣΤΑΤ
        double surplus = 0.019; //ΕΛΣΤΑΤ
        double res = 0.234; //EUROSTAT
        double recycleRate = 0.22;  //EUROSTAT
        double emmisionsDiff = -0.026;  //EUROSTAT
        double gini = 33.5;  //EΛΣΤΑΤ
        double eduHealthExp = 0.101; //EUROSTAT
        double mentalHealthPer = 0.20; //ΕΚΤΕΠΝ(ΠΡΟΣΕΓΓΙΣΤΙΚΑ)
        double crimeRateDiff = 0.06; //ΕΛ. ΑΣ.
        //EIΣΑΓΩΓΗ ΤΩΝ ΒΑΡΩΝ ΓΙΑ ΤΟΥΣ ΥΠΟΛΟΓΙΣΜΟΎΣ ΑΠΟ ΤΟΝ ΧΡΗΣΤΗ
        double[] weights = new double[10]; //KΑΤΑΧΩΡΗΣΗ ΒΑΡΩΝ ΣΤΟΝ ΠΙΝΑΚΑ
        int i = 0;
        System.out.println("Εισάγετε τα βάρη για τα οικονομικά στοιχεία ");
        double wGDP = w.getWeight();
        weights[i] = wGDP;
        i+= 1;
        System.out.println("Το βάρος καταχωρήθηκε! Eισάγετε το επόμενο βάρος!");
        double wPubDebt = w.getWeight();
        weights[i] = wPubDebt;
        i+= 1;
        System.out.println("Το βάρος καταχωρήθηκε! Eισάγετε το επόμενο βάρος!");
        double wSurp = w.getWeight();
        weights[i] = wSurp;
        i+= 1;
        System.out.println("Εισάγετε τα βάρη για τα περιβαλλοντικά στοιχεία ");
        double wRES = w.getWeight();
        weights[i] = wRES;
        i+= 1;
        System.out.println("Το βάρος καταχωρήθηκε! Eισάγετε το επόμενο βάρος!");
        double wRecRate = w.getWeight();
        weights[i] = wRecRate;
        i+= 1;
        System.out.println("Το βάρος καταχωρήθηκε! Eισάγετε το επόμενο βάρος!");
        double wEmm = w.getWeight();
        weights[i] = wEmm;
        i+= 1;
        System.out.println("Εισάγετε τα βάρη για τα κοινωνικά στοιχεία");
        double wGini = w.getWeight();
        weights[i] = wGini;
        i+= 1;
        System.out.println("Το βάρος καταχωρήθηκε! Eισάγετε το επόμενο βάρος!");
        double wEdHealExp = w.getWeight();
        weights[i] = wEdHealExp;
        i+= 1;
        System.out.println("Το βάρος καταχωρήθηκε! Eισάγετε το επόμενο βάρος!");
        double wMentHealPer = w.getWeight();
        weights[i] = wMentHealPer;
        i+= 1;
        System.out.println("Το βάρος καταχωρήθηκε! Eισάγετε το επόμενο βάρος!");
        double wCrimeRate = w.getWeight();
        weights[i] = wCrimeRate;
        i+= 1;
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
        System.out.println("O τελικός βαθμός για το κράτος για το έτος 2024"
            + "με βάση τα στοιχεία είναι " + finalGrade);
            return finalGrade;
    }
}