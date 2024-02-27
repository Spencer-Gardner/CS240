package chess.Move;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMove implements PieceMove {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> combinedList = new ArrayList<>();
        combinedList.addAll(PieceMove.linearCalculator(board, myPosition, 1, 1));
        combinedList.addAll(PieceMove.linearCalculator(board, myPosition, -1, 1));
        combinedList.addAll(PieceMove.linearCalculator(board, myPosition, -1, -1));
        combinedList.addAll(PieceMove.linearCalculator(board, myPosition, 1, -1));
        return combinedList;
    }

}
