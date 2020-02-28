package widdis.unroe.chess.board;

import widdis.unroe.chess.board.pieces.*;

// TODO: Check & Checkmate detection
// TODO: Castling
// TODO: En Passant
// TODO: Pawn Promotion
public class Board {
    public static final int SIZE = 8;
    private Square[][] board;

    public Board() {
        // Initialize Board

        board = new Square[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
           for(int j = 0; j < SIZE; j++) {
               board[i][j] = new Square();
               board[i][j].setPos(i, j);
           }
        }
        setBoard("RNBQKBNR/PPPPPPPP/......../......../......../......../pppppppp/rnbqkbnr");
    }

    // Parse a string to a board, with lowercase corresponding to black pieces, uppercase corresponding to white.
    // Initial position is RNBQKBNR/PPPPPPPP/......../......../......../......../pppppppp/rnbqkbnr
    private void setBoard(String boardStr) {
        String[] rows = boardStr.split("/");
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                switch (rows[i].charAt(j)) {
                    case '.':
                        this.board[i][j].setPiece(null);
                        break;
                    case 'P':
                        this.board[i][j].setPiece(new Pawn(Piece.Color.WHITE));
                        break;
                    case 'p':
                        this.board[i][j].setPiece(new Pawn(Piece.Color.BLACK));
                        break;
                    case 'K':
                        this.board[i][j].setPiece(new King(Piece.Color.WHITE));
                        break;
                    case 'k':
                        this.board[i][j].setPiece(new King(Piece.Color.BLACK));
                        break;
                    case 'Q':
                        this.board[i][j].setPiece(new Queen(Piece.Color.WHITE));
                        break;
                    case 'q':
                        this.board[i][j].setPiece(new Queen(Piece.Color.BLACK));
                        break;
                    case 'R':
                        this.board[i][j].setPiece(new Rook(Piece.Color.WHITE));
                        break;
                    case 'r':
                        this.board[i][j].setPiece(new Rook(Piece.Color.BLACK));
                        break;
                    case 'N':
                        this.board[i][j].setPiece(new Knight(Piece.Color.WHITE));
                        break;
                    case 'n':
                        this.board[i][j].setPiece(new Knight(Piece.Color.BLACK));
                        break;
                    case 'B':
                        this.board[i][j].setPiece(new Bishop(Piece.Color.WHITE));
                        break;
                    case 'b':
                        this.board[i][j].setPiece(new Bishop(Piece.Color.BLACK));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid Board String: " + rows[i].charAt(j));
                }

            }
        }

    }

    public Square[][] getBoard() {
        return this.board;
    }
    public int checkWin() {
        boolean[] kings = new boolean[2]; //0 : white; 1 : black
        for(int i = 0; i < this.SIZE; i++) {
            for(int j = 0; j < this.SIZE; j++) {
                try {
                    if (board[i][j].getPiece().toString().equals("king")) {
                        if (board[i][j].getPiece().getColor().equals(Piece.Color.WHITE)) {
                            kings[0] = true;
                        } else {
                            kings[1] = true;
                        }
                    }
                } catch(NullPointerException npe) {} //Empty squares
            }
        }

        if(kings[0] == true && kings[1] == false) { //return 1 on White win
            return 1;
        }
        else if(kings[0] == false && kings[1] == true) { //return 2 on Black win
            return 2;
        }
        return 0;
    }

    public void move(String moveStr) {
        if(moveStr.length() != 4) {
            throw new IllegalArgumentException("Invalid Move!");
        }
        int[][] moves = parseMoveStr(moveStr);
        if (board[moves[0][0]][moves[0][1]].getPiece().checkIsLegal(
                board[moves[0][0]][moves[0][1]], board[moves[1][0]][moves[1][1]], board
        )) {
            board[moves[1][0]][moves[1][1]].setPiece(board[moves[0][0]][moves[0][1]].getPiece());
            board[moves[0][0]][moves[0][1]].setPiece(null);
        } else {
            throw new IllegalArgumentException("Illegal Move!");
        }
    }

    public int[][] parseMoveStr(String moveStr) {
        // For now, blindly assume input is proper
        moveStr = moveStr.toLowerCase();
        int[][] moves = new int[2][2];
        moves[0][1] = (int) moveStr.charAt(0) - 97;
        moves[0][0] = (int) moveStr.charAt(1) - 49;
        moves[1][1] = (int) moveStr.charAt(2) - 97;
        moves[1][0] = (int) moveStr.charAt(3) - 49;
        return moves;
    }
}
