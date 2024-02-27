package serviceTests;

import dataAccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.Test;
import service.ApplicationService;
import service.UserService;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.fail;

class ApplicationServiceTests {

    @Test
    void clear() {
        UserData clearUser = new UserData("clearUser", "clearPassword", "clearUser5@test.com");
        try {
            UserService.register(clearUser);
            ApplicationService.clear();
            assertDoesNotThrow(() -> UserService.register(clearUser));
        } catch (DataAccessException e) {
            fail("Failed to clear");
        }
    }
}