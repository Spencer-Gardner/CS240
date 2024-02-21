package dataAccess;

import model.UserData;

import java.util.HashMap;
import java.util.HashSet;
import model.UserData;

public class MemoryUserDAO implements UserDAO {

    public static HashMap<String, UserData> userData = new HashMap<>();

    public UserData getUser(String username) {
        return userData.get(username);
    }

    public static void addUser(UserData user) {
        userData.put(user.username(), user);
    }

    public static void clear() {
        userData.clear();
    }

}
