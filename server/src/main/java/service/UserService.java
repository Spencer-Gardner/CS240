package service;

import dataAccess.*;
import model.*;
import requests.LoginRequest;

public class UserService {

    public static AuthData register(UserData user) throws DataAccessException {
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        MemoryUserDAO.addUser(user);
        return new AuthData(MemoryAuthDAO.addAuth(user.username()), user.username());
    }

    public static AuthData login(LoginRequest user) throws DataAccessException {
        if (MemoryUserDAO.verifyUser(user.username(), user.password())) {            ;
            return new AuthData(MemoryAuthDAO.addAuth(user.username()), user.username());
        } else {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }

    public static void logout(String authToken) throws DataAccessException {
        MemoryAuthDAO.removeAuth(authToken);
    }
}
