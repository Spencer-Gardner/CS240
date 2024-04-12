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
    private static final String[] LETTERS = {"a", "b", "c", "d", "e", "f", "g", "h"};

    private static final Map<String, String> PIECE_MAP = new HashMap<>();
    static {
        PIECE_MAP.put("WHITE_KING", SET_TEXT_BOLD + " K ");
        PIECE_MAP.put("WHITE_QUEEN", SET_TEXT_BOLD + " Q ");
        PIECE_MAP.put("WHITE_BISHOP", SET_TEXT_BOLD + " B ");
        PIECE_MAP.put("WHITE_KNIGHT", SET_TEXT_BOLD + " N ");
        PIECE_MAP.put("WHITE_ROOK", SET_TEXT_BOLD + " R ");
        PIECE_MAP.put("WHITE_PAWN", SET_TEXT_BOLD + " P ");
        PIECE_MAP.put("BLACK_KING", SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + " K ");
        PIECE_MAP.put("BLACK_QUEEN", SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + " Q ");
        PIECE_MAP.put("BLACK_BISHOP", SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + " B ");
        PIECE_MAP.put("BLACK_KNIGHT", SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + " N ");
        PIECE_MAP.put("BLACK_ROOK", SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + " R ");
        PIECE_MAP.put("BLACK_PAWN", SET_TEXT_BOLD + SET_TEXT_COLOR_BLACK + " P ");
    }

    public static void drawChessBoard(ChessGame game, ChessGame.TeamColor color) {
        if (color == ChessGame.TeamColor.WHITE) {
            System.out.println("\u001B[0m");
            System.out.print("  ");
            for (int i = LETTERS.length - 1; i >= 0; i--) {
                System.out.print(" " + LETTERS[i] + " ");
            }
            System.out.println("\u001B[0m");
            for (int row = 0; row < 8; row++) {
                System.out.print((row + 1) + " ");
                for (int col = 0; col < 8; col++) {
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
        } else {
            System.out.println("\u001B[0m");
            System.out.print("  ");
            for (String letter : LETTERS) {
                System.out.print(" " + letter + " ");
            }
            System.out.println("\u001B[0m");
            for (int row = 7; row >= 0; row--) {
                System.out.print((row + 1) + " ");
                for (int col = 7; col >= 0; col--) {
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
        }
    }

    public static void highlight(ChessGame game, ChessGame.TeamColor color, ChessPosition position) {
        ChessPiece highlightPiece = game.getBoard().getPiece(position);
        Collection<ChessMove> moves = highlightPiece.pieceMoves(game.getBoard(), position);
        if (color == ChessGame.TeamColor.WHITE) {
            System.out.println("\u001B[0m");
            System.out.print("  ");
            for (int i = LETTERS.length - 1; i >= 0; i--) {
                System.out.print(" " + LETTERS[i] + " ");
            }
            System.out.println("\u001B[0m");
            for (int row = 0; row < 8; row++) {
                System.out.print((row + 1) + " ");
                for (int col = 0; col < 8; col++) {
                    for (var move : moves) {
                        if (Objects.equals(move.getEndPosition(), new ChessPosition(row, col))) {
                            System.out.print(SET_BG_COLOR_GREEN);
                        } else {
                            if ((row + col) % 2 == 0) {
                                System.out.print(SET_BG_COLOR_DARK_GREEN);
                            } else {
                                System.out.print(SET_BG_COLOR_LIGHT_GREY);
                            }
                        }
                        ChessPiece piece = game.getBoard().getPiece(new ChessPosition(row + 1, col + 1));
                        if (piece != null) {
                            System.out.print(PIECE_MAP.get(piece.getTeamColor() + "_" + piece.getPieceType()));
                        } else {
                            System.out.print("   ");
                        }
                    }
                }
                System.out.println("\u001B[0m");
            }
        } else {
            System.out.println("\u001B[0m");
            System.out.print("  ");
            for (String letter : LETTERS) {
                System.out.print(" " + letter + " ");
            }
            System.out.println("\u001B[0m");
            for (int row = 7; row >= 0; row--) {
                System.out.print((row + 1) + " ");
                for (int col = 7; col >= 0; col--) {
                    for (var move : moves) {
                        if (Objects.equals(move.getEndPosition(), new ChessPosition(row, col))) {
                            System.out.print(SET_BG_COLOR_GREEN);
                        } else {
                            if ((row + col) % 2 == 0) {
                                System.out.print(SET_BG_COLOR_DARK_GREEN);
                            } else {
                                System.out.print(SET_BG_COLOR_LIGHT_GREY);
                            }
                        }
                        ChessPiece piece = game.getBoard().getPiece(new ChessPosition(row + 1, col + 1));
                        if (piece != null) {
                            System.out.print(PIECE_MAP.get(piece.getTeamColor() + "_" + piece.getPieceType()));
                        } else {
                            System.out.print("   ");
                        }
                    }
                }
                System.out.println("\u001B[0m");
            }
        }
    }

}
