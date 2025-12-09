package com.example.chess;

import javafx.scene.layout.Pane;

import java.io.FileNotFoundException;

public class Rook extends Piece{

    public Rook(SquarePair pos) {
        super(pos);
        pieceType = "rook";
    }

    public void findLegalMoves(ChessBoard chessBoard){
        legalMoves.clear();
        int row = pos.getRow();
        int col = pos.getCol();

        for(int i=row+1;i<8;i++){
            SquarePair pair = new SquarePair(i,col);
            Piece pieceAtPos = chessBoard.getPiece(pair);
            if(pieceAtPos==null) legalMoves.add(pair);
            else if(pieceAtPos.getColor().equals(getColor())){
                break;
            }
            else{
                legalMoves.add(pair);
                break;
            }
        }

        for(int i=row-1;i>=0;i--){
            SquarePair pair = new SquarePair(i,col);
            Piece pieceAtPos = chessBoard.getPiece(pair);
            if(pieceAtPos==null) legalMoves.add(pair);
            else if(pieceAtPos.getColor().equals(getColor())){
                break;
            }
            else{
                legalMoves.add(pair);
                break;
            }
        }

        for(int i=col+1;i<8;i++){
            SquarePair pair = new SquarePair(row,i);
            Piece pieceAtPos = chessBoard.getPiece(pair);
            if(pieceAtPos==null) legalMoves.add(pair);
            else if(pieceAtPos.getColor().equals(getColor())){
                break;
            }
            else{
                legalMoves.add(pair);
                break;
            }
        }

        for(int i=col-1;i>=0;i--){
            SquarePair pair = new SquarePair(row,i);
            Piece pieceAtPos = chessBoard.getPiece(pair);
            if(pieceAtPos==null) legalMoves.add(pair);
            else if(pieceAtPos.getColor().equals(getColor())){
                break;
            }
            else{
                legalMoves.add(pair);
                break;
            }
        }
    }


}
