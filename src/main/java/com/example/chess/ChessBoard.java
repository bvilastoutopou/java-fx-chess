package com.example.chess;

import java.io.FileNotFoundException;

public class ChessBoard {
    private final int SIZE = 8;
    private Piece[][] chessBoard = new Piece[SIZE][SIZE];

    public ChessBoard() throws FileNotFoundException {
        setChessBoard();
    }

    public void setPiece(Piece piece, SquarePair pair){
        int row = pair.getRow();
        int col = pair.getCol();
        chessBoard[row][col] = piece;
    }

    public Piece getPiece(SquarePair pair){
        int row = pair.getRow();
        int col = pair.getCol();
        return chessBoard[row][col];
    }

    public void setChessBoard() throws FileNotFoundException {
        SquarePair whiteRookLeftPos = new SquarePair(7,0);
        WhiteRook whiteRookLeft = new WhiteRook(whiteRookLeftPos);
        setPiece(whiteRookLeft,whiteRookLeftPos);
        SquarePair whiteRookRightPos = new SquarePair(7,7);
        WhiteRook whiteRookRight = new WhiteRook(whiteRookRightPos);
        setPiece(whiteRookRight,whiteRookRightPos);

        SquarePair blackRookLeftPos = new SquarePair(0,0);
        BlackRook blackRookLeft = new BlackRook(blackRookLeftPos);
        setPiece(blackRookLeft,blackRookLeftPos);
        SquarePair blackRookRightPos = new SquarePair(0,7);
        BlackRook blackRookRight = new BlackRook(blackRookRightPos);
        setPiece(blackRookRight,blackRookRightPos);
    }

}
