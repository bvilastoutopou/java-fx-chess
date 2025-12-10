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
        if(movesDone==0){
            Piece leftSide = chessBoard.getPiece(new SquarePair(row,0));
            Piece rightSide = chessBoard.getPiece(new SquarePair(row,7));

            if(leftSide!=null && leftSide.getPieceType().equals("rook")){
                if(leftSide.movesDone==0){
                    boolean hasBetweenPieces = false;
                    for(int i=1;i<4;i++){
                        if(chessBoard.getPiece(new SquarePair(row,i))!=null){
                            hasBetweenPieces = true;
                            break;
                        }
                    }
                    if(!hasBetweenPieces){
                        specialMoves.add(new SquarePair(row,col-2));
                    }
                }
            }

            if(rightSide!=null && rightSide.getPieceType().equals("rook")){
                if(rightSide.movesDone==0){
                    boolean hasBetweenPieces = false;
                    for(int i=5;i<7;i++){
                        if(chessBoard.getPiece(new SquarePair(row,i))!=null){
                            hasBetweenPieces = true;
                            break;
                        }
                    }
                    if(!hasBetweenPieces){
                        specialMoves.add(new SquarePair(row,col+2));
                    }
                }
            }
        }
    }


}
