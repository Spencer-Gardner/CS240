package service;

import dataAccess.*;
import model.*;
import requests.*;
import results.*;
import java.util.ArrayList;

public class GameService {
//    public static GameDAO gameDAO = new MemoryGameDAO();
//    public static AuthDAO authDAO = new MemoryAuthDAO();

    static GameDAO gameDAO;
    static AuthDAO authDAO;

    static {
        try {
            gameDAO = new SQLGameDAO();
            authDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static ListResult listGames(String authToken) throws DataAccessException {
        if (authDAO.verifyAuth(authToken)) {
            return new ListResult((ArrayList<GameData>) gameDAO.listGames());
        } else {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }

    public static CreateResult createGame(CreateRequest game, String authToken) throws DataAccessException {
        if (authDAO.verifyAuth(authToken)) {
            return new CreateResult(gameDAO.addGame(game.gameName()));
        } else {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }

    public static void joinGame(JoinRequest game, String authToken) throws DataAccessException {
        if (authDAO.verifyAuth(authToken)) {
            gameDAO.updateGame(game.playerColor(), game.gameID(), authToken);
        } else {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }

}
