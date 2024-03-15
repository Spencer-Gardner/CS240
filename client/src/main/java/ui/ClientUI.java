package ui;

import java.util.Scanner;

public class ClientUI {
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean isLoggedIn = false;
    private static boolean isInGame = false;
    private static String authToken;

    public static void main(String[] args) {

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

    private static void preLoginCommands(String command) {
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
//                facade.login(username, password);
                isLoggedIn = true;
                System.out.println("Logged In");
                break;
            case "register":
                System.out.print("+ Enter Username: ");
                username = scanner.nextLine();
                System.out.print("+ Enter Password: ");
                password = scanner.nextLine();
//                facade.register(username, password, email);
                System.out.print("+ Enter Email: ");
                email = scanner.nextLine();
                System.out.println("Registered");
                break;
            default:
                System.out.println("Unknown command -- type 'help' for available commands.");
                break;
        }
    }

    private static void postLoginCommands(String command) {
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
//                facade.logout();
                isLoggedIn = false;
                System.out.println("Logged Out");
                break;
            case "create":
                System.out.print("+ Enter Game Name: ");
                name = scanner.nextLine();
//                facade.create(name);
//                System.out.println("Created new game with ID... " + <ID>);
                break;
            case "list":
//                facade.list();
//                System.out.print("<List>");
                break;
            case "join":
                System.out.print("+ Enter Game ID: ");
                id = scanner.nextLine();
                System.out.print("+ Enter Color (white|black): ");
                color = scanner.nextLine();
//                facade.join(id, color);
//                gameplay
                isInGame = true;
                break;
            case "observe":
                System.out.print("+ Enter Game ID: ");
                id = scanner.nextLine();
//                facade.observe(id);
//                gameplay
                isInGame = true;
                break;
            default:
                System.out.println("Unknown command -- type 'help' for available commands.");
                break;
        }
    }

    private static void gameplayCommands(String command) { }

}
