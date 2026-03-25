package com.example.chess;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class MainMenuController {
    @FXML
    Button playButton;
    @FXML
    Button themesButton;
    @FXML
    Button languageButton;
    @FXML
    Text chess;
    @FXML
    ImageView darkLightMode;
    @FXML
    AnchorPane mainMenu;
    ResourceBundle bundle;


    public void initialize(){
        Language.setLocale(SettingsManager.get("language"));
        bundle = Language.getBundle();
        String isDarkModeOn = SettingsManager.get("dark.mode");
        if(isDarkModeOn.equals("on")){
            mainMenu.getStyleClass().add("dark-mode-on");
        }
        else{
            mainMenu.getStyleClass().remove("dark-mode-on");
        }
        applyTranslations();
        playButtonHandler();
        themesButtonHandler();
        languageButtonHandler();
        switchDarkLightMode();
    }

    public void themesButtonHandler(){
        themesButton.setOnMouseClicked(event -> {
            try {
                switchToThemes(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        themesButton.setOnMouseEntered(event -> {
            themesButton.getStyleClass().add("main-button-hover");
        });

        themesButton.setOnMouseExited(event -> {
            themesButton.getStyleClass().remove("main-button-hover");
        });
    }

    public void switchToThemes(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("themes.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setOnCloseRequest(null);
        stage.setScene(scene);
    }

    public void playButtonHandler(){
        playButton.setOnMouseClicked(event -> {
            try {
                switchToMainGame(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        playButton.setOnMouseEntered(event -> {
            playButton.getStyleClass().add("main-button-hover");
        });

        playButton.setOnMouseExited(event -> {
            playButton.getStyleClass().remove("main-button-hover");
        });
    }

    public void switchToMainGame(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main-game.fxml"));
        Parent root = loader.load();
        ChessController chessController = loader.getController();
        if(chessController!=null){
            chessController.refreshTheme();
        }
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        assert chessController != null;
        chessController.exitWarning(stage);
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public void languageButtonHandler(){
        languageButton.setOnMouseClicked(event -> {
            try {
                switchToLanguage(event);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        languageButton.setOnMouseEntered(event -> {
            languageButton.getStyleClass().add("main-button-hover");
        });

        languageButton.setOnMouseExited(event -> {
            languageButton.getStyleClass().remove("main-button-hover");
        });
    }

    public void switchToLanguage(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("language.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setOnCloseRequest(null);
        stage.setScene(scene);
    }

    public void applyTranslations(){
        playButton.setText(bundle.getString("menu.play"));
        themesButton.setText(bundle.getString("menu.themes"));
        languageButton.setText(bundle.getString("menu.language"));
        chess.setText(bundle.getString("title.chess"));
    }

    public void switchDarkLightMode(){
        darkLightMode.setOnMouseClicked(event -> {
            String isDarkModeOn = SettingsManager.get("dark.mode");
            if(isDarkModeOn.equals("off")){
                SettingsManager.set("dark.mode","on");
                mainMenu.getStyleClass().add("dark-mode-on");
            }
            else{
                SettingsManager.set("dark.mode","off");
                mainMenu.getStyleClass().remove("dark-mode-on");
            }
        });
    }
}
