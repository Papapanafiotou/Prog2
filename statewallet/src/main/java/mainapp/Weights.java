package mainapp;

import java.util.Scanner;

/**
 * Διαχειρίζεται την εισαγωγή και τον υπολογισμό των βαρών για την εξαγωγή
 * των τελικών βαθμών αξιολόγησης.
 */
public final class Weights {

    /** Μέγεθος πίνακα βαρών. */
    private static final int ARRAY_SIZE = 10;
    /** Όριο δεικτών για οικονομικά στοιχεία. */
    private static final int ECON_LIMIT = 3;
    /** Όριο δεικτών για περιβαλλοντικά στοιχεία. */
    private static final int ENV_LIMIT = 6;
    /** Όριο δεικτών για κοινωνικά στοιχεία. */
    private static final int SOC_LIMIT = 10;
    /** Ελάχιστο επιτρεπτό βάρος. */
    private static final double MIN_WEIGHT = 0.0;
    /** Μέγιστο επιτρεπτό βάρος. */
    private static final double MAX_WEIGHT = 1.0;
    /** Ανοχή για συγκρίσεις double. */
    private static final double TOLERANCE = 0.001;
    /** Μέγεθος πίνακα διαφορών. */
    private static final int DIFF_ARRAY_SIZE = 7;
    /** Μέγεθος πίνακα βαθμών (έτη). */
    private static final int GRADES_ARRAY_SIZE = 8;
    /** Έτος έναρξης ανάλυσης. */
    private static final int START_YEAR = 2018;

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

    /** Το αντικείμενο Scanner για είσοδο. */
    private final Scanner scanner;

    /**
     * Κατασκευαστής.
     *
     * @param inputScanner Ο Scanner για την ανάγνωση εισόδου.
     */
    public Weights(final Scanner inputScanner) {
        this.scanner = inputScanner;
    }

    /**
     * Ζητάει από τον χρήστη να εισάγει ένα βάρος (0-1).
     *
     * @return Το βάρος που εισήχθη.
     */
    public double getWeight() {
        boolean flag = false;
        double weight;
        do {
            try {
                weight = scanner.nextDouble();
                if (weight < MIN_WEIGHT || weight > MAX_WEIGHT) {
                    System.out.println("Παρακαλώ εισάγετε αριθμό"
                            + " μεταξύ του 0 και του 1");
                } else {
                    flag = true;
                }
            } catch (Exception e) {
                System.out.println("Μη έγκυρη είσοδος. "
                        + "Δώστε αριθμό (π.χ 0,5):");
                scanner.nextLine(); // Καθαρισμός buffer
                weight = -1; // Συνεχίζει το loop
            }
        } while (!flag);
        return weight;
    }

    /**
     * Υπολογίζει και εμφανίζει τα ποσοστά επίδρασης των στοιχείων στον τελικό
     * βαθμό.
     *
     * @param a  Ο πίνακας των βαθμών των επιμέρους στοιχείων.
     * @param w1 Το βάρος για τα οικονομικά στοιχεία.
     * @param w2 Το βάρος για τα περιβαλλοντικά στοιχεία.
     * @param w3 Το βάρος για τα κοινωνικά στοιχεία.
     * @return Πίνακας με τα τελικά ποσοστά επίδρασης.
     */
    public double[] showTotalWeights(final double[] a, final double w1,
                                     final double w2, final double w3) {
        double[] totalPercentage = new double[ARRAY_SIZE];
        for (int i = 0; i < ARRAY_SIZE; i++) {
            if (i < ECON_LIMIT) {
                totalPercentage[i] = w1 * a[i];
            } else if (i < ENV_LIMIT) {
                totalPercentage[i] = w2 * a[i];
            } else {
                totalPercentage[i] = w3 * a[i];
            }
        }
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
        System.out.println("Τα επιμέρους στοιχεία που χρησιμοποιήθηκαν "
                + "για τον υπολογισμό του τελικού βαθμού και οι τελικές "
                + "ποσοστιαίες επιδράσεις τους στον τελικό βαθμό είναι: "
        );
        for (int i = 0; i < ARRAY_SIZE; i++) {
            System.out.println("Στοιχείο: " + names[i]
                    + " Ποσοστό: "
                    + String.format("%.2f", totalPercentage[i]));
        }
        return totalPercentage;
    }

    /**
     * Υπολογίζει τους βαθμούς για όλα τα έτη και εμφανίζει συγκριτικά
     * αποτελέσματα.
     *
     * @param a Τα βάρη των επιμέρους στοιχείων.
     */
    public void getAllGrades(final double[] a) {
        int[] years = new int[GRADES_ARRAY_SIZE];
        for (int i = 0; i < GRADES_ARRAY_SIZE; i++) {
            years[i] = START_YEAR + i;
        }

        double[] grades = new double[GRADES_ARRAY_SIZE];
        int index = 0;

        for (int year : years) {
            DataforGrade d = new DataforGrade();
            double[] data = d.getData(year);
            int[] elemGrade = new int[ARRAY_SIZE];
            EnvElemGrades e = new EnvElemGrades();
            EconElemGrades ec = new EconElemGrades();
            SocElemGrades s = new SocElemGrades();

            elemGrade[IDX_GDP] = ec.getGDPGrowthGrade(data[IDX_GDP]);
            elemGrade[IDX_DEBT] = ec.getPublicDebtGrade(data[IDX_DEBT]);
            elemGrade[IDX_SURP] = ec.getSurplusGrade(data[IDX_SURP]);
            elemGrade[IDX_RES] = e.getResGrade(data[IDX_RES]);
            elemGrade[IDX_REC] = e.getRecycleGrade(data[IDX_REC]);
            elemGrade[IDX_EMM] = e.getEmissionGrade(data[IDX_EMM]);
            elemGrade[IDX_GINI] = s.getGINIGrade(data[IDX_GINI]);
            elemGrade[IDX_HEALTH] = s.getHealthEduGrade(data[IDX_HEALTH]);
            elemGrade[IDX_MENTAL] = s.getMentalHealthGrade(data[IDX_MENTAL]);
            elemGrade[IDX_CRIME] = s.getCrimeGrade(data[IDX_CRIME]);

            double totalGrade = 0;
            for (int i = 0; i < ARRAY_SIZE; i++) {
                totalGrade += (elemGrade[i] * a[i]);
            }
            grades[index] = totalGrade;
            index++;
        }

        double[] differences = new double[DIFF_ARRAY_SIZE];
        for (int i = 0; i < DIFF_ARRAY_SIZE; i++) {
            differences[i] = grades[i + 1] - grades[i];
        }

        String[] diffYears = {
            "2019-2018", "2020-2019", "2021-2020",
            "2022-2021", "2023-2022", "2024-2023", "2025-2024"
        };

        double maxDiff = differences[0];
        double minDiff = differences[0];
        int maxIndex = 0;
        int minIndex = 0;

        for (int i = 1; i < differences.length; i++) {
            if (differences[i] > maxDiff) {
                maxDiff = Math.max(maxDiff, differences[i]);
                maxIndex = i;
            }
            if (differences[i] < minDiff) {
                minDiff = Math.min(minDiff, differences[i]);
                minIndex = i;
            }
        }

        double totaldiff = 0;
        for (double diff : differences) {
            totaldiff += diff;
        }
        double avg = totaldiff / DIFF_ARRAY_SIZE;

        System.out.println("Οι βαθμοί για το κράτος ανά έτος είναι:");
        for (int i = 0; i < grades.length; i++) {
            System.out.println("ΧΡΟΝΙΑ: " + years[i] + " ΒΑΘΜΟΣ: "
                    + String.format("%.2f", grades[i]));
            if (i > 0) {
                System.out.print(" ΜΕΤΑΒΟΛΗ ΒΑΘΜΟΥ ΑΠΟ ΤΟ ΠΡΟΗΓΟΥΜΕΝΟ ΕΤΟΣ "
                        + String.format("%.2f", differences[i - 1])
                );
            }
            System.out.println();
        }

        System.out.println("--ΑΠΟΤΕΛΕΣΜΑΤΑ ΣΥΓΚΡΙΣΕΙΣ ΒΑΘΜΩΝ--");
        System.out.println("Ο βαθμός του κράτους την τελευταία χρονιά πριν"
                + " αναλάβει η κυβέρνηση ήταν " + grades[0]);
        System.out.println("O πιο πρόσφατος βαθμός για το κράτος (2025) "
                + "με βάσει τις προβλέψεις είναι "
                + grades[GRADES_ARRAY_SIZE - 1]);

        if (avg > 0) {
            System.out.println("Κατά μέσο όρο, ο βαθμός του κράτους με βάση"
                    + " τα βάρη που δώθηκαν αυξανόταν κατά "
                    + String.format("%.3f", avg)
                    + "την περίοδο 2018-2025. Οι βαθμοί απόδοσης παρουσίασαν"
                    + " ανοδική πορεία!"
            );
        } else {
            System.out.println("Κατά μέσο όρο, ο βαθμός του κράτους με βάση"
                    + "τα βάρη που δώθηκαν μειωνόταν κατά "
                    + String.format("%.3f", avg)
                    + "την περίοδο 2018-2025. Οι βαθμοί απόδοσης παρουσίασαν"
                    + " πτωτική πορεία"
            );
        }
        System.out.println("Η μεγαλύτερη αύξηση βαθμού παρατηρήθηκε τις "
                + "χρονιές " + diffYears[maxIndex]
                + " ενώ η μεγαλύτερη μείωση τις χρονιές "
                + diffYears[minIndex]
        );

        EconomicsChart e = new EconomicsChart();
        String[] xronies = {
            "2018", "2019", "2020", "2021",
            "2022", "2023", "2024", "2025"
        };
        e.displayGraph("ΠΟΡΕΙΑ ΒΑΘΜΩΝ ΚΡΑΤΟΥΣ 2018 - 2025", xronies, grades);
    }

    /**
     * Διαχειρίζεται τη διαδικασία εισαγωγής βαρών για όλες τις κατηγορίες.
     *
     * @return Πίνακας με τα εισαγόμενα βάρη.
     */
    public double[] addWeights() {
        double[] weights = new double[ARRAY_SIZE];
        int i = 0;

        System.out.println("ΣΤΟΙΧΕΙΑ ΟΙΚΟΝΟΜΙΚΟΥ ΤΟΜΕΑ");
        double t1 = 0;
        do {
            System.out.println("Εισάγετε τo βάρος για τη μεταβολή του ΑΕΠ");
            double wGDP = getWeight();
            t1 += wGDP;
            weights[i] = wGDP;
            i++;

            System.out.println("Το βάρος καταχωρήθηκε! Εισάγετε το"
                    + " βάρος για το δημόσιο χρέος ως ποσοστό του ΑΕΠ."
            );
            double wPubDebt = getWeight();
            t1 += wPubDebt;
            weights[i] = wPubDebt;
            i++;

            System.out.println("Το βάρος καταχωρήθηκε! Eισάγετε το βάρος για "
                    + "το πρωτογενές πλεόνασμα ως ποσοστό του ΑΕΠ."
            );
            double wSurp = getWeight();
            t1 += wSurp;
            weights[i] = wSurp;
            i++;

            if (Math.abs(t1 - 1.0) > TOLERANCE) {
                i = 0;
                t1 = 0;
                System.out.println("Το άθροισμα των βαρών πρέπει να "
                        + "ισούται με 1! Εισάγετε ξανά τα βάρη");
            }
        } while (Math.abs(t1 - 1.0) > TOLERANCE);

        System.out.println("Το βάρος καταχωρήθηκε!\n ΠΕΡΙΒΑΛΛΟΝΤΙΚΑ ΣΤΟΙΧΕΙΑ\n "
                + "Eισάγετε το βάρος για το"
                + " ποσοστό αξιοποίησης ανανεώσιμων πηγών ενέργειας."
        );
        double t2 = 0;
        do {
            double wRES = getWeight();
            weights[i] = wRES;
            i++;
            t2 += wRES;
            System.out.println("Το βάρος καταχωρήθηκε! Eισάγετε το βάρος για"
                    + " το ποσοστό ανακύκλωσης αστικών αποβλήτων."
            );
            double wRecRate = getWeight();
            weights[i] = wRecRate;
            i++;
            t2 += wRecRate;
            System.out.println("Το βάρος καταχωρήθηκε! Eισάγετε το βάρος "
                    + "για την ποσοστιαία μεταβολή της εκπομπής ρύπων."
            );
            double wEmm = getWeight();
            weights[i] = wEmm;
            i++;
            t2 += wEmm;
            if (Math.abs(t2 - 1.0) > TOLERANCE) {
                i = ECON_LIMIT;
                t2 = 0;
                System.out.println("Το άθροισμα των βαρών πρέπει να "
                        + "ισούται με 1! Εισάγετε ξανά τα βάρη");
            }
        } while (Math.abs(t2 - 1.0) > TOLERANCE);

        System.out.println("Το βάρος καταχωρήθηκε!\n KΟΙΝΩΝΙΚΑ ΣΤΟΙΧΕΙΑ\n"
                + "Eισάγετε το βάρος για τον"
                + " κοινωνικό δείκτη GINI"
        );
        double t3 = 0;
        do {
            double wGini = getWeight();
            weights[i] = wGini;
            i++;
            t3 += wGini;
            System.out.println("Το βάρος καταχωρήθηκε! Eισάγετε το βάρος"
                    + " για τις δαπάνες υγείας και παιδείας ως ποσοστό του ΑΕΠ"
            );
            double wEdHealExp = getWeight();
            weights[i] = wEdHealExp;
            i++;
            t3 += wEdHealExp;
            System.out.println("Το βάρος καταχωρήθηκε! Eισάγετε βάρος για "
                    + " την εκτίμηση του ποσοστού ανθρώπων "
                    + "με προβλήματα ψυχικής υγείας"
            );
            double wMentHealPer = getWeight();
            weights[i] = wMentHealPer;
            i++;
            t3 += wMentHealPer;
            System.out.println("Το βάρος καταχωρήθηκε! Eισάγετε το βάρος"
                    + " για την ποσοστιαία μεταβολή της εγκληματικότητας."
            );
            double wCrimeRate = getWeight();
            weights[i] = wCrimeRate;
            t3 += wCrimeRate;
            if (Math.abs(t3 - 1.0) > TOLERANCE) {
                i = ENV_LIMIT;
                t3 = 0;
                System.out.println("Το άθροισμα των βαρών πρέπει να "
                        + "ισούται με 1! Εισάγετε ξανά τα βάρη");
            }
        } while (Math.abs(t3 - 1.0) > TOLERANCE);

        System.out.println(
                "Το βάρος καταχωρήθηκε! Όλα τα βάρη έχουν εισαχθεί");
        return weights;
    }
}
