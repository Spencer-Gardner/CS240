package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final ConcurrentHashMap<Integer, HashMap<String, Session>> connections;

    public ConnectionManager() {
        this.connections = new ConcurrentHashMap<>();
    }

    public void addSessionToGame(int gameID, String authToken, Session session) {
        HashMap<String, Session> game = connections.get(gameID);
        if (game == null) {
            HashMap<String, Session> newConnection = new HashMap<>();
            newConnection.put(authToken, session);
            connections.put(gameID, newConnection);
        } else {
            game.put(authToken, session);
            connections.put(gameID, game);
        }
    }

    public HashMap<String, Session> getSessionsForGame(int gameID) {
        return connections.get(gameID);
    }

    public void removeSessionFromGame(int gameID, String authToken, Session session) {
        HashMap<String, Session> game = connections.get(gameID);
        if (session.isOpen()) {
            game.remove(authToken, session);
            session.close();
        }
    }

    public void sendMessage(int gameID, LoadGameMessage message, String authToken) throws IOException {
        String messageJSON = new Gson().toJson(message);
        Session session = connections.get(gameID).get(authToken);
        if (session.isOpen()) {
            session.getRemote().sendString(messageJSON);
        }
    }

    public void broadcast(int gameID, NotificationMessage message, String exceptAuth) throws IOException {
        String messageJSON = new Gson().toJson(message);
        HashMap<String, Session> relevantSessions = getSessionsForGame(gameID);
        for (String authToken : relevantSessions.keySet()) {
            Session session = connections.get(gameID).get(authToken);
            if (session.isOpen()) {
                if (!authToken.equals(exceptAuth)) {
                    session.getRemote().sendString(messageJSON);
                }
            }
        }
    }

    public void broadcastAll(int gameID, ServerMessage message) throws IOException {
        String messageJSON = new Gson().toJson(message);
        HashMap<String, Session> relevantSessions = getSessionsForGame(gameID);
        for (String authToken : relevantSessions.keySet()) {
            Session session = connections.get(gameID).get(authToken);
            if (session.isOpen()) {
                session.getRemote().sendString(messageJSON);
            }
        }
    }

}