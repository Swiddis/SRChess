package widdis.unroe.chess.board.pieces;

public class Pawn extends Piece {
    private Color color;
    public Pawn(Color color) {
        this.color = color;
    }


    @Override
    public String toFEN() {
        if(color == Color.WHITE) {
            return "P";
        }
        return "p";
    }

    @Override
    public String toUnicode() {
        if(color == Color.WHITE) {
            return "\u2659";
        }
        return "\u265F";
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
