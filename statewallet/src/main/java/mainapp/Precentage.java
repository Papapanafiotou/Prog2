package mainapp;

import java.util.Scanner;

public class Precentage {
    /*  H μέθοδος getPrecentage υπολογίζει το ποσοστό ενός εσόδου ή εξόδου
    από τα συνολικά έσοδα ή έξοδα 
    */
    public double getPrecentage() {
        double precent = 0.0;
        Scanner scan = new Scanner(System.in);
        System.out.println("Για ποιον λογαριασμό θέλετε να υπολογίσετε " +
         "το ποσοστό;");
        String name = scan.nextLine();
        Search search = new Search();
        String table = search.searchTable(name);
        GetTotal t = new GetTotal();
        double total;
        double amount = search.searchAmount(name); 
        if (table == "esoda") {
            total = t.getTotalRevenue();
            try {
                precent = amount / total;
                System.out.println("Το ποσοστό του " + name +
                " στα συνολικά έσοδα έιναι " + precent);
            } catch (ArithmeticException e) {
                System.out.println("Δεν είναι δυνατή η διαίρεση με το μηδέν!");
            }
        } else {
            total = t.getTotalExpenses();
             try {
                precent = amount / total;
                System.out.println("Το ποσοστό του " + name +
                " στα συνολικά έξοδα έιναι " + precent);
            } catch (ArithmeticException e) {
                System.out.println("Δεν είναι δυνατή η διαίρεση με το μηδέν!");
            }
        } 
        return precent;
    }
}
