package dataAccess;

import model.GameData;
import chess.ChessGame;
import java.util.*;

public class MemoryGameDAO implements GameDAO {

    public static HashMap<Integer, GameData> gameData = new HashMap<>();

    public static int addGame(String name) {
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
