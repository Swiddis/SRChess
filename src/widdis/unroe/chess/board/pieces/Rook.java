package widdis.unroe.chess.board.pieces;

public class Rook extends Piece {
    private Color color;
    public Rook(Color color) {
        this.color = color;
    }


    @Override
    public String toFEN() {
        if(color == Color.WHITE) {
            return "R";
        }
        return "r";
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
