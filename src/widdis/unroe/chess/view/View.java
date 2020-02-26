package widdis.unroe.chess.view;

import widdis.unroe.chess.board.Board;
import widdis.unroe.chess.board.pieces.Piece;

public class View {
    public void showBoard(Piece.Color activePlayer, Board board) {
        boolean checkered = true;
        String boardString = "";
        for(int i = board.HEIGHT-1; i >= 0; i--) {
            for(int j = 0; j < board.WIDTH; j++) {

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
                    boardString += board.getBoard()[i][j].getPiece().toFEN();
                }
                boardString += "\033[0m"; // Reset
            }
            boardString+="|\r\n";
            checkered = !checkered;
        }
        System.out.println(boardString);
    }
}
//board.getBoard()[j][i].getPiece().toFEN()
//+ j + " " + i