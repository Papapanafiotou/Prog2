package mainapp;

import java.util.Scanner;

public class Precentage {
    /*  H μέθοδος getPrecentage υπολογίζει το ποσοστό ενός εσόδου ή εξόδου
    από τα συνολικά έσοδα ή έξοδα 
    */
    public double getPrecentage() {
        BudgetManager manager = new BudgetManager(null);
        double precent = 0.0;
        Scanner scan = new Scanner(System.in, "CP737");
        System.out.println("Για ποιον λογαριασμό θέλετε να υπολογίσετε " +
         "το ποσοστό;");
        String name = scan.nextLine();
        Search search = new Search(null);
        String table = search.searchTable(name);
        double[] total;
        double amount = search.searchAmount(name); 
        if (table == "esoda") {
            total = manager.getTotal("esoda");
            double t = total[0];
            try {
                precent = (amount / t) * 100;
                System.out.println("Το ποσοστό του " + name +
                " στα συνολικά έσοδα έιναι " + precent + " %");
            } catch (ArithmeticException e) {
                System.out.println("Δεν είναι δυνατή η διαίρεση με το μηδέν!");
            }
        } else {
            total = manager.getTotal("eksoda");
            double t = total[0];
             try {
                precent = (amount / t) * 100;
                System.out.println("Το ποσοστό του " + name +
                " στα συνολικά έξοδα έιναι " + precent + " %");
            } catch (ArithmeticException e) {
                System.out.println("Δεν είναι δυνατή η διαίρεση με το μηδέν!");
            }
        } 
        return precent;
    }
}
