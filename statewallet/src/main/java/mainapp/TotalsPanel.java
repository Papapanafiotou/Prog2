package mainapp;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class TotalsPanel extends JPanel {
     private final BudgetManager manager;

    private final JLabel originalLabel;
    private final JLabel currentLabel;
    private final JLabel diffLabel;

    private static final Color POSITIVE = new Color(34, 139, 34);

    public TotalsPanel(final BudgetManager manager) {
        this.manager = manager;

        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(BorderFactory.createTitledBorder("Σύνολα Πίνακα"));

        Font font = new Font("Segoe UI", Font.BOLD, 13);