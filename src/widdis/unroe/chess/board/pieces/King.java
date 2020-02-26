package widdis.unroe.chess.board.pieces;

public class King extends Piece {
    private Color color;
    public King(Color color) {
        this.color = color;
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
