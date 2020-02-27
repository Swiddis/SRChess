package widdis.unroe.chess.board.pieces;

import widdis.unroe.chess.board.Board;
import widdis.unroe.chess.board.Square;

import java.util.HashSet;

public class Queen extends Piece {
    private Color color;
    public Queen(Color color) {
        this.color = color;
    }

    @Override
    public HashSet<Square> getLegalMoves(Square curr, Square[][] board) {
        HashSet<Square> moveSet = new HashSet<>();
        int[] p = curr.getPos();
        for (int r = p[0] + 1; r < Board.HEIGHT; r++) {
            if (board[r][p[1]].isEmpty()) moveSet.add(board[r][p[1]]);
            else {
                if (!board[r][p[1]].getPiece().getColor().equals(this.getColor())) moveSet.add(board[r][p[1]]);
                break;
            }
        }
        for (int r = p[0] - 1; r >= 0; r--) {
            if (board[r][p[1]].isEmpty()) moveSet.add(board[r][p[1]]);
            else {
                if (!board[r][p[1]].getPiece().getColor().equals(this.getColor())) moveSet.add(board[r][p[1]]);
                break;
            }
        }
        for (int c = p[1] - 1; c >= 0; c--) {
            if (board[p[0]][c].isEmpty()) moveSet.add(board[p[0]][c]);
            else {
                if (!board[p[0]][c].getPiece().getColor().equals(this.getColor())) moveSet.add(board[p[0]][c]);
                break;
            }
        }
        for (int c = p[1] - 1; c >= 0; c--) {
            if (board[p[0]][c].isEmpty()) moveSet.add(board[p[0]][c]);
            else {
                if (!board[p[0]][c].getPiece().getColor().equals(this.getColor())) moveSet.add(board[p[0]][c]);
                break;
            }
        }
        int tr = Math.max(p[0], p[1]);
        int tl = Math.max(Board.WIDTH-p[0], p[1]);
        int br = Math.max(p[0], Board.HEIGHT-p[0]);
        int bl = Math.max(Board.WIDTH-p[0], Board.HEIGHT-p[1]);
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

    @Override
    public String toFEN() {
        if(color == Color.WHITE) {
            return "Q";
        }
        return "q";
    }

    @Override
    public String toUnicode() {
        return null;
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
