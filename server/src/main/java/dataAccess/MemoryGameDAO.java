package dataAccess;

import java.util.Collection;
import java.util.HashMap;
import model.GameData;

import java.util.HashSet;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import chess.ChessGame;


public class MemoryGameDAO implements GameDAO {

    public static HashMap<Integer, GameData> gameData = new HashMap<>();

    public static void addGame(String name) {
        int id = ThreadLocalRandom.current().nextInt();
        gameData.put(id, new GameData(id, "Empty", "Empty", name, new ChessGame()));
    }

    public static GameData getGame(int id) {
        return gameData.get(id);
    }

    public static void updateGame(String color, int id) {
        GameData game = gameData.get(id);
//        if (color.equals("White") && game.whiteUsername().equals("Empty")) {
//            game.whiteUsername() = "username";
//        }
    }

    public static Collection<GameData> listGames() {
        return gameData.values();
    }

    public static void clear() {
        gameData.clear();
    }

}
