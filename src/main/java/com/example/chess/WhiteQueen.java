package com.example.chess;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class WhiteQueen extends Queen{

    public WhiteQueen(SquarePair pos) throws FileNotFoundException {
        super(pos);
        setColor("white");
        InputStream inputStream = getClass().getResourceAsStream("/com/example/chess/pieces/white-queen.png");
        assert inputStream != null;
        img = new Image(inputStream);
        imageView = new ImageView(img);
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);
    }
}
