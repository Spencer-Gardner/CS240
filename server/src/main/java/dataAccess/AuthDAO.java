package dataAccess;

public interface AuthDAO {

    boolean verifyAuth(String authToken) throws DataAccessException;

    String getUser(String authToken) throws DataAccessException;

    String addAuth(String username) throws DataAccessException;

    void removeAuth(String authToken) throws DataAccessException;

    void clear() throws DataAccessException;

}
