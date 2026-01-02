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

    /**
     * Δέχεται μόνο τον πίνακα με τα ποσοστά και εμφανίζει την πίτα
     * με τα προκαθορισμένα ονόματα οικονομικών δεικτών.
     */
    public static void showEconomicPie(double[] percentages) {
        // Ο πίνακας με τα ονόματα που ζήτησες
        String[] names = {
            "gdpGrowth",
            "publicDebt",
            "surplus",
            "res",
            "recycleRate",
            "emmisionsDiff",
            "gini",
            "eduHealthExp",
            "mentalHealthPer",
            "crimeRateDiff"
        };

        JFrame frame = new JFrame("Economic Indicators Chart");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawChart(g, names, percentages, getWidth(), getHeight());
            }
        };

        frame.add(panel);
        frame.setSize(900, 500); // Λίγο πιο πλατύ για τα ονόματα
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void drawChart(Graphics g, String[] labels, double[] values, int w, int h) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int size = Math.min(w, h) - 120;
        int x = 50, y = 60;
        double startAngle = 0;
        
        // Σταθερό Seed για να έχουν οι δείκτες πάντα τα ίδια χρώματα σε κάθε run
        Random rand = new Random(42); 

        for (int i = 0; i < values.length; i++) {
            if (i >= labels.length) break; // Ασφάλεια αν τα values είναι > 10

            double arcAngle = values[i] * 360.0;
            
            // Παραγωγή χρώματος
            g2d.setColor(new Color(rand.nextInt(200), rand.nextInt(200), rand.nextInt(200)));
            
            // Σχεδίαση κομματιού
            g2d.fillArc(x, y, size, size, (int)Math.round(startAngle), (int)Math.round(arcAngle));
            
            // Σχεδίαση Υπομνήματος
            int lx = x + size + 50;
            int ly = y + (i * 30);
            g2d.fillRect(lx, ly, 20, 20);
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 13));
            String text = labels[i] + ": " + String.format("%.1f%%", values[i] * 100);
            g2d.drawString(text, lx + 30, ly + 15);
            
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