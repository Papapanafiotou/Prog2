package mainapp;

import java.util.Scanner;
import java.sql.SQLException;
public class StateWallet {
    public static void main(String[] args) {
        try {
        PinakesImporter importer = new PinakesImporter("jdbc:sqlite:budget.db");
importer.importAll();  
        System.out.println("Ολα ok!");
        System.out.println(" --ΜΕΝΟΥ ΕΠΙΛΟΓΩΝ-- ");
        
        Scanner scan = new Scanner(System.in);
        int choice = scan.nextInt();
        switch (choice) {
    case 1:
        budgetChar b1 = new budgetChar();
        b1.budgetCharacterism();;
        break;
    case 2:
        break;
    case 3:
        break;
    case 4:
        break;
    case 5:
        break;
    default:
        break;
}
    } catch (Exception e) {
        e.printStackTrace();
    }
    } 
}
