package dataAccess;

import java.util.*;

import model.GameData;

import java.util.concurrent.ThreadLocalRandom;
import chess.ChessGame;

import javax.xml.crypto.Data;


public class MemoryGameDAO implements GameDAO {

    public static HashMap<Integer, GameData> gameData = new HashMap<>();

    public static int addGame(String name) throws DataAccessException {
        try {
            int id = ThreadLocalRandom.current().nextInt();
            gameData.put(id, new GameData(id, "Empty", "Empty", name, new ChessGame(), new ArrayList<>()));
            return id;
        } catch (Exception e) {
            throw new DataAccessException(500, "Error: description");
        }
    }

    public static void updateGame(String color, int id, String authToken) throws DataAccessException {
        try {
            if (gameData.containsKey(id)) {
                GameData game = gameData.get(id);
                if (color.equals("White")) {
                    if (game.whiteUsername().equals("Empty")) {
                        gameData.replace(game.gameID(), new GameData(game.gameID(), MemoryAuthDAO.getUser(authToken), game.blackUsername(), game.gameName(), game.game(), game.observers()));
                    } else {
                        throw new DataAccessException(403, "Error: already taken");
                    }
                } else if (color.equals("Black")) {
                    if (game.blackUsername().equals("Empty")) {
                        gameData.replace(game.gameID(), new GameData(game.gameID(), game.whiteUsername(), MemoryAuthDAO.getUser(authToken), game.gameName(), game.game(), game.observers()));
                    } else {
                        throw new DataAccessException(403, "Error: already taken");
                    }
                } else {
                    ArrayList<String> observers = game.observers();
                    String user = MemoryAuthDAO.getUser(authToken);
                    ArrayList<String> combinedList = new ArrayList<>(observers);
                    combinedList.add(user);
                    gameData.replace(game.gameID(), new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game(), combinedList));
                }
            } else {
                throw new DataAccessException(400, "Error: bad request");
            }
        } catch (Exception e) {
            throw new DataAccessException(500, "Error: description");
        }

    }

    public static Collection<GameData> listGames() {
        return gameData.values();
    }

    public static void clear() throws DataAccessException {
        try {
            gameData.clear();
        } catch (Exception e) {
            throw new DataAccessException(500, "Error: description");
        }
    }
}
