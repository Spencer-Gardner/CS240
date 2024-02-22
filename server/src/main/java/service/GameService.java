package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import results.ListResult;
import model.GameData;
import results.CreateResult;
import requests.CreateRequest;
import requests.JoinRequest;

import java.util.ArrayList;

public class GameService {

    public static ListResult listGames(String authToken) throws DataAccessException {
        if (MemoryAuthDAO.verifyAuth(authToken)) {
            return new ListResult((ArrayList<GameData>) MemoryGameDAO.listGames());
        } else {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }

    public static CreateResult createGame(CreateRequest game, String authToken) throws DataAccessException {
        if (MemoryAuthDAO.verifyAuth(authToken)) {
            return new CreateResult(MemoryGameDAO.addGame(game.gameName()));
        } else {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }

    public static void joinGame(JoinRequest game, String authToken) throws DataAccessException {
        if (MemoryAuthDAO.verifyAuth(authToken)) {
            MemoryGameDAO.updateGame(game.playerColor(), game.gameID(), authToken);
        } else {
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }
}
