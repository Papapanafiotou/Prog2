package mainapp;

import java.util.Scanner;
public class Log {
    public void logMenu() {
    Accounts acc = new Accounts();
    acc.createTable();
    System.out.println("Για δημιουργία λογαριασμού πατήστε 1, για σύνδεση 2");
}
}