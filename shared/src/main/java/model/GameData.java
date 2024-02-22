package model;

import chess.ChessGame;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game, ArrayList<String> observers) {
    public static GameData defaultConstructor (int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game, ArrayList<String> observers) {
        return new GameData(gameID, whiteUsername, blackUsername, gameName, game, new ArrayList<>());
    }
}
