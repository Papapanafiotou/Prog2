package mainapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class MinMaX {
    private String database_url;
    /* H μέθοδοσ αυτή ρωτά τον χρήστη άν θέλει να βρεί ελάχιστο ή 
     * μέγιστο και στην συνέχεια τον ρωτά για ποιά κατηγορία λογα-
     * ριαμών. Επιστρέφει την τιμή του μεγίστου/ ελάχιστου ποσού
     * που υπολογίζεται μέσω των μεθόδων παρακάτω
     */
    public MinMaX(String url) {
        this.database_url = url;
    }

    public void showMinMax() {
        long value = 0;
        Scanner scan = new Scanner(System.in, "CP737");
        String name;
        Search s = new Search(database_url);
        System.out.println("Θα θέλατε να υπολογίσετε μέγιστο ή ελάχιστο; (1 για ελάχιστο - 2 για μέγιστο )");
        int answer = scan.nextInt();
        scan.nextLine();
        boolean flag3 = false;
        do {
        if (answer == 1) {
            System.out.println("Θέλετε το ελάχιστο έσοδο, έξοδο ή την ελάχιστη δαπάνη υπουργείου;");
            System.out.println("( 1 για έσοδο, 2 για έξοδο, 3 για δαπάνη υπουργείου )");
            int answer2 = scan.nextInt();
            scan.nextLine();
            boolean flag = false;
            do {
            if (answer2 == 1) {
                value = (long) getMinMax(1, 1);
                name = s.searchString(value);
                flag = true;
                System.out.println("Το ελάχιστο έσοδο είναι το "
                + name + " με ποσό " + value);
            } else if (answer2 == 2) {
                value = (long) getMinMax(1, 2);
                name = s.searchString(value);
                flag = true;
                 System.out.println("Το ελάχιστο έξοδο είναι το "
                + name +" με ποσό " + value);
            }else if (answer2 == 3) { 
                value = (long) getMinMax(1, 3);
                name = s.searchString(value);
                flag = true;
                System.out.println("Η ελάχιστη δαπάνη σε υπουργείο "  
                 + "είναι στο "+ name + " με ποσο "+ value);
            } else {
                System.out.println("Ο τύπος δέν αναγνωρίζεται");
            }
        } while (flag = false); 
        flag3 = true; 
        } else if (answer == 2) {
            System.out.println("Θέλετε το μέγιστο έσοδο, έξοδο ή την μέγιστη δαπάνη υπουργείου;");
            System.out.println("( 1 για έσοδο, 2 για έξοδο, 3 για δαπάνη υπουργείου )");
            int answer3 = scan.nextInt();
            scan.nextLine();
            boolean flag2 = false;
            do {
            if (answer3 == 1) {
                value = (long) getMinMax(2, 1);
                name = s.searchString(value);
                flag2 = true;
                System.out.println("Tο μέγιστο έσοδο είναι το "
                + name +" με ποσό " + value);
            } else if (answer3 == 2) {
                value = (long) getMinMax(2, 2);
                name = s.searchString(value);
                flag2 = true;
                 System.out.println("Το μέγιστο έξοδο είναι το "
                 + name + " με ποσό " + value);
            } else if (answer3 == 3) { 
                value = (long) getMinMax(2, 3);
                name = s.searchString(value);
                flag2 = true;
                System.out.println("Η μέγιστη δαπάνη σε υπουργείο" + 
                " είναι στο "+ name +" με ποσό " + value);
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

public double getMinMax (int x, int y) {
    String sql = null;
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
      try (Connection conn = DriverManager.getConnection(database_url);
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
        }
              






