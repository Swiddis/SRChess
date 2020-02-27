package widdis.unroe.chess.board;

import widdis.unroe.chess.board.pieces.Piece;

public class Square {
    Piece piece;
    int row,col;

    public void setPiece(Piece p) {
        piece = p;
    }

    public Piece getPiece() {
        return piece;
    }

    public boolean isEmpty() {
        return piece == null;
    }

    public void setPos(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int[] getPos() {
        return new int[]{row, col};
    }
}
