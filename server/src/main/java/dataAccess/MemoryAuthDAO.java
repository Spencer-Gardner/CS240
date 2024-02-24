package dataAccess;

import model.UserData;
import java.util.Arrays;
import java.util.HashMap;
import java.security.SecureRandom;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    public static HashMap<String, String> authData = new HashMap<>();

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

    public static boolean verifyAuth(String authToken) throws DataAccessException {
        return authData.containsValue(authToken);
    }

    public static String getAuth(String username) throws DataAccessException {
        if (authData.containsKey(username)) {
            return authData.get(username);
        }
        throw new DataAccessException(401, "Error: unauthorized");
    }

    public static String getUser(String authToken) throws  DataAccessException {
        for (Map.Entry<String, String> entry : authData.entrySet()) {
            if (entry.getValue().equals(authToken)) {
                return entry.getKey();
            }
        }
        throw new DataAccessException(401, "Error: unauthorized");
    }

    public static void addAuth(String username) {
        String token = UUID.randomUUID().toString();
        authData.put(username, token);
    }

    public static void removeAuth(String username, String authToken) throws DataAccessException {
        if (authData.containsValue(authToken)) {
            authData.remove(username);
        } else {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }

    public static void clear() {
        authData.clear();
    }
}
