package server;

import spark.*;
import com.google.gson.Gson;
import model.*;
import service.*;
import java.nio.file.Paths;

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
        UserData newUser = gson.fromJson(request.body(), UserData.class);
        return gson.toJson(UserService.login(newUser));
    }

    private Object logout(Request request, Response response) {
        UserData newUser = gson.fromJson(request.body(), UserData.class);
        return gson.toJson(UserService.logout(newUser));
    }

    private Object listGames(Request request, Response response) {
        GameData games = gson.fromJson(request.body(), GameData.class);
        return gson.toJson(GameService.listGames(games));
    }

    private Object createGame(Request request, Response response) {
        GameData games = gson.fromJson(request.body(), GameData.class);
        return gson.toJson(GameService.createGame(games));
    }

    private Object joinGame(Request request, Response response) {
        GameData games = gson.fromJson(request.body(), GameData.class);
        return gson.toJson(GameService.joinGame(games));
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
