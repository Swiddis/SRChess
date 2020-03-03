package widdis.unroe.chess.board.pieces;

import widdis.unroe.chess.board.Board;
import widdis.unroe.chess.board.Square;

import java.util.HashSet;

public class Queen extends Piece {
    public Queen(Color color) {
        this.setColor(color);
    }

    @Override
    public HashSet<Square> getLegalMoves(Square curr, Square[][] board) {
        HashSet<Square> moveSet = new HashSet<>();
        moveSet.addAll((new Rook(this.getColor())).getLegalMoves(curr, board));
        moveSet.addAll((new Bishop(this.getColor())).getLegalMoves(curr, board));
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
        if(color == Color.WHITE) {
            return "\u2655";
        }
        return "\u265B";
    }

    @Override
    public String toString() {
        return "queen";
    }
}
