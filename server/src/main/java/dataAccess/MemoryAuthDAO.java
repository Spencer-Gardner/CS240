package dataAccess;

import model.UserData;

import java.util.Arrays;
import java.util.HashMap;
import java.security.SecureRandom;

public class MemoryAuthDAO implements AuthDAO {

    public static HashMap<String, String> authData = new HashMap<>();


    public String getAuth(String username) {
        return authData.get(username);
    }

    public static void addAuth(String username) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        String token = Arrays.toString(bytes);
        authData.put(username, token);
    }

    public static void removeAuth(String username) {
        authData.remove(username);
    }

    public static void clear() {
        authData.clear();
    }

}
