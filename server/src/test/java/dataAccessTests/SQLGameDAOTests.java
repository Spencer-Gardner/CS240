package dataAccessTests;

import static org.junit.jupiter.api.Assertions.*;

import chess.ChessGame;
import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.Test;

class SQLGameDAOTests {

    @Test
    void addGamePositive() throws DataAccessException {
        SQLGameDAO sql = new SQLGameDAO();
        assertDoesNotThrow(() -> sql.addGame("AddPositiveGame"));
    }

    @Test
    void addGameNegative() throws DataAccessException {
        GameDAO sql = null;
        assertThrows(NullPointerException.class, () -> sql.addGame(null));
    }

    @Test
    void updateGamePositive() throws DataAccessException {
        SQLUserDAO usql = new SQLUserDAO();
        SQLGameDAO sql = new SQLGameDAO();
        SQLAuthDAO asql = new SQLAuthDAO();
        usql.addUser(new UserData("UpdateUser", "Pass", "@test.com"));
        assertDoesNotThrow(() -> sql.updateGame("white", sql.addGame("Update"), asql.addAuth("UpdateUser")));
    }

    @Test
    void updateGameNegative() throws DataAccessException {
        SQLUserDAO usql = new SQLUserDAO();
        SQLGameDAO sql = new SQLGameDAO();
        SQLAuthDAO asql = new SQLAuthDAO();
        int id = sql.addGame("UpdateNegative");
        usql.addUser(new UserData("UpdateNegativeUser", "Pass", "@test.com"));
        String token = asql.addAuth("UpdateUser");
        sql.updateGame("white", id, token);
        assertThrows(DataAccessException.class, () -> sql.updateGame("white", id, token));
    }

    @Test
    void listGamesPositive() throws DataAccessException {
        SQLGameDAO sql = new SQLGameDAO();
        assertNotNull(sql.listGames());
    }

    @Test
    void listGamesNegative() throws DataAccessException {
        SQLGameDAO sql = null;
        assertThrows(NullPointerException.class, () -> sql.listGames());
    }

    @Test
    void clear() throws DataAccessException {
        SQLGameDAO sql = new SQLGameDAO();
        SQLUserDAO usql = new SQLUserDAO();
        sql.clear();
        usql.clear();
        assert(sql.listGames().isEmpty());
    }
}