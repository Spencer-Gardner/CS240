package chess.Move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMove implements PieceMove {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> combinedList = new ArrayList<>();
        combinedList.addAll(PieceMove.linearCalculator(board, myPosition, 1, 0));
        combinedList.addAll(PieceMove.linearCalculator(board, myPosition, -1, 0));
        combinedList.addAll(PieceMove.linearCalculator(board, myPosition, 0, 1));
        combinedList.addAll(PieceMove.linearCalculator(board, myPosition, 0, -1));
        return combinedList;
    }

}
