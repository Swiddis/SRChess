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
        boolean checkered = true;
        String boardString = " |";
        //Displays column labels
        for(int letter = (activePlayer == Piece.Color.WHITE ? 0 : board.HEIGHT-1 );
            (activePlayer == Piece.Color.WHITE ? letter < board.HEIGHT : letter >= 0);
            letter += (activePlayer == Piece.Color.WHITE ? 1 : -1)) {
            boardString += columnLabels[letter] + "|";
        }
        boardString +="\r\n";
        for(int i = (activePlayer == Piece.Color.WHITE ? board.HEIGHT-1 : 0);
            (activePlayer == Piece.Color.WHITE ? i >= 0 : i < board.HEIGHT);
            i += (activePlayer == Piece.Color.WHITE ? -1 : 1)) {
            boardString+=(i+1);
            for(int j = (activePlayer == Piece.Color.WHITE ? 0 : board.WIDTH-1);
                (activePlayer == Piece.Color.WHITE ? j < board.WIDTH : j >= 0);
                j += (activePlayer == Piece.Color.WHITE ? 1 : -1)) {

                boardString += "|";
                if(checkered) {
                    boardString += "\033[100m"; //Dark Gray Background
                }
                checkered = !checkered;
                if(board.getBoard()[i][j].getPiece() == null){
                    boardString += " ";
                }
                else {
                    String textColor = "\033[1;30m"; //Bold White Text
                    if(board.getBoard()[i][j].getPiece().getColor() == Piece.Color.BLACK) {
                        textColor = "\033[1;37m"; //Bold Gray Text
                    }
                    boardString += textColor;
                    boardString += getPieceCharacter(board, i, j);
                }
                boardString += "\033[0m"; // Reset
            }
            boardString+="|\r\n";
            checkered = !checkered;
        }
       this.displayMessage(boardString);
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

    public String promptForMove() {
        return io.getStringPrefix("Enter a move (eg a1a2): ");
    }
    public int menuPrompt() {
        this.displayMessage("Chess Menu");
        String[] options = {"Player vs Player", "Player vs Computer", "Computer vs Computer", "Exit"};
        return io.getIntFromMenu(options, false);
    }
}

