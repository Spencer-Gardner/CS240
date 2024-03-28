package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;
import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(command.getAuthString(), session);
            case JOIN_OBSERVER -> joinObserver(command.getAuthString(), session);
            case MAKE_MOVE -> move(command.getAuthString(), session);
            case LEAVE -> leave(command.getAuthString());
            case RESIGN -> resign(command.getAuthString());
        }
    }

    private void joinPlayer(String name, Session session) throws IOException {
        connections.add(name, session);
        var message = String.format("%s joined as player", name);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcast(name, notification);
    }

    private void joinObserver(String name, Session session) throws IOException {
        connections.add(name, session);
        var message = String.format("%s joined as observer", name);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcast(name, notification);
    }

    private void move(String name, Session session) throws IOException {
        //
    }

    private void leave(String name) throws IOException {
        connections.remove(name);
        var message = String.format("%s left the game", name);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(name, notification);
    }

    private void resign(String name) throws IOException {
        connections.remove(name);
        var message = String.format("%s resigned", name);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(name, notification);
    }

}