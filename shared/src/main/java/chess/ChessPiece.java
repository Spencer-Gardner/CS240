package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        List<ChessMove> validMoves = new ArrayList<>();

        int i = row;
        int j = col;
        while (i <= 7 && j <= 7) {
            ChessPiece square = board.getPiece(new ChessPosition(i+1, j+1));
            if (square == null) {
                validMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(i+1, j+1), PieceType.QUEEN));
            } else if (piece.getTeamColor() != square.getTeamColor()) {
                validMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(i+1, j+1), PieceType.QUEEN));
                break;
            }
            System.out.print(i+1 + "," + (j+1) + " ");
            i++;
            j++;
        }

        i = row-2;
        j = col;
        while (i >= 0 && j <= 7) {
            ChessPiece square = board.getPiece(new ChessPosition(i+1, j+1));
            if (square == null) {
                validMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(i+1, j+1), PieceType.QUEEN));
            } else if (piece.getTeamColor() != square.getTeamColor()) {
                validMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(i+1, j+1), PieceType.QUEEN));
                break;
            }
            System.out.print(i+1 + "," + (j+1) + " ");
            i--;
            j++;
        }

        i = row-2;
        j = col-2;
        while (i >= 0 && j >= 0) {
            ChessPiece square = board.getPiece(new ChessPosition(i+1, j+1));
            if (square == null) {
                validMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(i+1, j+1), PieceType.QUEEN));
            } else if (piece.getTeamColor() != square.getTeamColor()) {
                validMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(i+1, j+1), PieceType.QUEEN));
                break;
            }
            System.out.print(i+1 + "," + (j+1) + " ");
            i--;
            j--;
        }

        i = row;
        j = col-2;
        while (i <= 7 && j >= 0) {
            ChessPiece square = board.getPiece(new ChessPosition(i+1, j+1));
            if (square == null) {
                validMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(i+1, j+1), PieceType.QUEEN));
            } else if (piece.getTeamColor() != square.getTeamColor()) {
                validMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(i+1, j+1), PieceType.QUEEN));
                break;
            }
            System.out.print(i+1 + "," + (j+1) + " ");
            i++;
            j--;
        }

        return validMoves;
    }
}