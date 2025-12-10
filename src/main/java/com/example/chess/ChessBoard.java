package com.example.chess;

import java.io.FileNotFoundException;

public class ChessBoard {
    private final int SIZE = 8;
    private final Piece[][] chessBoard = new Piece[SIZE][SIZE];
    private SquarePair lastMove;
    private int halfMoveCounter;
    public ChessBoard() throws FileNotFoundException {
        lastMove = null;
        halfMoveCounter = 0;
        setChessBoard();
    }

    public boolean determineInsufficientMaterial(){
        int pieceCounter = 0;
        int whiteBishopCounter = 0;
        int whiteKnightCounter = 0;
        int blackBishopCounter = 0;
        int blackKnightCounter = 0;
        SquarePair whiteBishopPos = null;
        SquarePair blackBishopPos = null;
        for(int row=0;row<SIZE;row++){
            for(int col=0;col<SIZE;col++){
                Piece piece = getPiece(new SquarePair(row,col));
                if(piece!=null){
                    pieceCounter++;
                    if(piece.getPieceType().equals("pawn") || piece.getPieceType().equals("queen") || piece.getPieceType().equals("rook")){
                        return false;
                    }
                    else if(piece.getPieceType().equals("bishop")){
                        if(piece.getColor().equals("white")){
                            whiteBishopCounter++;
                            whiteBishopPos = new SquarePair(row,col);
                        }
                        else{
                            blackBishopCounter++;
                            blackBishopPos = new SquarePair(row,col);
                        }
                    }
                    else if(piece.getPieceType().equals("knight")){
                        if(piece.getColor().equals("white")){
                            whiteKnightCounter++;
                        }
                        else{
                            blackKnightCounter++;
                        }
                    }
                }
            }
        }
        if(pieceCounter==2){
            return true;
        }else if(pieceCounter==3){
            return whiteBishopCounter == 1 || blackBishopCounter == 1 || blackKnightCounter == 1 || whiteKnightCounter == 1;
        }else if(pieceCounter==4){
            if(whiteBishopCounter==1 && blackBishopCounter==1){
                boolean isLightWhite = (whiteBishopPos.getRow()+whiteBishopPos.getCol())%2 == 0;
                boolean isLightBlack = (blackBishopPos.getRow()+blackBishopPos.getCol())%2 == 0;
                return isLightBlack == isLightWhite;
            }
        }
        return false;
    }

    public int getHalfMoveCounter() {
        return halfMoveCounter;
    }

    public void incrementHalfMoveCounter(){
        halfMoveCounter++;
    }

    public void resetHalfMoveCounter(){
        halfMoveCounter = 0;
    }


    public boolean findAllMoves(String color) throws FileNotFoundException {
        for(int row=0;row<SIZE;row++){
            for(int col=0;col<SIZE;col++) {
                SquarePair pair = new SquarePair(row,col);
                Piece piece = getPiece(pair);
                if(piece!=null && piece.getColor().equals(color)){
                    piece.findAllowedMoves(this);
                    if(!piece.allowedMoves.isEmpty() || !piece.allowedSpecialMoves.isEmpty()){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public SquarePair findKing(String color){
        for(int row=0;row<SIZE;row++){
            for(int col=0;col<SIZE;col++){
                SquarePair pair = new SquarePair(row,col);
                if(getPiece(pair)!=null){
                    if(getPiece(pair).getColor().equals(color) && getPiece(pair).getPieceType().equals("king")){
                        return pair;
                    }
                }
            }
        }
        return null;
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

        SquarePair whiteKingPos = new SquarePair(7,4);
        WhiteKing whiteKing = new WhiteKing(whiteKingPos);
        setPiece(whiteKing,whiteKingPos);

        SquarePair blackKingPos = new SquarePair(0,4);
        BlackKing blackKing = new BlackKing(blackKingPos);
        setPiece(blackKing,blackKingPos);
    }

}
