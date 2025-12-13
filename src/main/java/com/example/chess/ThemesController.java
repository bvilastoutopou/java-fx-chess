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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class ThemesController {
    @FXML
    ImageView backArrow;

    @FXML
    Rectangle basicRec;
    @FXML
    Rectangle greenRec;
    @FXML
    Rectangle brownRec;
    @FXML
    Rectangle blueRec;
    @FXML
    Rectangle seaRec;
    @FXML
    Rectangle pinkRec;
    @FXML
    Rectangle orangeRec;
    @FXML
    Rectangle redRec;

    @FXML
    Text basic;
    @FXML
    Text green;
    @FXML
    Text brown;
    @FXML
    Text blue;
    @FXML
    Text sea;
    @FXML
    Text pink;
    @FXML
    Text orange;
    @FXML
    Text red;
    @FXML
    Text boards;
    @FXML
    Text themes;
    @FXML
    Button applyButton;
    @FXML
    Button cancelButton;
    @FXML
    Text back;
    @FXML
    Text chess;



    int clicked = -1;
    int selected = Integer.parseInt(SettingsManager.get("theme"));
    ResourceBundle bundle;

    Rectangle[] rectangles;
    public void initialize(){
        rectangles = new Rectangle[]{basicRec,greenRec,brownRec,blueRec,seaRec,pinkRec,orangeRec,redRec};
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

    public void selected(){
        int theme = selected;
        rectangles[theme].setStroke(Color.GREEN);
    }

    public void addHover(){
        for(int i=0;i<8;i++){
            int finalI = i;
            rectangles[i].setOnMouseEntered(event -> {
                if(!rectangles[finalI].getStroke().equals(Color.GREEN) && !rectangles[finalI].getStroke().equals(Color.ORANGE)) {
                    rectangles[finalI].setStroke(Color.YELLOW);
                }
            });
            rectangles[i].setOnMouseExited(event -> {
                if(!rectangles[finalI].getStroke().equals(Color.GREEN) && !rectangles[finalI].getStroke().equals(Color.ORANGE)) {
                    rectangles[finalI].setStroke(Color.web("#0075e5"));
                }
            });
        }
    }

    public void clickHandler(){
        for(int i=0;i<8;i++) {
            int finalI = i;
            rectangles[i].setOnMouseClicked(event -> {
                clicked = finalI;
                if(!rectangles[finalI].getStroke().equals(Color.GREEN)) rectangles[finalI].setStroke(Color.ORANGE);
                for(int j=0;j<8;j++){
                    if(j==selected || j==clicked)continue;
                    rectangles[j].setStroke(Color.web("#0075e5"));
                }
            });
        }
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

    public void applyButtonHandler(){
        applyButton.setOnMouseClicked(event -> {
            rectangles[selected].setStroke(Color.web("#0075e5"));
            rectangles[clicked].setStroke(Color.GREEN);
            selected = clicked;
            clicked = -1;
            SettingsManager.set("theme",Integer.toString(selected));
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
            rectangles[clicked].setStroke(Color.web("#0075e5"));
            clicked = -1;
        });

        cancelButton.setOnMouseEntered(event -> {
            cancelButton.getStyleClass().add("cancel-button-hover");
        });

        cancelButton.setOnMouseExited(event -> {
            cancelButton.getStyleClass().remove("cancel-button-hover");
        });
    }

    public void applyLanguage(){
        back.setText(bundle.getString("button.back"));
        themes.setText(bundle.getString("menu.themes"));
        cancelButton.setText(bundle.getString("button.cancel"));
        chess.setText(bundle.getString("title.chess"));
        applyButton.setText(bundle.getString("button.apply"));
        basic.setText(bundle.getString("theme.basic"));
        green.setText(bundle.getString("theme.green"));
        brown.setText(bundle.getString("theme.brown"));
        blue.setText(bundle.getString("theme.blue"));
        sea.setText(bundle.getString("theme.sea"));
        pink.setText(bundle.getString("theme.pink"));
        orange.setText(bundle.getString("theme.orange"));
        red.setText(bundle.getString("theme.red"));
        boards.setText(bundle.getString("menu.boards"));
    }

    public void switchToMainMenu(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main-menu.fxml"));
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
