package com.example.chess;

import javafx.scene.layout.Pane;

public class Queen extends Piece{

    public Queen(SquarePair pos) {
        super(pos);
        pieceType = "queen";
    }

    @Override
    public void findLegalMoves(ChessBoard chessBoard){
        legalMoves.clear();
        int row = pos.getRow();
        int col = pos.getCol();
        int originalRow = pos.getRow();
        int originalCol = pos.getCol();
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

        row = originalRow + 1;
        col = originalCol + 1;
        while (row<8 && col<8){
            SquarePair pair = new SquarePair(row,col);
            Piece pieceAtPos = chessBoard.getPiece(pair);
            if(pieceAtPos==null) legalMoves.add(pair);
            else if(pieceAtPos.getColor().equals(getColor())){
                break;
            }
            else{
                legalMoves.add(pair);
                break;
            }
            row++;
            col++;
        }

        row = originalRow - 1;
        col = originalCol - 1;
        while (row>=0 && col>=0){
            SquarePair pair = new SquarePair(row,col);
            Piece pieceAtPos = chessBoard.getPiece(pair);
            if(pieceAtPos==null) legalMoves.add(pair);
            else if(pieceAtPos.getColor().equals(getColor())){
                break;
            }
            else{
                legalMoves.add(pair);
                break;
            }
            row--;
            col--;
        }

        row = originalRow - 1;
        col = originalCol + 1;
        while (row>=0 && col<8){
            SquarePair pair = new SquarePair(row,col);
            Piece pieceAtPos = chessBoard.getPiece(pair);
            if(pieceAtPos==null) legalMoves.add(pair);
            else if(pieceAtPos.getColor().equals(getColor())){
                break;
            }
            else{
                legalMoves.add(pair);
                break;
            }
            row--;
            col++;
        }

        row = originalRow + 1;
        col = originalCol - 1;
        while (row<8 && col>=0){
            SquarePair pair = new SquarePair(row,col);
            Piece pieceAtPos = chessBoard.getPiece(pair);
            if(pieceAtPos==null) legalMoves.add(pair);
            else if(pieceAtPos.getColor().equals(getColor())){
                break;
            }
            else{
                legalMoves.add(pair);
                break;
            }
            row++;
            col--;
        }
    }
}
