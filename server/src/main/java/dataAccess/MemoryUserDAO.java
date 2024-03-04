package dataAccess;

import model.UserData;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    public static HashMap<String, UserData> userData = new HashMap<>();

    public boolean verifyUser(String username, String password) {
        UserData user = userData.get(username);
        if (userData.containsKey(username)) {
            return user.password().equals(password);
        } else {
            return false;
        }
    }

    public void addUser(UserData user) throws DataAccessException {
        if (userData.containsKey(user.username())) {
            throw new DataAccessException(403, "Error: already taken");
        } else {
            userData.put(user.username(), user);
        }
    }

    public void clear() {
        userData.clear();
    }

}
