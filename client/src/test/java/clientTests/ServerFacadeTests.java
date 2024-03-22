package clientTests;

import server.Server;
import facade.ServerFacade;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;
    private static String authToken;
    private static String id;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    @Order(1)
    void registerPositive() throws IOException {
        authToken = facade.register("player1", "password", "p1@email.com");
        assertTrue(authToken.length() > 10);
    }

    @Test
    @Order(2)
    void registerNegative() throws IOException {
        assertThrows(IOException.class, () -> {
            facade.register("player1", "password", "p1@email.com");
        });
    }

    @Test
    @Order(3)
    void loginPositive() throws IOException {
        authToken = facade.login("player1", "password");
        assertTrue(authToken.length() > 10);
    }

    @Test
    @Order(4)
    void loginNegative() throws IOException {
        assertThrows(IOException.class, () -> {
            facade.login("hacker", "attempt");
        });
    }

    @Test
    @Order(5)
    void createPositive() throws IOException {
        id = facade.create(authToken, "New Game");
        assertNotNull(id);
    }

    @Test
    @Order(6)
    void createNegative() throws IOException {
        assertThrows(IOException.class, () -> {
           facade.create("unauthorized", "New Game");
        });
    }

    @Test
    @Order(7)
    void listPositive() throws IOException {
        String list = facade.list(authToken).toString();
        assertTrue(list.length() > 10);
    }

    @Test
    @Order(8)
    void listNegative() throws IOException {
        assertThrows(IOException.class, () -> {
            facade.list("unauthorized");
        });
    }

    @Test
    @Order(9)
    void joinPositive() throws IOException {
        assertDoesNotThrow(() -> {
            facade.join(authToken, id, "white");
        });
    }

    @Test
    @Order(10)
    void joinNegative() throws IOException {
        assertThrows(IOException.class, () -> {
            facade.join(authToken, "NA", "white");
        });
    }

    @Test
    @Order(11)
    void observePositive() throws IOException {
        assertDoesNotThrow(() -> {
            facade.observe(authToken, id);
        });
    }

    @Test
    @Order(12)
    void observeNegative() throws IOException {
        assertThrows(IOException.class, () -> {
            facade.observe(authToken, "NA");
        });
    }

    @Test
    @Order(13)
    void logoutPositive() throws IOException {
        assertDoesNotThrow(() -> {
            facade.logout(authToken);
        });
    }

    @Test
    @Order(14)
    void logoutNegative() throws IOException {
        assertThrows(IOException.class, () -> {
            facade.logout("unauthorized");
        });
    }

}