package widdis.unroe.chess.board.pieces;

public abstract class Piece {
    public enum Color {WHITE, BLACK}

    Color color;
    @Override
    abstract public String toString();
    abstract public String toFEN();
    abstract public String toUnicode();
    abstract public Color getColor();
}

