package chess.Move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMove implements PieceMove {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> combinedList = new ArrayList<>();
        combinedList.addAll(Calculator(board, myPosition, 1, 0));
        combinedList.addAll(Calculator(board, myPosition, -1, 0));
        combinedList.addAll(Calculator(board, myPosition, 0, 1));
        combinedList.addAll(Calculator(board, myPosition, 0, -1));
        combinedList.addAll(Calculator(board, myPosition, 1, 1));
        combinedList.addAll(Calculator(board, myPosition, -1, 1));
        combinedList.addAll(Calculator(board, myPosition, -1, -1));
        combinedList.addAll(Calculator(board, myPosition, 1, -1));
        return combinedList;
    }

    public static Collection<ChessMove> Calculator(ChessBoard board, ChessPosition position, int v, int h) {
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition start = new ChessPosition(row, col);
        ChessPiece piece = board.getPiece(start);
        List<ChessMove> validMoves = new ArrayList<>();

        row = row + v;
        col = col + h;
        while (validPosition(row, col)) {
            ChessPiece square = board.getPiece(new ChessPosition(row, col));
            if (square == null) {
                validMoves.add(new ChessMove(start, new ChessPosition(row, col), null));
            } else if (piece.getTeamColor() != square.getTeamColor()) {
                validMoves.add(new ChessMove(start, new ChessPosition(row, col), null));
                break;
            } else if (piece.getTeamColor() == square.getTeamColor()) {
                break;
            }
            System.out.print(row + "," + col + " ");
            row = row + v;
            col = col + h;
        }

        return validMoves;
    }

    public static boolean validPosition(int x, int y) {
        return x >= 1 && x <= 8 && y >= 1 && y <= 8;
    }
}
