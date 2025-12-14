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
    ChessBoard chessBoard = new ChessBoard();
    private SquarePair selectedSquare = null;
    private Pane[][] squares = new Pane[SIZE][SIZE];
    boolean whitePlays;
    private String lightColor;
    private String darkColor;
    private int minutes;
    int blackSeconds;
    int whiteSeconds;
    private Timeline whiteTimeline;
    private Timeline blackTimeline;
    private ResourceBundle bundle;

    private String checkmateColor;
    private String checkColor;
    private String selectionColor;
    private String hoverColor;
    private String allowedColor;
    private String specialColor;
    private String captureColor;
    boolean resetTimers;

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

    private String whiteWinsSound = getClass().getResource("/com/example/chess/sounds/whiteWins.mp3").toExternalForm();

    private String blackWinsSound = getClass().getResource("/com/example/chess/sounds/blackWins.mp3").toExternalForm();

    private String drawSound = getClass().getResource("/com/example/chess/sounds/draw.mp3").toExternalForm();

    private String checkSound = getClass().getResource("/com/example/chess/sounds/check.mp3").toExternalForm();



    public ChessController() throws FileNotFoundException {
    }

    @FXML
    public void initialize() throws FileNotFoundException {
        String key = chessBoard.getPositionKey(whitePlays);
        chessBoard.getRepetitionTable().put(key, 1);
        gameOver = false;
        String fen = SettingsManager.get("FEN");
        chessBoard.fenLoader(fen);
        if(!fen.equals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1")){
            resetTimers = false;
        }else{
            resetTimers = true;
        }
        String[] fenParts = fen.split(" ");
        whitePlays = fenParts[1].equals("w");
        Language.setLocale(SettingsManager.get("language"));
        bundle = Language.getBundle();

        applyLanguage();
        if(whitePlays){
            playingText.setText(bundle.getString("status.whiteplays"));
        }else{
            playingText.setText(bundle.getString("status.blackplays"));
        }
        newGame();
        draw();
        fillBoard();
        backArrowHandler();
        try {
            loadPieces();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        if(resetTimers) {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle(bundle.getString("timer.select"));

            dialog.setOnCloseRequest(Event::consume);

            Button classic = new Button(bundle.getString("timer.classic"));
            Button rapid = new Button(bundle.getString("timer.rapid"));
            Button blitz = new Button(bundle.getString("timer.blitz"));
            Button bullet = new Button(bundle.getString("timer.bullet"));

            final String[] res = {null};

            classic.setOnAction(event -> {
                res[0] = "classic";
                dialog.close();
            });
            rapid.setOnAction(event -> {
                res[0] = "rapid";
                dialog.close();
            });
            blitz.setOnAction(event -> {
                res[0] = "blitz";
                dialog.close();
            });
            bullet.setOnAction(event -> {
                res[0] = "bullet";
                dialog.close();
            });
            HBox box = new HBox(10, classic, rapid, blitz, bullet);
            box.setAlignment(Pos.CENTER);
            box.setPadding(new Insets(15));

            Scene scene = new Scene(box);
            dialog.setScene(scene);
            dialog.showAndWait();

            if (res[0].equals("classic")) {
                minutes = 30;
            } else if (res[0].equals("rapid")) {
                minutes = 10;
            } else if (res[0].equals("blitz")) {
                minutes = 3;
            } else {
                minutes = 1;
            }
            whiteSeconds = minutes * 60;
            blackSeconds = minutes * 60;
            updateTimerText(blackTime, blackSeconds);
            updateTimerText(whiteTime, whiteSeconds);
            startWhiteTimer();
        }else{

            whiteSeconds = Integer.parseInt(SettingsManager.get("white.seconds"));
            blackSeconds = Integer.parseInt(SettingsManager.get("black.seconds"));
            updateTimerText(blackTime, blackSeconds);
            updateTimerText(whiteTime, whiteSeconds);
            if(whitePlays){
                startWhiteTimer();
            }else{
                startBlackTimer();
            }
        }
    }

    private int getTheme() {
        return Integer.parseInt(SettingsManager.get("theme"));
    }



    public void getColors(){
        int theme = getTheme();
        if(theme ==0){
            lightColor = "#E8EDF9";
            darkColor = "#B7C0D8";
            checkmateColor = "black";
            checkColor = "darkred";
            selectionColor = "orange";
            hoverColor = "yellow";
            allowedColor = "green";
            specialColor = "purple";
            captureColor = "red";
        }else if(theme ==1){
            lightColor = "#EEEED2";
            darkColor = "#769656";
            checkmateColor = "black";
            checkColor = "darkred";
            selectionColor = "orange";
            hoverColor = "yellow";
            allowedColor = "blue";
            specialColor = "purple";
            captureColor = "red";
        }else if(theme ==2){
            lightColor = "#F0D9B5";
            darkColor = "#B58863";
            checkmateColor = "black";
            checkColor = "darkred";
            selectionColor = "orange";
            hoverColor = "yellow";
            allowedColor = "green";
            specialColor = "blue";
            captureColor = "red";
        }
        else if(theme ==3){
            lightColor = "#E0E0FF";
            darkColor = "#4A6FA5";
            checkmateColor = "black";
            checkColor = "darkred";
            selectionColor = "orange";
            hoverColor = "yellow";
            allowedColor = "lightgreen";
            specialColor = "purple";
            captureColor = "red";
        }
        else if(theme ==4){
            lightColor = "#DFF7F6";
            darkColor = "#4AA6A8";
            checkmateColor = "black";
            checkColor = "darkred";
            selectionColor = "orange";
            hoverColor = "yellow";
            allowedColor = "darkblue";
            specialColor = "purple";
            captureColor = "red";
        }
        else if(theme ==5){
            lightColor = "#FFE4F0";
            darkColor = "#D15A8E";
            checkmateColor = "black";
            checkColor = "blue";
            selectionColor = "lightblue";
            hoverColor = "yellow";
            allowedColor = "green";
            specialColor = "darkblue";
            captureColor = "red";
        }
        else if(theme ==6){
            lightColor = "#FFE7D9";
            darkColor = "#D26B41";
            checkmateColor = "black";
            checkColor = "darkred";
            selectionColor = "blue";
            hoverColor = "yellow";
            allowedColor = "green";
            specialColor = "purple";
            captureColor = "red";
        }
        else{
            lightColor = "#FDE3E3";
            darkColor = "#A33A3A";
            checkmateColor = "black";
            checkColor = "blue";
            selectionColor = "orange";
            hoverColor = "yellow";
            allowedColor = "lightgreen";
            specialColor = "lightblue";
            captureColor = "red";
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
                    Media blackWinsSoundMedia = new Media(blackWinsSound);
                    MediaPlayer blackWinsSoundMediaPlayer = new MediaPlayer(blackWinsSoundMedia);
                    blackWinsSoundMediaPlayer.play();
                    playingText.setText(bundle.getString("result.blackwins"));
                    changeBorderColor(checkmateColor,kingPos);
                }
                else{
                    Media drawSoundMedia = new Media(drawSound);
                    MediaPlayer drawSoundMediaPlayer = new MediaPlayer(drawSoundMedia);
                    drawSoundMediaPlayer.play();
                    playingText.setText(bundle.getString("result.stalemate"));
                }
            }else {
                SquarePair kingPos = chessBoard.findKing("white");
                King king = (King)chessBoard.getPiece(kingPos);
                boolean isChecked = king.isChecked(chessBoard);
                if(isChecked) {
                    Media checkSoundMedia = new Media(checkSound);
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
                    Media whiteWinsSoundMedia = new Media(whiteWinsSound);
                    MediaPlayer whiteWinsSoundMediaPlayer = new MediaPlayer(whiteWinsSoundMedia);
                    whiteWinsSoundMediaPlayer.play();
                    playingText.setText(bundle.getString("result.whitewins"));
                    changeBorderColor(checkmateColor,kingPos);
                } else {
                    playingText.setText(bundle.getString("result.stalemate"));
                }
            }
            else{
                SquarePair kingPos = chessBoard.findKing("black");
                King king = (King)chessBoard.getPiece(kingPos);
                boolean isChecked = king.isChecked(chessBoard);
                if(isChecked) {
                    Media checkSoundMedia = new Media(checkSound);
                    MediaPlayer checkSoundMediaPlayer = new MediaPlayer(checkSoundMedia);
                    checkSoundMediaPlayer.play();
                }
                playingText.setText(bundle.getString("status.blackplays"));
            }
        }
        if(!gameOver){
            String key = chessBoard.getPositionKey(whitePlays);
            if(chessBoard.getHalfMoveCounter()>=100 || chessBoard.determineInsufficientMaterial() || chessBoard.getRepetitionTable().get(key)>=3){
                Media drawSoundMedia = new Media(drawSound);
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
        SquarePair whiteKingPos = chessBoard.findKing("white");
        SquarePair blackKingPos = chessBoard.findKing("black");
        WhiteKing whiteKing = (WhiteKing) chessBoard.getPiece(whiteKingPos);
        BlackKing blackKing = (BlackKing) chessBoard.getPiece(blackKingPos);

        try {
            if (whiteKing.isChecked(chessBoard)) {
                changeBorderColor(checkColor, whiteKingPos);
            } else {
                changeBorderColor(null, whiteKingPos);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            if (blackKing.isChecked(chessBoard)) {
                changeBorderColor(checkColor, blackKingPos);
            } else {
                changeBorderColor(null, blackKingPos);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void addHover(Pane square,String color,SquarePair pair){
        square.setOnMouseEntered(event -> {
            if(!gameOver) {
                if (!pair.equals(selectedSquare) && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: "+  allowedColor +"; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: "+ captureColor +"; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: " + specialColor +"; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: "+checkColor+"; -fx-border-width:2")) {
                    changeBorderColor(hoverColor, pair);
                }
            }
        });

        square.setOnMouseExited(event -> {
            if(!gameOver) {
                if (!pair.equals(selectedSquare) && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: "+  allowedColor +"; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: "+ captureColor +"; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: " + specialColor +"; -fx-border-width:2") && !square.getStyle().equals("-fx-background-color: " + color + "; -fx-border-color: "+checkColor+"; -fx-border-width:2")) {
                    changeBorderColor(null,pair);
                }
            }
        });
    }

    public void exitWarning(Stage stage){
        stage.setOnCloseRequest(windowEvent ->{
            stopTimers();
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle(bundle.getString("confirm.exit.title"));

            Button save = new Button(bundle.getString("confirm.exit.save"));
            Button noSave = new Button(bundle.getString("confirm.exit.nosave"));

            final String[] res = {null};

            dialog.setOnCloseRequest(windowEvent2 ->{
                res[0] = "cancel";
                dialog.close();
            });

            save.setOnAction(actionEvent -> {
                res[0] = "save";
                dialog.close();
            });
            noSave.setOnAction(actionEvent -> {
                res[0] = "noSave";
                dialog.close();
            });


            HBox box = new HBox(10, save, noSave);
            box.setAlignment(Pos.CENTER);
            box.setPadding(new Insets(15));

            Scene scene = new Scene(box);
            dialog.setScene(scene);
            dialog.showAndWait();

            if(res[0].equals("save")){
                String fen = chessBoard.fenGenerator(whitePlays);
                SettingsManager.set("FEN", fen);
                SettingsManager.set("white.seconds",Integer.toString(whiteSeconds));
                SettingsManager.set("black.seconds",Integer.toString(blackSeconds));
                stage.close();
            }else if(res[0].equals("noSave")){
                resetTimers = true;
                stage.close();
            }else{

                if(whitePlays){
                    startWhiteTimer();
                }else{
                    startBlackTimer();
                }
                windowEvent.consume();
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
                        changeBorderColor(checkColor, whiteKingPos);
                    } else {
                        changeBorderColor(null, whiteKingPos);
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                try {
                    if (blackKing.isChecked(chessBoard)) {
                        changeBorderColor(checkColor, blackKingPos);
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
                                changeBorderColor(checkColor, whiteKingPos);
                            } else {
                                changeBorderColor(null, whiteKingPos);
                            }
                            if (blackKing.isChecked(chessBoard)) {
                                changeBorderColor(checkColor, blackKingPos);
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
                            changeBorderColor(checkColor, whiteKingPos);
                        } else {
                            changeBorderColor(null, whiteKingPos);
                        }
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        if (blackKing.isChecked(chessBoard)) {
                            changeBorderColor(checkColor, blackKingPos);
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
                        changeBorderColor(selectionColor, selectedSquare);
                        try {
                            selectedPiece.findAllowedMoves(chessBoard);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        for (SquarePair squarePair : selectedPiece.allowedMoves) {
                            Piece pieceAtPos = chessBoard.getPiece(squarePair);
                            if (pieceAtPos == null) {
                                changeBorderColor(allowedColor, squarePair);
                            } else {
                                changeBorderColor(captureColor, squarePair);
                            }
                        }
                        for (SquarePair squarePair : selectedPiece.allowedSpecialMoves) {
                            changeBorderColor(specialColor, squarePair);
                        }

                    }

                } else {
                    if (selectedPiece != null && !selectedPiece.getColor().equals("white")) {
                        selectedSquare = pair;
                        changeBorderColor(selectionColor, selectedSquare);
                        try {
                            selectedPiece.findAllowedMoves(chessBoard);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        for (SquarePair squarePair : selectedPiece.allowedMoves) {
                            Piece pieceAtPos = chessBoard.getPiece(squarePair);
                            if (pieceAtPos == null) {
                                changeBorderColor(allowedColor, squarePair);
                            } else {
                                changeBorderColor(captureColor, squarePair);
                            }
                        }
                        for (SquarePair squarePair : selectedPiece.allowedSpecialMoves) {
                            changeBorderColor(specialColor, squarePair);
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
            stopTimers();
            resetTimers = true;
            selectedSquare = null;
            squares = new Pane[SIZE][SIZE];
            whitePlays = true;
            gameOver = false;
            playingText.setText(bundle.getString("status.whiteplays"));
            SettingsManager.set("FEN","rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
            try {
                chessBoard.setChessBoard();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            try {
                initialize();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

        newGameButton.setOnMouseEntered(event -> {
            newGameButton.getStyleClass().add("gray-button");
        });

        newGameButton.setOnMouseExited(event -> {
            newGameButton.getStyleClass().remove("gray-button");
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
                Media drawSoundMedia = new Media(drawSound);
                MediaPlayer drawSoundMediaPlayer = new MediaPlayer(drawSoundMedia);
                drawSoundMediaPlayer.play();
                gameOver = true;
                playingText.setText(bundle.getString("button.draw"));
            }
        });

        drawButton.setOnMouseEntered(event -> {
            drawButton.getStyleClass().add("gray-button");
        });

        drawButton.setOnMouseExited(event -> {
            drawButton.getStyleClass().remove("gray-button");
        });
    }



    public void backArrowHandler(){
        backArrow.setOnMouseClicked(event -> {
            try {
                stopTimers();
                Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.setTitle(bundle.getString("confirm.back.title"));

                Button save = new Button(bundle.getString("confirm.back.save"));
                Button noSave = new Button(bundle.getString("confirm.back.nosave"));

                final String[] res = {null};

                dialog.setOnCloseRequest(windowEvent ->{
                    res[0] = "cancel";
                    dialog.close();
                });

                save.setOnAction(actionEvent -> {
                    res[0] = "save";
                    dialog.close();
                });
                noSave.setOnAction(actionEvent -> {
                    res[0] = "noSave";
                    dialog.close();
                });


                HBox box = new HBox(10, save, noSave);
                box.setAlignment(Pos.CENTER);
                box.setPadding(new Insets(15));

                Scene scene = new Scene(box);
                dialog.setScene(scene);
                dialog.showAndWait();

                if(res[0].equals("save")){
                    String fen = chessBoard.fenGenerator(whitePlays);
                    SettingsManager.set("FEN", fen);
                    SettingsManager.set("white.seconds",Integer.toString(whiteSeconds));
                    SettingsManager.set("black.seconds",Integer.toString(blackSeconds));
                    switchToMainMenu(event);
                }else if(res[0].equals("noSave")){
                    resetTimers = true;
                    switchToMainMenu(event);
                }else{
                    if(whitePlays){
                        startWhiteTimer();
                    }else{
                        startBlackTimer();
                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void stopTimers() {


        if (whiteTimeline != null) whiteTimeline.stop();
        if (blackTimeline != null) blackTimeline.stop();
    }

    void startWhiteTimer(){
        stopTimers();
        whiteTimeline = new Timeline(new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1),
                event -> {
                    whiteSeconds--;
                    updateTimerText(whiteTime, whiteSeconds);
                    if (whiteSeconds <= 0) {
                        stopTimers();
                        gameOver = true;
                        Media blackWinsSoundMedia = new Media(blackWinsSound);
                        MediaPlayer blackWinsSoundMediaPlayer = new MediaPlayer(blackWinsSoundMedia);
                        blackWinsSoundMediaPlayer.play();
                        playingText.setText(bundle.getString("result.blackwins"));
                    }

                })
        );
        whiteTimeline.setCycleCount(Timeline.INDEFINITE);
        whiteTimeline.play();
    }

    void startBlackTimer(){
        stopTimers();
        blackTimeline = new Timeline(new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1),
                event -> {
                    blackSeconds--;
                    updateTimerText(blackTime, blackSeconds);
                    if (blackSeconds <= 0) {
                        stopTimers();
                        gameOver = true;
                        Media whiteWinsSoundMedia = new Media(whiteWinsSound);
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
        stage.setOnCloseRequest(null);
        stage.setScene(scene);
    }


}