package mainapp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Παρέχει μεθόδους για την απεικόνιση οικονομικών δεδομένων σε γραφήματα
 * (Line Chart & Pie Chart).
 */
public class EconomicsChart {

    // Σταθερές Line Chart
    /** Πλάτος παραθύρου γραφήματος γραμμής. */
    private static final int LINE_CHART_W = 800;
    /** Ύψος παραθύρου γραφήματος γραμμής. */
    private static final int LINE_CHART_H = 600;
    /** Ανώτατο όριο άξονα Υ. */
    private static final double Y_AXIS_UPPER = 10.0;
    /** Βήμα άξονα Υ. */
    private static final double Y_AXIS_TICK = 1.0;

    // Σταθερές Pie Chart
    /** Πλάτος παραθύρου πίτας. */
    private static final int PIE_FRAME_W = 1100;
    /** Ύψος παραθύρου πίτας. */
    private static final int PIE_FRAME_H = 600;
    /** Μέγεθος πίτας. */
    private static final int PIE_SIZE = 380;
    /** Θέση Χ πίτας. */
    private static final int PIE_X = 50;
    /** Θέση Υ πίτας. */
    private static final int PIE_Y = 80;
    /** Αρχική γωνία σχεδίασης. */
    private static final double START_ANGLE = 90.0;
    /** Πλήρης κύκλος (μοίρες). */
    private static final double FULL_CIRCLE = 360.0;
    /** Όριο τυχαίου χρώματος (για αποφυγή πολύ ανοιχτών χρωμάτων). */
    private static final int COLOR_LIMIT = 200;
    /** Σπόρος για τυχαίους αριθμούς. */
    private static final int RANDOM_SEED = 42;
    /** Πάχος γραμμής περιγράμματος. */
    private static final float STROKE_WIDTH = 1.2f;

    // Σταθερές Legend (Υπόμνημα)
    /** Γραμμές ανά στήλη υπομνήματος. */
    private static final int ROWS_PER_COL = 6;
    /** Offset στήλης υπομνήματος. */
    private static final int COL_OFFSET = 320;
    /** Offset γραμμής υπομνήματος. */
    private static final int ROW_OFFSET = 40;
    /** Βασική θέση Χ υπομνήματος. */
    private static final int LEGEND_X_BASE = 70;
    /** Πλάτος κουτιού χρώματος. */
    private static final int BOX_W = 25;
    /** Ύψος κουτιού χρώματος. */
    private static final int BOX_H = 18;
    /** Γωνία καμπυλότητας κουτιού (W). */
    private static final int ARC_W = 5;
    /** Γωνία καμπυλότητας κουτιού (H). */
    private static final int ARC_H = 5;
    /** Offset κειμένου Χ. */
    private static final int TEXT_X_OFFSET = 35;
    /** Offset κειμένου Υ. */
    private static final int TEXT_Y_OFFSET = 14;
    /** Μέγεθος γραμματοσειράς. */
    private static final int FONT_SIZE = 12;
    /** Πολλαπλασιαστής ποσοστού. */
    private static final int PERCENT_MULT = 100;

    /**
     * Εμφανίζει ένα γραμμικό διάγραμμα (Line Chart) χρησιμοποιώντας JavaFX.
     *
     * @param titlos  Ο τίτλος του γραφήματος.
     * @param xronies Πίνακας με τα έτη (άξονας Χ).
     * @param vathmoi Πίνακας με τους βαθμούς (άξονας Υ).
     */
    public void displayGraph(final String titlos, final String[] xronies,
                             final double[] vathmoi) {
        // Απαραίτητο για να αρχικοποιηθεί το JavaFX αν καλείται από main
        new JFXPanel();

        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle(titlos);

            final CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Έτος");

            final NumberAxis yAxis = new NumberAxis(0, Y_AXIS_UPPER,
                    Y_AXIS_TICK);
            yAxis.setLabel("Βαθμός (0-10)");

            final LineChart<String, Number> lineChart
                    = new LineChart<>(xAxis, yAxis);
            lineChart.setTitle(titlos);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Διακύμανση Αποδοτικότητας");

            // Γέμισμα δεδομένων από τους πίνακες
            for (int i = 0; i < vathmoi.length; i++) {
                series.getData().add(new XYChart.Data<>(xronies[i],
                        vathmoi[i]));
            }

            Scene scene = new Scene(lineChart, LINE_CHART_W, LINE_CHART_H);
            lineChart.getData().add(series);

            stage.setScene(scene);
            stage.show();
        });
    }

    /**
     * Εμφανίζει ένα διάγραμμα πίτας (Pie Chart) χρησιμοποιώντας Java Swing.
     *
     * @param names       Οι ονομασίες των κατηγοριών.
     * @param percentages Τα ποσοστά συμμετοχής (0.0 - 1.0).
     */
    public void showPieChart(final String[] names,
                             final double[] percentages) {
        JFrame frame = new JFrame("Ανάλυση Οικονομικών Μεγεθών");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                drawPie(g, names, percentages);
            }
        };

        frame.add(panel);
        // Πλάτος για να χωρέσουν οι 2 στήλες ονομάτων
        frame.setSize(PIE_FRAME_W, PIE_FRAME_H);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void drawPie(final Graphics g, final String[] labels,
                         final double[] values) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        double startAngle = START_ANGLE;
        Random rand = new Random(RANDOM_SEED);

        for (int i = 0; i < values.length; i++) {
            // Υπολογισμός μοιρών
            double arcAngle = values[i] * FULL_CIRCLE;

            // Επιλογή χρώματος
            Color c = new Color(rand.nextInt(COLOR_LIMIT),
                    rand.nextInt(COLOR_LIMIT), rand.nextInt(COLOR_LIMIT));
            g2d.setColor(c);

            // Σχεδίαση κομματιού (αρνητικό arcAngle για δεξιόστροφη φορά)
            g2d.fillArc(PIE_X, PIE_Y, PIE_SIZE, PIE_SIZE,
                    (int) Math.round(startAngle),
                    (int) Math.round(-arcAngle));

            // ΛΕΥΚΟ ΠΕΡΙΓΡΑΜΜΑ
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(STROKE_WIDTH));
            g2d.drawArc(PIE_X, PIE_Y, PIE_SIZE, PIE_SIZE,
                    (int) Math.round(startAngle),
                    (int) Math.round(-arcAngle));

            // --- ΥΠΟΜΝΗΜΑ ΣΕ 2 ΣΤΗΛΕΣ ---
            int column = i / ROWS_PER_COL;
            int row = i % ROWS_PER_COL;
            int lx = PIE_X + PIE_SIZE + LEGEND_X_BASE
                    + (column * COL_OFFSET);
            int ly = PIE_Y + (row * ROW_OFFSET);

            g2d.setColor(c);
            g2d.fillRoundRect(lx, ly, BOX_W, BOX_H, ARC_W, ARC_H);

            g2d.setColor(Color.DARK_GRAY);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, FONT_SIZE));

            String text = labels[i] + " ("
                    + String.format("%.2f", values[i] * PERCENT_MULT)
                    + "%)";
            g2d.drawString(text, lx + TEXT_X_OFFSET, ly + TEXT_Y_OFFSET);

            startAngle -= arcAngle; // Ενημέρωση γωνίας
        }
    }
}
