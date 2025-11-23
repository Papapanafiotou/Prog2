import java.util.Scanner;

public class Efficiency {
  /*Η μέθοδος αυτή υπολογίζει την αποδοτικότητα της δαπάνησ σε ένα 
  υπουργείο μέσα από ένα σύστημα βαθμών σε 4 βασικές κατηγορίες 
  επίδρασης, οικονομική κοινωνική στρατηγική και περιβαλλοντική.
  Κάθε βαθμός έχει διαφορετική επίδραση στο τελικό αποτέλεσμα
  λόγω των βαρών που δίνονται απο τον χρήστη για κάθε κατηγορία */

  public double getEfficiency() {
    Scanner scan = new Scanner(System.in);
    Search search = new Search();
    System.out.println("Για ποιο υπουργείο θα υπολογιστεί η αποδοτικότητα");
    String ministry= scan.nextLine();
    double expense = search.searchAmount(ministry);
    if (expense == 0) {
        System.out.println("Το υπουργείο δεν βρέθηκε, δοκιμάστε ξανά!");
        return 0.0;
    } else {
        System.out.println("Παρακαλώ εισάγετε τα βάρη για τις 4 κατηγορίες επίδρασης.");
        double w1= scan.nextDouble();
        double w2= scan.nextDouble();
        double w3= scan.nextDouble();
        double w4= scan.nextDouble();
        EconGrade g1 = new EconGrade();
        int gr1 = g1.getEcGrade();
        EnvGrade g2= new EnvGrade();
        int gr2 = g2.getEnvGrade();
        SocGrade g3 = new SocGrade();
        int gr3 = g3.getSocGrade();
        System.out.println("Πόσο σημαντική ήταν η στρατηγική επίδραση της δαπάνης σύμφωνα με ειδικούς");
        int gr4= scan.nextInt(); 
        do{
        if (gr4 < 1 || gr4 > 10) {
            System.out.println("Εισάγετε αριθμό μεταξυ του 1 και του 10.");
            gr4= scan.nextInt(); 
        }
    } while (gr4<1 || gr4>10);
    double efficiency = (w1 *gr1 + w2 *gr2 + w3 *gr3 + w4 *gr4) *1000000/expense;
    System.out.println("Η αποδοτικότητα του" + ministry + 
    "ισούται με" + efficiency);
    return efficiency;
  }   
}
}
