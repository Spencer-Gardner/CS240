package chess;

import java.util.*;

import static chess.ChessPiece.PieceType.KING;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private static ChessBoard board;
    private static TeamColor team;

    public ChessGame() { }

    @Override
    public String toString() {
        return "ChessGame{}";
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        ChessGame.team = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK;

        public TeamColor getOpposite() {
            return this == WHITE ? BLACK : WHITE;
        }
    }

    /**
     * Gets valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        return board.getPiece(startPosition).pieceMoves(board, startPosition);
    }

    public Collection<ChessMove> allValidMoves(TeamColor teamColor) {
        List<ChessMove> teamMoves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece != null) {
                    if (piece.getTeamColor() == teamColor) {
                        teamMoves.addAll(validMoves(new ChessPosition(i, j)));
                    }
                }
            }
        }
        return teamMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.startPosition());
        board.removePiece(move.startPosition());
        board.removePiece(move.endPosition());
        board.addPiece(move.endPosition(), piece);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        List<ChessMove> opponentMoves = new ArrayList<>(allValidMoves(teamColor.getOpposite()));
        ChessPosition kingPosition = getKingPosition(teamColor);
        for (ChessMove move : opponentMoves) {
            if (move.getEndPosition() == kingPosition) {
                return true;
            }
        }
        return false;
    }

    public ChessPosition getKingPosition(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece != null) {
                    if (piece.getTeamColor() == teamColor && piece.getPieceType() == KING) {
                        return new ChessPosition(i, j);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

//    public boolean canFlee(TeamColor teamColor) {
//        ChessPosition kingPosition = getKingPosition(teamColor);
//        List<ChessMove> kingMoves = new ArrayList<>(validMoves(kingPosition));
//        for (ChessMove move : kingMoves) {
//
//        }
//    }
//
//    public boolean checkTest(TeamColor teamColor, ChessPosition testPosition) {
//        Set<ChessMove> opponentMoves = new HashSet<>();
//        for (int i = 1; i <= 8; i++) {
//            for (int j = 1; j <= 8; j++) {
//                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
//                if (piece != null) {
//                    if (piece.getTeamColor() != teamColor) {
//                        opponentMoves.addAll(validMoves(new ChessPosition(i, j)));
//                    }
//                }
//            }
//        }
//        for (ChessMove move : opponentMoves) {
//            if (move.getEndPosition() == testPosition) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public boolean canProtect(TeamColor teamColor) {
//
//    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        List<ChessMove> teamMoves = new ArrayList<>(allValidMoves(teamColor));
        return !isInCheck(teamColor) && teamMoves.isEmpty();
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        ChessGame.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

}
