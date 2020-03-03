package widdis.unroe.chess.controller;

import widdis.unroe.chess.ai.Stockfish;
import widdis.unroe.chess.board.Board;
import widdis.unroe.chess.board.pieces.Piece;
import widdis.unroe.chess.view.View;

import java.io.IOException;

public class ChessController {
    private View view = new View();
    private Board board;
    private int[] latestMove = new int[2];
    private Piece.Color activePlayer = Piece.Color.WHITE;
    public void run() throws IOException {
        boolean exitRequested = false;
        while(!exitRequested) {
            board = new Board();
            switch(view.menuPrompt()){
                case 1:
                    //player vs player
                    playervsplayer();
                    break;
                case 2:
                    //player vs comp
                    playervsAI();
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
            isInPlay = playerTurn();
            view.promptForContinue();
            toggleActivePlayer();

            if(board.checkWin() != 0) {
                isInPlay = false;
                endGame();
            }

            latestMove[0] = -1;
            latestMove[1] = -1;
        }
        endGame();
    }



    private void playervsAI() throws IOException {
        Stockfish ai = new Stockfish(3);

        boolean isInPlay = true;
        while (isInPlay) {
            if (activePlayer.equals(Piece.Color.WHITE)) { // Player's turn
                isInPlay = playerTurn();
                view.promptForContinue();
            }
            else {
                compTurn(ai);
            }

            toggleActivePlayer();
            if (board.checkWin() != 0) {
                isInPlay = false;
                endGame();
            }
            latestMove[0] = -1;
            latestMove[1] = -1;
        }
    }


    private boolean playerTurn() {
        view.showBoard(activePlayer, board);
        view.displayMessage(getActivePlayerName() + "'s turn");
        boolean pieceMoved = movePiece();
        view.showBoard(activePlayer, board, latestMove);
        if (pieceMoved) {
            if (board.checkForPromotion(activePlayer, latestMove[0], latestMove[1])) {
                String newPiece = view.promptPromotion();

                board.promote(activePlayer, latestMove[0], latestMove[1], newPiece);
            }
            view.displayMessage("Piece moved! Press enter to continue");
        } else {
            view.displayMessage(getActivePlayerName() + " gave up!");
             return false;
        }
        return true;
    }


    private void compTurn(Stockfish comp) throws IOException {

        board.move(comp.makeMove(board.getMoveHistory()));

        //move
        //show board with latest move

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

        view.displayMessage(getActivePlayerName() + " won the game!\r\nPress enter to continue");
        view.promptForContinue();


    }



    private String getActivePlayerName() {
        return activePlayer.toString().substring(0,1) +  activePlayer.toString().toLowerCase().substring(1);
    }
}

