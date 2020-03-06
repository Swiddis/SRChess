package widdis.unroe.chess.view;

import edu.neumont.simlib.ConsoleIO;
import widdis.unroe.chess.board.Board;
import widdis.unroe.chess.board.pieces.Piece;

public class View {
    private ConsoleIO io = new ConsoleIO();


    public enum DisplayCharacter {
        FEN,
        UNICODE
    }

    private final String[] columnLabels = {
            "A","B","C","D","E","F","G","H"
    };

    private DisplayCharacter displayCharacter= DisplayCharacter.FEN;

    public void showBoard(Piece.Color activePlayer, Board board) {
        showBoard(activePlayer, board, null);
    }

    public void showBoard(Piece.Color activePlayer, Board board, int[] latestMove) {
        boolean checkered = true;
        StringBuilder boardString = new StringBuilder(" |");
        //Displays column labels
        for(int letter = (activePlayer == Piece.Color.WHITE ? 0 : Board.SIZE -1 );
            (activePlayer == Piece.Color.WHITE ? letter < Board.SIZE : letter >= 0);
            letter += (activePlayer == Piece.Color.WHITE ? 1 : -1)) {
            boardString.append(columnLabels[letter]).append("|");
        }
        boardString.append("\r\n");
        for(int i = (activePlayer == Piece.Color.WHITE ? Board.SIZE -1 : 0);
            (activePlayer == Piece.Color.WHITE ? i >= 0 : i < Board.SIZE);
            i += (activePlayer == Piece.Color.WHITE ? -1 : 1)) {
            boardString.append(i + 1);
            for(int j = (activePlayer == Piece.Color.WHITE ? 0 : Board.SIZE -1);
                (activePlayer == Piece.Color.WHITE ? j < Board.SIZE : j >= 0);
                j += (activePlayer == Piece.Color.WHITE ? 1 : -1)) {

                boardString.append("|");
                    if (latestMove != null && latestMove[0]==i && latestMove[1] ==j) {

                        if(activePlayer == Piece.Color.WHITE) {
                            boardString.append("\033[47m"); //Highlight
                        }
                        else {
                            boardString.append("\033[40m"); //Highlight
                        }
                    }

                else if(checkered) {
                    boardString.append("\033[100m"); //Dark Gray Background
                }
                checkered = !checkered;
                if(board.getBoard()[i][j].getPiece() == null){
                    boardString.append(" ");
                }
                else {
                    String textColor = "\033[1;30m"; //Bold White Text
                    if(board.getBoard()[i][j].getPiece().getColor() == Piece.Color.BLACK) {
                        textColor = "\033[1;37m"; //Bold Gray Text
                    }
                    boardString.append(textColor);
                    boardString.append(getPieceCharacter(board, i, j));
                }
                boardString.append("\033[0m"); // Reset
            }
            boardString.append("|\r\n");
            checkered = !checkered;
        }

        this.displayMessage(boardString.toString());
    }

    private String getPieceCharacter(Board board, int i, int j) {
        if(displayCharacter == DisplayCharacter.FEN) {
            return board.getBoard()[i][j].getPiece().toFEN();
        }
        else {
            return board.getBoard()[i][j].getPiece().toUnicode();
        }
    }


    public void displayMessage(String output) {
        System.out.println(output);
    }

    public void promptForContinue() {
        io.getString();
    }

    public String promptPromotion() {
        String newPiece = io.getStringPrefix("What would you like to promote your pawn to? (q,r,n,b): ", "[qrnbQRNB]");
        newPiece = newPiece.trim().toLowerCase();
        this.displayMessage("Invalid Piece!");
        return newPiece;
    }
    public String promptForMove() {
        String move = io.getStringPrefix("Enter a move (eg a1a2) or resign: ");
        if(move.equals("resign") || move.equals("exit") || move.equals("quit") ) {
            throw new IllegalArgumentException("QUIT");
        }
        else if(move.length() != 4) {
            throw new IllegalArgumentException("Invalid Move!");
        }
        return move;
    }
    public int menuPrompt() {
        this.displayMessage("Chess Menu");
        String[] options = {"Player vs Player", "Player vs Computer", "Computer vs Computer", "Exit"};
        return io.getIntFromMenu(options, false);
    }

    public int promptForDifficulty() {
        return io.getIntPrefix("What difficulty would you like to play against? (1-8): ", 1,8);
    }
}

