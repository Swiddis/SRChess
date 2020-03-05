package widdis.unroe.chess.board.pieces;

import widdis.unroe.chess.board.Square;

import java.util.HashSet;

public abstract class Piece implements Cloneable {
    public abstract Piece clone() throws CloneNotSupportedException;

    public enum Color {WHITE, BLACK}
    protected Color color;

    @Override
    public abstract String toString();
    public abstract String toFEN();
    public abstract String toUnicode();

    public void setColor(Color c) {
        color = c;
    }
    public Color getColor() {
        return color;
    }


    public abstract HashSet<Square> getLegalMoves(Square curr, Square[][] board);

    public boolean checkIsLegal(Square curr, Square dest, Square[][] board) {
        HashSet<Square> legalMoves = getLegalMoves(curr, board);
        return legalMoves.contains(dest);
    }
}
