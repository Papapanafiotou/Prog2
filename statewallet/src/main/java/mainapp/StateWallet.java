package mainapp;

import java.sql.SQLException;
public class StateWallet {
    public static void main(String[] args) {
        try {
        PinakesImporter importer = new PinakesImporter("jdbc:sqlite:budget.db");
importer.importAll();  
        System.out.println("Ολα ok!");
    } catch (Exception e) {
        e.printStackTrace();
    }
    } 
}
