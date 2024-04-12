package facade;

import com.google.gson.Gson;
import ui.ClientUI;
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

    public WebSocketFacade(String url) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new javax.websocket.MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    if ((notification.getServerMessageType()).equals(ServerMessage.ServerMessageType.LOAD_GAME)) {
                        ClientUI.setGame(((LoadGameMessage) notification).getGame());
                    } else if ((notification.getServerMessageType()).equals(ServerMessage.ServerMessageType.NOTIFICATION)) {
                        System.out.println(SET_TEXT_COLOR_YELLOW + ((NotificationMessage) notification).getMessage() + RESET_TEXT_COLOR);
                    } else {
                        System.out.println(SET_TEXT_COLOR_RED + ((ErrorMessage) notification).getErrorMessage() + RESET_TEXT_COLOR);
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
            var command = new JoinPlayerCommand(authToken, gameID, playerColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new Exception();
        }
    }

    public void joinObserver(String authToken, int gameID) throws Exception {
        try {
            var command = new JoinObserverCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new Exception();
        }
    }

    public void move(String authToken, int gameID, ChessMove move) throws Exception {
        try {
            var command = new MakeMoveCommand(authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new Exception();
        }
    }

    public void leave(String authToken, int gameID) throws Exception {
        try {
            var command = new LeaveCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
            this.session.close();
        } catch (IOException ex) {
            throw new Exception();
        }
    }

    public void resign(String authToken, int gameID) throws Exception {
        try {
            var command = new ResignCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
            this.session.close();
        } catch (IOException ex) {
            throw new Exception();
        }
    }

}
