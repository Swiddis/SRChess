package widdis.unroe.chess.board;

import widdis.unroe.chess.board.pieces.*;

import java.awt.*;
import java.util.Arrays;

public class Board {
    private static final int HEIGHT = 8, WIDTH = 8;
    Square[][] board;

    public Board() {
        // Initialize Board
        board = new Square[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            Arrays.fill(board[i], new Square());
        }
        setBoard(board, "RNBQKBNR/PPPPPPPP/......../......../......../......../pppppppp/rnbqkbnr");
    }

    // Parse a string to a board, with lowercase corresponding to black pieces, uppercase corresponding to white.
    // Initial position is RNBQKBNR/PPPPPPPP/......../......../......../......../pppppppp/rnbqkbnr
    private void setBoard(Square[][] board, String boardStr) {
        String[] rows = boardStr.split("/");
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                switch (rows[i].charAt(j)) {
                    case '.':
                        board[i][j].setPiece(null);
                        break;
                    case 'P':
                        board[i][j].setPiece(new Pawn(Piece.Color.WHITE));
                        break;
                    case 'p':
                        board[i][j].setPiece(new Pawn(Piece.Color.BLACK));
                        break;
                    case 'K':
                        board[i][j].setPiece(new King(Piece.Color.WHITE));
                        break;
                    case 'k':
                        board[i][j].setPiece(new King(Piece.Color.BLACK));
                        break;
                    case 'Q':
                        board[i][j].setPiece(new Queen(Piece.Color.WHITE));
                        break;
                    case 'q':
                        board[i][j].setPiece(new Queen(Piece.Color.BLACK));
                        break;
                    case 'R':
                        board[i][j].setPiece(new Rook(Piece.Color.WHITE));
                        break;
                    case 'r':
                        board[i][j].setPiece(new Rook(Piece.Color.BLACK));
                        break;
                    case 'N':
                        board[i][j].setPiece(new Knight(Piece.Color.WHITE));
                        break;
                    case 'n':
                        board[i][j].setPiece(new Knight(Piece.Color.BLACK));
                        break;
                    case 'B':
                        board[i][j].setPiece(new Bishop(Piece.Color.WHITE));
                        break;
                    case 'b':
                        board[i][j].setPiece(new Bishop(Piece.Color.BLACK));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid Board String: " + rows[i].charAt(j));
                }
            }
        }
    }

    public int checkWin() {
        return 0;
    }

    public void move(String moveStr) {

    }

    public String parseMoveStr(String moveStr) {
        return moveStr;
    }
}
