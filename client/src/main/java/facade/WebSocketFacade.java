package facade;

import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.EscapeSequences.RESET_TEXT_COLOR;

public class WebSocketFacade extends Endpoint {

    Session session;

    public WebSocketFacade(String url) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    System.out.println(SET_TEXT_COLOR_RED + notification.getServerMessageType() + RESET_TEXT_COLOR);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception();
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void join(String authToken) throws Exception {
        try {
            var action = new UserGameCommand(authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new Exception();
        }
    }

    public void leave(String authToken) throws Exception {
        try {
            var action = new UserGameCommand(authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException ex) {
            throw new Exception();
        }
    }

}
