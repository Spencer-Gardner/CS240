package model;

import chess.ChessGame;
import java.util.concurrent.ThreadLocalRandom;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) { }
