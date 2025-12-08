package com.example.chess;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

abstract public class Piece {
    protected int movesDone;
    protected SquarePair pos;
    private String color;
    protected String pieceType;
    protected ArrayList<SquarePair> legalMoves = new ArrayList<SquarePair>();
    protected ArrayList<SquarePair> specialMoves = new ArrayList<SquarePair>();
    protected FileInputStream inputStream;
    protected Image img;
    protected ImageView imageView;

    public Piece(SquarePair pos) {
        this.pos = pos;
        movesDone = 0;
    }

    public void setMovesDone(int movesDone) {
        this.movesDone = movesDone;
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

    public boolean move(ChessBoard chessBoard, SquarePair destinationSquare, Pane[][] squares) throws FileNotFoundException {
        boolean found = false;
        boolean isSpecial = false;
        for(SquarePair pair : legalMoves){
            if(pair.equals(destinationSquare)){
                found = true;
                break;
            }
        }
        if(!found){
            for(SquarePair pair : specialMoves){
                if(pair.equals(destinationSquare)){
                    found = true;
                    isSpecial = true;
                    break;
                }
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
            if(isSpecial){
                if(oldRow==3 || oldRow==4){
                    squares[oldRow][newCol].getChildren().clear();
                    chessBoard.setPiece(null,new SquarePair(oldRow,newCol));
                }
                if (newRow == 0 || newRow == 7) {
                    if(getPieceType().equals("pawn")){
                        if(getColor().equals("white")){
                            WhitePawn pawn = (WhitePawn)this;
                            String promoteTo = pawn.promote();
                            if(promoteTo.equals("queen")){
                                WhiteQueen queen = new WhiteQueen(pos);
                                queen.setMovesDone(movesDone);
                                chessBoard.setPiece(queen,pos);
                                squares[newRow][newCol].getChildren().clear();
                                squares[newRow][newCol].getChildren().add(queen.getImageView());
                            }
                            else if(promoteTo.equals("rook")){
                                WhiteRook rook = new WhiteRook(pos);
                                rook.setMovesDone(movesDone);
                                chessBoard.setPiece(rook,pos);
                                squares[newRow][newCol].getChildren().clear();
                                squares[newRow][newCol].getChildren().add(rook.getImageView());
                            }
                            else if(promoteTo.equals("bishop")){
                                WhiteBishop bishop = new WhiteBishop(pos);
                                bishop.setMovesDone(movesDone);
                                chessBoard.setPiece(bishop,pos);
                                squares[newRow][newCol].getChildren().clear();
                                squares[newRow][newCol].getChildren().add(bishop.getImageView());
                            }
                            else if(promoteTo.equals("knight")){
                                WhiteKnight knight = new WhiteKnight(pos);
                                knight.setMovesDone(movesDone);
                                chessBoard.setPiece(knight,pos);
                                squares[newRow][newCol].getChildren().clear();
                                squares[newRow][newCol].getChildren().add(knight.getImageView());
                            }
                        }else{
                            BlackPawn pawn = (BlackPawn)this;
                            String promoteTo = pawn.promote();
                            if(promoteTo.equals("queen")){
                                BlackQueen queen = new BlackQueen(pos);
                                queen.setMovesDone(movesDone);
                                chessBoard.setPiece(queen,pos);
                                squares[newRow][newCol].getChildren().clear();
                                squares[newRow][newCol].getChildren().add(queen.getImageView());
                            }
                            else if(promoteTo.equals("rook")){
                                BlackRook rook = new BlackRook(pos);
                                rook.setMovesDone(movesDone);
                                chessBoard.setPiece(rook,pos);
                                squares[newRow][newCol].getChildren().clear();
                                squares[newRow][newCol].getChildren().add(rook.getImageView());
                            }
                            else if(promoteTo.equals("bishop")){
                                BlackBishop bishop = new BlackBishop(pos);
                                bishop.setMovesDone(movesDone);
                                chessBoard.setPiece(bishop,pos);
                                squares[newRow][newCol].getChildren().clear();
                                squares[newRow][newCol].getChildren().add(bishop.getImageView());
                            }
                            else if(promoteTo.equals("knight")){
                                BlackKnight knight = new BlackKnight(pos);
                                knight.setMovesDone(movesDone);
                                chessBoard.setPiece(knight,pos);
                                squares[newRow][newCol].getChildren().clear();
                                squares[newRow][newCol].getChildren().add(knight.getImageView());
                            }
                        }
                    }
                }
            }
            chessBoard.setLastMove(destinationSquare);
        }
        return found;
    }
}
