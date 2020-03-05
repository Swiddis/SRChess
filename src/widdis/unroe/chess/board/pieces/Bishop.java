package widdis.unroe.chess.board.pieces;

import widdis.unroe.chess.board.Board;
import widdis.unroe.chess.board.Square;

import java.util.HashSet;

public class Bishop extends Piece {
    public Bishop(Color color) {
        this.setColor(color);
    }

    public Bishop clone() {
        return new Bishop(color);
    }

    @Override
    public HashSet<Square> getLegalMoves(Square curr, Square[][] board) {
        HashSet<Square> moveSet = new HashSet<>();
        int[] p = curr.getPos();
        for (int d = 1; p[0] + d < Board.SIZE && p[1] + d < Board.SIZE; d++) {
            if (board[p[0]+d][p[1]+d].isEmpty()) moveSet.add(board[p[0]+d][p[1]+d]);
            else {
                if (!board[p[0]+d][p[1]+d].getPiece().getColor().equals(this.getColor()))
                    moveSet.add(board[p[0]+d][p[1]+d]);
                break;
            }
        }
        for (int d = 1; p[0] + d < Board.SIZE && p[1] - d >= 0; d++) {
            if (board[p[0]+d][p[1]-d].isEmpty()) moveSet.add(board[p[0]+d][p[1]-d]);
            else {
                if (!board[p[0]+d][p[1]-d].getPiece().getColor().equals(this.getColor()))
                    moveSet.add(board[p[0]+d][p[1]-d]);
                break;
            }
        }
        for (int d = 1; p[0] - d >= 0 && p[1] - d >= 0; d++) {
            if (board[p[0]-d][p[1]-d].isEmpty()) moveSet.add(board[p[0]-d][p[1]-d]);
            else {
                if (!board[p[0]-d][p[1]-d].getPiece().getColor().equals(this.getColor()))
                    moveSet.add(board[p[0]-d][p[1]-d]);
                break;
            }
        }
        for (int d = 1; p[0] - d >= 0 && p[1] + d < Board.SIZE; d++) {
            if (board[p[0]-d][p[1]+d].isEmpty()) moveSet.add(board[p[0]-d][p[1]+d]);
            else {
                if (!board[p[0]-d][p[1]+d].getPiece().getColor().equals(this.getColor()))
                    moveSet.add(board[p[0]-d][p[1]+d]);
                break;
            }
        }
        return moveSet;
    }

    public String toFEN() {
        if(color == Color.WHITE) {
            return "B";
        }
        return "b";
    }

    @Override
    public String toUnicode() {
        if(color == Color.WHITE) {
            return "\u2657";
        }
        return "\u265D";
    }

    @Override
    public String toString() {
        return "bishop";
    }
}
