package widdis.unroe.chess;

import widdis.unroe.chess.board.Board;
import widdis.unroe.chess.board.pieces.Piece;
import widdis.unroe.chess.view.View;

public class viewTester {
    public static void main(String[] args) {
        View view = new View();
        Board board = new Board();
        view.showBoard(Piece.Color.WHITE, board);
    }
}
