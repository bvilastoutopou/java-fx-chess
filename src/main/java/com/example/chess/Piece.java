package com.example.chess;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.util.ArrayList;

abstract public class Piece {
    private int movesDone;
    protected SquarePair pos;
    private String color;
    protected String pieceType;
    protected ArrayList<SquarePair> legalMoves = new ArrayList<SquarePair>();
    protected FileInputStream inputStream;
    protected Image img;
    protected ImageView imageView;

    public Piece(SquarePair pos) {
        this.pos = pos;
        movesDone = 0;
    }

    public Piece(){}

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPieceType() {
        return pieceType;
    }

    public ImageView getImageView() {
        return imageView;
    }

    abstract public void findLegalMoves(ChessBoard chessBoard);

    public boolean move(ChessBoard chessBoard, SquarePair destinationSquare, Pane[][] squares){
        boolean found = false;
        for(SquarePair pair : legalMoves){
            if(pair.equals(destinationSquare)){
                found = true;
                break;
            }
        }
        if(found){
            int oldRow = pos.getRow();
            int oldCol = pos.getCol();
            squares[oldRow][oldCol].getChildren().clear();
            chessBoard.setPiece(null,pos);
            int newRow = destinationSquare.getRow();
            int newCol = destinationSquare.getCol();
            if (chessBoard.getPiece(destinationSquare) != null) {
                squares[newRow][newCol].getChildren().clear();
            }
            squares[newRow][newCol].getChildren().add(imageView);
            chessBoard.setPiece(this, destinationSquare);
            pos = destinationSquare;
            movesDone++;
        }
        return found;
    }
}
