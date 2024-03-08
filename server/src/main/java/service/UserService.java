package service;

import dataAccess.*;
import model.*;
import requests.LoginRequest;

import java.sql.SQLException;

public class UserService {
    static UserDAO userDAO;
    static AuthDAO authDAO;

    static {
        try {
            userDAO = new SQLUserDAO();
            authDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

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
