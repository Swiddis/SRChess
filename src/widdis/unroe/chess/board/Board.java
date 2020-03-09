package widdis.unroe.chess.board;

import widdis.unroe.chess.board.pieces.*;
import java.util.ArrayList;

public class Board {
    public static final int SIZE = 8;
    private Square[][] board;
    private Square[][] previousBoard;
    private int prevFmc;
    private int fiftyMoveCounter;


    private ArrayList<String> moveHistory;

    public Board() {
        // Initialize Board
        board = new Square[SIZE][SIZE];
        previousBoard = new Square[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
           for(int j = 0; j < SIZE; j++) {
               board[i][j] = new Square();
               board[i][j].setPos(i, j);
               board[i][j].setHasMoved(false);
           }
        }
        setBoard();
        // Also get move history, needed to use UCI protocol
        moveHistory = new ArrayList<>();
        fiftyMoveCounter = 0;
    }

    // Parse a string to a board, with lowercase corresponding to black pieces, uppercase corresponding to white.
    // Initial position is RNBQKBNR/PPPPPPPP/......../......../......../......../pppppppp/rnbqkbnr
    private void setBoard() {
        String startPos = "RNBQKBNR/PPPPPPPP/......../......../......../......../pppppppp/rnbqkbnr";
        String[] rows = startPos.split("/");
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

    public ArrayList<String> getMoveHistory() {
        return moveHistory;
    }

    public Square[][] getBoard() {
        return this.board;
    }

    // Needs to take move input in long algebraic notation without hyphens or capture delimiters, as per UCI protocol
    // https://en.wikipedia.org/wiki/Algebraic_notation_%28chess%29#Long_algebraic_notation
    public int[] move(String moveStr) {
        if (moveStr.length() < 4 || moveStr.length() > 5) {
            throw new IllegalArgumentException("Invalid Move! " + moveStr);
        }
        // m is the parsed move
        // m[0] is the source position, m[1] is the destination position
        // parseMoveStr will ignore a 5th character if it's present, we can manually handle it later.
        int[][] m = parseMoveStr(moveStr);
        if (board[m[0][0]][m[0][1]].getPiece().checkIsLegal(
                board[m[0][0]][m[0][1]], board[m[1][0]][m[1][1]], board
        )) {
            try {
                for (int i = 0; i < SIZE; i++) {
                    for (int j = 0; j < SIZE; j++) {
                        this.previousBoard[i][j] = board[i][j].clone();
                    }
                }
            } catch (CloneNotSupportedException ex) {
                throw new RuntimeException(ex);
            }
            prevFmc = fiftyMoveCounter;
            if (board[m[1][0]][m[1][1]].isEmpty() && !(board[m[0][0]][m[0][1]].getPiece() instanceof Pawn)) {
                fiftyMoveCounter++;
            } else {
                fiftyMoveCounter = 0;
            }
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
                    board[m[0][0]][3].setPiece(board[m[0][0]][0].getPiece());
                    board[m[0][0]][0].setPiece(null);
                }
            }
            // Special handling for en passant
            // Check if en passant capture
            if (board[m[1][0]][m[1][1]].isEnPassant() &&
                    board[m[1][0]][m[1][1]].getPiece() instanceof Pawn) {
                if (m[0][0] < m[1][0]) {
                    board[m[1][0] - 1][m[1][1]].setPiece(null);
                } else {
                    board[m[1][0] + 1][m[1][1]].setPiece(null);
                }
            // Also set en passant squares
            } else {
                for (int i = 0; i < SIZE; i++) {
                    for (int j = 0; j < SIZE; j++) {
                        board[i][j].setEnPassant(false);
                    }
                }
                if (board[m[1][0]][m[1][1]].getPiece() instanceof Pawn &&
                        Math.abs(m[1][0] - m[0][0]) == 2) {
                    board[m[1][0] - (m[1][0] - m[0][0]) / 2][m[1][1]].setEnPassant(true);
                }
            }
            // Special handling for pawn promotion
            // If the move is length 4 and a pawn needs to promote, assume someone else will handle it.
            // If a 5th character is present in the string, process it on the spot.
            if ((m[1][0] == 0 || m[1][0] == 7) && board[m[1][0]][m[1][1]].getPiece() instanceof Pawn
                    && moveStr.length() == 5) {
                promote(board[m[1][0]][m[1][1]].getPiece().getColor(),
                        m[1][0], m[1][1], moveStr.charAt(4) + "", false);
            }

            // Finally, we need to make sure that the user didn't put themself into check.
            if (isCheck(board[m[1][0]][m[1][1]].getPiece().getColor().inverse())) {
                unmove();
                fiftyMoveCounter = prevFmc;
                throw new IllegalArgumentException("Illegal Move: user is in check!");
            }

            // If all of this went through correctly, the move was valid, add to history
            moveHistory.add(moveStr);
            return new int[] {m[1][0] , m[1][1]};
        } else {
            throw new IllegalArgumentException("Illegal Move! " + moveStr);
        }
    }

    private void unmove() {
        try {
            moveHistory.remove(moveHistory.size() - 1);
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    this.board[i][j] = previousBoard[i][j].clone();
                }
            }
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    // Pass in the defending player
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
                if (!board[i][j].isEmpty() && board[i][j].getPiece().getColor() == color) {
                    char c1 = (char) (j + 97), c2 = (char) (i + 49);
                    for (Square move : board[i][j].getPiece().getLegalMoves(board[i][j], board)) {
                        char c3 = (char) (move.getPos()[1] + 97), c4 = (char) (move.getPos()[0] + 49);
                        prevFmc = fiftyMoveCounter;
                        this.move(new String(new char[]{c1, c2, c3, c4}));
                        fiftyMoveCounter = prevFmc;
                        // For every move, check if the opponent will be put in check
                        if (this.isCheck(color.inverse())) {
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
        return isCheck(color.inverse()) && isMate(color);
    }

    // Pass in attacking color
    public boolean isStalemate(Piece.Color color) {
        return !isCheck(color.inverse()) && isMate(color);
    }

    public boolean checkForPromotion(Piece.Color activePlayer, int x, int y) {
        if(board[x][y].getPiece() instanceof Pawn) {
            if (activePlayer == Piece.Color.WHITE && x == 7) return true;
            else return activePlayer == Piece.Color.BLACK && x == 0;
        }
        return false;
    }

    public void promote(Piece.Color activePlayer, int x, int y, String newPiece, boolean update) {
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
        if (update) moveHistory.set(moveHistory.size() - 1, moveHistory.get(moveHistory.size() - 1) + newPiece);
    }

    public boolean checkFiftyMoves() {
        // We need to double it from 50 because one move is one step for each player, and we increment every step
        return fiftyMoveCounter >= 100;
    }

    public boolean checkThreefold() {
        if (moveHistory.size() <= 6) {
            return false;
        }
        int s = moveHistory.size();
        int[] i = new int[]{s - 1, s - 3, s - 5, s - 2, s - 4, s - 6};
        return (moveHistory.get(i[0]).equals(moveHistory.get(i[1])) && moveHistory.get(i[1]).equals(moveHistory.get(i[2])) &&
                moveHistory.get(i[3]).equals(moveHistory.get(i[4])) && moveHistory.get(i[4]).equals(moveHistory.get(i[5])));
    }

    public boolean checkInsufficientMaterial() {
        /*
        There are 4 types of insufficient material draws to look for:
        King vs King
        King + Bishop vs King
        King + Knight vs King
        King + Bishop vs King + Bishop w/ bishops on same color
        */
        int whiteBishops = 0, blackBishops = 0, whiteKnights = 0, blackKnights = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j].isEmpty()) continue;
                Piece p = board[i][j].getPiece();
                if (p instanceof King) {
                    continue;
                } else if (p instanceof Bishop) {
                    if (p.getColor() == Piece.Color.WHITE) whiteBishops++;
                    else if (p.getColor() == Piece.Color.BLACK) blackBishops++;
                } else if (p instanceof Knight) {
                    if (p.getColor() == Piece.Color.WHITE) whiteKnights++;
                    else if (p.getColor() == Piece.Color.BLACK) blackKnights++;
                } else {
                    return false;
                }
            }
        }
        // Draw by knights
        if (whiteKnights + blackKnights == 1 && whiteBishops + blackBishops == 0) {
            return true;
        // Trivial draw by bishops
        } else if (whiteBishops + blackBishops == 1 && whiteKnights + blackKnights == 0) {
            return true;
        // Nontrivial draw by bishops
        } else if (whiteBishops == 1 && blackBishops == 1 && whiteKnights + blackKnights == 0) {
            Piece.Color firstBishopColor = null;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (!board[i][j].isEmpty() && board[i][j].getPiece() instanceof Bishop) {
                        if (firstBishopColor == null) {
                            if ((i + j) % 2 == 0) {
                                firstBishopColor = Piece.Color.BLACK;
                            } else {
                                firstBishopColor = Piece.Color.WHITE;
                            }
                        } else {
                            if ((i + j) % 2 == 0) {
                                return firstBishopColor == Piece.Color.BLACK;
                            } else {
                                return firstBishopColor == Piece.Color.WHITE;
                            }
                        }
                    }
                }
            }
        // Lone kings
        } else {
            return whiteKnights + blackKnights == 0 && whiteBishops + blackBishops == 0;
        }
        return false; // Logically I don't think this can fall through, but the compiler says otherwise.
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
