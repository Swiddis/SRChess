package widdis.unroe.chess.board.pieces;

import widdis.unroe.chess.board.Board;
import widdis.unroe.chess.board.Square;

import java.util.HashSet;

public class Bishop extends Piece {
    private Color color;
    public Bishop(Color color) {
        this.color = color;
    }

    @Override
    public HashSet<Square> getLegalMoves(Square curr, Square[][] board) {
        HashSet<Square> moveSet = new HashSet<>();
        int[] p = curr.getPos();
        int tr = Math.max(p[0], p[1]);
        int tl = Math.max(Board.WIDTH-p[0], p[1]);
        int br = Math.min(p[0], Board.HEIGHT-p[0]);
        int bl = Math.min(Board.WIDTH-p[0], Board.HEIGHT-p[1]);
        for (int d = tr + 1; tr + d < Board.HEIGHT; d++) {
            if (board[p[0]+d][p[1]+d].isEmpty()) moveSet.add(board[p[0]+d][p[1]+d]);
            else {
                if (!board[p[0]+d][p[1]+d].getPiece().getColor().equals(this.getColor()))
                    moveSet.add(board[p[0]+d][p[1]+d]);
                break;
            }
        }
        for (int d = tl + 1; tl + d < Board.HEIGHT; d++) {
            if (board[p[0]+d][p[1]-d].isEmpty()) moveSet.add(board[p[0]+d][p[1]-d]);
            else {
                if (!board[p[0]+d][p[1]-d].getPiece().getColor().equals(this.getColor()))
                    moveSet.add(board[p[0]+d][p[1]-d]);
                break;
            }
        }
        for (int d = bl - 1; bl - d >= 0; d--) {
            if (board[p[0]-d][p[1]-d].isEmpty()) moveSet.add(board[p[0]-d][p[1]-d]);
            else {
                if (!board[p[0]-d][p[1]-d].getPiece().getColor().equals(this.getColor()))
                    moveSet.add(board[p[0]-d][p[1]-d]);
                break;
            }
        }
        for (int d = br - 1; br - d >= 0; d--) {
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
    public Color getColor() {
        return this.color;
    }

    @Override
    public String toString() {
        return null;
    }
}
