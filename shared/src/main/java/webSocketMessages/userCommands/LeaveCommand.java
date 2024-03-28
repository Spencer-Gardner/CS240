package webSocketMessages.userCommands;

import chess.ChessMove;

public class LeaveCommand extends UserGameCommand {
    private final int gameID;

    public LeaveCommand (String authToken, int gameID, ChessMove move) {
        super(authToken);
        this.gameID = gameID;
        this.commandType = CommandType.LEAVE;
    }

    public int getGameID() {
        return gameID;
    }

}
