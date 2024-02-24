package dataAccess;

import java.util.*;

import model.GameData;

import java.util.concurrent.ThreadLocalRandom;
import chess.ChessGame;

import javax.xml.crypto.Data;


public class MemoryGameDAO implements GameDAO {

    public static HashMap<Integer, GameData> gameData = new HashMap<>();

    public static int addGame(String name) throws DataAccessException {
        Random random = new Random();
        int id = random.nextInt(Integer.MAX_VALUE) + 1;
        gameData.put(id, new GameData(id, null, null, name, new ChessGame(), new ArrayList<>()));
        return id;
    }

    public static void updateGame(String color, int id, String authToken) throws DataAccessException {
        if (gameData.containsKey(id)) {
            GameData game = gameData.get(id);
            if (color == null) {
                ArrayList<String> observers = game.observers();
                String user = MemoryAuthDAO.getUser(authToken);
                ArrayList<String> combinedList = new ArrayList<>(observers);
                combinedList.add(user);
                gameData.replace(game.gameID(), new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game(), combinedList));
            } else if (color.equals("White")) {
                if (game.whiteUsername() == null) {
                    gameData.replace(game.gameID(), new GameData(game.gameID(), MemoryAuthDAO.getUser(authToken), game.blackUsername(), game.gameName(), game.game(), game.observers()));
                } else {
                    throw new DataAccessException(403, "Error: already taken");
                }
            } else if (color.equals("Black")) {
                if (game.blackUsername() == null) {
                    gameData.replace(game.gameID(), new GameData(game.gameID(), game.whiteUsername(), MemoryAuthDAO.getUser(authToken), game.gameName(), game.game(), game.observers()));
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
