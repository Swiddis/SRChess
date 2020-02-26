package widdis.unroe.chess.board;

import widdis.unroe.chess.board.pieces.Piece;

public class Square {
    Piece piece;
    public void setPiece(Piece p) {
        piece = p;
    }

    public Piece getPiece() {
        return piece;
    }
}
