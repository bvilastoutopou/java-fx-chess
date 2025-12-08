package com.example.chess;



public class Pawn extends Piece{
    public Pawn(SquarePair pos) {
        super(pos);
        pieceType = "pawn";
    }

    @Override
    public void findLegalMoves(ChessBoard chessBoard){
        legalMoves.clear();
        specialMoves.clear();
        int row = pos.getRow();
        int col = pos.getCol();
        if(getColor().equals("white")){
            if(chessBoard.getPiece(new SquarePair(row-1,col))==null){
                legalMoves.add(new SquarePair(row-1,col));
                if(movesDone==0 && chessBoard.getPiece(new SquarePair(row-2,col))==null){
                    legalMoves.add(new SquarePair(row-2,col));
                }
            }
            if(col+1<8) {
                SquarePair pair = new SquarePair(row - 1, col+1);
                if (chessBoard.getPiece(pair) != null) {
                    if(!chessBoard.getPiece(pair).getColor().equals(getColor())) {
                        legalMoves.add(pair);
                    }
                }
                pair = new SquarePair(row,col+1);
                if(row==3 && chessBoard.getPiece(pair)!=null){
                    if(!chessBoard.getPiece(pair).getColor().equals(getColor()) && chessBoard.getLastMove().equals(pair) && chessBoard.getPiece(pair).movesDone==1 && chessBoard.getPiece(pair).getPieceType().equals("pawn")){
                        specialMoves.add(new SquarePair(row-1,col+1));
                    }
                }
            }
            if(col-1>=0){
                SquarePair pair = new SquarePair(row - 1, col-1);
                if (chessBoard.getPiece(pair) != null) {
                    if(!chessBoard.getPiece(pair).getColor().equals(getColor())) {
                        legalMoves.add(pair);
                    }
                }
                pair = new SquarePair(row,col-1);
                if(row==3 && chessBoard.getPiece(pair)!=null){
                    if(!chessBoard.getPiece(pair).getColor().equals(getColor()) && chessBoard.getLastMove().equals(pair) && chessBoard.getPiece(pair).movesDone==1 && chessBoard.getPiece(pair).getPieceType().equals("pawn")){
                        specialMoves.add(new SquarePair(row-1,col-1));
                    }
                }
            }

        }else{
            if(chessBoard.getPiece(new SquarePair(row+1,col))==null){
                legalMoves.add(new SquarePair(row+1,col));
                if(movesDone==0 && chessBoard.getPiece(new SquarePair(row+2,col))==null){
                    legalMoves.add(new SquarePair(row+2,col));
                }
            }
            if(col+1<8) {
                SquarePair pair = new SquarePair(row + 1, col+1);
                if (chessBoard.getPiece(pair) != null) {
                    if(!chessBoard.getPiece(pair).getColor().equals(getColor())) {
                        legalMoves.add(pair);
                    }
                }
                pair = new SquarePair(row,col+1);
                if(row==4 && chessBoard.getPiece(pair)!=null){
                    if(!chessBoard.getPiece(pair).getColor().equals(getColor()) && chessBoard.getLastMove().equals(pair) && chessBoard.getPiece(pair).movesDone==1 && chessBoard.getPiece(pair).getPieceType().equals("pawn")){
                        specialMoves.add(new SquarePair(row+1,col+1));
                    }
                }
            }
            if(col-1>=0){
                SquarePair pair = new SquarePair(row + 1, col-1);
                if (chessBoard.getPiece(pair) != null) {
                    if(!chessBoard.getPiece(pair).getColor().equals(getColor())) {
                        legalMoves.add(pair);
                    }
                }
                pair = new SquarePair(row,col-1);
                if(row==4 && chessBoard.getPiece(pair)!=null){
                    if(!chessBoard.getPiece(pair).getColor().equals(getColor()) && chessBoard.getLastMove().equals(pair) && chessBoard.getPiece(pair).movesDone==1 && chessBoard.getPiece(pair).getPieceType().equals("pawn")){
                        specialMoves.add(new SquarePair(row+1,col-1));
                    }
                }
            }
        }
    }
}
