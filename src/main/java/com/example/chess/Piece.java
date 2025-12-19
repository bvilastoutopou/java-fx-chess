package com.example.chess;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
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
    protected ArrayList<SquarePair> allowedMoves = new ArrayList<SquarePair>();
    protected ArrayList<SquarePair> allowedSpecialMoves = new ArrayList<SquarePair>();
    protected FileInputStream inputStream;
    protected Image img;
    protected ImageView imageView;
    protected String moveSound = getClass().getResource("/com/example/chess/sounds/move.mp3").toExternalForm();
    protected String captureSound = getClass().getResource("/com/example/chess/sounds/capture.mp3").toExternalForm();
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


    abstract public void findLegalMoves(ChessBoard chessBoard) throws FileNotFoundException;



    public boolean move(ChessBoard chessBoard, SquarePair destinationSquare, Pane[][] squares) throws FileNotFoundException {
        Media moveSoundMedia = new Media(moveSound);
        Media captureSoundMedia = new Media(captureSound);
        MediaPlayer moveSoundMediaPlayer = new MediaPlayer(moveSoundMedia);
        MediaPlayer captureSoundMediaPlayer = new MediaPlayer(captureSoundMedia);
        boolean found = false;
        boolean isSpecial = false;
        for(SquarePair pair : allowedMoves){
            if(pair.equals(destinationSquare)){
                found = true;
                break;
            }
        }
        if(!found){
            for(SquarePair pair : allowedSpecialMoves){
                if(pair.equals(destinationSquare)){
                    found = true;
                    isSpecial = true;
                    break;
                }
            }
        }
        if(found){
            boolean reset = false;
            if(pieceType.equals("pawn")){
                reset = true;
                chessBoard.resetHalfMoveCounter();
            }
            int oldRow = pos.getRow();
            int oldCol = pos.getCol();
            squares[oldRow][oldCol].getChildren().clear();
            chessBoard.setPiece(null,pos);
            int newRow = destinationSquare.getRow();
            int newCol = destinationSquare.getCol();
            if (chessBoard.getPiece(destinationSquare) != null) {
                captureSoundMediaPlayer.play();
                chessBoard.resetHalfMoveCounter();
                squares[newRow][newCol].getChildren().clear();
            }
            else{
                moveSoundMediaPlayer.play();
                if(!reset)chessBoard.incrementHalfMoveCounter();
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
                    }else if(getPieceType().equals("king")){
                        if(newCol==2) {
                            if(getColor().equals("white")) {
                                squares[newRow][0].getChildren().clear();
                                WhiteRook rook = (WhiteRook) chessBoard.getPiece(new SquarePair(newRow, 0));
                                rook.pos = new SquarePair(newRow, newCol + 1);
                                chessBoard.setPiece(null, new SquarePair(newRow, 0));
                                squares[newRow][newCol + 1].getChildren().add(rook.getImageView());
                                chessBoard.setPiece(rook, new SquarePair(newRow, newCol + 1));

                            }else{
                                squares[newRow][0].getChildren().clear();
                                BlackRook rook = (BlackRook) chessBoard.getPiece(new SquarePair(newRow, 0));
                                rook.pos = new SquarePair(newRow, newCol + 1);
                                chessBoard.setPiece(null, new SquarePair(newRow, 0));
                                squares[newRow][newCol + 1].getChildren().add(rook.getImageView());
                                chessBoard.setPiece(rook, new SquarePair(newRow, newCol + 1));
                            }
                        }
                        else{
                            if(getColor().equals("white")) {
                                squares[newRow][7].getChildren().clear();
                                WhiteRook rook = (WhiteRook) chessBoard.getPiece(new SquarePair(newRow, 7));
                                rook.pos = new SquarePair(newRow, newCol - 1);
                                chessBoard.setPiece(null, new SquarePair(newRow, 7));
                                squares[newRow][newCol - 1].getChildren().add(rook.getImageView());
                                chessBoard.setPiece(rook, new SquarePair(newRow, newCol - 1));
                            }else{
                                squares[newRow][7].getChildren().clear();
                                BlackRook rook = (BlackRook) chessBoard.getPiece(new SquarePair(newRow, 7));
                                rook.pos = new SquarePair(newRow, newCol - 1);
                                chessBoard.setPiece(null, new SquarePair(newRow, 7));
                                squares[newRow][newCol - 1].getChildren().add(rook.getImageView());
                                chessBoard.setPiece(rook, new SquarePair(newRow, newCol - 1));
                            }
                        }
                    }
                }
            }
            color = chessBoard.getPiece(destinationSquare).getColor();
            boolean whitePlays;
            if(color.equals("white")){
                whitePlays = false;
            }else{
                chessBoard.incrementFullMoveCounter();
                whitePlays = true;
            }
            chessBoard.setLastMove(destinationSquare);
            String key = chessBoard.getPositionKey(whitePlays);
            chessBoard.getRepetitionTable().put(key,chessBoard.getRepetitionTable().getOrDefault(key, 0) + 1);;
        }
        return found;
    }

    public boolean simulateMove(ChessBoard chessBoard, SquarePair destinationSquare,boolean isEnpassant) throws FileNotFoundException {

        SquarePair oldPos = new SquarePair(pos);
        Piece oldPiece;
        if(!isEnpassant) {
            oldPiece = chessBoard.getPiece(destinationSquare);
        }else{
            oldPiece = chessBoard.getPiece(new SquarePair(oldPos.getRow(),destinationSquare.getCol()));
        }

        if(isEnpassant){
            chessBoard.setPiece(null, new SquarePair(oldPos.getRow(),destinationSquare.getCol()));
        }

        chessBoard.setPiece(null, oldPos);
        chessBoard.setPiece(this, destinationSquare);
        this.pos = destinationSquare;



        SquarePair kingPos = chessBoard.findKing(getColor());
        King king = (King)chessBoard.getPiece(kingPos);
        boolean safe = !king.isChecked(chessBoard);


        chessBoard.setPiece(this, oldPos);
        this.pos = oldPos;
        if(!isEnpassant) {
            chessBoard.setPiece(oldPiece, destinationSquare);
        }else{
            chessBoard.setPiece(oldPiece, new SquarePair(oldPos.getRow(),destinationSquare.getCol()));
        }

        return safe;
    }

    public void findAllowedMoves(ChessBoard chessBoard) throws FileNotFoundException {
        findLegalMoves(chessBoard);
        allowedMoves.clear();
        allowedSpecialMoves.clear();
        for(SquarePair pair: legalMoves){
            simulateMove(chessBoard,pair,false);
            boolean res = simulateMove(chessBoard,pair,false);
            if(res){
                allowedMoves.add(pair);
            }
        }
        for(SquarePair pair: specialMoves){
            if(getPieceType().equals("king")){
                King king = (King)this;
                boolean isChecked = king.isChecked(chessBoard);
                if(!isChecked){
                    if(pair.equals(new SquarePair(pos.getRow(),2))){
                        boolean oneSquare = simulateMove(chessBoard,new SquarePair(pos.getRow(),3),false);
                        boolean twoSquare = simulateMove(chessBoard, pair,false);
                        if(oneSquare && twoSquare){
                            allowedSpecialMoves.add(pair);
                        }
                    }
                    else{
                        boolean oneSquare = simulateMove(chessBoard,new SquarePair(pos.getRow(),5),false);
                        boolean twoSquare = simulateMove(chessBoard, pair,false);
                        if(oneSquare && twoSquare){
                            allowedSpecialMoves.add(pair);
                        }
                    }
                }
            }
            else {
                int row = pos.getRow();
                boolean res;
                if(row==1 || row==6) {
                    res = simulateMove(chessBoard, pair,false);
                }else{
                    res = simulateMove(chessBoard, pair,true);
                }
                if (res) {
                    allowedSpecialMoves.add(pair);
                }
            }
        }
    }

}
