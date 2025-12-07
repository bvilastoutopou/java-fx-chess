package com.example.chess;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class BlackKnight extends Knight{
    public BlackKnight(SquarePair pos) throws FileNotFoundException {
        super(pos);
        setColor("black");
        inputStream = new FileInputStream("C:\\Users\\capta\\Desktop\\all\\programs\\java\\java fx\\Ergasia\\Chess\\src\\main\\resources\\com\\example\\chess\\pieces\\black-knight.png");
        img = new Image(inputStream);
        imageView = new ImageView(img);
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);
    }
}
