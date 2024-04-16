package facade;

import com.google.gson.Gson;
import ui.ClientUI;
import ui.RenderBoard;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import chess.ChessGame;
import chess.ChessMove;

import static ui.EscapeSequences.*;

public class WebSocketFacade extends Endpoint {

    Session session;
    RenderBoard renderBoard = new RenderBoard();

    public WebSocketFacade(String url, ClientUI client) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            Object lock = new Object();
            this.session.addMessageHandler(new javax.websocket.MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    synchronized (lock) {
                        ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                        if ((notification.getServerMessageType()).equals(ServerMessage.ServerMessageType.LOAD_GAME)) {
                            LoadGameMessage load = new Gson().fromJson(message, LoadGameMessage.class);
                            client.setGame(load.getGame());
                            renderBoard.drawChessBoard(load.getGame(), client.getColor());
                        } else if ((notification.getServerMessageType()).equals(ServerMessage.ServerMessageType.NOTIFICATION)) {
                            NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                            System.out.println(SET_TEXT_COLOR_YELLOW + notificationMessage.getMessage() + SET_TEXT_COLOR_WHITE);
                        } else {
                            ErrorMessage error = new Gson().fromJson(message, ErrorMessage.class);
                            System.out.println(SET_TEXT_COLOR_RED + error + SET_TEXT_COLOR_WHITE);
                        }
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception();
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) throws Exception {
        try {
            JoinPlayerCommand command = new JoinPlayerCommand(authToken, gameID, playerColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new Exception();
        }
    }

    public void joinObserver(String authToken, int gameID) throws Exception {
        try {
            JoinObserverCommand command = new JoinObserverCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new Exception();
        }
    }

    public void move(String authToken, int gameID, ChessMove move) throws Exception {
        try {
            MakeMoveCommand command = new MakeMoveCommand(authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new Exception();
        }
    }

    public void leave(String authToken, int gameID) throws Exception {
        try {
            LeaveCommand command = new LeaveCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
            this.session.close();
        } catch (IOException ex) {
            throw new Exception();
        }
    }

    public void resign(String authToken, int gameID) throws Exception {
        try {
            ResignCommand command = new ResignCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
            this.session.close();
        } catch (IOException ex) {
            throw new Exception();
        }
    }

}
