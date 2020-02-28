package widdis.unroe.chess.board.pieces;

import widdis.unroe.chess.board.Board;
import widdis.unroe.chess.board.Square;

import java.util.HashSet;

public class Pawn extends Piece {
    private Color color;
    public Pawn(Color color) {
        this.color = color;
    }

    @Override
    public HashSet<Square> getLegalMoves(Square curr, Square[][] board) {
        HashSet<Square> moveSet = new HashSet<>();
        int[] p = curr.getPos();
        int step = this.getColor().equals(Piece.Color.WHITE) ? 1 : -1;
        // First, check for the simple moves: a step forward and captures
        if (board[p[0]+step][p[1]].isEmpty()) {
            moveSet.add(board[p[0]+step][p[1]]);
        }
        if (!board[p[0]+step][p[1]+1].isEmpty() && !board[p[0]+step][p[1]+1].getPiece().getColor().equals(this.getColor())) {
            moveSet.add(board[p[0]+step][p[1]+1]);
        }
        if (!board[p[0]+step][p[1]-1].isEmpty() && !board[p[0]+step][p[1]-1].getPiece().getColor().equals(this.getColor())) {
            moveSet.add(board[p[0]+step][p[1]-1]);
        }
        // Also check for the double move from the initial position
        // Verify we're at our starting position
        if ((step > 0 && p[0] == 1) || (step < 0 && p[0] == 6)) {
            // Then verify the destination is empty
            if (board[p[0]+2*step][p[1]].isEmpty()) {
                moveSet.add(board[p[0]+2*step][p[1]]);
            }
        }
        return moveSet;
    }

    @Override
    public String toFEN() {
        if(color == Color.WHITE) {
            return "P";
        }
        return "p";
    }

    @Override
    public String toUnicode() {
        if(color == Color.WHITE) {
            return "\u2659";
        }
        return "\u265F";
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
