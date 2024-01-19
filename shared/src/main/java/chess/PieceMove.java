package chess;

import java.util.Collection;

public interface PieceMove {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
}
