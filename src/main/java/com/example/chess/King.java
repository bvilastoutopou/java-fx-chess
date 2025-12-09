package com.example.chess;

import java.io.FileNotFoundException;

public class King extends Piece{
    public King(SquarePair pos) {
        super(pos);
        pieceType = "king";
    }

    public boolean isChecked(ChessBoard chessBoard) throws FileNotFoundException {
        for(int row=0;row<8;row++){
            for(int col=0;col<8;col++){
                Piece piece = chessBoard.getPiece(new SquarePair(row,col));

                if(piece==null)continue;;

                if(piece.getColor().equals(getColor()))continue;

                piece.findLegalMoves(chessBoard);
                for(SquarePair pair: piece.legalMoves){
                    if(pair.equals(pos)){
                        return true;
                    }
                }
                for(SquarePair pair: piece.specialMoves){
                    if(pair.equals(pos)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void findLegalMoves(ChessBoard chessBoard) throws FileNotFoundException {
        legalMoves.clear();
        specialMoves.clear();
        int row = pos.getRow();
        int col = pos.getCol();
        Piece pieceAtPos;
        if(row-1>=0){
            SquarePair pair = new SquarePair(row-1,col);
            pieceAtPos = chessBoard.getPiece(pair);
            if(pieceAtPos==null || !pieceAtPos.getColor().equals(getColor())) {
                legalMoves.add(pair);
            }
            if(col-1>=0){
                pair = new SquarePair(row-1,col-1);
                pieceAtPos = chessBoard.getPiece(pair);
                if(pieceAtPos==null || !pieceAtPos.getColor().equals(getColor())) {
                    legalMoves.add(pair);
                }
            }
            if(col+1<8){
                pair = new SquarePair(row-1,col+1);
                pieceAtPos = chessBoard.getPiece(pair);
                if(pieceAtPos==null || !pieceAtPos.getColor().equals(getColor())) {
                    legalMoves.add(pair);
                }
            }
        }
        if(row+1<8){
            SquarePair pair = new SquarePair(row+1,col);
            pieceAtPos = chessBoard.getPiece(pair);
            if(pieceAtPos==null || !pieceAtPos.getColor().equals(getColor()))  {
                legalMoves.add(pair);
            }
            if(col-1>=0){
                pair = new SquarePair(row+1,col-1);
                pieceAtPos = chessBoard.getPiece(pair);
                if(pieceAtPos==null || !pieceAtPos.getColor().equals(getColor()))  {
                    legalMoves.add(pair);
                }
            }
            if(col+1<8){
                pair = new SquarePair(row+1,col+1);
                pieceAtPos = chessBoard.getPiece(pair);
                if(pieceAtPos==null || !pieceAtPos.getColor().equals(getColor())) {
                    legalMoves.add(pair);
                }
            }
        }
        if(col-1>=0){
            SquarePair pair = new SquarePair(row,col-1);
            pieceAtPos = chessBoard.getPiece(pair);
            if(pieceAtPos==null || !pieceAtPos.getColor().equals(getColor()))  {
                legalMoves.add(pair);
            }
        }
        if(col+1<8){
            SquarePair pair = new SquarePair(row,col+1);
            pieceAtPos = chessBoard.getPiece(pair);
            if(pieceAtPos==null || !pieceAtPos.getColor().equals(getColor())) {
                legalMoves.add(pair);
            }
        }
    }


}
