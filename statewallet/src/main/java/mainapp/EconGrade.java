package mainapp;

import java.util.Scanner;

public class EconGrade {
    /*H μέθοδος αυτή επιστρέφει τον βαθμό των οικονομικών ωφελών από τη δαπάνη
    με βάση την μείωση/αύξηση των εσόδων/ εξόδων που 
    δημιουργείται από τη δαπάνη */
public int getEcGrade() {
    Scanner scan = new Scanner(System.in);
System.out.println("Ποιο είναι το ποσό αύξησης/ μείωσης εσόδων/ εξόδων?");
double p=scan.nextDouble();
if (p < 1000000) {
    return 1;
} else if (p < 1500000) {
    return 2;
} else if (p < 2500000) {
    return 3;
} else if (p < 5000000) {
    return 4;
} else if (p < 7500000) {
    return 5;
} else if (p < 10000000) {
    return 6; 
} else if (p < 15000000) {
    return 7;
} else if (p < 22000000) {
    return 8;
} else if (p < 29500000) {
    return 9;
} else {
    return 10;
}
}
}
