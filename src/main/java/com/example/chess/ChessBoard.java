package com.example.chess;

import javafx.scene.layout.Pane;

import java.io.FileNotFoundException;

public class ChessBoard {
    private final int SIZE = 8;
    private Piece[][] chessBoard = new Piece[SIZE][SIZE];
    private SquarePair lastMove;
    public ChessBoard() throws FileNotFoundException {
        lastMove = null;
        setChessBoard();
    }


    public SquarePair getLastMove() {
        return lastMove;
    }

    public void setLastMove(SquarePair lastMove) {
        this.lastMove = lastMove;
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

        SquarePair whiteBishopLeftPos = new SquarePair(7,2);
        WhiteBishop whiteBishopLeft = new WhiteBishop(whiteBishopLeftPos);
        setPiece(whiteBishopLeft,whiteBishopLeftPos);
        SquarePair whiteBishopRightPos = new SquarePair(7,5);
        WhiteBishop whiteBishopRight = new WhiteBishop(whiteBishopRightPos);
        setPiece(whiteBishopRight,whiteBishopRightPos);

        SquarePair blackBishopLeftPos = new SquarePair(0,2);
        BlackBishop blackBishopLeft = new BlackBishop(blackBishopLeftPos);
        setPiece(blackBishopLeft,blackBishopLeftPos);
        SquarePair blackBishopRightPos = new SquarePair(0,5);
        BlackBishop blackBishopRight = new BlackBishop(blackBishopRightPos);
        setPiece(blackBishopRight,blackBishopRightPos);

        SquarePair whiteQueenPos = new SquarePair(7,3);
        WhiteQueen whiteQueen = new WhiteQueen(whiteQueenPos);
        setPiece(whiteQueen,whiteQueenPos);

        SquarePair blackQueenPos = new SquarePair(0,3);
        BlackQueen blackQueen = new BlackQueen(blackQueenPos);
        setPiece(blackQueen,blackQueenPos);

        SquarePair whiteKnightLeftPos = new SquarePair(7,1);
        WhiteKnight whiteKnightLeft = new WhiteKnight(whiteKnightLeftPos);
        setPiece(whiteKnightLeft,whiteKnightLeftPos);
        SquarePair whiteKnightRightPos = new SquarePair(7,6);
        WhiteKnight whiteKnightRight = new WhiteKnight(whiteKnightRightPos);
        setPiece(whiteKnightRight,whiteKnightRightPos);

        SquarePair blackKnightLeftPos = new SquarePair(0,1);
        BlackKnight blackKnightLeft = new BlackKnight(blackKnightLeftPos);
        setPiece(blackKnightLeft,blackKnightLeftPos);
        SquarePair blackKnightRightPos = new SquarePair(0,6);
        BlackKnight blackKnightRight = new BlackKnight(blackKnightRightPos);
        setPiece(blackKnightRight,blackKnightRightPos);

        for(int i=0;i<SIZE;i++){
            SquarePair whitePawnPos = new SquarePair(6,i);
            WhitePawn whitePawn = new WhitePawn(whitePawnPos);
            setPiece(whitePawn,whitePawnPos);
        }

        for(int i=0;i<SIZE;i++){
            SquarePair blackPawnPos = new SquarePair(1,i);
            BlackPawn blackPawn = new BlackPawn(blackPawnPos);
            setPiece(blackPawn,blackPawnPos);
        }
    }

}
