package dataAccess;

import java.sql.*;
import com.google.gson.Gson;
import model.GameData;
import chess.ChessGame;
import java.util.*;

public class SQLGameDAO implements GameDAO {
    private final Connection conn;
    static AuthDAO authDAO;

    public SQLGameDAO() throws DataAccessException {
        conn = DatabaseManager.getConnection();
        try {
            authDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public int addGame(String name) throws DataAccessException {
        int id = 0;
        try (var statement = conn.prepareStatement("INSERT INTO game (gamedata) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
            var json = new Gson().toJson(new GameData(id, null, null, name, new ChessGame(), new ArrayList<>()), GameData.class);
            statement.setString(1, json);
            statement.executeUpdate();
            var rs = statement.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getInt(1);
                json = new Gson().toJson(new GameData(id, null, null, name, new ChessGame(), new ArrayList<>()), GameData.class);
                try (var updateStatement = conn.prepareStatement("UPDATE game SET gamedata = ? WHERE id = ?")) {
                    updateStatement.setString(1, json);
                    updateStatement.setInt(2, id);
                    updateStatement.executeUpdate();
                }
            }
            return id;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void updateGame(ChessGame.TeamColor color, int id, String authToken) throws DataAccessException {
        GameData game = null;
        try (var statement = conn.prepareStatement("SELECT gamedata FROM game WHERE id=?")) {
            statement.setInt(1, id);
            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                    var json = rs.getString("gamedata");
                    game = new Gson().fromJson(json, GameData.class);
                } else {
                    throw new DataAccessException(400, "Error: bad request");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        String user = authDAO.getUser(authToken);
        if (game != null) {
            if (color == null) {
                ArrayList<String> observers = game.observers();
                ArrayList<String> combinedList = new ArrayList<>(observers);
                combinedList.add(user);
                try (var statement = conn.prepareStatement("UPDATE game SET gamedata=? WHERE id=?")) {
                    statement.setInt(2, id);
                    var json = new Gson().toJson(new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), game.game(), combinedList), GameData.class);
                    statement.setString(1, json);
                    statement.executeUpdate();
                } catch (SQLException e) {
                    throw new DataAccessException(e.getMessage());
                }
            } else if (color.equals(ChessGame.TeamColor.WHITE)) {
                if (game.whiteUsername() == null) {
                    try (var statement = conn.prepareStatement("UPDATE game SET gamedata=? WHERE id=?")) {
                        statement.setInt(2, id);
                        var json = new Gson().toJson(new GameData(game.gameID(), user, game.blackUsername(), game.gameName(), game.game(), game.observers()), GameData.class);
                        statement.setString(1, json);
                        statement.executeUpdate();
                    } catch (SQLException e) {
                        throw new DataAccessException(e.getMessage());
                    }
                } else {
                    throw new DataAccessException(403, "Error: already taken");
                }
            } else if (color.equals(ChessGame.TeamColor.BLACK)) {
                if (game.blackUsername() == null) {
                    try (var statement = conn.prepareStatement("UPDATE game SET gamedata=? WHERE id=?")) {
                        statement.setInt(2, id);
                        var json = new Gson().toJson(new GameData(game.gameID(), game.whiteUsername(), user, game.gameName(), game.game(), game.observers()), GameData.class);
                        statement.setString(1, json);
                        statement.executeUpdate();
                    } catch (SQLException e) {
                        throw new DataAccessException(e.getMessage());
                    }
                } else {
                    throw new DataAccessException(403, "Error: already taken");
                }
            }
        }
    }

    public Collection<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> list = new ArrayList<>();
        try (var statement = conn.prepareStatement("SELECT gamedata FROM game")) {
            try (var rs = statement.executeQuery()) {
                while (rs.next()) {
                    var json = rs.getString("gamedata");
                    list.add(new Gson().fromJson(json, GameData.class));
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return list;
    }

    public void clear() throws DataAccessException {
        try (var statement = conn.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE game");
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

}
