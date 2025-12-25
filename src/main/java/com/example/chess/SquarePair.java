package com.example.chess;

import java.util.Objects;

public class SquarePair {
    private int row;
    private int col;

    public SquarePair(int row, int col) {
        this.col = col;
        this.row = row;
    }

    public SquarePair(SquarePair other) {
        this.row = other.row;
        this.col = other.col;
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

    public String squarePairToMove(){
        return "" + (char)('a' + col) + (8 - row);
    }

    public static SquarePair moveToSquarePair(String move){
        int col = move.charAt(0) - 'a';
        int row = 8 - (move.charAt(1) - '0');
        return new SquarePair(row, col);
    }
}
