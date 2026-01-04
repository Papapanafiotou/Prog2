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

    public static void showPieChart(String[] names, double[] percentages) {
        JFrame frame = new JFrame("Πίτα Δεδομένων");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Καλούμε τη σχεδίαση περνώντας μόνο τα 2 δικά σου ορίσματα
                drawPie(g, names, percentages);
            }
        };

        frame.add(panel);
        frame.setSize(800, 500); 
        frame.setVisible(true);
    }

    // Εσωτερική μέθοδος σχεδίασης
    private static void drawPie(Graphics g, String[] labels, double[] values) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = 300; // Σταθερό μέγεθος για να μην μπερδεύουν τα ορίσματα
        int x = 50, y = 50;
        double startAngle = 0;
        Random rand = new Random(42); 

        for (int i = 0; i < values.length; i++) {
            double arcAngle = values[i] * 360.0;
            g2d.setColor(new Color(rand.nextInt(200), rand.nextInt(200), rand.nextInt(200)));
            g2d.fillArc(x, y, size, size, (int)Math.round(startAngle), (int)Math.round(arcAngle));
            
            // Legend (Υπόμνημα)
            g2d.fillRect(x + size + 40, y + (i * 25), 15, 15);
            g2d.setColor(Color.BLACK);
            g2d.drawString(labels[i], x + size + 65, y + (i * 25) + 12);
            
            startAngle += arcAngle;
        }
    }

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
}