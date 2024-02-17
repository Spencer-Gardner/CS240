package server;

import java.nio.file.Paths;
import spark.*;
import com.google.gson.Gson;
import model.*;
import service.*;
import requests.LoginRequest;
import requests.JoinRequest;
import requests.CreateRequest;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this:: createGame);
        Spark.put("/game", this::joinGame);

        Spark.awaitInitialization();
        return Spark.port();
    }


    private final static Gson gson = new Gson();

    private Object clear(Request request, Response response) {
        ApplicationService.clear();
        return null;
    }

    private Object register(Request request, Response response) {
        UserData newUser = gson.fromJson(request.body(), UserData.class);
        return gson.toJson(UserService.register(newUser));
    }

    private Object login(Request request, Response response) {
        LoginRequest user = gson.fromJson(request.body(), LoginRequest.class);
        return gson.toJson(UserService.login(user));
    }

    private Object logout(Request request, Response response) {
        String authToken = request.headers("Authentication");
        UserService.logout(authToken);
        return null;
    }

    private Object listGames(Request request, Response response) {
        String authToken = request.headers("Authentication");
        return gson.toJson(GameService.listGames(authToken));
    }

    private Object createGame(Request request, Response response) {
        String authToken = request.headers("Authentication");
        CreateRequest game = gson.fromJson(request.body(), CreateRequest.class);
        return gson.toJson(GameService.createGame(game, authToken));
    }

    private Object joinGame(Request request, Response response) {
        String authToken = request.headers("Authentication");
        JoinRequest game = gson.fromJson(request.body(), JoinRequest.class);
        GameService.joinGame(game, authToken);
        return null;
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
