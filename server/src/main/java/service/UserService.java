package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import requests.LoginRequest;

import javax.xml.crypto.Data;

public class UserService {

    public static AuthData register(UserData user) throws DataAccessException {
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        MemoryUserDAO.addUser(user);
        MemoryAuthDAO.addAuth(user.username());
        return new AuthData(MemoryAuthDAO.getAuth(user.username()), user.username());
    }

    public static AuthData login(LoginRequest user) throws DataAccessException {
        if (MemoryUserDAO.verifyUser(user.username()) && MemoryUserDAO.checkPassword(user.username(), user.password())) {
            MemoryAuthDAO.addAuth(user.username());
            return new AuthData(MemoryAuthDAO.getAuth(user.username()), user.username());
        } else {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }

    public static void logout(String authToken) throws DataAccessException {
        MemoryAuthDAO.removeAuth(MemoryAuthDAO.getUser(authToken), authToken);
    }
}
