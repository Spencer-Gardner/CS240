package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinObserverCommand extends UserGameCommand {
    private final int gameID;

    public JoinObserverCommand (String authToken, int gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.JOIN_OBSERVER;
    }

    public int getGameID() {
        return gameID;
    }

}
