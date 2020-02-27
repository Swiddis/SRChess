package widdis.unroe.chess.controller;

import widdis.unroe.chess.board.Board;
import widdis.unroe.chess.board.pieces.Piece;
import widdis.unroe.chess.view.View;

public class ChessController {
    View view = new View();
    Board board = new Board();
    public void run() {
        view.showBoard(Piece.Color.WHITE, board);
        board.move("b2b3");
        view.showBoard(Piece.Color.WHITE, board);
    }
}
