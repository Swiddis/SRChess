package widdis.unroe.chess.controller;

import widdis.unroe.chess.ai.UCIEngine;
import widdis.unroe.chess.board.Board;
import widdis.unroe.chess.board.pieces.Piece;
import widdis.unroe.chess.view.View;

import java.io.IOException;

public class ChessController {

    private View view = new View();
    private Board board;
    private int[] latestMove = new int[2];
    private Piece.Color activePlayer = Piece.Color.WHITE;

    /**
     * Initial method run by the static main class.
     * Continues to loop through menu until exit is requested.
     */
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

    /**
     * Loops through player vs player condition, taking each users input, moving the pieces and
     * checking for end game conditions.
     * If an end game condition is met, exit the loop and go to the endGame() method.
     */
    private void playervsplayer() {
        boolean isInPlay = true;
        while(isInPlay) {
            isInPlay = playerTurn();
            view.promptForContinue();
            toggleActivePlayer();
            if(endOfTurnCheck()) {
                view.showBoard(activePlayer, board);
                isInPlay = false;
            }
            latestMove[0] = -1;
            latestMove[1] = -1;
        }
        endGame();
    }

    /**
     * Loops through player vs computer condition. Prompts user and gets computer input.
     * Checks end conditions on each turn. If end condition is met, break out of loop and
     * go to endGame() method.
     */
    private void playervsAI() throws IOException {
        int difficulty = view.promptForDifficulty();
        UCIEngine ai = new UCIEngine(difficulty);

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
            if(endOfTurnCheck()) {
                view.showBoard(activePlayer, board);
                isInPlay = false;
            }

            latestMove[0] = -1;
            latestMove[1] = -1;
        }
        endGame();
    }

    /**
     * Loops through Computer vs Computer condition. Gets input and performs checks upon them.
     * Breaks loop when end game check is positive. Then goes to endGame() method.
     */
    private void AIvsAI() throws IOException {
        UCIEngine ai_WHITE = new UCIEngine(8);
        UCIEngine ai_BLACK = new UCIEngine(8);
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
                view.showBoard(Piece.Color.WHITE, board);
                isInPlay = false;
            }
        }
        endGame();
    }

    /**
     * Displays the board. Gets the users move input.
     * Check if the user can promote their pawn. If so, give them the view promotion prompt
     * @returns whether the player moved (true) or resigned (false)
     */
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
                board.promote(activePlayer, latestMove[0], latestMove[1], newPiece, true);
            }
            view.displayMessage("Piece moved! Press enter to continue");
        } else {
            view.displayMessage(getActivePlayerName() + " gave up!");
             return false;
        }
        return true;
    }


    /**
     * Set the computers move on the board.
     * @param comp the computer player making the move.
     */
    private void compTurn(UCIEngine comp) throws IOException {
        board.move(comp.makeMove(board.getMoveHistory()));
    }

    /**
     * Checks for check, checkmate, stalemate, fifty moves, insufficient material, and threefold repetition.
     * @return If detected that the game is over, return true. Else false
     */
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

    /**
     * Players move piece method. Continuously prompts user for move input until valid or exit condition is found.
     * sets move to latestMove, used later to highlight board square in display.
     * @returns true if piece moved. False if exit condition is found.
     */
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

    /**
     * Toggles the active player. Used to change players turn.
     */
    private void toggleActivePlayer() {
        activePlayer = activePlayer.inverse();
    }

    /**
     * Method that runs at end of game loop. Prompts the user to continue.
     */
    private void endGame() {
        view.displayMessage("Press enter to continue");
        view.promptForContinue();
    }

    /**
     * Formats the active players name. Capitalizes first letter only.
     * @returns formatted name String.
     */
    private String getActivePlayerName() {
        return activePlayer.toString().substring(0,1) +  activePlayer.toString().toLowerCase().substring(1);
    }
}
