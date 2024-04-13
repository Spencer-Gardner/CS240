package dataAccess;

import java.sql.*;
import com.google.gson.Gson;
import model.GameData;
import chess.ChessGame;
import java.util.*;

public class SQLGameDAO implements GameDAO {
    static AuthDAO authDAO;

    public SQLGameDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
        try {
            authDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public int addGame(String name) throws DataAccessException {
        int id = 0;
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("INSERT INTO game (gamedata) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
                var json = new Gson().toJson(new GameData(id, "", "", name, new ChessGame(), new ArrayList<>()), GameData.class);
                statement.setString(1, json);
                statement.executeUpdate();
                var rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getInt(1);
                    json = new Gson().toJson(new GameData(id, "", "", name, new ChessGame(), new ArrayList<>()), GameData.class);
                    try (var updateStatement = conn.prepareStatement("UPDATE game SET gamedata = ? WHERE id = ?")) {
                        updateStatement.setString(1, json);
                        updateStatement.setInt(2, id);
                        updateStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return id;
    }

    public void updateGame(String color, int id, String authToken) throws DataAccessException {
        GameData game = null;
        try (var conn = DatabaseManager.getConnection()) {
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
                    }
                } else if (color.equalsIgnoreCase("white")) {
                    if (game.whiteUsername().isEmpty()) {
                        try (var statement = conn.prepareStatement("UPDATE game SET gamedata=? WHERE id=?")) {
                            statement.setInt(2, id);
                            var json = new Gson().toJson(new GameData(game.gameID(), user, game.blackUsername(), game.gameName(), game.game(), game.observers()), GameData.class);
                            statement.setString(1, json);
                            statement.executeUpdate();
                        }
                    } else {
                        throw new DataAccessException(403, "Error: already taken");
                    }
                } else if (color.equalsIgnoreCase("black")) {
                    if (game.blackUsername().isEmpty()) {
                        try (var statement = conn.prepareStatement("UPDATE game SET gamedata=? WHERE id=?")) {
                            statement.setInt(2, id);
                            var json = new Gson().toJson(new GameData(game.gameID(), game.whiteUsername(), user, game.gameName(), game.game(), game.observers()), GameData.class);
                            statement.setString(1, json);
                            statement.executeUpdate();
                        }
                    } else {
                        throw new DataAccessException(403, "Error: already taken");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public Collection<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> list = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT gamedata FROM game")) {
                try (var rs = statement.executeQuery()) {
                    while (rs.next()) {
                        var json = rs.getString("gamedata");
                        list.add(new Gson().fromJson(json, GameData.class));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return list;
    }

    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.createStatement()) {
                statement.executeUpdate("TRUNCATE TABLE game");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public ChessGame getGame(int id) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT gamedata FROM game WHERE id=?")) {
                statement.setInt(1, id);
                try (var rs = statement.executeQuery()) {
                    if (rs.next()) {
                        var json = rs.getString("gamedata");
                        GameData game = new Gson().fromJson(json, GameData.class);
                        return game.game();
                    } else {
                        throw new DataAccessException(400, "Error: bad request");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void updateBoard(int id, ChessGame newGame) throws DataAccessException, SQLException {
        GameData game = null;
        try (var conn = DatabaseManager.getConnection()) {
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
            }
        }
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("UPDATE game SET gamedata=? WHERE id=?")) {
                statement.setInt(2, id);
                var json = new Gson().toJson(new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), newGame, game.observers()), GameData.class);
                statement.setString(1, json);
                statement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public String getWhiteUser(int id) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT gamedata FROM game WHERE id=?")) {
                statement.setInt(1, id);
                try (var rs = statement.executeQuery()) {
                    if (rs.next()) {
                        var json = rs.getString("gamedata");
                        GameData game = new Gson().fromJson(json, GameData.class);
                        return game.whiteUsername();
                    } else {
                        throw new DataAccessException(400, "Error: bad request");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public String getBlackUser(int id) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT gamedata FROM game WHERE id=?")) {
                statement.setInt(1, id);
                try (var rs = statement.executeQuery()) {
                    if (rs.next()) {
                        var json = rs.getString("gamedata");
                        GameData game = new Gson().fromJson(json, GameData.class);
                        return game.blackUsername();
                    } else {
                        throw new DataAccessException(400, "Error: bad request");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void removePlayer(int id, String color) throws DataAccessException, SQLException {
        GameData game = null;
        try (var conn = DatabaseManager.getConnection()) {
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
            }
        }
        if (Objects.equals(color, "WHITE")) {
            try (var conn = DatabaseManager.getConnection()) {
                try (var statement = conn.prepareStatement("UPDATE game SET gamedata=? WHERE id=?")) {
                    statement.setInt(2, id);
                    var json = new Gson().toJson(new GameData(game.gameID(), "", game.blackUsername(), game.gameName(), game.game(), game.observers()), GameData.class);
                    statement.setString(1, json);
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        } else if (Objects.equals(color, "BLACK")) {
            try (var conn = DatabaseManager.getConnection()) {
                try (var statement = conn.prepareStatement("UPDATE game SET gamedata=? WHERE id=?")) {
                    statement.setInt(2, id);
                    var json = new Gson().toJson(new GameData(game.gameID(), game.whiteUsername(), "", game.gameName(), game.game(), game.observers()), GameData.class);
                    statement.setString(1, json);
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        }
    }

}
