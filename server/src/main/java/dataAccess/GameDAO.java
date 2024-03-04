package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {

    int addGame(String name);

    void updateGame(ChessGame.TeamColor color, int id, String authToken) throws DataAccessException;

    Collection<GameData> listGames();

    void clear();

}
