package mainapp;

import java.util.Random;
import java.util.Scanner;
public class Log {
    public boolean logMenu() {
    Accounts acc = new Accounts();
    acc.createTable();
    Scanner scan = new Scanner(System.in);
    while (true) {
            System.out.println("\n--- Μενού ---");
            System.out.println("1. Δημιουργία λογαριασμού");
            System.out.println("2. Σύνδεση");
            System.out.println("3. Αλλαγή κωδικού");
            System.out.println("4. Έξοδος");
    int answer = scan.nextInt();
    if (answer == 1) {
        scan.nextLine();
        System.out.println("Εισάγετε το username");
        String name = scan.nextLine();
        scan.nextLine();
        System.out.println("Εισάγετε τον αριθμό ταυτότητάς "
            + "με λατινικούς χαρακτήρες και χωρίς κενό. Ο αριθμός ταυτότητας"
            + "είναι απαρραίτητος για την ανάκτηση του κωδικόυ σας σε "
            + "περίπτωση που τον ξεχάσετε!"
        );
        String numID = scan.nextLine(); 
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
        } while (!valid);
        }
        acc.createAccount(name, pass, numID);
    } else if (answer == 2) {
        scan.nextLine();
        System.out.println("Εισάγετε το username σας");
        String name = scan.nextLine();
        String realpass = acc.getPassword(name);
        int count = 0;
        boolean connect;
        do {
            System.out.println("Πληκτρολογήστε τον κωδικό πρόσβασης");
            String userPass = scan.nextLine();
            connect =acc.logIn(realpass, userPass);
            count++;
            if (connect == true) {
               return true;
            }
        } while (connect == false && count < 5);
        if (count == 5) {
            System.out.println("Αν ξεχάσατε τον κωδικό σας πατήστε 1 "
            + "αλλιώς άλλο ψηφίο");
            int an = scan.nextInt();
            if (an == 1) {
                acc.forgotPass(name);
            } else {
                return false;
            }
        } 
    } else if (answer == 3) {
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
        } else {
            System.out.println("Λάθος κωδικός! Προσπαθήστε ξανά!");
        }   
    } else if (answer == 4) {
                return false;
            }
    }
    }
}