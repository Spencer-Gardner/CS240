package dataAccess;

import java.sql.*;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {
    private final Connection conn;

    public SQLAuthDAO() throws DataAccessException {
        conn = DatabaseManager.getConnection();
    }

    public boolean verifyAuth(String authToken) throws DataAccessException {
        try (var statement = conn.prepareStatement("SELECT token FROM auth WHERE token=?")) {
            statement.setString(1, authToken);
            try (var rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public String getUser(String authToken) throws DataAccessException {
        try (var statement = conn.prepareStatement("SELECT username FROM auth WHERE token=?")) {
            statement.setString(1, authToken);
            try (var rs = statement.executeQuery()) {
                if (rs.next()) {
                   return rs.getString(1);
                } else {
                    throw new DataAccessException(400, "Error: bad request");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(400, "Error: bad request");
        }
    }

    public String addAuth(String username) throws DataAccessException {
        try (var statement = conn.prepareStatement("INSERT INTO auth (token, username) VALUES (?, ?)")) {
            String authToken = UUID.randomUUID().toString();
            statement.setString(1, authToken);
            statement.setString(2, username);
            statement.executeUpdate();
            return authToken;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void removeAuth(String authToken) throws DataAccessException {
        try (var statement = conn.prepareStatement("DELETE FROM auth WHERE token=?")) {
            statement.setString(1, authToken);
            int affected = statement.executeUpdate();
            if (affected == 0) {
                throw new DataAccessException(401, "Error: unauthorized");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void clear() throws DataAccessException {
        try (var statement = conn.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE auth");
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

}
