package serviceTests;

import dataAccess.DataAccessException;
import model.*;
import org.junit.jupiter.api.Test;
import requests.LoginRequest;
import service.UserService;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceTests {

    @Test
    void registerPositive() {
        UserData newUser = new UserData("newUser", "newPassword", "newUser@test.com");
        try {
            AuthData authData = UserService.register(newUser);
            assertEquals("newUser", authData.username());
        } catch (DataAccessException e) {
            fail("User does not match registration");
        }
    }

    @Test
    void registerNegative() {
        UserData invalidRegistration = new UserData(null, null, null);
        try {
            AuthData authData = UserService.register(invalidRegistration);
            fail("Null invalid input for user");
        } catch (DataAccessException e) {
            assertEquals(400, e.getCode());
        }
    }

    @Test
    void loginPositive() {
        UserData oldUser = new UserData("oldUser", "oldPassword", "oldUser@test.com");
        LoginRequest returning = new LoginRequest("oldUser", "oldPassword");
        try {
            UserService.register(oldUser);
            AuthData authData = UserService.login(returning);
            assertEquals("oldUser", authData.username());
        } catch (DataAccessException e) {
            fail("Login does not match");
        }
    }

    @Test
    void loginNegative() {
        LoginRequest invalidLogin = new LoginRequest("invalid", "invalid");
        try {
            AuthData authData = UserService.login(invalidLogin);
            fail("Invalid input should fail authorization");
        } catch (DataAccessException e) {
            assertEquals(401, e.getCode());
        }
    }

    @Test
    void logoutPositive() {
        try {
            UserData newUser = new UserData("endUser", "endPassword", "endUser@test.com");
            AuthData authData = UserService.register(newUser);
            assertDoesNotThrow(() -> UserService.logout(authData.authToken()));
        } catch (DataAccessException e) {
            fail("Unexpected error thrown");
        }
    }

    @Test
    void logoutNegative() {
        try {
            UserService.logout("invalid");
            fail("Unexpected error thrown");
        } catch (DataAccessException e) {
            assertEquals(401, e.getCode());
        }
    }
}
