package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMove implements PieceMove {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        List<ChessMove> validMoves = new ArrayList<>();

        int i = row;
        int j = col;
        while (i <= 7 && j <= 7) {
            ChessPiece square = board.getPiece(new ChessPosition(i+1, j+1));
            if (square == null) {
                validMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(i+1, j+1), null));
            } else if (piece.getTeamColor() != square.getTeamColor()) {
                validMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(i+1, j+1), null));
                break;
            } else if (piece.getTeamColor() == square.getTeamColor()) {
                break;
            }
            System.out.print(i+1 + "," + (j+1) + " ");
            i++;
            j++;
        }

        i = row-2;
        j = col;
        while (i >= 0 && j <= 7) {
            ChessPiece square = board.getPiece(new ChessPosition(i+1, j+1));
            if (square == null) {
                validMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(i+1, j+1), null));
            } else if (piece.getTeamColor() != square.getTeamColor()) {
                validMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(i+1, j+1), null));
                break;
            } else if (piece.getTeamColor() == square.getTeamColor()) {
                break;
            }
            System.out.print(i+1 + "," + (j+1) + " ");
            i--;
            j++;
        }

        i = row-2;
        j = col-2;
        while (i >= 0 && j >= 0) {
            ChessPiece square = board.getPiece(new ChessPosition(i+1, j+1));
            if (square == null) {
                validMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(i+1, j+1), null));
            } else if (piece.getTeamColor() != square.getTeamColor()) {
                validMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(i+1, j+1), null));
                break;
            } else if (piece.getTeamColor() == square.getTeamColor()) {
                break;
            }
            System.out.print(i+1 + "," + (j+1) + " ");
            i--;
            j--;
        }

        i = row;
        j = col-2;
        while (i <= 7 && j >= 0) {
            ChessPiece square = board.getPiece(new ChessPosition(i+1, j+1));
            if (square == null) {
                validMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(i+1, j+1), null));
            } else if (piece.getTeamColor() != square.getTeamColor()) {
                validMoves.add(new ChessMove(new ChessPosition(row, col), new ChessPosition(i+1, j+1), null));
                break;
            } else if (piece.getTeamColor() == square.getTeamColor()) {
                break;
            }
            System.out.print(i+1 + "," + (j+1) + " ");
            i++;
            j--;
        }

        return validMoves;
    }
}
