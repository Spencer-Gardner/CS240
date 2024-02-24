package dataAccess;

import model.UserData;

import java.util.HashMap;
import model.UserData;

public class MemoryUserDAO implements UserDAO {

    public static HashMap<String, UserData> userData = new HashMap<>();

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static boolean verifyUser(String username) {
        return userData.containsKey(username);
    }

    public static boolean checkPassword(String username, String password) {
        UserData user = userData.get(username);
        return user.password().equals(password);
    }

    public static void addUser(UserData user) throws DataAccessException {
        if (userData.containsKey(user.username())) {
            throw new DataAccessException(403, "Error: already taken");
        } else {
            userData.put(user.username(), user);
        }
    }

    public static void clear() {
        userData.clear();
    }
}
