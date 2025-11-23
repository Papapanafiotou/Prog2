import java.util.Scanner;
/*Η μέθοδος δίνει τον βαθμό για τα περιβαλλοντικά ωφέλη
απο το 1 μεχρι το 10 για την δαπάνη
 ανάλογα με το ποσοστό εξοικονόμησης ενέργειας ή μείωσης ρύπων  */
public class EnvGrade {
  public int getEnvGrade() {
    Scanner scan = new Scanner(System.in);
    System.out.println("Πόσο τοις εκατό μειώθηκαν οι ρύποι/ πόσο τοις εκατό μειώθηκε η κατανάλωση ενέργειας?");
    double precent = scan.nextDouble();
    if (precent < 0.01) {
        return 1;
    } else if (precent < 0.03) {
        return 2;
    } else if (precent < 0.05) {
        return 3;
    } else if (precent < 0.08) {
        return 4;
    } else if (precent < 0.12) {
        return 5;    
    } else if (precent < 0.15) {
        return 6;
    } else if (precent < 0.18) {
        return 7;
    } else if (precent < 0.22) {
        return 8;
    } else if (precent < 0.25) {
        return 9;
    } else {
        return 1;
    }
  } 
}
