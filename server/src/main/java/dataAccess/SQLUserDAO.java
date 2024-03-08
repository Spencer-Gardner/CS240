package dataAccess;

import model.UserData;
import java.sql.*;
import com.google.gson.Gson;

public class SQLUserDAO implements UserDAO {

    public SQLUserDAO() throws DataAccessException {
        DatabaseManager.createDatabase();
    }

    public boolean verifyUser(String username, String password) throws DataAccessException {
        UserData user = null;
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("SELECT userdata FROM user WHERE username=?")) {
                statement.setString(1, username);
                try (var rs = statement.executeQuery()) {
                    if (rs.next()) {
                        var json = rs.getString("userdata");
                        user = new Gson().fromJson(json, UserData.class);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return user != null && user.password().equals(password);
    }

    public void addUser(UserData user) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.prepareStatement("INSERT INTO user (username, userdata) VALUES (?, ?)")) {
                statement.setString(1, user.username());
                var json = new Gson().toJson(user, UserData.class);
                statement.setString(2, json);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(403, "Error: already taken");
        }
    }

    public void clear() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var statement = conn.createStatement()) {
                statement.executeUpdate("TRUNCATE TABLE user");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

}
