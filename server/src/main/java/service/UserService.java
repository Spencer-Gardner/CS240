package service;

import dataAccess.*;
import model.*;
import requests.LoginRequest;

public class UserService {
    static UserDAO userDAO = new MemoryUserDAO();
    static AuthDAO authDAO = new MemoryAuthDAO();

    public static AuthData register(UserData user) throws DataAccessException {
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new DataAccessException(400, "Error: bad request");
        }
        userDAO.addUser(user);
        return new AuthData(authDAO.addAuth(user.username()), user.username());
    }

    public static AuthData login(LoginRequest user) throws DataAccessException {
        if (userDAO.verifyUser(user.username(), user.password())) {            ;
            return new AuthData(authDAO.addAuth(user.username()), user.username());
        } else {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }

    public static void logout(String authToken) throws DataAccessException {
        authDAO.removeAuth(authToken);
    }

}
