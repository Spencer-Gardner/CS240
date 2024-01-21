package chess.Move;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.QUEEN;

public class PawnMove implements PieceMove {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn())).getTeamColor() == WHITE) {
            return WhiteCalculator(board, myPosition);
        } else if (board.getPiece(new ChessPosition(myPosition.getRow(), myPosition.getColumn())).getTeamColor() == BLACK) {
            return BlackCalculator(board, myPosition);
        } else {
            return null;
        }
    }

    public static Collection<ChessMove> WhiteCalculator(ChessBoard board, ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition start = new ChessPosition(row, col);
        List<ChessMove> validMoves = new ArrayList<>();

        if (validPosition(row+1, col+1) && board.getPiece(new ChessPosition(row+1, col+1)) != null && board.getPiece(new ChessPosition(row+1, col+1)).getTeamColor() == BLACK) {
            if (row == 7) {
                validMoves.add(new ChessMove(start, new ChessPosition(row+1, col+1), QUEEN));
            } else {
                validMoves.add(new ChessMove(start, new ChessPosition(row+1, col+1), null));
            }
        }
        if (validPosition(row+1, col-1) && board.getPiece(new ChessPosition(row+1, col-1)) != null && board.getPiece(new ChessPosition(row+1, col-1)).getTeamColor() == BLACK) {
            if (row == 7) {
                validMoves.add(new ChessMove(start, new ChessPosition(row+1, col-1), QUEEN));
            } else {
                validMoves.add(new ChessMove(start, new ChessPosition(row+1, col-1), null));
            }
        }
        if (validPosition(row+1, col) && board.getPiece(new ChessPosition(row+1, col)) == null) {
            if (row == 7) {
                validMoves.add(new ChessMove(start, new ChessPosition(row+1, col), QUEEN));
            } else {
                validMoves.add(new ChessMove(start, new ChessPosition(row+1, col), null));
            }
        }
        if (row == 2 && board.getPiece(new ChessPosition(row+1, col)) == null && board.getPiece(new ChessPosition(row+2, col)) == null) {
            validMoves.add(new ChessMove(start, new ChessPosition(row+2, col), null));
        }

        return validMoves;
    }

    public static Collection<ChessMove> BlackCalculator(ChessBoard board, ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition start = new ChessPosition(row, col);
        List<ChessMove> validMoves = new ArrayList<>();

        if (validPosition(row-1, col+1) && board.getPiece(new ChessPosition(row-1, col+1)) != null && board.getPiece(new ChessPosition(row-1, col+1)).getTeamColor() == WHITE) {
            validMoves.add(new ChessMove(start, new ChessPosition(row-1, col+1), null));
        }
        if (validPosition(row-1, col-1) && board.getPiece(new ChessPosition(row-1, col-1)) != null && board.getPiece(new ChessPosition(row-1, col-1)).getTeamColor() == WHITE) {
            validMoves.add(new ChessMove(start, new ChessPosition(row-1, col-1), null));
        }
        if (validPosition(row-1, col) && board.getPiece(new ChessPosition(row-1, col)) == null) {
            validMoves.add(new ChessMove(start, new ChessPosition(row-1, col), null));
        }
        if (row == 7 && board.getPiece(new ChessPosition(row-1, col)) == null && board.getPiece(new ChessPosition(row-2, col)) == null) {
            validMoves.add(new ChessMove(start, new ChessPosition(row-2, col), null));
        }

        return validMoves;
    }

    public static boolean validPosition(int x, int y) {
        return x >= 1 && x <= 8 && y >= 1 && y <= 8;
    }
}
