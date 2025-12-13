package com.example.chess;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageController {

    int clicked = -1;
    int selected = codeToInt(SettingsManager.get("language"));
    ResourceBundle bundle;

    @FXML
    Text language;
    @FXML
    ImageView backArrow;
    @FXML
    Button applyButton;
    @FXML
    Button cancelButton;
    @FXML
    Text back;
    @FXML
    Text chess;
    @FXML
    Button english;
    @FXML
    Button greek;
    @FXML
    Button german;
    @FXML
    Button french;
    @FXML
    Button spanish;
    @FXML
    Button italian;
    Button[] buttons;

    public void initialize(){
        buttons = new Button[]{english,greek,german,french,spanish,italian};
        Language.setLocale(SettingsManager.get("language"));
        bundle = Language.getBundle();
        applyLanguage();
        backArrowHandler();
        selected();
        addHover();
        clickHandler();
        applyButtonHandler();
        cancelButtonHandler();
    }

    public void addHover(){
        for(int i=0;i<6;i++){
            int finalI = i;
            buttons[i].setOnMouseEntered(event -> {
                if(!buttons[finalI].getStyleClass().contains("apply-button") && !buttons[finalI].getStyleClass().contains("language-button-clicked")) {
                    buttons[finalI].getStyleClass().add("language-button-hover");
                }
            });
            buttons[i].setOnMouseExited(event -> {
                if(!buttons[finalI].getStyleClass().contains("apply-button") && !buttons[finalI].getStyleClass().contains("language-button-clicked")) {
                    buttons[finalI].getStyleClass().remove("language-button-hover");
                }
            });
        }
    }

    public void clickHandler(){
        for(int i=0;i<6;i++) {
            int finalI = i;
            buttons[i].setOnMouseClicked(event -> {
                clicked = finalI;
                if(!buttons[finalI].getStyleClass().contains("apply-button")){
                    buttons[finalI].getStyleClass().remove("language-button-hover");
                    buttons[finalI].getStyleClass().add("language-button-clicked");
                }
                for(int j=0;j<6;j++){
                    if(j==selected || j==clicked)continue;
                    buttons[j].getStyleClass().remove("language-button-clicked");
                    buttons[j].getStyleClass().remove("language-button-hover");
                }
            });
        }


    }

    public void applyButtonHandler(){
        applyButton.setOnMouseClicked(event -> {
            buttons[selected].getStyleClass().remove("apply-button");
            buttons[selected].getStyleClass().add("main-button");
            buttons[clicked].getStyleClass().remove("language-button-clicked");
            buttons[clicked].getStyleClass().add("apply-button");
            selected = clicked;
            clicked = -1;
            SettingsManager.set("language",intToCode(selected));
            applyLanguage();
            Parent root = null;
            try {
                root = FXMLLoader.load(getClass().getResource("language.fxml"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Stage stage = (Stage) backArrow.getScene().getWindow();
            stage.setScene(new Scene(root));
        });

        applyButton.setOnMouseEntered(event -> {
            applyButton.getStyleClass().add("apply-button-hover");
        });

        applyButton.setOnMouseExited(event -> {
            applyButton.getStyleClass().remove("apply-button-hover");
        });
    }

    public void cancelButtonHandler(){
        cancelButton.setOnMouseClicked(event -> {
            buttons[clicked].getStyleClass().remove("language-button-clicked");
            buttons[clicked].getStyleClass().add("main-button");
            clicked = -1;
        });

        cancelButton.setOnMouseEntered(event -> {
            cancelButton.getStyleClass().add("cancel-button-hover");
        });

        cancelButton.setOnMouseExited(event -> {
            cancelButton.getStyleClass().remove("cancel-button-hover");
        });
    }

    public void selected(){
        int language = selected;
        buttons[language].getStyleClass().add("apply-button");
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

    public void switchToMainMenu(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main-menu.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    public int codeToInt(String code){
        return switch (code) {
            case "el" -> 1;
            case "de" -> 2;
            case "fr" -> 3;
            case "es" -> 4;
            case "it" -> 5;
            default -> 0;
        };
    }

    public String intToCode(int x){
        return switch (x) {
            case 1 -> "el";
            case 2 -> "de";
            case 3 -> "fr";
            case 4 -> "es";
            case 5 -> "it";
            default -> "en";
        };
    }

    public void applyLanguage() {
        back.setText(bundle.getString("button.back"));
        language.setText(bundle.getString("menu.language"));
        cancelButton.setText(bundle.getString("button.cancel"));
        chess.setText(bundle.getString("title.chess"));
        applyButton.setText(bundle.getString("button.apply"));
    }
}
