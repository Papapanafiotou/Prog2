package mainapp;

import java.util.Random;
import java.util.Scanner;

/**
 * Διαχειρίζεται το μενού σύνδεσης και δημιουργίας λογαριασμού (Κονσόλα).
 */
public final class Log {

    /** Επιλογή: Δημιουργία. */
    private static final int MENU_CREATE = 1;
    /** Επιλογή: Σύνδεση. */
    private static final int MENU_LOGIN = 2;
    /** Επιλογή: Αλλαγή κωδικού. */
    private static final int MENU_CHANGE = 3;
    /** Επιλογή: Ανάκτηση κωδικού. */
    private static final int MENU_FORGOT = 4;
    /** Επιλογή: Έξοδος. */
    private static final int MENU_EXIT = 5;

    /** Μέγιστες προσπάθειες σύνδεσης. */
    private static final int MAX_ATTEMPTS = 5;
    /** Μήκος τυχαίου κωδικού. */
    private static final int RANDOM_PASS_LENGTH = 10;
    /** Επιλογή τυχαίου κωδικού. */
    private static final int RANDOM_OPTION = 1;

    /**
     * Εμφανίζει το μενού και διαχειρίζεται την αλληλεπίδραση.
     *
     * @return true αν γίνει επιτυχής σύνδεση, false αν γίνει έξοδος.
     */
    public boolean logMenu() {
        Accounts acc = new Accounts();
        acc.createTable();
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("=============================================");
            System.out.println("ENTER THESE FOR TESTING");
            System.out.println("USERNAME: test1");
            System.out.println("PASSWORD: Test12345!");
            System.out.println("=============================================");
            System.out.println("\n--- Μενού ---");
            System.out.println("1. Δημιουργία λογαριασμού");
            System.out.println("2. Σύνδεση");
            System.out.println("3. Αλλαγή κωδικού");
            System.out.println("4. Ξέχασα τον κωδικό");
            System.out.println("5. Έξοδος");
            int answer = scan.nextInt();

            if (answer == MENU_CREATE) {
                handleCreate(scan, acc);
            } else if (answer == MENU_LOGIN) {
                if (handleLogin(scan, acc)) {
                    return true;
                }
            } else if (answer == MENU_CHANGE) {
                handleChange(scan, acc);
            } else if (answer == MENU_FORGOT) {
                scan.nextLine();
                System.out.println("Εισάγετε το username σας");
                String name = scan.nextLine();
                acc.forgotPass(name);
            } else if (answer == MENU_EXIT) {
                return false;
            }
        }
    }

    private void handleCreate(final Scanner scan, final Accounts acc) {
        scan.nextLine();
        System.out.println("Εισάγετε το username");
        String name = scan.nextLine();
        System.out.println("Εισάγετε αριθμό ταυτότητας (λατινικά).");
        String numID = scan.nextLine();
        System.out.println("1 για τυχαίο κωδικό, άλλο για χειροκίνητο.");
        String pass;
        int an = scan.nextInt();
        if (an == RANDOM_OPTION) {
            Random random = new Random();
            String chars = "abcdefghijklmnopqrstuvwxyz0123456789@#!$_+";
            StringBuilder randomString = new StringBuilder();
            for (int i = 0; i < RANDOM_PASS_LENGTH; i++) {
                int index = random.nextInt(chars.length());
                randomString.append(chars.charAt(index));
            }
            pass = randomString.toString();
            System.out.println("Ο κωδικός ειναι: " + pass);
        } else {
            System.out.println("Εισάγετε τον κωδικό! (8+ chars)");
            scan.nextLine();
            boolean valid;
            do {
                pass = scan.nextLine();
                valid = Accounts.validatePassword(pass);
            } while (!valid);
        }
        acc.createAccount(name, pass, numID);
    }

    private boolean handleLogin(final Scanner scan, final Accounts acc) {
        scan.nextLine();
        System.out.println("Εισάγετε το username σας");
        String name = scan.nextLine();
        String realpass = acc.getPassword(name);
        int count = 0;
        boolean connect;
        do {
            System.out.println("Πληκτρολογήστε τον κωδικό πρόσβασης");
            String userPass = scan.nextLine();
            connect = acc.logIn(realpass, userPass);
            count++;
            if (connect) {
                return true;
            }
        } while (count < MAX_ATTEMPTS);

        if (count == MAX_ATTEMPTS) {
            System.out.println("Ξεχάσατε τον κωδικό; (1 για ανάκτηση)");
            int an = scan.nextInt();
            if (an == 1) {
                acc.forgotPass(name);
            }
        }
        return false;
    }

    private void handleChange(final Scanner scan, final Accounts acc) {
        scan.nextLine();
        System.out.println("Εισάγετε το username σας");
        String name = scan.nextLine();
        String realpass = acc.getPassword(name);
        System.out.println("Πληκτρολογήστε τον τρέχον κωδικό");
        String userPass = scan.nextLine();
        if (acc.logIn(realpass, userPass)) {
            boolean valid;
            String newP;
            System.out.println("Εισάγετε τον νέο κωδικό!");
            do {
                newP = scan.nextLine();
                valid = Accounts.validatePassword(newP);
            } while (!valid);
            acc.newPass(newP, name);
        } else {
            System.out.println("Λάθος κωδικός!");
        }
    }
}
