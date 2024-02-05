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

    private ChessBoard board;
    private TeamColor team;

    public ChessGame() { }

    public ChessGame(ChessGame copy) {
        this.board = new ChessBoard(copy.board);
        this.team = copy.team;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(getBoard(), chessGame.getBoard()) && team == chessGame.team;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBoard(), team);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", team=" + team +
                '}';
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
        this.team = team;
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
        Set<ChessMove> validMoves = new HashSet<>(board.getPiece(startPosition).pieceMoves(board, startPosition));
        validMoves.removeIf(Objects::isNull);
        validMoves.removeIf(move -> !isValid(move));
        return validMoves;
    }

    public boolean isValid(ChessMove move) {
        if ((board.getPiece(move.getStartPosition())) == null || !board.getPiece(move.getStartPosition()).getTeamColor().equals(team)) {
            return false;
        }
        ChessGame tempCopy = new ChessGame(this);
        ChessBoard tempBoard = tempCopy.getBoard();
        ChessPiece piece = tempBoard.getPiece(move.getStartPosition());
        tempBoard.addPiece(move.getEndPosition(), piece);
        tempBoard.removePiece(move.getStartPosition());

        return !tempCopy.isInCheck(team);

//        List<ChessMove> opponentMoves = new ArrayList<>(tempCopy.teamMoves(team.getOpposite()));
//        ChessPosition kingPosition = tempCopy.kingPosition(team);
//        for (ChessMove tempMove : opponentMoves) {
//            if (tempMove.getEndPosition().equals(kingPosition)) {
//                return false;
//            }
//        }
    }

    public Collection<ChessMove> teamMoves(TeamColor teamColor) {
        Set<ChessMove> teamMoves = new HashSet<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece != null) {
                    if (piece.getTeamColor().equals(teamColor)) {
                        teamMoves.addAll(board.getPiece(new ChessPosition(i, j)).pieceMoves(board, new ChessPosition(i, j)));
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
        try{
            if (isValid(move)) {
                ChessPiece piece = board.getPiece(move.getStartPosition());
                board.addPiece(move.getEndPosition(), piece);
                board.removePiece(move.getStartPosition());
            } else {
                throw new InvalidMoveException("Invalid move");
            }
        } catch (InvalidMoveException x){
            System.out.println("Caught an InvalidMoveException: " + x);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Set<ChessMove> opponentMoves = new HashSet<>(teamMoves(teamColor.getOpposite()));
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
        return !canFlee() && !canProtect();
    }

    public boolean canFlee() {
        ChessPosition kingPosition = kingPosition(team);
        Set<ChessMove> kingMoves = new HashSet<>(validMoves(kingPosition));
        for (ChessMove move : kingMoves) {
            if (!checkTest(team, move.getEndPosition())) {
                return true;
            }
        }
        return false;
    }

    public boolean checkTest(TeamColor teamColor, ChessPosition testPosition) {
        Set<ChessMove> opponentMoves = new HashSet<>(teamMoves(teamColor.getOpposite()));
        for (ChessMove move : opponentMoves) {
            if (move.getEndPosition().equals(testPosition)) {
                return true;
            }
        }
        return false;
    }

    public boolean canProtect() {
        Collection<ChessMove> protectMoves = new HashSet<>(protectMoves(threatPaths(threats(team))));
        for (ChessMove move : protectMoves) {
            ChessGame tempCopy = new ChessGame(this);
            try {
                tempCopy.makeMove(move);
            } catch (InvalidMoveException e) {
                throw new RuntimeException(e);
            }
            Set<ChessMove> opponentMoves = new HashSet<>();
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    ChessPiece piece = tempCopy.getBoard().getPiece(new ChessPosition(i, j));
                    if (piece != null) {
                        if (piece.getTeamColor().equals(team)) {
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
        Set<ChessMove> threatMoves = new HashSet<>();
        ChessPosition kingPosition = kingPosition(teamColor);
        Set<ChessPosition> threats = new HashSet<>();
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
        Set<ChessPosition> threatPaths = new HashSet<>();
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
        Set<ChessMove> protectMoves = new HashSet<>();
        Set<ChessMove> teamMoves = new HashSet<>(teamMoves(team));
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
        Set<ChessPosition> bishopPath = new HashSet<>();
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
        Set<ChessPosition> rookPath = new HashSet<>();
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
        Set<ChessMove> teamMoves = new HashSet<>(teamMoves(teamColor));
        return !isInCheck(teamColor) && teamMoves.isEmpty();
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
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
