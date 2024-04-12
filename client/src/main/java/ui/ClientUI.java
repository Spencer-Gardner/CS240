package ui;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import facade.ServerFacade;
import java.io.IOException;
import chess.ChessGame;
import com.google.gson.JsonArray;
import facade.WebSocketFacade;

import static ui.EscapeSequences.RESET_TEXT_COLOR;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class ClientUI {
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean isLoggedIn = false;
    private static boolean isInGame = false;
    private static String authToken;
    private static String id;
    private static ChessGame.TeamColor color;
    private static ArrayList<String> list = new ArrayList<>();
    private static ChessGame game;
    public static ServerFacade facade = new ServerFacade(8080);
    public static WebSocketFacade socket;

    static {
        try {
            socket = new WebSocketFacade("http://localhost:8080");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws Exception {

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
                } catch (Exception e) {
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
                } catch (Exception e) {
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

    private static void postLoginCommands(String command) throws Exception {
        Scanner scanner = new Scanner(System.in);
        String name;
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
                } catch (Exception e) {
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
                } catch (Exception e) {
                    System.out.println("Invalid");
                    break;
                }
                System.out.println("Created");
                break;
            case "list":
                JsonArray games;
                try {
                    games = facade.list(authToken);
                } catch (Exception e) {
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
                String inputColor = scanner.nextLine();                
                try {
                    color = convertColor(inputColor);
                    facade.join(authToken, list.get(Integer.parseInt(id)), inputColor);
                    socket.joinPlayer(authToken, Integer.parseInt(list.get(Integer.parseInt(id))), color);
                    RenderBoard.drawChessBoard(game, color);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    break;
                }
                isInGame = true;
                break;
            case "observe":
                System.out.print("+ Enter Game #: ");
                id = scanner.nextLine();
                try {
                    facade.observe(authToken, list.get(Integer.parseInt(id)));
                    socket.joinObserver(authToken, Integer.parseInt(id));
                    RenderBoard.drawChessBoard(game, color);
                } catch (Exception e) {
                    System.out.println("Error");
                    break;
                }
                isInGame = true;
                break;
            default:
                System.out.println("Unknown -- type 'help' for available commands.");
                break;
        }
    }

    private static void gameplayCommands(String command) {
        String startRow;
        String startCol;
        String endRow;
        String endCol;
        switch (command.toLowerCase()) {
            case "help":
                System.out.println("'redraw' - to redraw the chess board");
                System.out.println("'leave' - to exit the game");
                System.out.println("'move' - to make a move");
                System.out.println("'resign' - to resign");
                System.out.println("'highlight' - to highlight legal moves");
                break;
            case "redraw":
                try {
                    RenderBoard.drawChessBoard(game, color);
                } catch (Exception e) {
                    System.out.println("Error");
                    break;
                }
                break;
            case "leave":
                try {
                    socket.leave(authToken, Integer.parseInt(id));
                } catch (Exception e) {
                    System.out.println("Error");
                    break;
                }
                isInGame = false;
                break;
            case "move":
                System.out.print("+ Enter Piece Row: ");
                startRow = scanner.nextLine();
                System.out.print("+ Enter Piece Column: ");
                startCol = scanner.nextLine();
                System.out.print("+ Enter Move Row: ");
                endRow = scanner.nextLine();
                System.out.print("+ Enter Move Column: ");
                endCol = scanner.nextLine();
                System.out.print("+ Enter Promotion Piece (bishop|knight|rook|queen|null): ");
                String promotionPiece = scanner.nextLine();
                try {
                    socket.move(authToken, Integer.parseInt(id), new ChessMove(new ChessPosition(Integer.parseInt(startRow), Integer.parseInt(startCol)), new ChessPosition(Integer.parseInt(endRow), Integer.parseInt(endCol)), convertPiece(promotionPiece)));
                    RenderBoard.drawChessBoard(game, color);
                } catch (Exception e) {
                    System.out.println("Error");
                    break;
                }
                break;
            case "resign":
                System.out.print("+ Are you sure? (y|n): ");
                String response = scanner.nextLine();
                if (Objects.equals(response, "y")) {
                    try {
                        socket.resign(authToken, Integer.parseInt(id));
                    } catch (Exception e) {
                        System.out.println("Error");
                        break;
                    }
                } else if (Objects.equals(response, "n")) {
                    break;
                } else {
                    System.out.println("Invalid");
                    break;
                }
            case "highlight":
                System.out.print("+ Enter Piece Row: ");
                startRow = scanner.nextLine();
                System.out.print("+ Enter Piece Column: ");
                startCol = scanner.nextLine();
                try {
                    RenderBoard.highlight(game, color, new ChessPosition(Integer.parseInt(startRow), Integer.parseInt(startCol)));
                } catch (Exception e) {
                    System.out.println("Error");
                    break;
                }
                break;
            default:
                System.out.println("Unknown -- type 'help' for available commands.");
                break;
        }
    }
    
    
    public static void setGame(ChessGame game) {
        ClientUI.game = game;
    }

    private static ChessGame.TeamColor convertColor(String color) throws Exception {
        if (Objects.equals(color, "white")) {
            return ChessGame.TeamColor.WHITE;
        } else if (Objects.equals(color, "black")) {
            return ChessGame.TeamColor.BLACK;
        } else {
            throw new Exception(SET_TEXT_COLOR_RED + "* INVALID COLOR *" + RESET_TEXT_COLOR);
        }
    }

    private static ChessPiece.PieceType convertPiece(String piece) throws Exception {
        return switch (piece) {
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            case "rook" -> ChessPiece.PieceType.ROOK;
            case "queen" -> ChessPiece.PieceType.QUEEN;
            case "null" -> null;
            case null, default -> throw new Exception();
        };
    }
    
}