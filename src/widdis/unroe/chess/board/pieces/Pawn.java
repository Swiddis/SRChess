package widdis.unroe.chess.board.pieces;

import widdis.unroe.chess.board.Board;
import widdis.unroe.chess.board.Square;

import java.util.HashSet;

public class Pawn extends Piece {
    public Pawn(Color color) {
        this.setColor(color);
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
        for (int i : new int[]{1, -1}) {
            if (p[1] + i >= 0 && p[1] + i < Board.SIZE)
                if ((!board[p[0] + step][p[1] + i].isEmpty() &&
                        !board[p[0] + step][p[1] + i].getPiece().getColor().equals(this.getColor())) ||
                        board[p[0] + step][p[1] + i].isEnPassant()) {
                    moveSet.add(board[p[0] + step][p[1] + i]);
                }
        }
        // Also check for the double move from the initial position
        // Verify we're at our starting position
        if ((step > 0 && p[0] == 1) || (step < 0 && p[0] == 6)) {
            // Then verify the destination is empty
            if (board[p[0]+2*step][p[1]].isEmpty()) {
                moveSet.add(board[p[0]+2*step][p[1]]);
                // Also handle en passant for double move
                // Reset the full board's en passant before updating this piece's
                for (int i = 0; i < Board.SIZE; i++) {
                    for (int j = 0; j < Board.SIZE; j++) {
                        board[i][j].setEnPassant(false);
                    }
                }
                board[p[0]+step][p[1]].setEnPassant(true);
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
    public String toString() {
        return "pawn";
    }
}
