package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLUserDAO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.UserData;

class SQLUserDAOTests {

    @Test
    void addUserPositive() throws DataAccessException {
        UserData data = new UserData("AddTest", "Pass", "@test.com");
        SQLUserDAO sql = new SQLUserDAO();
        sql.addUser(data);
        assertTrue(sql.verifyUser("AddTest", "Pass"));
    }

    @Test
    void addUserNegative() throws DataAccessException {
        UserData data = new UserData("AddTest", "Pass", "@test.com");
        SQLUserDAO sql = new SQLUserDAO();
        assertThrows(DataAccessException.class, () -> sql.addUser(data));
    }

    @Test
    void verifyUserPositive() throws DataAccessException{
        UserData data = new UserData("VerifyTest", "Pass", "@test.com");
        SQLUserDAO sql = new SQLUserDAO();
        sql.addUser(data);
        assertTrue(sql.verifyUser("VerifyTest", "Pass"));
    }

    @Test
    void verifyUserNegative() throws DataAccessException{
        SQLUserDAO sql = new SQLUserDAO();
        assertFalse(sql.verifyUser("FalseTest", "Pass"));
    }

    @Test
    void clear() throws DataAccessException {
        SQLUserDAO sql = new SQLUserDAO();
        sql.clear();
        assertFalse(sql.verifyUser("AddTest", "Pass"));
    }
}