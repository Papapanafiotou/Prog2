package Statewallet_ex_2025_2026;

import java.util.ArrayList;
import java.util.List;

public class UserPageDAO {

    public List<UserPage> getUsers() {
        List<UserPage> users = new ArrayList<>();
        users.add(new UserPage("Chris", "K", "kchris", "1111"));
        users.add(new UserPage("Marios", "Voutsas", "mvoutsas", "2222"));
        users.add(new UserPage("Christine", "Gallou", "cgallou", "3333"));
        users.add(new UserPage("Axilleas", "Damianidis", "adam", "4444"));
        users.add(new UserPage("Max", "Denaxas", "mdenaxas", "5555"));
        users.add(new UserPage("John", "Mpal", "jmpal", "6666"));
        users.add(new UserPage("Jason", "Damianidis", "jdam", "7777"));
        users.add(new UserPage("Konstantina", "Papapanag", "kpap", "8888"));
        return users;
    }

    public UserPage authenticate(String username, String password) throws Exception {
        for (UserPage u : getUsers()) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        throw new Exception("Wrong username or password");
    }
}
