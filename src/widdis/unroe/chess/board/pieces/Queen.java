package widdis.unroe.chess.board.pieces;

public class Queen extends Piece {
    private Color color;
    public Queen(Color color) {
        this.color = color;
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
