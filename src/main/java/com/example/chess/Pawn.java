package com.example.chess;


import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Pawn extends Piece{
    public Pawn(SquarePair pos) {
        super(pos);
        pieceType = "pawn";
    }

    public String promote(){
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Pawn promotion");

        dialog.setOnCloseRequest(Event::consume);

        Button queen = new Button("Queen");
        Button rook = new Button("Rook");
        Button bishop = new Button("Bishop");
        Button knight = new Button("Knight");

        final String[] res = {null};

        queen.setOnAction(event -> {res[0]="queen";dialog.close();});
        rook.setOnAction(event -> {res[0]="rook";dialog.close();});
        bishop.setOnAction(event -> {res[0]="bishop";dialog.close();});
        knight.setOnAction(event -> {res[0]="knight";dialog.close();});

        HBox box = new HBox(10,queen,rook,bishop,knight);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(15));

        Scene scene = new Scene(box);
        dialog.setScene(scene);

        dialog.showAndWait();

        return res[0];
    }

    @Override
    public void findLegalMoves(ChessBoard chessBoard){
        legalMoves.clear();
        specialMoves.clear();
        int row = pos.getRow();
        int col = pos.getCol();
        if(getColor().equals("white")){
            if(chessBoard.getPiece(new SquarePair(row-1,col))==null){
                if(row-1>0) {
                    legalMoves.add(new SquarePair(row - 1, col));
                }else{
                    specialMoves.add(new SquarePair(row - 1, col));
                }
                if(movesDone==0 && chessBoard.getPiece(new SquarePair(row-2,col))==null){
                    legalMoves.add(new SquarePair(row-2,col));
                }
            }
            if(col+1<8) {
                SquarePair pair = new SquarePair(row - 1, col+1);
                if (chessBoard.getPiece(pair) != null) {
                    if(!chessBoard.getPiece(pair).getColor().equals(getColor())) {
                        if(row-1>0) {
                            legalMoves.add(pair);
                        }else{
                            specialMoves.add(pair);
                        }
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
                        if(row-1>0) {
                            legalMoves.add(pair);
                        }else{
                            specialMoves.add(pair);
                        }
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
                if(row+1<7) {
                    legalMoves.add(new SquarePair(row+1,col));
                }else{
                    specialMoves.add(new SquarePair(row+1,col));
                }
                if(movesDone==0 && chessBoard.getPiece(new SquarePair(row+2,col))==null){
                    legalMoves.add(new SquarePair(row+2,col));
                }
            }
            if(col+1<8) {
                SquarePair pair = new SquarePair(row + 1, col+1);
                if (chessBoard.getPiece(pair) != null) {
                    if(!chessBoard.getPiece(pair).getColor().equals(getColor())) {
                        if(row+1<7) {
                            legalMoves.add(pair);
                        }else{
                            specialMoves.add(pair);
                        }
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
                        if(row+1<7) {
                            legalMoves.add(pair);
                        }else{
                            specialMoves.add(pair);
                        }
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
