package com.example.chess;

public class Knight extends Piece{

    public Knight(SquarePair pos) {
        super(pos);
        pieceType = "knight";
    }

    @Override
    public void findLegalMoves(ChessBoard chessBoard){
        legalMoves.clear();
        int row = pos.getRow();
        int col = pos.getCol();
        if(row+2<8){
            if(col+1<8){
                SquarePair pair = new SquarePair(row+2,col+1);
                Piece pieceAtPos = chessBoard.getPiece(pair);
                if(pieceAtPos==null || !pieceAtPos.getColor().equals(getColor())) legalMoves.add(pair);

            }
            if(col-1>=0) {
                SquarePair pair = new SquarePair(row+2,col-1);
                Piece pieceAtPos = chessBoard.getPiece(pair);
                if(pieceAtPos==null || !pieceAtPos.getColor().equals(getColor())) legalMoves.add(pair);
            }
        }

        if(row+1<8){
            if(col+2<8){
                SquarePair pair = new SquarePair(row+1,col+2);
                Piece pieceAtPos = chessBoard.getPiece(pair);
                if(pieceAtPos==null || !pieceAtPos.getColor().equals(getColor())) legalMoves.add(pair);

            }
            if(col-2>=0) {
                SquarePair pair = new SquarePair(row+1,col-2);
                Piece pieceAtPos = chessBoard.getPiece(pair);
                if(pieceAtPos==null || !pieceAtPos.getColor().equals(getColor())) legalMoves.add(pair);
            }
        }

        if(row-2>=0){
            if(col+1<8){
                SquarePair pair = new SquarePair(row-2,col+1);
                Piece pieceAtPos = chessBoard.getPiece(pair);
                if(pieceAtPos==null || !pieceAtPos.getColor().equals(getColor())) legalMoves.add(pair);

            }
            if(col-1>=0) {
                SquarePair pair = new SquarePair(row-2,col-1);
                Piece pieceAtPos = chessBoard.getPiece(pair);
                if(pieceAtPos==null || !pieceAtPos.getColor().equals(getColor())) legalMoves.add(pair);
            }
        }

        if(row-1>=0){
            if(col+2<8){
                SquarePair pair = new SquarePair(row-1,col+2);
                Piece pieceAtPos = chessBoard.getPiece(pair);
                if(pieceAtPos==null || !pieceAtPos.getColor().equals(getColor())) legalMoves.add(pair);

            }
            if(col-2>=0) {
                SquarePair pair = new SquarePair(row-1,col-2);
                Piece pieceAtPos = chessBoard.getPiece(pair);
                if(pieceAtPos==null || !pieceAtPos.getColor().equals(getColor())) legalMoves.add(pair);
            }
        }
    }
}
