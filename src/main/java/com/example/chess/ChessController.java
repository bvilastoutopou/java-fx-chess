package com.example.chess;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;



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
    private boolean gameOver = false;
    @FXML
    Button newGameButton;
    @FXML
    Button drawButton;

    private String whiteWins = "C:\\Users\\capta\\Desktop\\all\\programs\\java\\java fx\\Ergasia\\Chess\\src\\main\\resources\\com\\example\\chess\\sounds\\whiteWins.mp3";
    private String blackWins = "C:\\Users\\capta\\Desktop\\all\\programs\\java\\java fx\\Ergasia\\Chess\\src\\main\\resources\\com\\example\\chess\\sounds\\blackWins.mp3";
    private String draw = "C:\\Users\\capta\\Desktop\\all\\programs\\java\\java fx\\Ergasia\\Chess\\src\\main\\resources\\com\\example\\chess\\sounds\\draw.mp3";
    private String check = "C:\\Users\\capta\\Desktop\\all\\programs\\java\\java fx\\Ergasia\\Chess\\src\\main\\resources\\com\\example\\chess\\sounds\\check.mp3";

    public ChessController() throws FileNotFoundException {
    }

    @FXML
    public void initialize() {
        String key = chessBoard.getPositionKey(whitePlays);
        chessBoard.getRepetitionTable().put(key,chessBoard.getRepetitionTable().put(key, 1));
        gameOver = false;
        newGame();
        draw();
        fillBoard();
        try {
            loadPieces();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    public void changeTurn() throws FileNotFoundException {
        if(whitePlays){
            boolean hasMoves = chessBoard.findAllMoves("white");
            if(!hasMoves){
                gameOver = true;
                SquarePair kingPos = chessBoard.findKing("white");
                King king = (King)chessBoard.getPiece(kingPos);
                boolean isChecked = king.isChecked(chessBoard);
                if(isChecked){
                    Media blackWinsSoundMedia = new Media(new File(blackWins).toURI().toString());
                    MediaPlayer blackWinsSoundMediaPlayer = new MediaPlayer(blackWinsSoundMedia);
                    blackWinsSoundMediaPlayer.play();
                    playingText.setText("Black Wins");
                    changeBorderColor("black",kingPos);
                }
                else{
                    Media drawSoundMedia = new Media(new File(draw).toURI().toString());
                    MediaPlayer drawSoundMediaPlayer = new MediaPlayer(drawSoundMedia);
                    drawSoundMediaPlayer.play();
                    playingText.setText("Stalemate");
                }
            }else {
                SquarePair kingPos = chessBoard.findKing("white");
                King king = (King)chessBoard.getPiece(kingPos);
                boolean isChecked = king.isChecked(chessBoard);
                if(isChecked) {
                    Media checkSoundMedia = new Media(new File(check).toURI().toString());
                    MediaPlayer checkSoundMediaPlayer = new MediaPlayer(checkSoundMedia);
                    checkSoundMediaPlayer.play();
                }
                playingText.setText("White Plays");
            }
        }
        else{
            boolean hasMoves = chessBoard.findAllMoves("black");
            if(!hasMoves) {
                gameOver = true;
                SquarePair kingPos = chessBoard.findKing("black");
                King king = (King) chessBoard.getPiece(kingPos);
                boolean isChecked = king.isChecked(chessBoard);
                if (isChecked) {
                    Media whiteWinsSoundMedia = new Media(new File(whiteWins).toURI().toString());
                    MediaPlayer whiteWinsSoundMediaPlayer = new MediaPlayer(whiteWinsSoundMedia);
                    whiteWinsSoundMediaPlayer.play();
                    playingText.setText("White Wins");
                    changeBorderColor("black",kingPos);
                } else {
                    playingText.setText("Stalemate");
                }
            }
            else{
                SquarePair kingPos = chessBoard.findKing("black");
                King king = (King)chessBoard.getPiece(kingPos);
                boolean isChecked = king.isChecked(chessBoard);
                if(isChecked) {
                    Media checkSoundMedia = new Media(new File(check).toURI().toString());
                    MediaPlayer checkSoundMediaPlayer = new MediaPlayer(checkSoundMedia);
                    checkSoundMediaPlayer.play();
                }
                playingText.setText("Black Plays");
            }
        }
        if(!gameOver){
            String key = chessBoard.getPositionKey(whitePlays);
            if(chessBoard.getHalfMoveCounter()>=100 || chessBoard.determineInsufficientMaterial() || chessBoard.getRepetitionTable().get(key)>=3){
                Media drawSoundMedia = new Media(new File(draw).toURI().toString());
                MediaPlayer drawSoundMediaPlayer = new MediaPlayer(drawSoundMedia);
                drawSoundMediaPlayer.play();
                gameOver = true;
                playingText.setText("     Draw");
            }
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
            if(!gameOver) {
                if (!pair.equals(selectedSquare) && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: green; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: red; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: purple; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: darkred; -fx-border-width:2")) {
                    square.setStyle("-fx-background-color: " + color + "; -fx-border-color: yellow; -fx-border-width:2");
                }
            }
        });

        square.setOnMouseExited(event -> {
            if(!gameOver) {
                if (!pair.equals(selectedSquare) && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: green; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: red; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: purple; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: darkred; -fx-border-width:2")) {
                    square.setStyle("-fx-background-color: " + color + ";");
                }
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
            if(!gameOver) {
                SquarePair whiteKingPos = chessBoard.findKing("white");
                SquarePair blackKingPos = chessBoard.findKing("black");
                WhiteKing whiteKing = (WhiteKing) chessBoard.getPiece(whiteKingPos);
                BlackKing blackKing = (BlackKing) chessBoard.getPiece(blackKingPos);
                try {
                    if (whiteKing.isChecked(chessBoard)) {
                        changeBorderColor("darkred", whiteKingPos);
                    } else {
                        changeBorderColor(null, whiteKingPos);
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (blackKing.isChecked(chessBoard)) {
                        changeBorderColor("darkred", blackKingPos);
                    } else {
                        changeBorderColor(null, blackKingPos);
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                Piece selectedPiece = chessBoard.getPiece(pair);
                if (!pair.equals(selectedSquare) && selectedSquare != null) {
                    changeBorderColor(null, selectedSquare);
                    Piece oldPiece = chessBoard.getPiece(selectedSquare);
                    for (SquarePair squarePair : new ArrayList<>(oldPiece.allowedMoves)) {
                        changeBorderColor(null, squarePair);
                    }
                    for (SquarePair squarePair : new ArrayList<>(oldPiece.specialMoves)) {
                        changeBorderColor(null, squarePair);

                    }
                    try {
                        if (chessBoard.getPiece(selectedSquare).move(chessBoard, pair, squares)) {
                            whitePlays = !whitePlays;
                            changeTurn();
                            if(gameOver)return;
                            for (int row = 0; row < SIZE; row++) {
                                for (int col = 0; col < SIZE; col++) {
                                    changeBorderColor(null, new SquarePair(row, col));
                                }
                            }
                            if (whiteKing.isChecked(chessBoard)) {
                                changeBorderColor("darkred", whiteKingPos);
                            } else {
                                changeBorderColor(null, whiteKingPos);
                            }
                            if (blackKing.isChecked(chessBoard)) {
                                changeBorderColor("darkred", blackKingPos);
                            } else {
                                changeBorderColor(null, blackKingPos);
                            }
                        }
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    selectedSquare = null;
                    try {
                        if (whiteKing.isChecked(chessBoard)) {
                            changeBorderColor("darkred", whiteKingPos);
                        } else {
                            changeBorderColor(null, whiteKingPos);
                        }
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        if (blackKing.isChecked(chessBoard)) {
                            changeBorderColor("darkred", blackKingPos);
                        } else {
                            changeBorderColor(null, blackKingPos);
                        }
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }
                if (whitePlays) {
                    if (selectedPiece != null && !selectedPiece.getColor().equals("black")) {
                        selectedSquare = pair;
                        changeBorderColor("orange", selectedSquare);
                        try {
                            selectedPiece.findAllowedMoves(chessBoard);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        for (SquarePair squarePair : selectedPiece.allowedMoves) {
                            Piece pieceAtPos = chessBoard.getPiece(squarePair);
                            if (pieceAtPos == null) {
                                changeBorderColor("green", squarePair);
                            } else {
                                changeBorderColor("red", squarePair);
                            }
                        }
                        for (SquarePair squarePair : selectedPiece.allowedSpecialMoves) {
                            changeBorderColor("purple", squarePair);
                        }

                    }

                } else {
                    if (selectedPiece != null && !selectedPiece.getColor().equals("white")) {
                        selectedSquare = pair;
                        changeBorderColor("orange", selectedSquare);
                        try {
                            selectedPiece.findAllowedMoves(chessBoard);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        for (SquarePair squarePair : selectedPiece.allowedMoves) {
                            Piece pieceAtPos = chessBoard.getPiece(squarePair);
                            if (pieceAtPos == null) {
                                changeBorderColor("green", squarePair);
                            } else {
                                changeBorderColor("red", squarePair);
                            }
                        }
                        for (SquarePair squarePair : selectedPiece.allowedSpecialMoves) {
                            changeBorderColor("purple", squarePair);
                        }
                    }
                }
            }
        });
    }

    public void newGame(){
        newGameButton.setOnMouseClicked(event ->{
            try {
                chessBoard = new ChessBoard();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            selectedSquare = null;
            squares = new Pane[SIZE][SIZE];
            whitePlays = true;
            gameOver = false;
            playingText.setText("White Plays");
            initialize();
        });
    }

    public void draw(){
        drawButton.setOnMouseClicked(event -> {
            if(!gameOver){
            Media drawSoundMedia = new Media(new File(draw).toURI().toString());
            MediaPlayer drawSoundMediaPlayer = new MediaPlayer(drawSoundMedia);
            drawSoundMediaPlayer.play();
            gameOver = true;
            playingText.setText("     Draw");
            }
        });
    }
}