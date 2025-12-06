package com.example.chess;

import java.util.Objects;

public class SquarePair {
    private int row;
    private int col;

    public SquarePair(int row, int col) {
        this.col = col;
        this.row = row;
    }



    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SquarePair that = (SquarePair) o;
        return getRow() == that.getRow() && getCol() == that.getCol();
    }

}
