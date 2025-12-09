package mainapp;

import java.util.Scanner;
public class Log {
    public void logMenu() {
    Accounts acc = new Accounts();
    acc.createTable();
    System.out.println("Για δημιουργία λογαριασμού πατήστε 1, για σύνδεση 2");
    Scanner scan = new Scanner(System.in);
    int answer = scan.nextInt();
    if (answer == 1) {
        System.out.println("Εισάγετε το username");
        String name = scan.nextLine();
        System.out.println("Εισάγετε τον κωδικό προσβασης");
        String pass = scan.nextLine();
        acc.createAccount(name, pass);
        return; 
    } else {
        System.out.println("Εισάγετε το username σας");
        String name = scan.nextLine();
        String realpass = acc.getPassword(name);
        System.out.println("Πληκτρολογήστε τον κωδικό πρόσβασης");
        String userPass = scan.nextLine();
        boolean connect = acc.logIn(realpass, userPass);
        if (connect == false) {
            return;
        }
    }
}
}