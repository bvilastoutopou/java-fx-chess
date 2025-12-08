package com.example.chess;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;

public class ChessController {
    @FXML
    private GridPane board;
    private final int SIZE = 8;
    private ChessBoard chessBoard = new ChessBoard();
    private SquarePair selectedSquare = null;
    private Pane[][] squares = new Pane[SIZE][SIZE];
    private boolean whitePlays = true;
    @FXML
    private Text playingText;
    public ChessController() throws FileNotFoundException {
    }

    @FXML
    public void initialize() {
        fillBoard();
        try {
            loadPieces();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void changeTurn(){
        if(whitePlays){
            playingText.setText("White Plays");
        }
        else{
            playingText.setText("Black Plays");
        }
    }

    @FXML
    private void fillBoard(){
        for(int row=0;row<SIZE;row++){
            for(int col=0;col<SIZE;col++){
                SquarePair pair = new SquarePair(row,col);
                Pane square = new Pane();
                square.setPrefSize(60,60);
                boolean isLight = (row+col)%2 == 0;
                String color;
                if(isLight){
                    color = "E8EDF9";
                }
                else{
                    color = "B7C0D8";
                }
                square.setStyle("-fx-background-color: " + color);
                addHover(square,color,pair);
                clickHandler(square,pair);
                squares[row][col] = square;
                board.add(square,col,row);
            }
        }
    }

    @FXML
    private void addHover(Pane square,String color,SquarePair pair){
        square.setOnMouseEntered(event -> {
            if(!pair.equals(selectedSquare) && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: green; -fx-border-width:2")  && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: red; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: purple; -fx-border-width:2")) {
                square.setStyle("-fx-background-color: " + color + "; -fx-border-color: yellow; -fx-border-width:2");
            }
        });

        square.setOnMouseExited(event -> {
            if(!pair.equals(selectedSquare) && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: green; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: red; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: purple; -fx-border-width:2")) {
                square.setStyle("-fx-background-color: " + color + ";");
            }
        });
    }

    @FXML
    public void loadPieces() throws FileNotFoundException {
        for(int row=0;row<SIZE;row++){
            for(int col=0;col<SIZE;col++){
                Piece piece = chessBoard.getPiece(new SquarePair(row,col));
                if(piece==null)continue;
                squares[row][col].getChildren().add(piece.getImageView());
            }
        }
    }

    @FXML
    private void changeBorderColor(String color,SquarePair pair){
        int row = pair.getRow();
        int col = pair.getCol();
        boolean isLight = (row+col)%2 == 0;
        String backgroundColor;
        if(isLight){
            backgroundColor = "E8EDF9";
        }
        else{
            backgroundColor = "B7C0D8";
        }
        if(color!=null) {
            squares[row][col].setStyle("-fx-background-color: " + backgroundColor + "; -fx-border-color: " + color + "; -fx-border-width:2");
        }else{
            squares[row][col].setStyle("-fx-background-color: " + backgroundColor + ";");
        }
    }

    @FXML
    private void clickHandler(Pane square,SquarePair pair){
        square.setOnMouseClicked(event -> {
            Piece selectedPiece = chessBoard.getPiece(pair);
            if(!pair.equals(selectedSquare) && selectedSquare!=null){
                changeBorderColor(null,selectedSquare);
                Piece oldPiece = chessBoard.getPiece(selectedSquare);
                for(SquarePair squarePair : oldPiece.legalMoves){
                    changeBorderColor(null,squarePair);
                }
                for(SquarePair squarePair : oldPiece.specialMoves){
                    changeBorderColor(null,squarePair);
                }
                try {
                    if(chessBoard.getPiece(selectedSquare).move(chessBoard,pair,squares)){
                        whitePlays = !whitePlays;
                        changeTurn();
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                selectedSquare = null;
                return;
            }
            if(whitePlays) {
                if (selectedPiece != null && !selectedPiece.getColor().equals("black")) {
                    selectedSquare = pair;
                    changeBorderColor("orange", selectedSquare);
                    selectedPiece.findLegalMoves(chessBoard);
                    for (SquarePair squarePair : selectedPiece.legalMoves) {
                        Piece pieceAtPos = chessBoard.getPiece(squarePair);
                        if (pieceAtPos == null) {
                            changeBorderColor("green", squarePair);
                        } else {
                            changeBorderColor("red", squarePair);
                        }
                    }
                    for (SquarePair squarePair : selectedPiece.specialMoves){
                        changeBorderColor("purple", squarePair);
                    }

                }
            }
            else{
                if (selectedPiece != null && !selectedPiece.getColor().equals("white")) {
                    selectedSquare = pair;
                    changeBorderColor("orange", selectedSquare);
                    selectedPiece.findLegalMoves(chessBoard);
                    for (SquarePair squarePair : selectedPiece.legalMoves) {
                        Piece pieceAtPos = chessBoard.getPiece(squarePair);
                        if (pieceAtPos == null) {
                            changeBorderColor("green", squarePair);
                        } else {
                            changeBorderColor("red", squarePair);
                        }
                    }
                    for (SquarePair squarePair : selectedPiece.specialMoves){
                        changeBorderColor("purple", squarePair);
                    }
                }
            }
        });
    }
}