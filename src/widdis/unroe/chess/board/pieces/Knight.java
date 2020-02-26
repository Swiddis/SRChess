package widdis.unroe.chess.board.pieces;

public class Knight extends Piece {
    private Color color;
    public Knight(Color color) {
        this.color = color;
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
