package dataAccess;

import model.UserData;
import java.util.Arrays;
import java.util.HashMap;
import java.security.SecureRandom;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {

    public static HashMap<String, String> authData = new HashMap<>();

    public static boolean verifyAuth(String authToken) throws DataAccessException {
        try {
            return authData.containsValue(authToken);
        } catch (Exception e) {
            throw new DataAccessException(500, "Error: description");
        }
    }

    public static String getAuth(String username) throws DataAccessException {
        try {
            return authData.get(username);
        } catch (Exception e) {
            throw new DataAccessException(500, "Error: description");
        }
    }

    public static String getUser(String authToken) throws  DataAccessException {
        try {
            String username = "";
            for (Map.Entry<String, String> entry : authData.entrySet()) {
                if (entry.getValue().equals(authToken)) {
                    username = entry.getKey();
                }
            }
            return username;
        } catch (Exception e) {
            throw new DataAccessException(500, "Error: description");
        }
    }

    public static void addAuth(String username) throws DataAccessException {
        try {
            SecureRandom random = new SecureRandom();
            byte[] bytes = new byte[20];
            random.nextBytes(bytes);
            String token = Arrays.toString(bytes);
            authData.put(username, token);
        } catch (Exception e) {
            throw new DataAccessException(500, "Error: description");
        }
    }

    public static void removeAuth(String authToken) throws DataAccessException {
        try {
            if (authData.containsValue(authToken)) {
                authData.remove(authToken);
            } else {
                throw new DataAccessException(401, "Error: unauthorized");
            }
        } catch (Exception e) {
            throw new DataAccessException(500, "Error: description");
        }
    }

    public static void clear() throws DataAccessException {
        try {
            authData.clear();
        } catch (Exception e) {
            throw new DataAccessException(500, "Error: description");
        }
    }
}
