package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
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
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections;

    public WebSocketHandler(ConnectionManager connections) {
        this.connections = connections;
    }

    final Object lock = new Object();
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, InvalidMoveException, SQLException {
        synchronized (lock) {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
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
    }

    private void joinPlayer(JoinPlayerCommand command, Session session) throws IOException {
        try {
            String authToken = command.getAuthString();
            if (checkAuth(authToken)) {
                sendError(session, "* INVALID AUTHORIZATION *");
                return;
            }
            int gameID = command.getGameID();
            if (checkID(gameID)) {
                sendError(session, "* INVALID ID *");
                return;
            }
            String user = UserService.getUser(authToken);
            String white = GameService.getWhiteUser(gameID);
            String black = GameService.getBlackUser(gameID);
            ChessGame.TeamColor playerColor = command.getPlayerColor();
            if (playerColor.equals(ChessGame.TeamColor.WHITE)) {
                if (white == null || !white.equals(user)) {
                    sendError(session, "* INVALID COLOR *");
                    return;
                }
            } else if (playerColor.equals(ChessGame.TeamColor.BLACK)) {
                if (black == null || !black.equals(user)) {
                    sendError(session, "* INVALID COLOR *");
                    return;
                }
            }
            connections.addSessionToGame(gameID, authToken, session);
            connections.sendMessage(gameID, new LoadGameMessage(GameService.getGame(gameID)), authToken);
            connections.broadcast(gameID, new NotificationMessage(user + " joined " + playerColor), authToken);
        } catch (Exception e) {
            sendError(session, "* ERROR *");
        }
    }

    private void joinObserver(JoinObserverCommand command, Session session) throws IOException, DataAccessException {
        try {
            String authToken = command.getAuthString();
            if (checkAuth(authToken)) {
                sendError(session, "* INVALID AUTHORIZATION *");
                return;
            }
            int gameID = command.getGameID();
            if (checkID(gameID)) {
                sendError(session, "* INVALID ID *");
                return;
            }
            String user = UserService.getUser(authToken);
            connections.addSessionToGame(gameID, authToken, session);
            connections.sendMessage(gameID, new LoadGameMessage(GameService.getGame(gameID)), authToken);
            connections.broadcast(gameID, new NotificationMessage(user + " joined as observer"), authToken);
        } catch (Exception e) {
            sendError(session, "* ERROR *");
        }
    }

    private void move(MakeMoveCommand command, Session session) throws IOException, DataAccessException, InvalidMoveException, SQLException {
        try {
            String authToken = command.getAuthString();
            if (checkAuth(authToken)) {
                sendError(session, "* INVALID AUTHORIZATION *");
                return;
            }
            int gameID = command.getGameID();
            if (checkID(gameID)) {
                sendError(session, "* INVALID ID *");
                return;
            }
            String user = UserService.getUser(authToken);
            String white = GameService.getWhiteUser(gameID);
            String black = GameService.getBlackUser(gameID);
            ChessGame game = GameService.getGame(gameID);
            ChessMove move = command.getMove();
            if (!game.validMoves(move.getStartPosition()).contains(move)) {
                sendError(session, "* INVALID MOVE *");
                return;
            }
            if (!white.equals(user) && !black.equals(user)) {
                sendError(session, "* INVALID OBSERVER MOVE *");
                return;
            }
            if (game.getTeamTurn() == ChessGame.TeamColor.WHITE && Objects.equals(black, user)) {
                sendError(session, "* INVALID TURN *");
                return;
            } else if (game.getTeamTurn() == ChessGame.TeamColor.BLACK && white.equals(user)) {
                sendError(session, "* INVALID TURN *");
                return;
            }
            if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
                connections.broadcast(gameID, new NotificationMessage("--" + white + " IN CHECK --"), authToken);
            }
            if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                connections.broadcast(gameID, new NotificationMessage("--" + white + " IN CHECKMATE --"), authToken);
            }
            if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
                connections.broadcast(gameID, new NotificationMessage("--" + black + " IN CHECK --"), authToken);
            }
            if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
                connections.broadcast(gameID, new NotificationMessage("--" + black + " IN CHECK --"), authToken);
            }
            if (!game.getState()) {
                sendError(session, "* GAME OVER *");
                return;
            }
            game.makeMove(move);
            GameService.updateBoard(gameID, game);
            connections.broadcastAll(gameID, new LoadGameMessage(game));
            connections.broadcast(gameID, new NotificationMessage("--" + user + " MOVED --> " + move + " --"), authToken);
        } catch (Exception e) {
            sendError(session, "* ERROR *");
        }
    }

    private void leave(LeaveCommand command, Session session) throws IOException, DataAccessException, SQLException {
        String authToken = command.getAuthString();
        if (checkAuth(authToken)) {
            sendError(session, "* INVALID AUTHORIZATION *");
            return;
        }
        int gameID = command.getGameID();
        if (checkID(gameID)) {
            sendError(session, "* INVALID ID *");
            return;
        }
        String user = UserService.getUser(authToken);
        String white = GameService.getWhiteUser(gameID);
        String black = GameService.getBlackUser(gameID);
        if (white != null && white.equals(user)) {
            GameService.removePlayer(gameID,"WHITE");;
        }
        if (black != null && black.equals(user)) {
            GameService.removePlayer(gameID,"BLACK");
        }
        connections.removeSessionFromGame(gameID, authToken, session);
        connections.broadcast(gameID, new NotificationMessage("-- " + user + " LEFT GAME --"), authToken);
    }

    private void resign(ResignCommand command, Session session) throws IOException, DataAccessException, SQLException {
        String authToken = command.getAuthString();
        if (checkAuth(authToken)) {
            sendError(session, "* INVALID AUTHORIZATION *");
            return;
        }
        int gameID = command.getGameID();
        if (checkID(gameID)) {
            sendError(session, "* INVALID ID *");
            return;
        }
        String user = UserService.getUser(authToken);
        String white = GameService.getWhiteUser(gameID);
        String black = GameService.getBlackUser(gameID);
        ChessGame game = GameService.getGame(gameID);
        if (white == null || black == null) {
            sendError(session, "* CANNOT RESIGN *");
            return;
        }
        if (!white.equals(user) && !black.equals(user)) {
            sendError(session, "* CANNOT RESIGN *");
            return;
        }
        if (!game.getState()) {
            sendError(session, "* GAME OVER *");
            return;
        }
        game.setState(Boolean.FALSE);
        GameService.updateBoard(gameID, game);
        connections.broadcastAll(gameID, new NotificationMessage("-- " + user + " RESIGNED | GAME OVER --"));
    }


    private boolean checkAuth(String authToken) throws DataAccessException {
        return UserService.getUser(authToken) == null;
    }

    private boolean checkID(int gameID) throws DataAccessException {
        return GameService.getGame(gameID) == null;
    }

    private void sendError(Session session, String message) throws IOException {
        String errorMsg = new Gson().toJson(new ErrorMessage(message));
        session.getRemote().sendString(errorMsg);
    }

}
