package serviceTests;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Test;
import requests.CreateRequest;
import requests.JoinRequest;
import results.CreateResult;
import service.GameService;
import service.UserService;
import static chess.ChessGame.TeamColor.WHITE;
import static org.junit.jupiter.api.Assertions.*;

class GameServiceTests {

    @Test
    void listGamesPositive() {
        UserData testUser = new UserData("testUser", "testPassword", "testUser@test.com");
        CreateRequest newGame1 = new CreateRequest("newGame1");
        CreateRequest newGame2 = new CreateRequest("newGame2");
        CreateRequest newGame3 = new CreateRequest("newGame3");
        try {
            AuthData authData = UserService.register(testUser);
            GameService.createGame(newGame1, authData.authToken());
            GameService.createGame(newGame2, authData.authToken());
            GameService.createGame(newGame3, authData.authToken());
            assertNotNull(GameService.listGames(authData.authToken()));
        } catch (DataAccessException e) {
            fail("No games returned");
        }
    }

    @Test
    void listGamesNegative() {
        UserData testUser = new UserData("testUser2", "testPassword2", "testUser2@test.com");
        CreateRequest newGame1 = new CreateRequest("newGame4");
        CreateRequest newGame2 = new CreateRequest("newGame5");
        CreateRequest newGame3 = new CreateRequest("newGame6");
        try {
            AuthData authData = UserService.register(testUser);
            GameService.createGame(newGame1, authData.authToken());
            GameService.createGame(newGame2, authData.authToken());
            GameService.createGame(newGame3, authData.authToken());
            GameService.listGames("fake");
            fail("Returns list of games");
        } catch (DataAccessException e) {
            assertEquals(401, e.getCode());
        }
    }

    @Test
    void createGamePositive() {
        UserData testUser = new UserData("testUser3", "testPassword3", "testUser3@test.com");
        CreateRequest createRequest = new CreateRequest("newGame7");
        try {
            AuthData authData = UserService.register(testUser);
            CreateResult result = GameService.createGame(createRequest, authData.authToken());
            assertNotNull(result);
        } catch (DataAccessException e) {
            fail("Failed to create game");
        }
    }

    @Test
    void createGameNegative() {
        UserData testUser = new UserData("testUser4", "testPassword4", "testUser4@test.com");
        CreateRequest createRequest = new CreateRequest("newGame8");
        try {
            GameService.createGame(createRequest, "false");
            fail("Created invalid game");
        } catch (DataAccessException e) {
            assertEquals(401, e.getCode());
        }
    }

    @Test
    void joinGamePositive() {
        UserData testUser = new UserData("testUser5", "testPassword5", "testUser5@test.com");
        CreateRequest createRequest = new CreateRequest("newGame9");
        try {
            AuthData authData = UserService.register(testUser);
            CreateResult result = GameService.createGame(createRequest, authData.authToken());
            JoinRequest joinRequest = new JoinRequest("white", result.gameID());
            assertDoesNotThrow(() -> GameService.joinGame(joinRequest, authData.authToken()));
        } catch (DataAccessException e) {
            fail("Failed to join game");
        }
    }

    @Test
    void joinGameNegative() {
        UserData testUser = new UserData("testUser6", "testPassword6", "testUser6@test.com");
        CreateRequest createRequest = new CreateRequest("newGame10");
        try {
            AuthData authData = UserService.register(testUser);
            CreateResult result = GameService.createGame(createRequest, authData.authToken());
            JoinRequest joinRequest = new JoinRequest("white", result.gameID());
            GameService.joinGame(joinRequest, "empty");
            fail("Joined invalid game");
        } catch (DataAccessException e) {
            assertEquals(401, e.getCode());
        }
    }
}
