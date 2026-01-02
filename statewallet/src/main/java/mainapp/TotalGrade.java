package mainapp;

import java.util.Scanner;

public class TotalGrade {
    public void getTotalGrade() {
    System.out.println("Για τον υπολογισμό του βαθμού ενός μεμονωμένου"
        + " έτους πατήστε 0, για τον υπολογισμό βαθμών για όλα τα έτη και"
         + " για συγκρίσεις πατήστε 1!"); 
        Scanner scan = new Scanner(System.in);
        EconElemGrades ec = new EconElemGrades();
        SocElemGrades soc = new SocElemGrades();
        EnvElemGrades env = new EnvElemGrades();
        Weights w = new Weights();
        DataforGrade d = new DataforGrade();
        EconomicsChart e = new EconomicsChart();
        int answer = scan.nextInt();
        if (answer == 0) {
        //Εύρεση δεδομένων για το έτος που ζητάει ο χρήστης
        int year = d.chooseYear();
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
        double[] weights = w.addWeights();
        double wGDP = weights[0];
        double wPubDebt = weights[1];
        double wSurp = weights[2];
        double wRES = weights[3];
        double wRecRate = weights[4];
        double wEmm = weights[5];
        double wGini = weights[6];
        double wEdHealExp = weights[7];
        double wMentHealPer = weights[8];
        double wCrimeRate = weights[9];
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
        System.out.println("O τελικός βαθμός για το κράτος για το έτος " + year 
        + " με βάση τα στοιχεία είναι " + String.format("%.2f", finalGrade));
        double[] totalWeights = w.showTotalWeights(weights, wEcon, wEnv, wSoc);
        e.showEconomicPie(totalWeights);
        } else if (answer == 1) {
          double[] weights = w.addWeights();
          double wGDP = weights[0];
          double wPubDebt = weights[1];
          double wSurp = weights[2];
          double wRES = weights[3];
          double wRecRate = weights[4];
          double wEmm = weights[5];
          double wGini = weights[6];
          double wEdHealExp = weights[7];
          double wMentHealPer = weights[8];
          double wCrimeRate = weights[9];
          System.out.println("Εισάγετε τα βάρη του τελικού βαθμού για τους "
            + " τρεις τομείς");
           System.out.println("Οικονομικός τομέας");    
           double wEcon = w.getWeight();
           System.out.println("Περιβαλλοντικός τομέας");    
           double wEnv = w.getWeight();
           System.out.println("Κοινωνικός τομέας");    
           double wSoc = w.getWeight();
           double[] finalWeights = w.showTotalWeights(weights, wEcon, wEnv, wSoc);
           w.getAllGrades(finalWeights);
        }   
    }
}
