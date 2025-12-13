package com.example.chess;

import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class ChessController {
    @FXML
    private GridPane board;
    private final int SIZE = 8;
    private ChessBoard chessBoard = new ChessBoard();
    private SquarePair selectedSquare = null;
    private Pane[][] squares = new Pane[SIZE][SIZE];
    private boolean whitePlays = true;
    private String lightColor;
    private String darkColor;
    private int minutes;
    private int blackSeconds;
    private int whiteSeconds;
    private Timeline whiteTimeline;
    private Timeline blackTimeline;
    ResourceBundle bundle;

    @FXML
    private Text playingText;
    private boolean gameOver = false;
    @FXML
    Button newGameButton;
    @FXML
    Button drawButton;
    @FXML
    ImageView backArrow;
    @FXML
    Text back;
    @FXML
    Text chess;
    @FXML
    Text game;
    @FXML
    Text blackTimer;
    @FXML
    Text whiteTimer;
    @FXML
    Text blackTime;
    @FXML
    Text whiteTime;

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
        Language.setLocale(SettingsManager.get("language"));
        bundle = Language.getBundle();
        applyLanguage();
        newGame();
        draw();
        fillBoard();
        backArrowHandler();
        try {
            loadPieces();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Select timer");

        dialog.setOnCloseRequest(Event::consume);

        Button classic = new Button("Classic");
        Button rapid = new Button("Rapid");
        Button blitz = new Button("Blitz");
        Button bullet = new Button("Bullet");

        final String[] res = {null};

        classic.setOnAction(event -> {res[0]="classic";dialog.close();});
        rapid.setOnAction(event -> {res[0]="rapid";dialog.close();});
        blitz.setOnAction(event -> {res[0]="blitz";dialog.close();});
        bullet.setOnAction(event -> {res[0]="bullet";dialog.close();});
        HBox box = new HBox(10,classic,rapid,blitz,bullet);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(15));

        Scene scene = new Scene(box);
        dialog.setScene(scene);
        dialog.showAndWait();

        if(res[0].equals("classic")){
            minutes = 30;
        }else if(res[0].equals("rapid")){
            minutes = 10;
        }else if(res[0].equals("blitz")){
            minutes = 3;
        }
        else{
            minutes = 1;
        }
        whiteSeconds = minutes*60;
        blackSeconds = minutes*60;
        updateTimerText(blackTime,blackSeconds);
        updateTimerText(whiteTime,whiteSeconds);
        startWhiteTimer();
    }

    private int getTheme() {
        return Integer.parseInt(SettingsManager.get("theme"));
    }



    public void getColors(){
        int theme = getTheme();
        if(theme ==0){
            lightColor = "#E8EDF9";
            darkColor = "#B7C0D8";
        }else if(theme ==1){
            lightColor = "#EEEED2";
            darkColor = "#769656";
        }else if(theme ==2){
            lightColor = "#F0D9B5";
            darkColor = "#B58863";
        }
        else if(theme ==3){
            lightColor = "#E0E0FF";
            darkColor = "#4A6FA5";
        }
        else if(theme ==4){
            lightColor = "#DFF7F6";
            darkColor = "#4AA6A8";
        }
        else if(theme ==5){
            lightColor = "#FFE4F0";
            darkColor = "#D15A8E";
        }
        else if(theme ==6){
            lightColor = "#FFE7D9";
            darkColor = "#D26B41";
        }
        else{
            lightColor = "#FDE3E3";
            darkColor = "#A33A3A";
        }
    }

    @FXML
    public void changeTurn() throws FileNotFoundException {
        if(whitePlays){
            startWhiteTimer();
            boolean hasMoves = chessBoard.findAllMoves("white");
            if(!hasMoves){
                gameOver = true;
                stopTimers();
                SquarePair kingPos = chessBoard.findKing("white");
                King king = (King)chessBoard.getPiece(kingPos);
                boolean isChecked = king.isChecked(chessBoard);
                if(isChecked){
                    Media blackWinsSoundMedia = new Media(new File(blackWins).toURI().toString());
                    MediaPlayer blackWinsSoundMediaPlayer = new MediaPlayer(blackWinsSoundMedia);
                    blackWinsSoundMediaPlayer.play();
                    playingText.setText(bundle.getString("result.blackwins"));
                    changeBorderColor("black",kingPos);
                }
                else{
                    Media drawSoundMedia = new Media(new File(draw).toURI().toString());
                    MediaPlayer drawSoundMediaPlayer = new MediaPlayer(drawSoundMedia);
                    drawSoundMediaPlayer.play();
                    playingText.setText(bundle.getString("result.stalemate"));
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
                playingText.setText(bundle.getString("status.whiteplays"));
            }
        }
        else{
            startBlackTimer();
            boolean hasMoves = chessBoard.findAllMoves("black");
            if(!hasMoves) {
                gameOver = true;
                stopTimers();
                SquarePair kingPos = chessBoard.findKing("black");
                King king = (King) chessBoard.getPiece(kingPos);
                boolean isChecked = king.isChecked(chessBoard);
                if (isChecked) {
                    Media whiteWinsSoundMedia = new Media(new File(whiteWins).toURI().toString());
                    MediaPlayer whiteWinsSoundMediaPlayer = new MediaPlayer(whiteWinsSoundMedia);
                    whiteWinsSoundMediaPlayer.play();
                    playingText.setText(bundle.getString("result.whitewins"));
                    changeBorderColor("black",kingPos);
                } else {
                    playingText.setText(bundle.getString("result.stalemate"));
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
                playingText.setText(bundle.getString("status.blackplays"));
            }
        }
        if(!gameOver){
            String key = chessBoard.getPositionKey(whitePlays);
            if(chessBoard.getHalfMoveCounter()>=100 || chessBoard.determineInsufficientMaterial() || chessBoard.getRepetitionTable().get(key)>=3){
                Media drawSoundMedia = new Media(new File(draw).toURI().toString());
                MediaPlayer drawSoundMediaPlayer = new MediaPlayer(drawSoundMedia);
                stopTimers();
                drawSoundMediaPlayer.play();
                gameOver = true;
                playingText.setText(bundle.getString("button.draw"));
            }
        }
    }


    public void refreshTheme() {
        fillBoard();
        try {
            loadPieces();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void fillBoard(){
        board.getChildren().clear();
        squares = new Pane[SIZE][SIZE];
        for(int row=0;row<SIZE;row++){
            for(int col=0;col<SIZE;col++){
                SquarePair pair = new SquarePair(row,col);
                Pane square = new Pane();
                getColors();
                square.setPrefSize(60,60);
                boolean isLight = (row+col)%2 == 0;
                String color;
                if(isLight){
                    color = lightColor;
                }
                else{
                    color = darkColor;
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
                if (!pair.equals(selectedSquare) && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: lightgreen; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: red; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: purple; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: darkred; -fx-border-width:2")) {
                    square.setStyle("-fx-background-color: " + color + "; -fx-border-color: yellow; -fx-border-width:2");
                }
            }
        });

        square.setOnMouseExited(event -> {
            if(!gameOver) {
                if (!pair.equals(selectedSquare) && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: lightgreen; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: red; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: purple; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: darkred; -fx-border-width:2")) {
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
            backgroundColor = lightColor;
        }
        else{
            backgroundColor = darkColor;
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
                                changeBorderColor("lightgreen", squarePair);
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
                                changeBorderColor("lightgreen", squarePair);
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
            playingText.setText(bundle.getString("status.whiteplays"));
            initialize();
        });
    }

    public void applyLanguage(){
        back.setText(bundle.getString("button.back"));
        whiteTimer.setText(bundle.getString("label.whitetimer"));
        blackTimer.setText(bundle.getString("label.blacktimer"));
        chess.setText(bundle.getString("title.chess"));
        game.setText(bundle.getString("title.game"));
        drawButton.setText(bundle.getString("button.draw"));
        newGameButton.setText(bundle.getString("button.newgame"));
        playingText.setText(bundle.getString("status.whiteplays"));
    }

    public void draw(){
        drawButton.setOnMouseClicked(event -> {
            if(!gameOver){
                stopTimers();
                Media drawSoundMedia = new Media(new File(draw).toURI().toString());
                MediaPlayer drawSoundMediaPlayer = new MediaPlayer(drawSoundMedia);
                drawSoundMediaPlayer.play();
                gameOver = true;
                playingText.setText(bundle.getString("button.draw"));
            }
        });
    }



    public void backArrowHandler(){
        backArrow.setOnMouseClicked(event -> {
            try {
                switchToMainMenu(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void stopTimers() {
        if (whiteTimeline != null) whiteTimeline.stop();
        if (blackTimeline != null) blackTimeline.stop();
    }

    private void startWhiteTimer(){
        stopTimers();
        whiteTimeline = new Timeline(new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1),
                event -> {
                    whiteSeconds--;
                    updateTimerText(whiteTime, whiteSeconds);
                    if (whiteSeconds <= 0) {
                        stopTimers();
                        gameOver = true;
                        Media blackWinsSoundMedia = new Media(new File(blackWins).toURI().toString());
                        MediaPlayer blackWinsSoundMediaPlayer = new MediaPlayer(blackWinsSoundMedia);
                        blackWinsSoundMediaPlayer.play();
                        playingText.setText(bundle.getString("result.blackwins"));
                    }

                })
        );
        whiteTimeline.setCycleCount(Timeline.INDEFINITE);
        whiteTimeline.play();
    }

    private void startBlackTimer(){
        stopTimers();
        blackTimeline = new Timeline(new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1),
                event -> {
                    blackSeconds--;
                    updateTimerText(blackTime, blackSeconds);
                    if (blackSeconds <= 0) {
                        stopTimers();
                        gameOver = true;
                        Media whiteWinsSoundMedia = new Media(new File(whiteWins).toURI().toString());
                        MediaPlayer whiteWinsSoundMediaPlayer = new MediaPlayer(whiteWinsSoundMedia);
                        whiteWinsSoundMediaPlayer.play();
                        playingText.setText(bundle.getString("result.whitewins"));
                    }

                })
        );
        blackTimeline.setCycleCount(Timeline.INDEFINITE);
        blackTimeline.play();
    }

    private void updateTimerText(Text timerText, int seconds) {
        int min = seconds / 60;
        int sec = seconds % 60;
        timerText.setText(String.format("%02d:%02d", min, sec));
    }

    public void switchToMainMenu(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main-menu.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }


}