package widdis.unroe.chess.controller;

import widdis.unroe.chess.board.Board;
import widdis.unroe.chess.board.pieces.Piece;
import widdis.unroe.chess.view.View;

public class ChessController {
    View view = new View();
    Board board = new Board();
    public void run() {
        String[] testGameMoves = {
                "e2e4", "e7e5", "g1f3", "b8c6", "f1b5"
        };
        for (String move : testGameMoves) {
            view.showBoard(Piece.Color.WHITE, board);
            board.move(move);
        }
        view.showBoard(Piece.Color.WHITE, board);
    }
}
