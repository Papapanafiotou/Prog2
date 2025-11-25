package mainapp;

import java.util.Scanner;

import javax.swing.JOptionPane;

import java.sql.SQLException;
public class StateWallet {
    public static void main(String[] args) {
        try {
        PinakesImporter importer = new PinakesImporter("jdbc:sqlite:budget.db");
importer.importAll();  
        System.out.println("Ολα ok!");
        System.out.println(" --ΜΕΝΟΥ ΕΠΙΛΟΓΩΝ-- ");
        String menu = "1. Χαρακτηρισμός προϋπολογισμού\n" +
              "2. Αποδοτικότητα υπουργείου\n" +
              "3. Εύρεση στοιχείου\n" +
              "4. Εύρεση μέγιστου/ελάχιστου\n" +
              "5. Ποσοστό";     
        System.out.println(menu);
        System.out.println("Πληκτρολογήστε την επιλογή σας !");      
        Scanner scan = new Scanner(System.in);
        int choice = scan.nextInt();
        switch (choice) {
    case 1:
        budgetChar b1 = new budgetChar();
        b1.budgetCharacterism();;
        break;
    case 2:
        Efficiency eff = new Efficiency();
        eff.getEfficiency();
        break;
    case 3:
        Search search = new Search();
        System.out.print("Πληκτρολόγησε το όνομα του Υπουργείου: ");
        Scanner scanner = new Scanner(System.in, "CP737");
        String inputName = scanner.nextLine();
        search.searchAmount(inputName);
        break;
    case 4:
        MinMaX minmax = new MinMaX();
        minmax.showMinMax();
        break;
    case 5:
        Precentage p1 = new Precentage();
        double prec = p1.getPrecentage();
        break;
    default:
        break;
}
    } catch (Exception e) {
        e.printStackTrace();
    }
}
} 

