package service;

import dataAccess.*;

public class ApplicationService {
    static UserDAO userDAO = new MemoryUserDAO();
    static GameDAO gameDAO = new MemoryGameDAO();
    static AuthDAO authDAO = new MemoryAuthDAO();

    public static void clear() throws DataAccessException {
        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();
    }

}
