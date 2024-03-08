package dataAccess;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager {
    private static final String databaseName;
    private static final String user;
    private static final String password;
    private static final String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    static void createDatabase() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            var dbStatement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            try (var preparedStatement = conn.prepareStatement(dbStatement)) {
                preparedStatement.executeUpdate();
            }
            conn.setCatalog(databaseName);
            var userStatement = """
                    CREATE TABLE  IF NOT EXISTS  user (
                        username VARCHAR(255) NOT NULL,
                        userdata LONGTEXT NOT NULL,
                        PRIMARY KEY (username)
                    )""";
            try (var preparedStatement = conn.prepareStatement(userStatement)) {
                preparedStatement.executeUpdate();
            }
            var gameStatement = """
                    CREATE TABLE  IF NOT EXISTS  game (
                        id INT NOT NULL AUTO_INCREMENT,
                        gamedata LONGTEXT NOT NULL,
                        PRIMARY KEY (id)
                    )""";
            try (var preparedStatement = conn.prepareStatement(gameStatement)) {
                preparedStatement.executeUpdate();
            }
            var authStatement = """
                    CREATE TABLE  IF NOT EXISTS  auth (
                        token VARCHAR(255) NOT NULL,
                        username VARCHAR(255) NOT NULL,
                        PRIMARY KEY (token)
                    )""";
            try (var preparedStatement = conn.prepareStatement(authStatement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

}