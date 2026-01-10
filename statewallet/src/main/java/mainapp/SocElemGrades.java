package mainapp;

/**
 * Υπολογίζει τους βαθμούς για τα κοινωνικά στοιχεία του κράτους.
 */
public final class SocElemGrades {

    // Σταθερές Βαθμών
    /** Βαθμός 10 (Άριστα). */
    private static final int GRADE_10 = 10;
    /** Βαθμός 9 (Πολύ καλά). */
    private static final int GRADE_9 = 9;
    /** Βαθμός 8 (Καλά). */
    private static final int GRADE_8 = 8;
    /** Βαθμός 7 (Μέτρια). */
    private static final int GRADE_7 = 7;
    /** Βαθμός 6 (Χαμηλά). */
    private static final int GRADE_6 = 6;
    /** Βαθμός 5 (Πολύ χαμηλά/Αποτυχία). */
    private static final int GRADE_5 = 5;

    // Σταθερές GINI
    /** Όριο GINI για βαθμό 10. */
    private static final double GINI_LIMIT_10 = 26.9;
    /** Όριο GINI για βαθμό 9. */
    private static final double GINI_LIMIT_9 = 28.9;
    /** Όριο GINI για βαθμό 8. */
    private static final double GINI_LIMIT_8 = 31.9;
    /** Όριο GINI για βαθμό 7. */
    private static final double GINI_LIMIT_7 = 33.9;
    /** Όριο GINI για βαθμό 6. */
    private static final double GINI_LIMIT_6 = 35.9;

    // Σταθερές Εγκληματικότητας (Crime)
    /** Όριο εγκληματικότητας για βαθμό 10. */
    private static final double CRIME_LIMIT_10 = -0.03;
    /** Όριο εγκληματικότητας για βαθμό 9. */
    private static final double CRIME_LIMIT_9 = -0.01;
    /** Όριο εγκληματικότητας για βαθμό 8. */
    private static final double CRIME_LIMIT_8 = 0.03;
    /** Όριο εγκληματικότητας για βαθμό 7. */
    private static final double CRIME_LIMIT_7 = 0.06;
    /** Όριο εγκληματικότητας για βαθμό 6. */
    private static final double CRIME_LIMIT_6 = 0.09;

    // Σταθερές Ψυχικής Υγείας (Mental Health)
    /** Όριο ψυχικής υγείας για βαθμό 10. */
    private static final double MENTAL_LIMIT_10 = 0.15;
    /** Όριο ψυχικής υγείας για βαθμό 9. */
    private static final double MENTAL_LIMIT_9 = 0.16;
    /** Όριο ψυχικής υγείας για βαθμό 8. */
    private static final double MENTAL_LIMIT_8 = 0.18;
    /** Όριο ψυχικής υγείας για βαθμό 7. */
    private static final double MENTAL_LIMIT_7 = 0.21;
    /** Όριο ψυχικής υγείας για βαθμό 6. */
    private static final double MENTAL_LIMIT_6 = 0.24;

    // Σταθερές Υγείας/Παιδείας (Health/Edu)
    /** Όριο δαπανών υγείας/παιδείας για βαθμό 10. */
    private static final double HE_LIMIT_10 = 0.13;
    /** Όριο δαπανών υγείας/παιδείας για βαθμό 9. */
    private static final double HE_LIMIT_9 = 0.11;
    /** Όριο δαπανών υγείας/παιδείας για βαθμό 8. */
    private static final double HE_LIMIT_8 = 0.095;
    /** Όριο δαπανών υγείας/παιδείας για βαθμό 7. */
    private static final double HE_LIMIT_7 = 0.08;
    /** Όριο δαπανών υγείας/παιδείας για βαθμό 6. */
    private static final double HE_LIMIT_6 = 0.07;

    // Δείκτες πίνακα βαρών
    /** Δείκτης για το πρώτο βάρος (GINI). */
    private static final int IDX_W1 = 0;
    /** Δείκτης για το δεύτερο βάρος (Εγκληματικότητα). */
    private static final int IDX_W2 = 1;
    /** Δείκτης για το τρίτο βάρος (Ψυχική Υγεία). */
    private static final int IDX_W3 = 2;
    /** Δείκτης για το τέταρτο βάρος (Υγεία/Παιδεία). */
    private static final int IDX_W4 = 3;

    /**
     * Υπολογίζει τον βαθμό για τον δείκτη GINI.
     *
     * @param gini Ο δείκτης GINI.
     * @return Ο βαθμός (5-10).
     */
    public int getGINIGrade(final double gini) {
        if (gini <= GINI_LIMIT_10) {
            return GRADE_10;
        } else if (gini <= GINI_LIMIT_9) {
            return GRADE_9;
        } else if (gini <= GINI_LIMIT_8) {
            return GRADE_8;
        } else if (gini <= GINI_LIMIT_7) {
            return GRADE_7;
        } else if (gini <= GINI_LIMIT_6) {
            return GRADE_6;
        } else {
            return GRADE_5;
        }
    }

    /**
     * Υπολογίζει τον βαθμό για την εγκληματικότητα.
     *
     * @param percent Το ποσοστό μεταβολής εγκληματικότητας.
     * @return Ο βαθμός (5-10).
     */
    public int getCrimeGrade(final double percent) {
        if (percent <= CRIME_LIMIT_10) {
            return GRADE_10;
        } else if (percent <= CRIME_LIMIT_9) {
            return GRADE_9;
        } else if (percent <= CRIME_LIMIT_8) {
            return GRADE_8;
        } else if (percent <= CRIME_LIMIT_7) {
            return GRADE_7;
        } else if (percent <= CRIME_LIMIT_6) {
            return GRADE_6;
        } else {
            return GRADE_5;
        }
    }

    /**
     * Υπολογίζει τον βαθμό για την ψυχική υγεία.
     *
     * @param percent Το ποσοστό ατόμων με προβλήματα.
     * @return Ο βαθμός (5-10).
     */
    public int getMentalHealthGrade(final double percent) {
        if (percent <= MENTAL_LIMIT_10) {
            return GRADE_10;
        } else if (percent <= MENTAL_LIMIT_9) {
            return GRADE_9;
        } else if (percent <= MENTAL_LIMIT_8) {
            return GRADE_8;
        } else if (percent <= MENTAL_LIMIT_7) {
            return GRADE_7;
        } else if (percent <= MENTAL_LIMIT_6) {
            return GRADE_6;
        } else {
            return GRADE_5;
        }
    }

    /**
     * Υπολογίζει τον βαθμό για τις δαπάνες Υγείας και Παιδείας.
     *
     * @param percent Το ποσοστό δαπανών επί του ΑΕΠ.
     * @return Ο βαθμός (5-10).
     */
    public int getHealthEduGrade(final double percent) {
        if (percent >= HE_LIMIT_10) {
            return GRADE_10;
        } else if (percent >= HE_LIMIT_9) {
            return GRADE_9;
        } else if (percent >= HE_LIMIT_8) {
            return GRADE_8;
        } else if (percent >= HE_LIMIT_7) {
            return GRADE_7;
        } else if (percent >= HE_LIMIT_6) {
            return GRADE_6;
        } else {
            return GRADE_5;
        }
    }

    /**
     * Υπολογίζει τον τελικό βαθμό για τον κοινωνικό τομέα.
     *
     * @param weights                  Πίνακας με τα 4 βάρη (w1, w2, w3, w4).
     * @param gini                     Δείκτης GINI.
     * @param crimePercent             Ποσοστό εγκληματικότητας.
     * @param mentalHealthPercent      Ποσοστό ψυχικής υγείας.
     * @param eduHealthExpensesPercent Ποσοστό δαπανών Υγείας/Παιδείας.
     * @return Ο τελικός κοινωνικός βαθμός.
     */
    public double getSocialGrade(final double[] weights,
                                 final double gini,
                                 final double crimePercent,
                                 final double mentalHealthPercent,
                                 final double eduHealthExpensesPercent) {
        GradeChar gradeChar = new GradeChar();

        int g1 = getGINIGrade(gini);
        System.out.println("O βαθμός για τον δείκτη GINI είναι: " + g1);
        gradeChar.giniChar(g1);

        int g2 = getCrimeGrade(crimePercent);
        System.out.println("O βαθμός για την μεταβολή της εγκληματικότητας "
                + "είναι: " + g2);
        gradeChar.crimeRateChar(g2);

        int g3 = getMentalHealthGrade(mentalHealthPercent);
        System.out.println("O βαθμός για την ψυχική υγεία είναι: " + g3);

        int g4 = getHealthEduGrade(eduHealthExpensesPercent);
        System.out.println("O βαθμός για τις δαπάνες Υγείας και Παιδείας "
                + "είναι: " + g4);
        gradeChar.edHealthChar(g4);

        double grade = weights[IDX_W1] * g1
                + weights[IDX_W2] * g2
                + weights[IDX_W3] * g3
                + weights[IDX_W4] * g4;

        System.out.println("O βαθμός για το κράτος στον κοινωνικό τομέα "
                + "με βάση τα στοιχεία είναι " + String.format("%.2f", grade));
        return grade;
    }
}
