package requests;

import chess.ChessGame;

public record JoinRequest(String playerColor, int gameID) { }
