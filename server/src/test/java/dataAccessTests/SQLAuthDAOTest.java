package dataAccessTests;

import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthDAOTest {

    @Test
    void verifyAuthPositive() throws DataAccessException {
        SQLUserDAO usql = new SQLUserDAO();
        SQLGameDAO dsql = new SQLGameDAO();
        SQLAuthDAO sql = new SQLAuthDAO();
        usql.addUser(new UserData("AuthUser", "Pass", "@test.com"));
        String token = sql.addAuth("AuthUser");
        assertTrue(sql.verifyAuth(token));
    }

    @Test
    void verifyAuthNegative() throws DataAccessException {
        SQLAuthDAO sql = new SQLAuthDAO();
        assertFalse(sql.verifyAuth("NotAToken"));
    }

    @Test
    void getUserPositive() throws DataAccessException {
        SQLAuthDAO sql = new SQLAuthDAO();
        String token = sql.addAuth("AuthUser");
        assertEquals("AuthUser", sql.getUser(token));
    }

    @Test
    void getUserNegative() throws DataAccessException {
        SQLAuthDAO sql = new SQLAuthDAO();
        assertThrows(DataAccessException.class, () -> sql.getUser("NotAToken"));
    }

    @Test
    void addAuthPositive() throws DataAccessException {
        SQLAuthDAO sql = new SQLAuthDAO();
        String token = sql.addAuth("AuthUser");
        assertNotNull(token);
    }

    @Test
    void addAuthNegative() throws DataAccessException {
        SQLAuthDAO sql = null;
        assertThrows(NullPointerException.class, () -> sql.addAuth(null));
    }

    @Test
    void removeAuthPositive() throws DataAccessException {
        SQLAuthDAO sql = new SQLAuthDAO();
        String token = sql.addAuth("AuthUser");
        assertDoesNotThrow(() -> sql.removeAuth(token));
    }

    @Test
    void removeAuthNegative() throws DataAccessException {
        SQLAuthDAO sql = null;
        assertThrows(NullPointerException.class, () -> sql.removeAuth("Token"));
    }

    @Test
    void clear() throws DataAccessException {
        SQLUserDAO usql = new SQLUserDAO();
        SQLAuthDAO sql = new SQLAuthDAO();
        usql.clear();
        sql.clear();
    }

}