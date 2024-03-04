package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;

public interface GameDAO {

    int addGame(String name) throws DataAccessException;

    void updateGame(ChessGame.TeamColor color, int id, String authToken) throws DataAccessException;

    Collection<GameData> listGames() throws DataAccessException;

    void clear() throws DataAccessException;

}
