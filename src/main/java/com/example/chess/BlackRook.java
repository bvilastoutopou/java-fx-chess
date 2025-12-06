package com.example.chess;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BlackRook extends Piece {

    public BlackRook(SquarePair pos) throws FileNotFoundException {
        super(pos);
        pieceType = "Rook";
        setColor("black");
        inputStream = new FileInputStream("C:\\Users\\capta\\Desktop\\all\\programs\\java\\java fx\\Ergasia\\Chess\\src\\main\\resources\\com\\example\\chess\\pieces\\black-rook.png");
        img = new Image(inputStream);
        imageView = new ImageView(img);
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);
    }



    @Override
    public void findLegalMoves(ChessBoard chessBoard){
        legalMoves.clear();
        int row = pos.getRow();
        int col = pos.getCol();

        for(int i=row+1;i<8;i++){
            SquarePair pair = new SquarePair(i,col);
            Piece pieceAtPos = chessBoard.getPiece(pair);
            if(pieceAtPos==null) legalMoves.add(pair);
            else if(pieceAtPos.getColor().equals("black")){
                break;
            }
            else if(pieceAtPos.getColor().equals("white")){
                legalMoves.add(pair);
                break;
            }
        }

        for(int i=row-1;i>=0;i--){
            SquarePair pair = new SquarePair(i,col);
            Piece pieceAtPos = chessBoard.getPiece(pair);
            if(pieceAtPos==null) legalMoves.add(pair);
            else if(pieceAtPos.getColor().equals("black")){
                break;
            }
            else if(pieceAtPos.getColor().equals("white")){
                legalMoves.add(pair);
                break;
            }
        }

        for(int i=col+1;i<8;i++){
            SquarePair pair = new SquarePair(row,i);
            Piece pieceAtPos = chessBoard.getPiece(pair);
            if(pieceAtPos==null) legalMoves.add(pair);
            else if(pieceAtPos.getColor().equals("black")){
                break;
            }
            else if(pieceAtPos.getColor().equals("white")){
                legalMoves.add(pair);
                break;
            }
        }

        for(int i=col-1;i>=0;i--){
            SquarePair pair = new SquarePair(row,i);
            Piece pieceAtPos = chessBoard.getPiece(pair);
            if(pieceAtPos==null) legalMoves.add(pair);
            else if(pieceAtPos.getColor().equals("black")){
                break;
            }
            else if(pieceAtPos.getColor().equals("white")){
                legalMoves.add(pair);
                break;
            }
        }
    }
}
