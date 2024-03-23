package ui;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import facade.ServerFacade;
import java.io.IOException;
import chess.ChessGame;
import com.google.gson.JsonArray;

public class ClientUI {
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean isLoggedIn = false;
    private static boolean isInGame = false;
    private static String authToken;
    private static ArrayList<String> list = new ArrayList<>();
    public static ServerFacade facade = new ServerFacade(8080);

    public static void main(String[] args) throws IOException {

        System.out.println("Welcome to the Chess Client UI!");
        System.out.println("Enter a command or type 'quit' to exit.");

        String input;
        do {
            System.out.print(">> ");
            input = scanner.nextLine();
            if (!isLoggedIn) {
                preLoginCommands(input);
            } else if (!isInGame) {
                postLoginCommands(input);
            } else {
                gameplayCommands(input);
            }
        } while (!input.equalsIgnoreCase("quit"));

        scanner.close();
    }

    private static void preLoginCommands(String command) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String username;
        String password;
        String email;
        switch (command.toLowerCase()) {
            case "help":
                System.out.println("'register' - to create account");
                System.out.println("'login' - to play chess");
                System.out.println("'quit' - to exit client");
                System.out.println("'help' - to list commands");
                break;
            case "quit":
                break;
            case "login":
                System.out.print("+ Enter Username: ");
                username = scanner.nextLine();
                System.out.print("+ Enter Password: ");
                password = scanner.nextLine();
                try {
                    authToken = facade.login(username, password);
                } catch (IOException e) {
                    System.out.println("Invalid");
                    break;
                }
                isLoggedIn = true;
                System.out.println("Logged In");
                break;
            case "register":
                System.out.print("+ Enter Username: ");
                username = scanner.nextLine();
                System.out.print("+ Enter Password: ");
                password = scanner.nextLine();
                System.out.print("+ Enter Email: ");
                email = scanner.nextLine();
                try {
                    authToken = facade.register(username, password, email);
                } catch (IOException e) {
                    System.out.println("Invalid");
                    break;
                }
                isLoggedIn = true;
                System.out.println("Registered... Logged In");
                break;
            default:
                System.out.println("Unknown -- type 'help' for available commands.");
                break;
        }
    }

    private static void postLoginCommands(String command) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String name;
        String id;
        String color;
        switch (command.toLowerCase()) {
            case "help":
                System.out.println("'create' - to create a new game");
                System.out.println("'list' - to list available games");
                System.out.println("'join' - to join an available game");
                System.out.println("'observe' - to observe an active game");
                System.out.println("'logout' - to logout");
                System.out.println("'quit' - to exit client");
                System.out.println("'help' - to list commands");
                break;
            case "quit":
                break;
            case "logout":
                try {
                    facade.logout(authToken);
                } catch (IOException e) {
                    System.out.println("Error");
                    break;
                }
                isLoggedIn = false;
                System.out.println("Logged Out");
                break;
            case "create":
                System.out.print("+ Enter Game Name: ");
                name = scanner.nextLine();
                try {
                    id = facade.create(authToken, name);
                } catch (IOException e) {
                    System.out.println("Invalid");
                    break;
                }
                System.out.println("Created new game with ID... " + id);
                break;
            case "list":
                JsonArray games;
                try {
                    games = facade.list(authToken);
                } catch (IOException e) {
                    System.out.println("Error");
                    break;
                }
                list.clear();
                for (int i = 0; i < games.size(); i++) {
                    list.add(games.get(i).getAsJsonObject().get("gameID").getAsString());
                    System.out.println(i + " - ID: " + games.get(i).getAsJsonObject().get("gameID").getAsString() + ", Name: " + games.get(i).getAsJsonObject().get("gameName").getAsString() + ", Player 1: " + games.get(i).getAsJsonObject().get("whiteUsername").getAsString() + ", Player 2: " + games.get(i).getAsJsonObject().get("blackUsername").getAsString());
                }
                break;
            case "join":
                System.out.print("+ Enter Game #: ");
                id = scanner.nextLine();
                System.out.print("+ Enter Color (white|black): ");
                color = scanner.nextLine();
                try {
                    facade.join(authToken, list.get(Integer.parseInt(id)), color);
                } catch (IOException e) {
                    System.out.println("Error");
                    break;
                }
                RenderBoard.drawChessBoard(new ChessGame());
                isInGame = true;
                break;
            case "observe":
                System.out.print("+ Enter Game #: ");
                id = scanner.nextLine();
                try {
                    facade.observe(authToken, list.get(Integer.parseInt(id)));
                } catch (IOException e) {
                    System.out.println("Error");
                    break;
                }
                RenderBoard.drawChessBoard(new ChessGame());
                isInGame = true;
                break;
            default:
                System.out.println("Unknown -- type 'help' for available commands.");
                break;
        }
    }

    private static void gameplayCommands(String command) {
        switch (command.toLowerCase()) {
            case "help":
                System.out.println("'quit' - to exit client");
                break;
            case "quit":
                break;
            case "back":
                isInGame = false;
                break;
            default:
                System.out.println("Unknown -- type 'help' for available commands.");
                break;
        }
    }

}
