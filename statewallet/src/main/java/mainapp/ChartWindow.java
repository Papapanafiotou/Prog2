package mainapp;

import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import java.util.Map;

/**
 * Κλάση για την εμφάνιση γραφημάτων χρησιμοποιώντας JavaFX.
 */
public class ChartWindow {
    
    private final String title;
    private final Map<Integer, Double> data;

    /**
     * Κατασκευαστής.
     * @param title Ο τίτλος του παραθύρου/διαγράμματος.
     * @param data Τα δεδομένα (Έτος -> Ποσό).
     */
    public ChartWindow(String title, Map<Integer, Double> data) {
        this.title = title;
        this.data = data;
    }

    /**
     * Εμφανίζει το παράθυρο με το διάγραμμα.
     */
    public void show() {
        Stage stage = new Stage();
        stage.setTitle(title);

        // Άξονας Χ: Έτη (Κατηγορίες)
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Έτος");

        // Άξονας Υ: Ποσά (Αριθμοί)
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Ποσό (€)");

        // Δημιουργία Ραβδογράμματος
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle(title);
        barChart.setLegendVisible(false); // Απόκρυψη υπομνήματος αν έχουμε μόνο μία σειρά

        // Εισαγωγή δεδομένων
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Ποσά");

        for (Map.Entry<Integer, Double> entry : data.entrySet()) {
            // Μετατροπή του έτους σε String για τον CategoryAxis
            series.getData().add(new XYChart.Data<>(String.valueOf(entry.getKey()), entry.getValue()));
        }

        barChart.getData().add(series);

        // Ρύθμιση σκηνής και εμφάνιση
        Scene scene = new Scene(barChart, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}