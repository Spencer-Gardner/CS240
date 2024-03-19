package facade;

import server.Server;

public class OpenServer {

    public static void main(String[] args) {
        int port = 8080;
        Server server = new Server();
        server.run(port);
    }

}
