package mainapp;

import java.sql.SQLException;

/**
 * Hello world!
 */
public class StateWallet {
    public static void main(String[] args) {
        try {
            PinakesImporter test = new PinakesImporter();
        }
        catch (SQLException e ) {
            System.out.println("e");
        }

    }
}
