package facade;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerFacade {
    private static String server;

    public ServerFacade(String port) {
        server = port;
    }

    public String login(String username, String password) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", password);
        JsonObject response = sendRequest("/session", "POST", null, json);
        return response.get("authToken").getAsString();
    }

    public String register(String username, String password, String email) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", password);
        json.addProperty("email", email);
        JsonObject response = sendRequest("/user", "POST", null, json);
        return response.get("authToken").getAsString();
    }

    public void logout(String authToken) throws IOException {
        sendRequest("/session", "DELETE", authToken, null);
    }

    public String create(String authToken, String name) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("gameName", name);
        JsonObject response = sendRequest("/game", "POST", authToken, json);
        return response.get("gameID").toString();
    }

    public JsonArray list(String authToken) throws IOException {
        JsonObject response = sendRequest("/game", "GET", authToken, null);
        return response.getAsJsonArray("games");
    }

    public void join(String authToken, String id, String color) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("playerColor", color);
        json.addProperty("gameID", id);
        sendRequest("/game", "PUT", authToken, json);
    }

    public void observe(String authToken, String id) throws IOException {
        JsonObject json = new JsonObject();
        json.addProperty("gameID", id);
        json.addProperty("playerColor", "");
        sendRequest("/game", "PUT", authToken, json);
    }

    private static JsonObject sendRequest(String path, String method, String header, Object body) throws IOException {
        URL url = new URL(server + path);
        HttpURLConnection http = (HttpURLConnection) url.openConnection();
        http.setRequestMethod(method);

        if (header != null) {
            http.setRequestProperty("Authorization", header);
        }

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
