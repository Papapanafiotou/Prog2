package mainapp;

public class Constrains {

    public static boolean negativeAmount(double amount){
        if (amount < 0) {
                System.out.println("!! ΣΦΑΛΜΑ: Το ποσό δεν μπορεί να είναι αρνητικό.");
                return true;
            }
        return false;
    }

}
