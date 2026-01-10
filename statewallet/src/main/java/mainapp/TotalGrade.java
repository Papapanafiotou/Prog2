package mainapp;

import java.util.Scanner;

/**
 * Υπολογίζει τον συνολικό βαθμό του κράτους συνδυάζοντας οικονομικά,
 * περιβαλλοντικά και κοινωνικά στοιχεία.
 */
public final class TotalGrade {

    /** Ανοχή για συγκρίσεις αριθμών κινητής υποδιαστολής. */
    private static final double TOLERANCE = 0.001;

    // Επιλογές Χρήστη
    /** Επιλογή για υπολογισμό μεμονωμένου έτους. */
    private static final int OPTION_SINGLE_YEAR = 0;
    /** Επιλογή για υπολογισμό όλων των ετών. */
    private static final int OPTION_ALL_YEARS = 1;

    // Δείκτες πίνακα δεδομένων (Data Indices)
    /** Δείκτης ΑΕΠ. */
    private static final int IDX_GDP = 0;
    /** Δείκτης Χρέους. */
    private static final int IDX_DEBT = 1;
    /** Δείκτης Πλεονάσματος. */
    private static final int IDX_SURP = 2;
    /** Δείκτης ΑΠΕ. */
    private static final int IDX_RES = 3;
    /** Δείκτης Ανακύκλωσης. */
    private static final int IDX_REC = 4;
    /** Δείκτης Ρύπων. */
    private static final int IDX_EMM = 5;
    /** Δείκτης GINI. */
    private static final int IDX_GINI = 6;
    /** Δείκτης Υγείας. */
    private static final int IDX_HEALTH = 7;
    /** Δείκτης Ψυχικής Υγείας. */
    private static final int IDX_MENTAL = 8;
    /** Δείκτης Εγκληματικότητας. */
    private static final int IDX_CRIME = 9;

    /**
     * Υπολογίζει τον συνολικό βαθμό.
     */
    public void getTotalGrade() {
        System.out.println("Για τον υπολογισμό του βαθμού ενός μεμονωμένου"
                + " έτους πατήστε 0, για τον υπολογισμό βαθμών για όλα τα έτη "
                + "και για συγκρίσεις πατήστε 1!");

        Scanner scan = new Scanner(System.in, "CP737");
        EconElemGrades ec = new EconElemGrades();
        SocElemGrades soc = new SocElemGrades();
        EnvElemGrades env = new EnvElemGrades();
        // Περνάμε τον scanner για να μην κολλήσει η είσοδος
        Weights w = new Weights(scan);
        DataforGrade d = new DataforGrade();
        EconomicsChart e = new EconomicsChart();

        int answer = scan.nextInt();

        if (answer == OPTION_SINGLE_YEAR) {
            // Εύρεση δεδομένων για το έτος που ζητάει ο χρήστης
            int year = d.chooseYear();
            // Εισαγωγή των δεδομένων στις μεταβλητές
            double[] yearsData = d.getData(year);
            double gdpGrowth = yearsData[IDX_GDP];
            double publicDebt = yearsData[IDX_DEBT];
            double surplus = yearsData[IDX_SURP];
            double res = yearsData[IDX_RES];
            double recycleRate = yearsData[IDX_REC];
            double emmisionsDiff = yearsData[IDX_EMM];
            double gini = yearsData[IDX_GINI];
            double eduHealthExp = yearsData[IDX_HEALTH];
            double mentalHealthPer = yearsData[IDX_MENTAL];
            double crimeRateDiff = yearsData[IDX_CRIME];

            // ΕΙΣΑΓΩΓΗ ΤΩΝ ΒΑΡΩΝ ΓΙΑ ΤΟΥΣ ΥΠΟΛΟΓΙΣΜΟΎΣ ΑΠΟ ΤΟΝ ΧΡΗΣΤΗ
            double[] weights = w.addWeights();
            double wGdp = weights[IDX_GDP];
            double wPubDebt = weights[IDX_DEBT];
            double wSurp = weights[IDX_SURP];
            double wRes = weights[IDX_RES];
            double wRecRate = weights[IDX_REC];
            double wEmm = weights[IDX_EMM];
            double wGini = weights[IDX_GINI];
            double wEdHealExp = weights[IDX_HEALTH];
            double wMentHealPer = weights[IDX_MENTAL];
            double wCrimeRate = weights[IDX_CRIME];

            // Υπολογισμός των βαθμών για τις 3 κατηγορίες
            double econGrade = ec.getEconomicGrade(wGdp, wPubDebt, wSurp,
                    surplus, publicDebt, gdpGrowth);

            double envGrade = env.getEnvironmentalGrade(wRes, wEmm, wRecRate,
                    res, emmisionsDiff, recycleRate);

            // Ομαδοποίηση βαρών κοινωνικού τομέα σε πίνακα
            // για να αποφύγουμε το σφάλμα "More than 7 parameters"
            double[] socWeights = {
                wGini, wCrimeRate, wMentHealPer, wEdHealExp
            };

            double socGrade = soc.getSocialGrade(socWeights,
                    gini, crimeRateDiff, mentalHealthPer, eduHealthExp);

            // Εισαγωγή των βαρών για τις 3 κατηγορίες
            double t = 0;
            double wEcon;
            double wEnv;
            double wSoc;
            do {
                System.out.println("Εισάγετε τα βάρη του τελικού βαθμού για "
                        + "τους τρεις τομείς");
                System.out.println("Οικονομικός τομέας");
                wEcon = w.getWeight();
                t += wEcon;
                System.out.println("Περιβαλλοντικός τομέας");
                wEnv = w.getWeight();
                t += wEnv;
                System.out.println("Κοινωνικός τομέας");
                wSoc = w.getWeight();
                t += wSoc;
                if (Math.abs(t - 1.0) > TOLERANCE) {
                    t = 0;
                    System.out.println("Τα βάρη πρέπει να αθροίζουν σε 1! "
                            + "Εισάγετε ξανά τα βάρη.");
                }
            } while (Math.abs(t - 1.0) > TOLERANCE);

            double finalGrade = (wEcon * econGrade) + (wEnv * envGrade)
                    + (wSoc * socGrade);
            System.out.println("O τελικός βαθμός για το κράτος για το έτος "
                    + year + " με βάση τα στοιχεία είναι "
                    + String.format("%.2f", finalGrade));

            double[] totalWeights = w.showTotalWeights(weights, wEcon,
                    wEnv, wSoc);
            String[] names = {
                "MΕΤΑΒΟΛΗ ΑΕΠ",
                "ΔΗΜΟΣΙΟ ΧΡΕΟΣ ΩΣ ΠΟΣΟΣΤΟ ΑΕΠ",
                "ΠΡΩΤΟΓΕΝΕΣ ΠΛΕΟΝΑΣΜΑ",
                "ΑΝΑΝΕΩΣΙΜΕΣ ΠΗΓΕΣ ΕΝΕΡΓΕΙΑΣ",
                "ΠΟΣΟΣΤΟ ΑΝΑΚΥΚΛΩΣΗΣ",
                "ΜΕΤΑΒΟΛΗ ΡΥΠΩΝ",
                "ΔΕΙΚΤΗΣ GINI",
                "ΕΞΟΔΑ ΓΙΑ ΥΓΕΙΑ ΚΑΙ ΠΑΙΔΕΙΑ",
                "ΠΟΣΟΣΤΟ ΑΝΘΡΩΠΩΝ ΜΕ ΠΡΟΒΛΗΜΑΤΑ ΨΥΧΙΚΗΣ ΥΓΕΙΑΣ",
                "ΜΕΤΑΒΟΛΗ ΕΓΚΛΗΜΑΤΙΚΟΤΗΤΑΣ"
            };
            e.showPieChart(names, totalWeights);

        } else if (answer == OPTION_ALL_YEARS) {
            double[] weights = w.addWeights();
            double t = 0;
            double wEcon;
            double wEnv;
            double wSoc;
            do {
                System.out.println("Εισάγετε τα βάρη του τελικού βαθμού για "
                        + "τους τρεις τομείς");
                System.out.println("Οικονομικός τομέας");
                wEcon = w.getWeight();
                t += wEcon;
                System.out.println("Περιβαλλοντικός τομέας");
                wEnv = w.getWeight();
                t += wEnv;
                System.out.println("Κοινωνικός τομέας");
                wSoc = w.getWeight();
                t += wSoc;
                if (Math.abs(t - 1.0) > TOLERANCE) {
                    t = 0;
                    System.out.println("Τα βάρη πρέπει να αθροίζουν σε 1! "
                            + "Εισάγετε ξανά τα βάρη.");
                }
            } while (Math.abs(t - 1.0) > TOLERANCE);

            double[] finalWeights = w.showTotalWeights(weights, wEcon,
                    wEnv, wSoc);
            w.getAllGrades(finalWeights);
        }
    }
}
