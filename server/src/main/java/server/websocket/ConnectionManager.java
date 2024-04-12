package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.NotificationMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, HashMap<String, Session>> connections = new ConcurrentHashMap<>();

    public void add(int gameID, String authToken, Session session) {
        connections.computeIfAbsent(gameID, k -> new HashMap<>()).put(authToken, session);
    }

    public void remove(int gameID, String authToken) {
        HashMap<String, Session> game = connections.get(gameID);
        game.remove(authToken);
    }

    public void broadcast(int gameId, String authToken, NotificationMessage notification) throws IOException {
        HashMap<String, Session> gameConnections = connections.get(gameId);
        if (gameConnections != null) {
            var iterator = gameConnections.entrySet().iterator();
            while (iterator.hasNext()) {
                var entry = iterator.next();
                String token = entry.getKey();
                Session session = entry.getValue();
                if (session.isOpen() && !Objects.equals(token, authToken)) {
                    session.getRemote().sendString(new Gson().toJson(notification));
                } else {
                    iterator.remove();
                }
            }
        }
    }

}