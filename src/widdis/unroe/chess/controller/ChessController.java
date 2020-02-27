package widdis.unroe.chess.controller;

import widdis.unroe.chess.board.Board;
import widdis.unroe.chess.board.pieces.Piece;
import widdis.unroe.chess.view.View;

public class ChessController {
    View view = new View();
    Board board = new Board();
    public void run() {
        boolean exitRequested = false;
        while(!exitRequested) {
            switch(view.menuPrompt()){
                case 1:
                    //player vs player
                    playervsplayer();
                    break;
                case 2:
                    //player vs comp
                    break;
                case 3:
                    //comp vs comp
                    break;
            }

        }

    }

    private void playervsplayer() {
        while(board.checkWin() == 0) {
            view.showBoard(Piece.Color.WHITE, board);
            board.move(view.promptForMove());
            view.showBoard(Piece.Color.WHITE, board);
            view.promptForContinue();

            
            view.showBoard(Piece.Color.BLACK, board);
            board.move(view.promptForMove());
        }
    }
}
