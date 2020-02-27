package widdis.unroe.chess.board.pieces;

import widdis.unroe.chess.board.Square;

import java.util.HashSet;

public abstract class Piece {
    public enum Color {WHITE, BLACK}

    Color color;

    @Override
    public abstract String toString();
    public abstract String toFEN();
    public abstract String toUnicode();
    public abstract Color getColor();


    public abstract HashSet<Square> getLegalMoves(Square curr, Square[][] board);

    public boolean checkIsLegal(Square curr, Square dest, Square[][] board) {
        System.out.println(curr + " " + dest);
        HashSet<Square> legalMoves = getLegalMoves(curr, board);
        System.out.println(legalMoves.toString());
        return legalMoves.contains(dest);
    }
}

