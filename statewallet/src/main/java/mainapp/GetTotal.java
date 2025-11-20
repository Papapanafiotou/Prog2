import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetTotal {
  private static final String url1 = "jdbc:sqlite:budget.db";
  
        public double getTotalRevenue()  { // μέθοδος που επιστρέφει τα συνολικά έσοδα
            double total = 0;
            try {
                String sql1 = "SELECT SUM(amount) AS total_revenue FROM esoda"; // εντολή sql
                Connection connect = DriverManager.getConnection(url1); // σύνδεση με βάση
                PreparedStatement pstmt = connect.prepareStatement(sql1);
                ResultSet rs = pstmt.executeQuery(); // εκτελεί την εντολή
                if (rs.next()) {
                 total = rs.getDouble("total_revenue"); // εκχωρεί το αποτέλεσμα
                }
            return total;
            } catch (SQLException e) {
               e.printStackTrace();
               return 0;
            }
        }
        public double getTotalExpenses() { // μέθοδος που επιστρέφει τα συνολικά έξοδα
            double exp = 0;
            try {
             String sql2 = "SELECT SUM(amount) AS total_expenses FROM eksoda"; //εντολή sql
             Connection connect2 = DriverManager.getConnection(url1);
                PreparedStatement pstmt2 = connect2.prepareStatement(sql2);
                ResultSet rs2 = pstmt2.executeQuery();  
                if (rs2.next()) {
                    exp = rs2.getDouble("total_expenses");
              
                }
                return exp; 
            } catch (SQLException e) {
               e.printStackTrace();
               return 0;
            }
        }


}
