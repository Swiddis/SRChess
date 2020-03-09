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
            activePlayer = Piece.Color.WHITE;
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
                    AIvsAI();
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
            if(endOfTurnCheck()) {
                isInPlay = false;
            }
            latestMove[0] = -1;
            latestMove[1] = -1;
        }
        endGame();
    }

    private void playervsAI() throws IOException {
        int difficulty = view.promptForDifficulty();
        Stockfish ai = new Stockfish(difficulty);

        boolean isInPlay = true;
        while (isInPlay) {
            if (activePlayer.equals(Piece.Color.WHITE)) { // Player's turn
                isInPlay = playerTurn();
                view.promptForContinue();
            }
            else {
                compTurn(ai);
            }
            if(endOfTurnCheck()) {
                isInPlay = false;
            }
            toggleActivePlayer();
            latestMove[0] = -1;
            latestMove[1] = -1;
        }
        endGame();
    }

    private void AIvsAI() throws IOException {
        Stockfish ai_WHITE = new Stockfish(8);
        Stockfish ai_BLACK = new Stockfish(8);
        boolean isInPlay = true;
        while (isInPlay) {
            view.showBoard(Piece.Color.WHITE, board);
            if (activePlayer.equals(Piece.Color.WHITE)) {
                compTurn(ai_WHITE);
            }
            else {
                compTurn(ai_BLACK);
            }

            toggleActivePlayer();
            if (endOfTurnCheck()) {
                isInPlay = false;
            }
        }
        endGame();
    }


    private boolean playerTurn() {
        view.showBoard(activePlayer, board);
        view.displayMessage(getActivePlayerName() + "'s turn");
        boolean pieceMoved;
        while(true) {
            try {
                pieceMoved = movePiece();
                break;
            } catch (ArrayIndexOutOfBoundsException e) {
                view.displayMessage("Invalid Move! Please try again");
            }
        }
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
    }


    private boolean endOfTurnCheck() {
        if(board.isCheckmate(activePlayer)){
            view.displayMessage(getActivePlayerName() + " is in checkmate!");
            toggleActivePlayer();
            view.displayMessage(getActivePlayerName() + " won the game!");
            return true;
        }
        // Switch the color for isCheck for some unworldly reason I cannot begin to fathom
        // Seriously, what demon have we summoned by writing this code
        else if(board.isCheck(activePlayer.inverse())) {
            view.displayMessage(getActivePlayerName() + " is in check!");
            return false;
        }
        else if(board.isStalemate(activePlayer)) {
            view.displayMessage("Game is a stalemate!");
            return true;
        }
        else if(board.checkFiftyMoves() || board.checkInsufficientMaterial() || board.checkThreefold()) {
            view.displayMessage("Game is a draw!");
            return true;
        }

        return false;
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
            } catch(NullPointerException npe) {
                view.displayMessage("Invalid Move! Please try again");
            }
        }
    }


    private void toggleActivePlayer() {
        activePlayer = activePlayer.inverse();
    }
    private void endGame() {

        view.displayMessage("Press enter to continue");
        view.promptForContinue();
    }

    private String getActivePlayerName() {
        return activePlayer.toString().substring(0,1) +  activePlayer.toString().toLowerCase().substring(1);
    }
}
