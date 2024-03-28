package server;

import spark.*;
import server.websocket.WebSocketHandler;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import service.*;
import model.*;
import requests.*;
import dataAccess.DataAccessException;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/connect", new WebSocketHandler());

        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this:: createGame);
        Spark.put("/game", this::joinGame);

        Spark.awaitInitialization();
        return Spark.port();
    }


    private final static Gson gson = new Gson();

    private Object clear(Request request, Response response) {
        try {
            ApplicationService.clear();
            return "{}";
        } catch (DataAccessException e) {
            response.status(e.getCode());
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }

    private Object register(Request request, Response response) {
        try {
            UserData newUser = gson.fromJson(request.body(), UserData.class);
            return gson.toJson(UserService.register(newUser));
        } catch (JsonSyntaxException e) {
            response.status(400);
            return gson.toJson(new ErrorResponse("Error: bad request"));
        }
        catch (DataAccessException e) {
            response.status(e.getCode());
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }

    private Object login(Request request, Response response) {
        try {
            LoginRequest user = gson.fromJson(request.body(), LoginRequest.class);
            return gson.toJson(UserService.login(user));
        } catch (DataAccessException e) {
            response.status(e.getCode());
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }

    private Object logout(Request request, Response response) {
        try {
            String authToken = request.headers("Authorization");
            UserService.logout(authToken);
            return "{}";
        } catch (DataAccessException e) {
            response.status(e.getCode());
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }

    private Object listGames(Request request, Response response) {
        try {
            String authToken = request.headers("Authorization");
            return gson.toJson(GameService.listGames(authToken));
        } catch (DataAccessException e) {
            response.status(e.getCode());
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }

    private Object createGame(Request request, Response response) {
        try {
            String authToken = request.headers("Authorization");
            CreateRequest game = gson.fromJson(request.body(), CreateRequest.class);
            return gson.toJson(GameService.createGame(game, authToken));
        } catch (JsonSyntaxException e) {
            response.status(400);
            return gson.toJson(new ErrorResponse("Error: bad request"));
        }
        catch (DataAccessException e) {
            response.status(e.getCode());
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }

    private Object joinGame(Request request, Response response) {
        try {
            String authToken = request.headers("Authorization");
            JoinRequest game = gson.fromJson(request.body(), JoinRequest.class);
            GameService.joinGame(game, authToken);
            return "{}";
        } catch (JsonSyntaxException e) {
            response.status(400);
            return gson.toJson(new ErrorResponse("Error: bad request"));
        }
        catch (DataAccessException e) {
            response.status(e.getCode());
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
    }

    private record ErrorResponse(String message) { }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
