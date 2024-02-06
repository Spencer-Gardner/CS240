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

    public ChessGame() {
        board = new ChessBoard();
        team = TeamColor.WHITE;
        board.resetBoard();
    }

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
        if ((board.getPiece(startPosition) == null)) {
            return null;
        }
        Set<ChessMove> validMoves = new HashSet<>(board.getPiece(startPosition).pieceMoves(board, startPosition));
        validMoves.removeIf(Objects::isNull);
        validMoves.removeIf(this::isNotValid);
        return validMoves;
    }

    public boolean isNotValid(ChessMove move) {
        ChessGame tempCopy = new ChessGame(this);
        ChessBoard tempBoard = tempCopy.getBoard();
        tempBoard.movePiece(move);
        return tempCopy.isInCheck(tempBoard.getPiece(move.getEndPosition()).getTeamColor());
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
        if (board.getPiece(move.getStartPosition()).getTeamColor().equals(team) && validMoves(move.getStartPosition()).contains(move)) {
            board.movePiece(move);
            if (move.getPromotionPiece() != null) {
                board.addPiece(move.getEndPosition(), new ChessPiece(team, move.getPromotionPiece()));
            }
            setTeamTurn(team.getOpposite());
        } else {
            throw new InvalidMoveException("Invalid move");
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
        Set<ChessMove> teamMoves = new HashSet<>(teamMoves(teamColor));
        teamMoves.removeIf(this::isNotValid);
        return isInCheck(teamColor) && teamMoves.isEmpty();
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
        teamMoves.removeIf(this::isNotValid);
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
