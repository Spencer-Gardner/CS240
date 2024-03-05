package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLUserDAO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import model.UserData;

class SQLUserDAOTest {

    @Test
    void verifyUser() {
    }

    @Test
    void addUser() throws DataAccessException {
        UserData data = new UserData("Test", "Pass", "@test.com");
        SQLUserDAO sql = new SQLUserDAO();
        sql.addUser(data);
    }

    @Test
    void clear() {
    }
}