package mainapp;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class PercentageUI extends JFrame {

    public PercentageUI(BudgetManager manager) {
        setTitle("Υπολογισμός Ποσοστών");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        JLabel label = new JLabel("Επιλέξτε Πίνακα:");
        label.setBounds(20, 20, 200, 20);
        add(label);

        JComboBox<String> combo = new JComboBox<>(
            new String[]{"Έσοδα", "Έξοδα", "Υπουργεία"}
        );
        combo.setBounds(20, 50, 200, 25);
        add(combo);
        
        JButton calcButton = new JButton("Υπολογισμός");
        calcButton.setBounds(20, 90, 200, 30);
        add(calcButton);
        JLabel result = new JLabel("");
        result.setBounds(20, 130, 250, 20);
        add(result);
        calcButton.addActionListener(e -> {
            String table;
            switch (combo.getSelectedIndex()) {
                case 0 -> table = "esoda";
                case 1 -> table = "eksoda";
                default -> table = "ypourgeia";
            }
            double[] total = manager.getTotal(table);
                        if (total[1] == 0) {
                result.setText("Σφάλμα: Σύνολο = 0");
            } else {
                result.setText("Σύνολο: "
                        + String.format("%.2f", total[1]));
            }
        });

        setVisible(true);
    }
}
