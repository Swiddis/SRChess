package widdis.unroe.chess.board;

import widdis.unroe.chess.board.pieces.Piece;

public class Square {
    private Piece piece;
    private int row,col;
    private boolean enPassant;
    private boolean hasMoved;

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

    @Override
    public String toString() {
        return this.row + ","+ this.col;
    }

    public boolean isEnPassant() {
        return enPassant;
    }

    public void setEnPassant(boolean b) {
        enPassant = b;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
}

