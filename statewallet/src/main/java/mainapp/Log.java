package mainapp;

import java.util.Random;
import java.util.Scanner;
public class Log {
    public boolean logMenu() {
    Accounts acc = new Accounts();
    acc.createTable();
    System.out.println("Για δημιουργία λογαριασμού πατήστε 1, για σύνδεση 2," 
    + "για αλλαγή κωδικού 3");
    Scanner scan = new Scanner(System.in);
    int answer = scan.nextInt();
    if (answer == 1) {
        scan.nextLine();
        System.out.println("Εισάγετε το username");
        String name = scan.nextLine();
        System.out.println("Για τυχαίο κωδικό πατήστε 1, "
          + "οποιοδήποτε άλλο νούμερο για να εισάγετε κωδικό");
        String pass;
        int an = scan.nextInt();
        if (an == 1) {
            Random random = new Random();
            String characterSet = "abcdefghijklmnopqrstuvwxyz0123456789@#!$_+";
            String randomString = "";
            for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(characterSet.length());
            char randomChar = characterSet.charAt(randomIndex);
            randomString = randomString + randomChar; 
            }
            pass = randomString;
            System.out.println("Ο κωδικός ειναι" + pass);
        } 
        else {
            System.out.println("Εισάγετε τον κωδικό!");
            System.out.println("Ο κωδικός πρόσβασης πρέπει να περιλαμβάνει:"
            + " Τουλάχιστον 8 χαρακτήρες, ένα πεζό και ένα κεφαλαίο γράμμα, "
            + " έναν αριθμό και έναν ειδικό χαρακτήρα.");
            scan.nextLine();
            boolean valid;
            do {

            pass = scan.nextLine();
            valid = acc.validatePassword(pass);
        } while (valid = false);
        }
        acc.createAccount(name, pass);
        return true; 
    } else if (answer == 2) {
        scan.nextLine();
        System.out.println("Εισάγετε το username σας");
        String name = scan.nextLine();
        String realpass = acc.getPassword(name);
        System.out.println("Πληκτρολογήστε τον κωδικό πρόσβασης");
        String userPass = scan.nextLine();
        boolean connect = acc.logIn(realpass, userPass);
        if (connect == true) {
            return true;
        }
    } else {
        scan.nextLine();
        System.out.println("Εισάγετε το username σας");
        String name = scan.nextLine();
        String realpass = acc.getPassword(name);
        System.out.println("Πληκτρολογήστε τον τρέχον κωδικό πρόσβασης");
        String userPass = scan.nextLine();
        boolean connect = acc.logIn(realpass, userPass);
        if (connect) {
            boolean valid;
            String newP;
            System.out.println("Εισάγετε τον νέο κωδικό πρόσβασης!");
            System.out.println("Ο κωδικός πρόσβασης πρέπει να περιλαμβάνει:"
            + " Τουλάχιστον 8 χαρακτήρες, ένα πεζό και ένα κεφαλαίο γράμμα, "
            + " έναν αριθμό και έναν ειδικό χαρακτήρα.");
            do {
              newP = scan.nextLine();
              valid = acc.validatePassword(newP);
            } while (valid == false);
            acc.newPass(newP,name);
            return true;
        } else {
            System.out.println("Λάθος κωδικός! Προσπαθήστε ξανά!");
        }   
    }
    return false;
    }
}