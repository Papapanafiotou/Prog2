package mainapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MinMaX {
    /* H μέθοδοσ αυτή ρωτά τον χρήστη άν θέλει να βρεί ελάχιστο ή 
     * μέγιστο και στην συνέχεια τον ρωτά για ποιά κατηγορία λογα-
     * ριαμών. Επιστρέφει την τιμή του μεγίστου/ ελάχιστου ποσού
     * που υπολογίζεται μέσω των μεθόδων παρακάτω
     */
    public void showMinMax() {
        double value = 0;
        Scanner scan = new Scanner(System.in);
        Search s = new Search();
        String name = s.searchString(value);
        System.out.println("Θα θέλατε να υπολογίσετε μέγιστο ή ελάχιστο;");
        String answer = scan.nextLine();
        boolean flag3 = false;
        do {
        if (answer.equals("ελάχιστο")) {
            System.out.println("Θέλετε το ελάχιστο έσοδο, έξοδο ή την ελάχιστη δαπάνη υπουργείου;");
            String answer2 = scan.nextLine();
            boolean flag = false;
            do {
            if (answer2.equals("έσοδο")) {
                value = getMinMax(1, 1);
                flag = true;
                System.out.println("Το ελάχιστο έσοδο είναι το "
                + name + "με ποσό" + value);
            } else if (answer2.equals("έξοδο")) {
                value = getMinMax(1, 2);
                flag = true;
                 System.out.println("Το ελάχιστο έξοδο είναι το "
                + name +"με ποσό" + value);
            }else if (answer2.equals("δαπάνη υπουργείου")) { 
                value = getMinMax(1, 3);
                flag = true;
                System.out.println("Η ελάχιστη δαπάνη σε υπουργείο "  
                 + "είναι στο"+ name + "με ποσο "+ value);
            } else {
                System.out.println("Ο τύπος δέν αναγνωρίζεται");
            }
        } while (flag = false); 
        flag3 = true; 
        } else if (answer.equals("μέγιστο")) {
            System.out.println("Θέλετε το μέγιστο έσοδο, έξοδο ή την μέγιστη δαπάνη υπουργείου;");
            String answer3 = scan.nextLine();
            boolean flag2 = false;
            do {
            if (answer3.equals("έσοδο")) {
                value = getMinMax(2, 1);
                flag2 = true;
                System.out.println("Tο μέγιστο έσοδο είναι το "
                + name +"με ποσό" + value);
            } else if (answer3.equals("έξοδο")) {
                value = getMinMax(2, 2);
                flag2 = true;
                 System.out.println("Το ελάχιστο έξοδο είναι το "
                 + name + "με ποσό" + value);
            } else if (answer3.equals("δαπάνη υπουργείου")) { 
                value = getMinMax(2, 3);
                flag2 = true;
                System.out.println("Η μέγιστη δαπάνη σε υπουργείο" + 
                "είναι στο"+ name +"με ποσό" + value);
            } else {
                System.out.println("Ο τύπος δέν αναγνωρίζεται");
            }
        } while (flag2 = false);
        flag3 = true;  
        } else {
            System.out.println( "Ο τύπος δεν αναγνωρίζεται, δοκιμάστε ξανα!");
        }
    } while (flag3 = false);
    }
}
public double getMinMax (int x, int y) {
    final String url = "jdbc:sqlite:budget.db";  
    String sql;
        if (x==1) {
            if (y == 1) {
              sql = "SELECT MIN(amount) AS value FROM esoda";
            } else if (y == 2) {
              sql = "SELECT MIN(amount) AS value FROM eksoda";
            } else if (y == 3) {
              sql = "SELECT MIN(amount) AS value FROM ypourgeia";
            }
        } else if (x == 2) {
            if (y == 1) {
              sql = "SELECT MAX(amount) AS value FROM esoda";
            } else if (y == 2) {
               sql = "SELECT MAX(amount) AS value FROM eksoda";  
            } else if (y == 3) {
               sql = "SELECT MAX(amount) AS value FROM ypourgeia"; 
            }
        }
        double value = 0;
      try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    value = rs.getDouble("value");
                }
             } catch (SQLException e) {
                e.printStackTrace();;
             }
    return value;           

}


