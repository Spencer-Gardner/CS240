package facade;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {
    private static String server;

    public ServerFacade(int port) {
        server = "http://localhost:8080/" + port;
    }

    public String login(String username, String password) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", password);
        JsonObject response = sendRequest("/session", "POST", json);
        return response.get("authToken").toString();
    }

    public String register(String username, String password, String email) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", password);
        json.addProperty("email", email);
        JsonObject response = sendRequest("/user", "POST", json);
        return response.get("authToken").toString();
    }

    public void logout() throws IOException {
        sendRequest("/session", "DELETE", null);
    }

    public String create(String name) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("gameName", name);
        JsonObject response = sendRequest("/game", "POST", json);
        return response.get("gameID").toString();
    }

    public String list() throws IOException {
        JsonObject response = sendRequest("/game", "GET", null);
        return response.get("authToken").toString();
    }

    public void join(String id, String color) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("gameID", id);
        json.addProperty("playerColor", color);
        sendRequest("/game", "PUT", json);
    }

    public void observe(String id) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("gameID", id);
        json.addProperty("playerColor", "");
        sendRequest("/game", "PUT", json);
    }

    private static JsonObject sendRequest(String path, String method, Object body) throws IOException {
        URL url = new URL(server + path);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod(method);

        if (body != null) {
            http.setDoOutput(true);
            try (var outputStream = http.getOutputStream()) {
                outputStream.write(body.toString().getBytes());
            }
        }

        JsonObject response = null;
        try (InputStream respBody = http.getInputStream();
             InputStreamReader reader = new InputStreamReader(respBody)) {
            response = new Gson().fromJson(reader, JsonObject.class);
        }

        return response;
    }

}
