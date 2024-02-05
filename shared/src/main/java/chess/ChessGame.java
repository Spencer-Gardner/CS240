package chess;

import java.util.*;

import static chess.ChessPiece.PieceType.*;

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

    public ChessGame createTempCopy() {
        ChessGame tempCopy = new ChessGame();
        tempCopy.setBoard(board);
        tempCopy.setTeamTurn(team);
        return tempCopy;
    }

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
                    if (piece.getTeamColor().equals(teamColor)) {
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
        try {
            ChessPiece piece = board.getPiece(move.startPosition());
            board.removePiece(move.startPosition());
            board.removePiece(move.endPosition());
            board.addPiece(move.endPosition(), piece);
        } catch (Exception e) {
            throw new InvalidMoveException("Invalid");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        List<ChessMove> opponentMoves = new ArrayList<>(allValidMoves(teamColor.getOpposite()));
        ChessPosition kingPosition = kingPosition(teamColor);
        for (ChessMove move : opponentMoves) {
            if (move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }
        return false;
    }

    public ChessPosition kingPosition(TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece != null) {
                    if (piece.getTeamColor().equals(teamColor) && piece.getPieceType().equals(KING)) {
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
        return !canFlee(teamColor) && !canProtect(teamColor);
    }

    public boolean canFlee(TeamColor teamColor) {
        ChessPosition kingPosition = kingPosition(teamColor);
        List<ChessMove> kingMoves = new ArrayList<>(validMoves(kingPosition));
        for (ChessMove move : kingMoves) {
            if (!checkTest(teamColor, move.endPosition())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkTest(TeamColor teamColor, ChessPosition testPosition) {
        Set<ChessMove> opponentMoves = new HashSet<>(allValidMoves(teamColor.getOpposite()));
        for (ChessMove move : opponentMoves) {
            if (move.getEndPosition().equals(testPosition)) {
                return true;
            }
        }
        return false;
    }

    public boolean canProtect(TeamColor teamColor) {
        Collection<ChessMove> protectMoves = new ArrayList<>(protectMoves(threatPaths(threats(team))));
        for (ChessMove move : protectMoves) {
            ChessGame tempCopy = createTempCopy();
            try {
                tempCopy.makeMove(move);
            } catch (InvalidMoveException e) {
                throw new RuntimeException(e);
            }
            List<ChessMove> opponentMoves = new ArrayList<>();
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    ChessPiece piece = tempCopy.getBoard().getPiece(new ChessPosition(i, j));
                    if (piece != null) {
                        if (piece.getTeamColor().equals(teamColor)) {
                            opponentMoves.addAll(validMoves(new ChessPosition(i, j)));
                        }
                    }
                }
            }
            for (ChessMove tempMove : opponentMoves) {
                if (tempMove.getEndPosition() != tempCopy.kingPosition(team)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Collection<ChessPosition> threats(TeamColor teamColor) {
        List<ChessMove> threatMoves = new ArrayList<>();
        ChessPosition kingPosition = kingPosition(teamColor);
        List<ChessPosition> threats = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece != null) {
                    if (piece.getTeamColor() != teamColor) {
                        threatMoves.addAll(validMoves(new ChessPosition(i, j)));
                        for (ChessMove move : threatMoves) {
                            if (move.getEndPosition().equals(kingPosition)) {
                                threats.add(new ChessPosition(i, j));
                                break;
                            }
                        }
                    }
                }
            }
        }
        return threats;
    }

    public Collection<ChessPosition> threatPaths(Collection<ChessPosition> threats) {
        List<ChessPosition> threatPaths = new ArrayList<>();
        for (ChessPosition threat : threats) {
            if (board.getPiece(threat).getPieceType().equals(PAWN)) {
                threatPaths.add(threat);
            } else if (board.getPiece(threat).getPieceType().equals(BISHOP)) {
                threatPaths.addAll(traceBishop(threat, kingPosition(team)));
            } else if (board.getPiece(threat).getPieceType().equals(KNIGHT)) {
                threatPaths.add(threat);
            } else if (board.getPiece(threat).getPieceType().equals(ROOK)) {
                threatPaths.addAll(traceRook(threat, kingPosition(team)));
            } else if (board.getPiece(threat).getPieceType().equals(QUEEN)) {
                threatPaths.addAll(traceBishop(threat, kingPosition(team)));
                threatPaths.addAll(traceRook(threat, kingPosition(team)));
            }
        }
        return threatPaths;
    }

    public Collection<ChessMove> protectMoves(Collection<ChessPosition> threatPaths) {
        List<ChessMove> protectMoves = new ArrayList<>();
        List<ChessMove> teamMoves = new ArrayList<>(allValidMoves(team));
        for (ChessMove move : teamMoves) {
            for (ChessPosition threat : threatPaths) {
                if (move.getEndPosition().equals(threat)) {
                    protectMoves.add(move);
                }
            }
        }
        return protectMoves;
    }

    public Collection<ChessPosition> traceBishop(ChessPosition bishopPosition, ChessPosition kingPosition) {
        int bishopRow = bishopPosition.getRow();
        int bishopCol = bishopPosition.getColumn();
        int kingRow = kingPosition.getRow();
        int kingCol = kingPosition.getColumn();
        List<ChessPosition> bishopPath = new ArrayList<>();
        if (bishopRow > kingRow && bishopCol > kingCol) {
            for (int i = kingRow; i <= bishopRow; i++) {
                for (int j = kingCol; j <= bishopCol; j++) {
                    bishopPath.add(new ChessPosition(i, j));
                }
            }
        } else if (bishopRow < kingRow && bishopCol > kingCol) {
            for (int i = kingRow; i >= bishopRow; i--) {
                for (int j = kingCol; j <= bishopCol; j++) {
                    bishopPath.add(new ChessPosition(i, j));
                }
            }
        } else if (bishopRow > kingRow && bishopCol < kingCol) {
            for (int i = kingRow; i <= bishopRow; i++) {
                for (int j = kingCol; j >= bishopCol; j--) {
                    bishopPath.add(new ChessPosition(i, j));
                }
            }
        } else if (bishopRow < kingRow && bishopCol < kingCol) {
            for (int i = kingRow; i >= bishopRow; i--) {
                for (int j = kingCol; j >= bishopCol; j--) {
                    bishopPath.add(new ChessPosition(i, j));
                }
            }
        }
        return bishopPath;
    }

    public Collection<ChessPosition> traceRook(ChessPosition rookPosition, ChessPosition kingPosition) {
        int rookRow = rookPosition.getRow();
        int rookCol = rookPosition.getColumn();
        int kingRow = kingPosition.getRow();
        int kingCol = kingPosition.getColumn();
        List<ChessPosition> rookPath = new ArrayList<>();
        if (rookRow > kingRow && rookCol == kingCol) {
            for (int i = kingRow; i <= rookRow; i++) {
                rookPath.add(new ChessPosition(i, rookCol));
            }
        } else if (rookRow < kingRow && rookCol == kingCol) {
            for (int i = kingRow; i >= rookRow; i--) {
                rookPath.add(new ChessPosition(i, rookCol));
            }
        } else if (rookRow == kingRow && rookCol > kingCol) {
            for (int j = kingCol; j <= rookCol; j++) {
                rookPath.add(new ChessPosition(rookRow, j));
            }
        } else if (rookRow == kingRow && rookCol < kingCol) {
            for (int j = kingCol; j >= rookCol; j--) {
                rookPath.add(new ChessPosition(rookCol, j));
            }
        }
        return rookPath;
    }

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
