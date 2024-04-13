package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

public interface GameDAO {

    int addGame(String name) throws DataAccessException;

    void updateGame(String color, int id, String authToken) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    void clear() throws DataAccessException;

    ChessGame getGame(int id) throws DataAccessException;

    void updateBoard(int id, ChessGame newGame) throws DataAccessException, SQLException;

    String getWhiteUser(int id) throws DataAccessException;


    String getBlackUser(int id) throws DataAccessException;

    void removePlayer(int gameID, String color) throws DataAccessException, SQLException;

}
