package webSocketMessages.serverMessages;

public class NotificationMessage extends ServerMessage {
    private final String message;

    public NotificationMessage(String message) {
        super(ServerMessageType.LOAD_GAME);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
