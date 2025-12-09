package com.example.chess;

import javafx.scene.layout.Pane;

import java.io.FileNotFoundException;

public class Bishop extends Piece {

    public Bishop(SquarePair pos) {
        super(pos);
        pieceType = "bishop";
    }

    public void findLegalMoves(ChessBoard chessBoard){
        legalMoves.clear();
        int originalRow = pos.getRow();
        int originalCol = pos.getCol();

        int row = originalRow + 1;
        int col = originalCol + 1;
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
