package mainapp;

public class Constrains {

    // Έλεγχος αν ο αριθμός είναι θετικός //
    public static boolean negativeAmount(double amount){
        if (amount < 0) {
                System.out.println("ΣΦΑΛΜΑ: Το ποσό δεν μπορεί να είναι αρνητικό.");
                return true;
            }
        return false;
    }

}
