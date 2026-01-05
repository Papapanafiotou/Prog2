package mainapp;
import javax.swing.*;

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