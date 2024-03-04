package dataAccess;

import model.UserData;

import java.sql.SQLException;

public interface UserDAO {

    boolean verifyUser(String username, String password) throws DataAccessException;

    void addUser(UserData user) throws DataAccessException, SQLException;

    void clear() throws DataAccessException;

}
