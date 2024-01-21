package chess.Move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KingMove implements PieceMove {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return Calculator(board, myPosition);
    }

    public static Collection<ChessMove> Calculator(ChessBoard board, ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        ChessPosition start = new ChessPosition(row, col);
        ChessPiece piece = board.getPiece(start);
        List<ChessMove> validMoves = new ArrayList<>();

        int r;
        int c;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                r = row + i;
                c = col + j;
                if (validPosition(r, c)) {
                    ChessPiece square = board.getPiece(new ChessPosition(r, c));
                    if (square == null || piece.getTeamColor() != square.getTeamColor()) {
                        validMoves.add(new ChessMove(start, new ChessPosition(r, c), null));
                    }
                }
            }
        }

        return validMoves;
    }

    public static boolean validPosition(int x, int y) {
        return x >= 1 && x <= 8 && y >= 1 && y <= 8;
    }

}
