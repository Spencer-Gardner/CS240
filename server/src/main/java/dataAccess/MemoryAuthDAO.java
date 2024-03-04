package dataAccess;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    public static HashMap<String, String> authData = new HashMap<>();

    public boolean verifyAuth(String authToken) {
        return authData.containsKey(authToken);
    }

    public String getUser(String authToken) throws DataAccessException {
        for (Map.Entry<String, String> entry : authData.entrySet()) {
            if (entry.getKey().equals(authToken)) {
                return entry.getValue();
            }
        }
        throw new DataAccessException(400, "Error: bad request");
    }

    public String addAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        authData.put(authToken, username);
        return authToken;
    }

    public void removeAuth(String authToken) throws DataAccessException {
        if (authData.containsKey(authToken)) {
            authData.remove(authToken);
        } else {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }

    public void clear() {
        authData.clear();
    }

}
