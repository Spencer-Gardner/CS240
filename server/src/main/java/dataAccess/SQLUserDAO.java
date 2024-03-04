package dataAccess;

import com.google.gson.Gson;
import model.UserData;

import java.sql.*;

public class SQLUserDAO implements UserDAO {
    public static Connection conn;

    public SQLUserDAO() throws DataAccessException {
        conn = DatabaseManager.getConnection();
    }

    public boolean verifyUser(String username, String password) throws DataAccessException {
        UserData user = null;
        try (var statement = conn.prepareStatement("SELECT userdata FROM user WHERE username=?")) {
            statement.setString(1, username);
            try (var rs = statement.executeQuery()) {
                while (rs.next()) {
                    var json = rs.getString("userdata");
                    user = new Gson().fromJson(json, UserData.class);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        if (user != null) {
            return user.password().equals(password);
        } else {
            return false;
        }
    }

    public void addUser(UserData user) throws DataAccessException {
        try (var statement = conn.prepareStatement("INSERT INTO user (username, userdata) VALUES (?, ?)")) {
            statement.setString(1, user.username());
            var json = new Gson().toJson(user);
            statement.setString(2, json);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(403, "Error: already taken");
        }
    }

    public void clear() throws DataAccessException {
        try (var statement = conn.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE user");
        } catch (SQLException e) {
            throw new DataAccessException(500, "Error: ");
        }
    }

}
