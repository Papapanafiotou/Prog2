package mainapp;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class EconomicsChart {

    public static void displayGraph(String titlos, String[] xronies, double[] vathmoi) {
        // Απαραίτητο για να αρχικοποιηθεί το JavaFX αν καλείται από main
        new JFXPanel(); 

        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle(titlos);

            final CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Έτος");

            final NumberAxis yAxis = new NumberAxis(0, 10, 1);
            yAxis.setLabel("Βαθμός (0-10)");

            final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
            lineChart.setTitle(titlos);

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Διακύμανση Αποδοτικότητας");

            // Γέμισμα δεδομένων από τους πίνακες
            for (int i = 0; i < vathmoi.length; i++) {
                series.getData().add(new XYChart.Data<>(xronies[i], vathmoi[i]));
            }

            Scene scene = new Scene(lineChart, 800, 600);
            lineChart.getData().add(series);

            stage.setScene(scene);
            stage.show();
        });
    }


    public static void showPieChart(String[] names, double[] percentages) {
    JFrame frame = new JFrame("Ανάλυση Οικονομικών Μεγεθών");
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
    JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawPie(g, names, percentages);
        }
    };

    frame.add(panel);
    frame.setSize(1100, 600); // Πλάτος για να χωρέσουν οι 2 στήλες ονομάτων
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
}

private static void drawPie(Graphics g, String[] labels, double[] values) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int size = 380; 
    int x = 50, y = 80;
    double startAngle = 90; // Ξεκινάμε από την κορυφή για καλύτερη συμμετρία
    Random rand = new Random(42); 

    for (int i = 0; i < values.length; i++) {
        // Υπολογισμός μοιρών (αφού είναι ήδη διαιρεμένα, π.χ. 0.90 * 360)
        double arcAngle = values[i] * 360.0;
        
        // Επιλογή χρώματος
        Color c = new Color(rand.nextInt(200), rand.nextInt(200), rand.nextInt(200));
        g2d.setColor(c);
        
        // Σχεδίαση κομματιού - χρησιμοποιούμε αρνητικό arcAngle για δεξιόστροφη φορά
        g2d.fillArc(x, y, size, size, (int)Math.round(startAngle), (int)Math.round(-arcAngle));
        
        // ΛΕΥΚΟ ΠΕΡΙΓΡΑΜΜΑ: Πολύ σημαντικό για να ξεχωρίζουν οι λεπτές γραμμές (τα μικρά ποσοστά)
        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(1.2f));
        g2d.drawArc(x, y, size, size, (int)Math.round(startAngle), (int)Math.round(-arcAngle));
        
        // --- ΥΠΟΜΝΗΜΑ ΣΕ 2 ΣΤΗΛΕΣ ---
        int column = i / 6; 
        int row = i % 6;
        int lx = x + size + 70 + (column * 320); 
        int ly = y + (row * 40);

        g2d.setColor(c);
        g2d.fillRoundRect(lx, ly, 25, 18, 5, 5); // Πιο όμορφα κουτάκια
        
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
        // Εμφάνιση ονόματος και ποσοστού με 2 δεκαδικά (πολλαπλασιάζουμε επί 100 μόνο για το κείμενο)
        String text = labels[i] + " (" + String.format("%.2f", values[i] * 100) + "%)";
        g2d.drawString(text, lx + 35, ly + 14);
        
        startAngle -= arcAngle; // Ενημέρωση γωνίας για το επόμενο κομμάτι
    }
}
}