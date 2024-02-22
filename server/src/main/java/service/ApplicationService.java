package service;

import dataAccess.*;

public class ApplicationService {

    public static void clear() throws DataAccessException {
        MemoryUserDAO.clear();
        MemoryGameDAO.clear();
        MemoryAuthDAO.clear();
    }

}
