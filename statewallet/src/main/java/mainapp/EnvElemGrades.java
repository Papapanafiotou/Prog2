package mainapp;

/**
 * Υπολογίζει τους βαθμούς για τα περιβαλλοντικά στοιχεία του κράτους.
 */
public final class EnvElemGrades {

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

    // Σταθερές Ανανεώσιμων Πηγών (RES)
    /** Όριο ΑΠΕ για βαθμό 10. */
    private static final double RES_LIMIT_10 = 0.30;
    /** Όριο ΑΠΕ για βαθμό 9. */
    private static final double RES_LIMIT_9 = 0.28;
    /** Όριο ΑΠΕ για βαθμό 8. */
    private static final double RES_LIMIT_8 = 0.26;
    /** Όριο ΑΠΕ για βαθμό 7. */
    private static final double RES_LIMIT_7 = 0.24;
    /** Όριο ΑΠΕ για βαθμό 6. */
    private static final double RES_LIMIT_6 = 0.22;

    // Σταθερές Ρύπων (Emission)
    /** Όριο ρύπων για βαθμό 10. */
    private static final double EMM_LIMIT_10 = -0.04;
    /** Όριο ρύπων για βαθμό 9. */
    private static final double EMM_LIMIT_9 = -0.02;
    /** Όριο ρύπων για βαθμό 8. */
    private static final double EMM_LIMIT_8 = 0.0;
    /** Όριο ρύπων για βαθμό 7. */
    private static final double EMM_LIMIT_7 = 1.9;
    /** Όριο ρύπων για βαθμό 6. */
    private static final double EMM_LIMIT_6 = 3.9;

    // Σταθερές Ανακύκλωσης (Recycle)
    /** Όριο ανακύκλωσης για βαθμό 10. */
    private static final double REC_LIMIT_10 = 0.32;
    /** Όριο ανακύκλωσης για βαθμό 9. */
    private static final double REC_LIMIT_9 = 0.28;
    /** Όριο ανακύκλωσης για βαθμό 8. */
    private static final double REC_LIMIT_8 = 0.24;
    /** Όριο ανακύκλωσης για βαθμό 7. */
    private static final double REC_LIMIT_7 = 0.20;
    /** Όριο ανακύκλωσης για βαθμό 6. */
    private static final double REC_LIMIT_6 = 0.16;

    /**
     * Υπολογίζει τον βαθμό για το ποσοστό χρήσης ανανεώσιμων πηγών ενέργειας.
     *
     * @param percent Το ποσοστό χρήσης (0.0 - 1.0).
     * @return Ο βαθμός (5-10).
     */
    public int getResGrade(final double percent) {
        if (percent >= RES_LIMIT_10) {
            return GRADE_10;
        } else if (percent >= RES_LIMIT_9) {
            return GRADE_9;
        } else if (percent >= RES_LIMIT_8) {
            return GRADE_8;
        } else if (percent >= RES_LIMIT_7) {
            return GRADE_7;
        } else if (percent >= RES_LIMIT_6) {
            return GRADE_6;
        } else {
            return GRADE_5;
        }
    }

    /**
     * Υπολογίζει τον βαθμό για την ετήσια ποσοστιαία μεταβολή ρύπων.
     *
     * @param percent Το ποσοστό μεταβολής.
     * @return Ο βαθμός (5-10).
     */
    public int getEmissionGrade(final double percent) {
        if (percent <= EMM_LIMIT_10) {
            return GRADE_10;
        } else if (percent <= EMM_LIMIT_9) {
            return GRADE_9;
        } else if (percent <= EMM_LIMIT_8) {
            return GRADE_8;
        } else if (percent <= EMM_LIMIT_7) {
            return GRADE_7;
        } else if (percent <= EMM_LIMIT_6) {
            return GRADE_6;
        } else {
            return GRADE_5;
        }
    }

    /**
     * Υπολογίζει τον βαθμό για το ποσοστό ανακύκλωσης αστικών αποβλήτων.
     *
     * @param percent Το ποσοστό ανακύκλωσης.
     * @return Ο βαθμός (5-10).
     */
    public int getRecycleGrade(final double percent) {
        if (percent >= REC_LIMIT_10) {
            return GRADE_10;
        } else if (percent >= REC_LIMIT_9) {
            return GRADE_9;
        } else if (percent >= REC_LIMIT_8) {
            return GRADE_8;
        } else if (percent >= REC_LIMIT_7) {
            return GRADE_7;
        } else if (percent >= REC_LIMIT_6) {
            return GRADE_6;
        } else {
            return GRADE_5;
        }
    }

    /**
     * Υπολογίζει τον τελικό βαθμό για τον περιβαλλοντικό τομέα.
     *
     * @param w1              Βαρύτητα ΑΠΕ.
     * @param w2              Βαρύτητα Ρύπων.
     * @param w3              Βαρύτητα Ανακύκλωσης.
     * @param resPercent      Ποσοστό ΑΠΕ.
     * @param emissionPercent Ποσοστό Ρύπων.
     * @param recyclePercent  Ποσοστό Ανακύκλωσης.
     * @return Ο τελικός περιβαλλοντικός βαθμός.
     */
    public double getEnvironmentalGrade(final double w1, final double w2,
                                        final double w3,
                                        final double resPercent,
                                        final double emissionPercent,
                                        final double recyclePercent) {
        GradeChar gradeChar = new GradeChar();

        int g1 = getResGrade(resPercent);
        System.out.println("O βαθμός για τo ποσοστό αξιοποίησης "
                + "ανανεώσιμων πηγών ενέργειας είναι : " + g1);
        gradeChar.resChar(g1);

        int g2 = getEmissionGrade(emissionPercent);
        System.out.println("O βαθμός για την μεταβολή των ρύπων είναι: "
                + g2);
        gradeChar.emissionChar(g2);

        int g3 = getRecycleGrade(recyclePercent);
        System.out.println("O βαθμός για τo ποσοστό ανακύκλωσης"
                + " των αστικών αποβλήτων είναι: " + g3);

        double grade = w1 * g1 + w2 * g2 + w3 * g3;
        System.out.println("O βαθμός για το κράτος στον περιβαλλοντικό"
                + " τομέα με βάση τα στοιχεία είναι "
                + String.format("%.2f", grade));
        return grade;
    }
}
