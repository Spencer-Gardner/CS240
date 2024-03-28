package webSocketMessages.serverMessages;

import chess.ChessBoard;

public class LoadGameMessage extends ServerMessage {
    private final ChessBoard game;

    public LoadGameMessage(ChessBoard game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public ChessBoard getGame() {
        return game;
    }

}
