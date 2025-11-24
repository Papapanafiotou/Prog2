package mainapp;

import java.util.Scanner;

public class SocGrade {
    /*η μέθοδος αυτή επιστρέφει τον βαθμό για τα κοινωνικά
    οφέλη από τη δαπάνη με βάση τον αριθμό των ανθρώπων που
    ωφελήθηκαν */
  public int getSocGrade() {
    Scanner scan = new Scanner(System.in);
    System.out.println("Πόσοι πολίτες ωφελείθηκαν;");
    int people = scan.nextInt();
    if (people < 50000) {
        return 1;
    } else if (people <100000) {
        return 2;
    } else if (people <200000) {
        return 3; 
    } else if (people <400000) {
        return 4;
    } else if (people <600000) {
        return 5;
    } else if (people < 800000) {
        return 6;
    } else if (people <1000000) {
        return 7;
    } else if (people < 1500000) {
        return 8;
    } else if (people <2000000) {
        return 9;
    } else {
        return 10;
    }        
}
}