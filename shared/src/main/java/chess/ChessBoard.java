package chess;

import java.util.Arrays;

import static chess.ChessGame.TeamColor.*;
import static chess.ChessPiece.PieceType.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {}

    public ChessBoard(ChessBoard copy) {
        this.squares = new ChessPiece[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece originalPiece = copy.squares[i][j];
                this.squares[i][j] = originalPiece;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                '}';
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    public void movePiece(ChessMove move) {
        int startRow = move.getStartPosition().getRow();
        int startCol = move.getStartPosition().getColumn();
        int endRow = move.getEndPosition().getRow();
        int endCol = move.getEndPosition().getColumn();

        squares[endRow-1][endCol-1] = squares[startRow][startCol];
        squares[startRow][startCol] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares[0][0] = new ChessPiece(WHITE, ROOK);
        squares[0][1] = new ChessPiece(WHITE, KNIGHT);
        squares[0][2] = new ChessPiece(WHITE, BISHOP);
        squares[0][3] = new ChessPiece(WHITE, QUEEN);
        squares[0][4] = new ChessPiece(WHITE, KING);
        squares[0][5] = new ChessPiece(WHITE, BISHOP);
        squares[0][6] = new ChessPiece(WHITE, KNIGHT);
        squares[0][7] = new ChessPiece(WHITE, ROOK);
        for (int i = 0; i < 8; i++) {
            squares[1][i] = new ChessPiece(WHITE, PAWN);
        }
        squares[7][0] = new ChessPiece(BLACK, ROOK);
        squares[7][1] = new ChessPiece(BLACK, KNIGHT);
        squares[7][2] = new ChessPiece(BLACK, BISHOP);
        squares[7][3] = new ChessPiece(BLACK, QUEEN);
        squares[7][4] = new ChessPiece(BLACK, KING);
        squares[7][5] = new ChessPiece(BLACK, BISHOP);
        squares[7][6] = new ChessPiece(BLACK, KNIGHT);
        squares[7][7] = new ChessPiece(BLACK, ROOK);
        for (int i = 0; i < 8; i++) {
            squares[6][i] = new ChessPiece(BLACK, PAWN);
        }
    }

}
