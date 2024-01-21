package chess.Move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public interface PieceMove {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
}
