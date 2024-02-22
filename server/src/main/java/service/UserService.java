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
        MemoryUserDAO.addUser(user);
        MemoryAuthDAO.addAuth(user.username());
        return new AuthData(user.username(), MemoryAuthDAO.getAuth(user.username()));
    }

    public static AuthData login(LoginRequest user) throws DataAccessException {
        if (MemoryUserDAO.verifyUser(user.username())) {
            MemoryAuthDAO.addAuth(user.username());
            return new AuthData(user.username(), MemoryAuthDAO.getAuth(user.username()));
        } else {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }

    public static void logout(String authToken) throws DataAccessException {
        // iterator?
        MemoryAuthDAO.removeAuth(authToken);
    }
}
