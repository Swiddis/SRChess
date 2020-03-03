package widdis.unroe.chess.board.pieces;

import widdis.unroe.chess.board.Board;
import widdis.unroe.chess.board.Square;

import java.util.HashSet;

public class King extends Piece {
    private Color color;
    public King(Color color) {
        this.color = color;
    }

    @Override
    public HashSet<Square> getLegalMoves(Square curr, Square[][] board) {
        HashSet<Square> moveSet = new HashSet<>();
        int[] p = curr.getPos();
        // Define all 8 possible ideal king moves
        int[][] steps = new int[][]{{1,1},{1,0},{1,-1},{0,1},{0,-1},{-1,1},{-1,0},{-1,-1}};
        for (int[] step : steps) {
            // Determine possible destination coordinates
            int ps0 = p[0] + step[0];
            int ps1 = p[1] + step[1];
            // Verify the move is within the bounds of the board and can be moved to
            if (ps0 < Board.SIZE && ps0 >= 0 && ps1 < Board.SIZE && ps1 >= 0 &&
                    (board[ps0][ps1].isEmpty() || !board[ps0][ps1].getPiece().getColor().equals(this.getColor()))) {
                moveSet.add(board[ps0][ps1]);
            }
        }
        // Also check for castling
        if (curr.hasNotMoved()) {
            // Castling kingside
            if (board[p[0]][7].hasNotMoved() &&
                    board[p[0]][6].isEmpty() && board[p[0]][5].isEmpty()) {
                moveSet.add(board[p[0]][6]);
            }
            // Castling queenside
            if (board[p[0]][0].hasNotMoved() &&
                    board[p[0]][1].isEmpty() && board[p[0]][2].isEmpty() && board[p[0]][3].isEmpty()) {
                moveSet.add(board[p[0]][1]);
            }
        }
        return moveSet;
    }

    @Override
    public String toFEN() {
        if(color == Color.WHITE) {
            return "K";
        }
        return "k";
    }

    @Override
    public String toUnicode() {
        if(color == Color.WHITE) {
            return "\u2654";
        }
        return "\u265A";

    }

    @Override
    public String toString() {
        return "king";
    }
}
