package com.example.chess;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class WhiteQueen extends Queen{

    public WhiteQueen(SquarePair pos) throws FileNotFoundException {
        super(pos);
        setColor("white");
        inputStream = new FileInputStream("C:\\Users\\capta\\Desktop\\all\\programs\\java\\java fx\\Ergasia\\Chess\\src\\main\\resources\\com\\example\\chess\\pieces\\white-queen.png");
        img = new Image(inputStream);
        imageView = new ImageView(img);
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);
    }
}
