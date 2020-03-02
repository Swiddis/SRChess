package widdis.unroe.chess.board.pieces;

import widdis.unroe.chess.board.Board;
import widdis.unroe.chess.board.Square;

import java.util.HashSet;

public class Rook extends Piece {
    private Color color;
    public Rook(Color color) {
        this.color = color;
    }

    @Override
    public HashSet<Square> getLegalMoves(Square curr, Square[][] board) {
        HashSet<Square> moveSet = new HashSet<>();
        int[] p = curr.getPos();
        for (int r = p[0] + 1; r < Board.SIZE; r++) {
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
        for (int c = p[1] + 1; c < Board.SIZE; c++) {
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
        return moveSet;
    }

    @Override
    public String toFEN() {
        if(color == Color.WHITE) {
            return "R";
        }
        return "r";
    }

    @Override
    public String toUnicode() {
        if(color == Color.WHITE) {
            return "\u2656";
        }
        return "\u265C";
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public String toString() {
        return "rook";
    }
}
