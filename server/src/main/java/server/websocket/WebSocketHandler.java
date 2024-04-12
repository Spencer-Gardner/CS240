package server.websocket;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.sql.SQLException;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections;

    public WebSocketHandler(ConnectionManager connections) {
        this.connections = connections;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, InvalidMoveException, SQLException {
        System.out.print("INOIBUHIOHUGOBNIO");
        var command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER:
                JoinPlayerCommand joinPlayerCommand = new Gson().fromJson(message, JoinPlayerCommand.class);
                joinPlayer(joinPlayerCommand, session);
                break;
            case JOIN_OBSERVER:
                JoinObserverCommand joinObserverCommand = new Gson().fromJson(message, JoinObserverCommand.class);
                joinObserver(joinObserverCommand, session);
                break;
            case MAKE_MOVE:
                MakeMoveCommand makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
                move(makeMoveCommand, session);
                break;
            case LEAVE:
                LeaveCommand leaveCommand = new Gson().fromJson(message, LeaveCommand.class);
                leave(leaveCommand, session);
                break;
            case RESIGN:
                ResignCommand resignCommand = new Gson().fromJson(message, ResignCommand.class);
                resign(resignCommand, session);
                break;
        }
    }

    private void joinPlayer(JoinPlayerCommand command, Session session) throws IOException, DataAccessException {
        try {
            System.out.print("OUIHOBIONODNOVNDOVND");
            connections.add(command.getGameID(), command.getAuthString(), session);
            var notification = new LoadGameMessage(GameService.getGame(command.getGameID()));
            String json = new Gson().toJson(notification);
            session.getRemote().sendString(json);
            var message = String.format("%s joined %s team", UserService.getUser(command.getAuthString()), command.getPlayerColor());
            connections.broadcast(command.getGameID(), command.getAuthString(), new NotificationMessage(message));
        } catch (Exception e) {
            String error = new Gson().toJson(new ErrorMessage("Error"));
            session.getRemote().sendString(error);
        }
    }

    private void joinObserver(JoinObserverCommand command, Session session) throws IOException, DataAccessException {
        try {
            connections.add(command.getGameID(), command.getAuthString(), session);
            var notification = new LoadGameMessage(GameService.getGame(command.getGameID()));
            String json = new Gson().toJson(notification);
            session.getRemote().sendString(json);
            var message = String.format("%s joined as observer", UserService.getUser(command.getAuthString()));
            connections.broadcast(command.getGameID(), command.getAuthString(), new NotificationMessage(message));
        } catch (Exception e) {
            String error = new Gson().toJson(new ErrorMessage("Error"));
            session.getRemote().sendString(error);
        }
    }

    private void move(MakeMoveCommand command, Session session) throws IOException, DataAccessException, InvalidMoveException, SQLException {
        try {
            ChessGame game = GameService.getGame(command.getGameID());
            game.makeMove(command.getMove());
            GameService.updateBoard(command.getGameID(), game);
            var notification = new LoadGameMessage(GameService.getGame(command.getGameID()));
            String json = new Gson().toJson(notification);
            session.getRemote().sendString(json);
            var message = String.format("%s moved... %s", UserService.getUser(command.getAuthString()), command.getMove());
            connections.broadcast(command.getGameID(), command.getAuthString(), new NotificationMessage(message));
        } catch (Exception e) {
            String error = new Gson().toJson(new ErrorMessage("Error"));
            session.getRemote().sendString(error);
        }
    }

    private void leave(LeaveCommand command, Session session) throws IOException, DataAccessException {
        connections.remove(command.getGameID(), command.getAuthString());
        var message = String.format("%s left the game", UserService.getUser(command.getAuthString()));
        connections.broadcast(command.getGameID(), command.getAuthString(), new NotificationMessage(message));
    }

    private void resign(ResignCommand command, Session session) throws IOException, DataAccessException {
        connections.remove(command.getGameID(), command.getAuthString());
        var message = String.format("%s resigned", UserService.getUser(command.getAuthString()));
        var notification = new NotificationMessage(message);
        String json = new Gson().toJson(notification);
        session.getRemote().sendString(json);
        connections.broadcast(command.getGameID(), command.getAuthString(), notification);
    }

}
