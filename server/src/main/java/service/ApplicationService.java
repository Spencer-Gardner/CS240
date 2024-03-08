package service;

import dataAccess.*;

public class ApplicationService {
    static UserDAO userDAO;
    static GameDAO gameDAO;
    static AuthDAO authDAO;

    static {
        try {
            userDAO = new SQLUserDAO();
            gameDAO = new SQLGameDAO();
            authDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void clear() throws DataAccessException {
        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();
    }

}
