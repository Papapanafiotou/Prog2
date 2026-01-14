package mainapp;

/**
 * Υπολογίζει τους βαθμούς για διάφορα οικονομικά στοιχεία του κράτους.
 */
public final class EconElemGrades {

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

    // Σταθερές ΑΕΠ (GDP)
    /** Όριο ΑΕΠ για βαθμό 10. */
    private static final double GDP_LIMIT_10 = 0.022;
    /** Όριο ΑΕΠ για βαθμό 9. */
    private static final double GDP_LIMIT_9 = 0.015;
    /** Όριο ΑΕΠ για βαθμό 8. */
    private static final double GDP_LIMIT_8 = 0.012;
    /** Όριο ΑΕΠ για βαθμό 7. */
    private static final double GDP_LIMIT_7 = 0.007;
    /** Όριο ΑΕΠ για βαθμό 6. */
    private static final double GDP_LIMIT_6 = 0.00;

    // Σταθερές Χρέους (Debt)
    /** Όριο Χρέους για βαθμό 10. */
    private static final double DEBT_LIMIT_10 = 141.0;
    /** Όριο Χρέους για βαθμό 9. */
    private static final double DEBT_LIMIT_9 = 145.0;
    /** Όριο Χρέους για βαθμό 8. */
    private static final double DEBT_LIMIT_8 = 150.0;
    /** Όριο Χρέους για βαθμό 7. */
    private static final double DEBT_LIMIT_7 = 154.0;
    /** Όριο Χρέους για βαθμό 6. */
    private static final double DEBT_LIMIT_6 = 158.0;

    // Σταθερές Πλεονάσματος (Surplus)
    /** Όριο Πλεονάσματος για βαθμό 10. */
    private static final double SURP_LIMIT_10 = 0.03;
    /** Όριο Πλεονάσματος για βαθμό 9. */
    private static final double SURP_LIMIT_9 = 0.023;
    /** Όριο Πλεονάσματος για βαθμό 8. */
    private static final double SURP_LIMIT_8 = 0.016;
    /** Όριο Πλεονάσματος για βαθμό 7. */
    private static final double SURP_LIMIT_7 = 0.009;
    /** Όριο Πλεονάσματος για βαθμό 6. */
    private static final double SURP_LIMIT_6 = 0.0;

    /**
     * Υπολογίζει τον βαθμό για τη μεγέθυνση του ΑΕΠ.
     *
     * @param percent Το ποσοστό μεγέθυνσης.
     * @return Ο βαθμός (5-10).
     */
    public int getGDPGrowthGrade(final double percent) {
        if (percent >= GDP_LIMIT_10) {
            return GRADE_10;
        } else if (percent >= GDP_LIMIT_9) {
            return GRADE_9;
        } else if (percent >= GDP_LIMIT_8) {
            return GRADE_8;
        } else if (percent >= GDP_LIMIT_7) {
            return GRADE_7;
        } else if (percent >= GDP_LIMIT_6) {
            return GRADE_6;
        } else {
            return GRADE_5;
        }
    }

    /**
     * Υπολογίζει τον βαθμό για το δημόσιο χρέος ως ποσοστό του ΑΕΠ.
     *
     * @param percent Το ποσοστό χρέους.
     * @return Ο βαθμός (5-10).
     */
    public int getPublicDebtGrade(final double percent) {
        if (percent < DEBT_LIMIT_10) {
            return GRADE_10;
        } else if (percent < DEBT_LIMIT_9) {
            return GRADE_9;
        } else if (percent < DEBT_LIMIT_8) {
            return GRADE_8;
        } else if (percent < DEBT_LIMIT_7) {
            return GRADE_7;
        } else if (percent < DEBT_LIMIT_6) {
            return GRADE_6;
        } else {
            return GRADE_5;
        }
    }

    /**
     * Υπολογίζει τον βαθμό για το πρωτογενές πλεόνασμα.
     *
     * @param percent Το ποσοστό πλεονάσματος.
     * @return Ο βαθμός (5-10).
     */
    public int getSurplusGrade(final double percent) {
        if (percent > SURP_LIMIT_10) {
            return GRADE_10;
        } else if (percent >= SURP_LIMIT_9) {
            return GRADE_9;
        } else if (percent >= SURP_LIMIT_8) {
            return GRADE_8;
        } else if (percent >= SURP_LIMIT_7) {
            return GRADE_7;
        } else if (percent >= SURP_LIMIT_6) {
            return GRADE_6;
        } else {
            return GRADE_5;
        }
    }

    /**
     * Υπολογίζει τον τελικό οικονομικό βαθμό βάσει βαρύτητας.
     *
     * @param w1             Βαρύτητα ΑΕΠ.
     * @param w2             Βαρύτητα Χρέους.
     * @param w3             Βαρύτητα Πλεονάσματος.
     * @param surplusPercent Ποσοστό Πλεονάσματος.
     * @param debtPercent    Ποσοστό Χρέους.
     * @param gdpPercent     Ποσοστό ΑΕΠ.
     * @return Ο τελικός βαθμός.
     */
    public double getEconomicGrade(final double w1, final double w2,
                                   final double w3, final double surplusPercent,
                                   final double debtPercent,
                                   final double gdpPercent) {
        GradeChar gradeChar = new GradeChar();

        int g1 = getGDPGrowthGrade(gdpPercent);
        System.out.println("O βαθμός για την μεταβολή του ΑΕΠ είναι: " + g1);
        gradeChar.gdpGrowthChar(g1);

        int g2 = getPublicDebtGrade(debtPercent);
        System.out.println("O βαθμός για το δημόσιο χρέος ως ποσοστό"
                + " του ΑΕΠ είναι: " + g2);

        int g3 = getSurplusGrade(surplusPercent);
        System.out.println("O βαθμός για το πρωτογενές πλεόνασμα είναι: "
                + g3);
        gradeChar.surplusChar(g3);

        double grade = w1 * g1 + w2 * g2 + w3 * g3;
        System.out.println("O βαθμός για το κράτος στον οικονομικό"
                + " τομέα με βάση τα στοιχεία είναι "
                + String.format("%.2f", grade));
        return grade;
    }
}
