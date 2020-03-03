package widdis.unroe.chess.board;

import widdis.unroe.chess.board.pieces.*;

import java.util.ArrayList;

public class Board {
    public static final int SIZE = 8;
    private Square[][] board;
    private Square[][] previousBoard;
    private ArrayList<String> moveHistory;

    public Board() {
        // Initialize Board
        board = new Square[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
           for(int j = 0; j < SIZE; j++) {
               board[i][j] = new Square();
               board[i][j].setPos(i, j);
               board[i][j].setHasMoved(false);
           }
        }
        setBoard("RNBQKBNR/PPPPPPPP/......../......../......../......../pppppppp/rnbqkbnr");
        // Also get move history, needed to use UCI protocol
        moveHistory = new ArrayList<>();
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
        for(int i = 0; i < SIZE; i++) {
            for(int j = 0; j < SIZE; j++) {
                try {
                    if (board[i][j].getPiece().toString().equals("king")) {
                        if (board[i][j].getPiece().getColor().equals(Piece.Color.WHITE)) {
                            kings[0] = true;
                        } else {
                            kings[1] = true;
                        }
                    }
                } catch(NullPointerException ignored) {} //Empty squares
            }
        }

        if(kings[0] && !kings[1]) { //return 1 on White win
            return 1;
        }
        else if(!kings[0] && kings[1]) { //return 2 on Black win
            return 2;
        }
        return 0;
    }

    // Needs to take move input in long algebraic notation without hyphens or capture delimiters, as per UCI protocol
    // https://en.wikipedia.org/wiki/Algebraic_notation_%28chess%29#Long_algebraic_notation
    public int[] move(String moveStr) {
        if(moveStr.length() != 4) {
            throw new IllegalArgumentException("Invalid Move!");
        }
        // m is the parsed move
        // m[0] is the source position, m[1] is the destination position
        int[][] m = parseMoveStr(moveStr);
        if (board[m[0][0]][m[0][1]].getPiece().checkIsLegal(
                board[m[0][0]][m[0][1]], board[m[1][0]][m[1][1]], board
        )) {
            board[m[1][0]][m[1][1]].setPiece(board[m[0][0]][m[0][1]].getPiece());
            board[m[0][0]][m[0][1]].setPiece(null);
            board[m[0][0]][m[0][1]].setHasMoved(true);
            board[m[1][0]][m[1][1]].setHasMoved(true);
            // Special handling for castling
            if (board[m[1][0]][m[1][1]].getPiece() instanceof King &&
                    Math.abs(m[1][1] - m[0][1]) == 2) {
                // Simply move the rook to the other side of the king
                // Check left first, otherwise it's to the right
                if (m[0][1] < m[1][1]) {
                    board[m[0][0]][5].setPiece(board[m[0][0]][7].getPiece());
                    board[m[0][0]][7].setPiece(null);
                } else {
                    board[m[0][0]][2].setPiece(board[m[0][0]][0].getPiece());
                    board[m[0][0]][0].setPiece(null);
                }
            }

            // Special handling for en passant
            if (board[m[1][0]][m[1][1]].isEnPassant() &&
                    board[m[1][0]][m[1][1]].getPiece() instanceof Pawn) {
                if (m[0][0] < m[1][0]) {
                    board[m[1][0] - 1][m[1][1]].setPiece(null);
                } else {
                    board[m[1][0] + 1][m[1][1]].setPiece(null);
                }
            }
            // If all of this went through correctly, the move was valid, add to history
            moveHistory.add(moveStr);
            this.previousBoard = this.board.clone();
            return new int[] {m[1][0] , m[1][1]};

        } else {
            throw new IllegalArgumentException("Illegal Move!");
        }
    }

    private void unmove() {
        moveHistory.remove(moveHistory.size() - 1);
        this.board = this.previousBoard.clone();
    }

    // Pass in the attacking player
    public boolean isCheck(Piece.Color color) {
        int kposx = -1, kposy = -1;
        // Locate the opposing king
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j].getPiece() instanceof King && board[i][j].getPiece().getColor() != color) {
                    kposy = i;
                    kposx = j;
                    break;
                }
            }
            if (kposx >= 0) break;
        }
        // Now check which of the shared pieces have the check as a legal move
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!board[i][j].isEmpty() && board[i][j].getPiece().getColor() == color &&
                    board[i][j].getPiece().checkIsLegal(board[i][j], board[kposy][kposx], board)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Pass in the attacking player
    private boolean isMate(Piece.Color color) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (!board[i][j].isEmpty() && board[i][j].getPiece().getColor() != color) {
                    char c1 = (char) (j + 97), c2 = (char) (i + 49);
                    for (Square move : board[i][j].getPiece().getLegalMoves(board[i][j], board)) {
                        char c3 = (char) (move.getPos()[0] + 97), c4 = (char) (move.getPos()[1] + 49);
                        this.move(new String(new char[]{c1, c2, c3, c4}));
                        // For every move, check if it's legal by seeing if the opponent will be put in check
                        if (this.isCheck(color)) {
                            unmove();
                        } else {
                            unmove();
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean isCheckmate(Piece.Color color) {
        return isCheck(color) && isMate(color);
    }

    // Pass in attacking color
    public boolean isStalemate(Piece.Color color) {
        return !isCheck(color) && isMate(color);
    }

    public boolean checkForPromotion(Piece.Color activePlayer, int x, int y) {
        if(board[x][y].getPiece() instanceof Pawn) {
            if (activePlayer == Piece.Color.WHITE && x == 7) {
                return true;
            } else if (activePlayer == Piece.Color.BLACK && x == 0) {
                return true;
            }
        }
        return false;
    }

    public void promote(Piece.Color activePlayer, int x, int y, String newPiece) {
        switch (newPiece) {
            case "q":
                board[x][y].setPiece(new Queen(activePlayer));
                break;
            case "r":
                board[x][y].setPiece(new Rook(activePlayer));
                break;
            case "b":
                board[x][y].setPiece(new Bishop(activePlayer));
                break;
            case "n":
                board[x][y].setPiece(new Knight(activePlayer));
                break;
        }
        moveHistory.set(moveHistory.size() - 1, moveHistory.get(moveHistory.size() - 1) + newPiece);
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
