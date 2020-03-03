package widdis.unroe.chess.controller;

import widdis.unroe.chess.board.Board;
import widdis.unroe.chess.board.pieces.Piece;
import widdis.unroe.chess.view.View;

public class ChessController {
    private View view = new View();
    private Board board = new Board();
    private int[] latestMove = new int[2];
    private Piece.Color activePlayer = Piece.Color.WHITE;
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
                case 4:
                    exitRequested = true;
                    break;
            }

        }

    }

    private void playervsplayer() {
        boolean isInPlay = true;
        while(isInPlay) {
            view.showBoard(activePlayer, board);
            view.displayMessage(getActivePlayerName() + "'s turn");
            boolean pieceMoved = movePiece();
            view.showBoard(activePlayer, board, latestMove);
            //clear latest move so there is no accidental highlight
            //latestMove[0] = -1;
            //latestMove[1] = -1;
            if(pieceMoved) {
                //
                if(board.checkForPromotion(activePlayer, latestMove[0], latestMove[1])) {
                    String newPiece = view.promptPromotion();
                    board.promote(activePlayer, latestMove[0], latestMove[1], newPiece);
                }
                view.displayMessage("Piece moved! Press enter to continue");
            }
            else {
                view.displayMessage(getActivePlayerName() +  " gave up!");
                isInPlay = false;
                endGame();
            }
            view.promptForContinue();
            toggleActivePlayer();

            if(board.checkWin() != 0) {
                isInPlay = false;
                endGame();
            }
        }
    }
    private boolean movePiece() {
        while(true) {
            try {
                latestMove = board.move(view.promptForMove());
                return true;
            } catch (IllegalArgumentException iae) {
                if(iae.getMessage().equals("QUIT")) {
                    return false;
                }
                else {
                    view.displayMessage("Invalid Move! Please try again");
                }
            }
        }
    }


    private void toggleActivePlayer() {
        activePlayer = activePlayer == Piece.Color.BLACK ? Piece.Color.WHITE : Piece.Color.BLACK;
    }
    private void endGame() {
        toggleActivePlayer();
        view.displayMessage(getActivePlayerName() + " won the game!\r\nPress enter to continue");
        view.promptForContinue();


    }



    private String getActivePlayerName() {
        return activePlayer.toString().substring(0,1) +  activePlayer.toString().toLowerCase().substring(1);
    }
}

