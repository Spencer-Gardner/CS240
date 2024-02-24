package dataAccess;

import java.util.*;

import model.GameData;

import java.util.concurrent.ThreadLocalRandom;
import chess.ChessGame;

import javax.xml.crypto.Data;


public class MemoryGameDAO implements GameDAO {

    public static HashMap<Integer, GameData> gameData = new HashMap<>();

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static int addGame(String name) throws DataAccessException {
        Random random = new Random();
        int id = random.nextInt(Integer.MAX_VALUE) + 1;
        gameData.put(id, new GameData(id, null, null, name, new ChessGame(), new ArrayList<>()));
        return id;
    }

    public static void updateGame(ChessGame.TeamColor color, int id, String authToken) throws DataAccessException {
        if (gameData.containsKey(id)) {
            GameData game = gameData.get(id);
            String user = MemoryAuthDAO.getUser(authToken);
            if (color == null) {
                ArrayList<String> observers = game.observers();
                ArrayList<String> combinedList = new ArrayList<>(observers);
                combinedList.add(user);
                gameData.put(game.gameID(), new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game(), combinedList));
            } else if (color.equals(ChessGame.TeamColor.WHITE)) {
                if (game.whiteUsername() == null) {
                    System.out.println("WHITE!!!!!!!!!!!!!!!!");
                    gameData.put(game.gameID(), new GameData(game.gameID(), user, game.blackUsername(), game.gameName(), game.game(), game.observers()));
                } else {
                    throw new DataAccessException(403, "Error: already taken");
                }
            } else if (color.equals(ChessGame.TeamColor.BLACK)) {
                if (game.blackUsername() == null) {
                    gameData.put(game.gameID(), new GameData(game.gameID(), game.whiteUsername(), user, game.gameName(), game.game(), game.observers()));
                } else {
                    throw new DataAccessException(403, "Error: already taken");
                }
            }
        } else {
            throw new DataAccessException(400, "Error: bad request");
        }
    }

    public static Collection<GameData> listGames() {
        return new ArrayList<GameData>(gameData.values());
    }

    public static void clear() {
        gameData.clear();
    }
}
