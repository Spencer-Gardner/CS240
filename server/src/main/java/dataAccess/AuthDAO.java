package dataAccess;

public interface AuthDAO {

    boolean verifyAuth(String authToken);

    String getUser(String authToken) throws DataAccessException;

    String addAuth(String username);

    void removeAuth(String authToken) throws DataAccessException;

    void clear();

}
