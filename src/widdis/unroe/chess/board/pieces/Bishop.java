package widdis.unroe.chess.board.pieces;

public class Bishop extends Piece {
    private Color color;
    public Bishop(Color color) {
        this.color = color;
    }

    public String toFEN() {
        if(color == Color.WHITE) {
            return "B";
        }
        return "b";
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
