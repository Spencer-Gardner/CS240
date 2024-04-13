package service;

import chess.ChessGame;
import dataAccess.*;
import model.*;
import requests.*;
import results.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class GameService {
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

    public static ChessGame getGame(int id) throws DataAccessException {
        return gameDAO.getGame(id);
    }

    public static void updateBoard(int id, ChessGame newGame) throws DataAccessException, SQLException {
        gameDAO.updateBoard(id, newGame);
    }

    public static String getWhiteUser(int id) throws DataAccessException {
        return gameDAO.getWhiteUser(id);
    }

    public static String getBlackUser(int id) throws DataAccessException {
        return gameDAO.getBlackUser(id);
    }

    public static void removePlayer(int id, String color) throws SQLException, DataAccessException {
        gameDAO.removePlayer(id, color);
    }


}
