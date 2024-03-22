package ui;

import java.util.HashMap;
import java.util.Map;
import static ui.EscapeSequences.*;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;


public class RenderBoard {
    private static final int BOARD_SIZE = 8;

    private static final Map<String, String> PIECE_MAP = new HashMap<>();
    static {
        PIECE_MAP.put("WHITE_KING", SET_TEXT_BOLD + " K ");
        PIECE_MAP.put("WHITE_QUEEN", SET_TEXT_BOLD + " Q ");
        PIECE_MAP.put("WHITE_BISHOP", SET_TEXT_BOLD + " B ");
        PIECE_MAP.put("WHITE_KNIGHT", SET_TEXT_BOLD + " K ");
        PIECE_MAP.put("WHITE_ROOK", SET_TEXT_BOLD + " R ");
        PIECE_MAP.put("WHITE_PAWN", SET_TEXT_BOLD + " P ");
        PIECE_MAP.put("BLACK_KING", SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + " K ");
        PIECE_MAP.put("BLACK_QUEEN", SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + " Q ");
        PIECE_MAP.put("BLACK_BISHOP", SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + " B ");
        PIECE_MAP.put("BLACK_KNIGHT", SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + " K ");
        PIECE_MAP.put("BLACK_ROOK", SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + " R ");
        PIECE_MAP.put("BLACK_PAWN", SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + " P ");
    }

    public static void drawChessBoard(ChessGame game) {
        System.out.println(" ");
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if ((row + col) % 2 == 0) {
                    System.out.print(SET_BG_COLOR_DARK_GREEN);
                } else {
                    System.out.print(SET_BG_COLOR_LIGHT_GREY);
                }
                ChessPiece piece = game.getBoard().getPiece(new ChessPosition(row + 1, col + 1));
                if (piece != null) {
                    System.out.print(PIECE_MAP.get(piece.getTeamColor() + "_" + piece.getPieceType()));
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println("\u001B[0m");
        }
        System.out.println(" ");
        for (int row = BOARD_SIZE - 1; row >= 0; row--) {
            for (int col = BOARD_SIZE - 1; col >= 0; col--) {
                if ((row + col) % 2 == 0) {
                    System.out.print(SET_BG_COLOR_DARK_GREEN);
                } else {
                    System.out.print(SET_BG_COLOR_LIGHT_GREY);
                }
                ChessPiece piece = game.getBoard().getPiece(new ChessPosition(row + 1, col + 1));
                if (piece != null) {
                    System.out.print(PIECE_MAP.get(piece.getTeamColor() + "_" + piece.getPieceType()));
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println("\u001B[0m");
        }
        System.out.println(" ");
    }

}
