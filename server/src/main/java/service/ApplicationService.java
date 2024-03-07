package service;

import dataAccess.*;

public class ApplicationService {
//    public static UserDAO userDAO = new MemoryUserDAO();
//    public static GameDAO gameDAO = new MemoryGameDAO();
//    public static AuthDAO authDAO = new MemoryAuthDAO();

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
