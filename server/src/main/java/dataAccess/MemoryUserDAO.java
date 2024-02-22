package dataAccess;

import model.UserData;

import java.util.HashMap;
import model.UserData;

public class MemoryUserDAO implements UserDAO {

    public static HashMap<String, UserData> userData = new HashMap<>();

    public static boolean verifyUser(String username) {
        return userData.containsKey(username);
    }

    public static void addUser(UserData user) throws DataAccessException {
        try {
            if (userData.get(user.username()) == null) {
                userData.put(user.username(), user);
            } else {
                throw new DataAccessException(403, "Error: already taken");
            }
        } catch (Exception e) {
            throw new DataAccessException(500, "Error: description");
        }
    }

    public static void clear() throws DataAccessException {
        try {
            userData.clear();
        } catch (Exception e) {
            throw new DataAccessException(500, "Error: description");
        }
    }
}
