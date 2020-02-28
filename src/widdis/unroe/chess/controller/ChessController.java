package widdis.unroe.chess.controller;

import widdis.unroe.chess.board.Board;
import widdis.unroe.chess.board.pieces.Piece;
import widdis.unroe.chess.view.View;

public class ChessController {
    View view = new View();
    Board board = new Board();
    Piece.Color activePlayer = Piece.Color.WHITE;
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
            view.showBoard(activePlayer, board);
            if(pieceMoved) {
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
                board.move(view.promptForMove());
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

