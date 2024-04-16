package ui;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static ui.EscapeSequences.*;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;


public class RenderBoard {
    private final String[] letters = {"8", "7", "6", "5", "4", "3", "2", "1"};

    private final Map<String, String> pieceMap = new HashMap<>();
    public RenderBoard() {
        pieceMap.put("WHITE_KING", SET_TEXT_BOLD + " K ");
        pieceMap.put("WHITE_QUEEN", SET_TEXT_BOLD + " Q ");
        pieceMap.put("WHITE_BISHOP", SET_TEXT_BOLD + " B ");
        pieceMap.put("WHITE_KNIGHT", SET_TEXT_BOLD + " N ");
        pieceMap.put("WHITE_ROOK", SET_TEXT_BOLD + " R ");
        pieceMap.put("WHITE_PAWN", SET_TEXT_BOLD + " P ");
        pieceMap.put("BLACK_KING", SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + " K ");
        pieceMap.put("BLACK_QUEEN", SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + " Q ");
        pieceMap.put("BLACK_BISHOP", SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + " B ");
        pieceMap.put("BLACK_KNIGHT", SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + " N ");
        pieceMap.put("BLACK_ROOK", SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + " R ");
        pieceMap.put("BLACK_PAWN", SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + " P ");
    }

    public void drawChessBoard(ChessGame game, ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.BLACK) {
            intro();
            for (int row = 0; row < 8; row++) {
                System.out.print((row + 1) + " ");
                for (int col = 0; col < 8; col++) {
                    even(game, row, col);
                }
                System.out.println("\u001B[0m");
            }
        } else {
            System.out.println("\u001B[0m");
            System.out.print("  ");
            for (String letter : letters) {
                System.out.print(" " + letter + " ");
            }
            System.out.println("\u001B[0m");
            for (int row = 7; row >= 0; row--) {
                System.out.print((row + 1) + " ");
                for (int col = 7; col >= 0; col--) {
                    even(game, row, col);
                }
                System.out.println("\u001B[0m");
            }
        }
    }

    private void even(ChessGame game, int row, int col) {
        if ((row + col) % 2 == 0) {
            System.out.print(SET_BG_COLOR_DARK_GREEN);
        } else {
            System.out.print(SET_BG_COLOR_LIGHT_GREY);
        }
        ChessPiece piece = game.getBoard().getPiece(new ChessPosition(row + 1, col + 1));
        if (piece != null) {
            System.out.print(pieceMap.get(piece.getTeamColor() + "_" + piece.getPieceType()));
        } else {
            System.out.print("   ");
        }
    }

    private void intro() {
        System.out.println("\u001B[0m");
        System.out.print("  ");
        for (int i = letters.length - 1; i >= 0; i--) {
            System.out.print(" " + letters[i] + " ");
        }
        System.out.println("\u001B[0m");
    }

    public void highlight(ChessGame game, ChessGame.TeamColor color, ChessPosition position) {
        ChessPiece highlightPiece = game.getBoard().getPiece(position);
        Collection<ChessMove> moves = highlightPiece.pieceMoves(game.getBoard(), position);
        if (color == ChessGame.TeamColor.BLACK) {
            intro();
            for (int row = 0; row < 8; row++) {
                System.out.print((row + 1) + " ");
                for (int col = 0; col < 8; col++) {
                    check(game, position, moves, row, col);
                }
                System.out.println("\u001B[0m");
            }
        } else {
            System.out.println("\u001B[0m");
            System.out.print("  ");
            for (String letter : letters) {
                System.out.print(" " + letter + " ");
            }
            System.out.println("\u001B[0m");
            for (int row = 7; row >= 0; row--) {
                System.out.print((row + 1) + " ");
                for (int col = 7; col >= 0; col--) {
                    check(game, position, moves, row, col);
                }
                System.out.println("\u001B[0m");
            }
        }
    }

    private void check(ChessGame game, ChessPosition position, Collection<ChessMove> moves, int row, int col) {
        boolean highlight = false;
        for (var move : moves) {
            if (Objects.equals(move.getEndPosition(), new ChessPosition(row + 1, col + 1))) {
                highlight = true;
                break;
            }
        }
        if (highlight) {
            System.out.print(SET_BG_COLOR_GREEN);
        } else if (position.equals(new ChessPosition(row + 1, col + 1))) {
            System.out.print(SET_BG_COLOR_YELLOW);
        } else {
            if ((row + col) % 2 == 0) {
                System.out.print(SET_BG_COLOR_DARK_GREEN);
            } else {
                System.out.print(SET_BG_COLOR_LIGHT_GREY);
            }
        }
        ChessPiece piece = game.getBoard().getPiece(new ChessPosition(row + 1, col + 1));
        if (piece != null) {
            System.out.print(pieceMap.get(piece.getTeamColor() + "_" + piece.getPieceType()));
        } else {
            System.out.print("   ");
        }
    }

}
