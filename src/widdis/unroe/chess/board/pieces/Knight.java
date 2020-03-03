package widdis.unroe.chess.board.pieces;

import widdis.unroe.chess.board.Board;
import widdis.unroe.chess.board.Square;

import java.util.HashSet;

public class Knight extends Piece {
    public Knight(Color color) {
        this.setColor(color);
    }

    @Override
    public HashSet<Square> getLegalMoves(Square curr, Square[][] board) {
        HashSet<Square> moveSet = new HashSet<>();
        int[] p = curr.getPos();
        // Define all 8 possible ideal knight moves
        int[][] hops = new int[][]{{1,2},{2,1},{-1,2},{2,-1},{1,-2},{-2,1},{-1,-2},{-2,-1}};
        for (int[] hop : hops) {
            // Determine possible destination coordinates
            int ph0 = p[0] + hop[0];
            int ph1 = p[1] + hop[1];
            // Verify the move is within the bounds of the board and can be moved to
            if (ph0 < Board.SIZE && ph0 >= 0 && ph1 < Board.SIZE && ph1 >= 0 &&
                    (board[ph0][ph1].isEmpty() || !board[ph0][ph1].getPiece().getColor().equals(this.getColor()))) {
                moveSet.add(board[ph0][ph1]);
            }
        }
        return moveSet;
    }

    @Override
    public String toFEN() {
        if(color == Color.WHITE) {
            return "N";
        }
        return "n";
    }

    @Override
    public String toUnicode() {
        if(color == Color.WHITE) {
            return "\u2658";
        }
        return "\u265E";
    }

    @Override
    public String toString() {
        return "knight";
    }
}
