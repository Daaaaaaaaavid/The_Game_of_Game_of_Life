package com.example.firstapp.fragments;

import android.graphics.Color;

public class StandardCell extends Cell {
    @Override
    public int getColor() {
        return Color.BLACK;
    }

    @Override
    public Cell nextState(Cell[][] grid, int x, int y) {
        int neighbors = countAliveNeighbors(grid, x, y);

        if (neighbors < 2 || neighbors > 3) {
            return null;
        }

        return this;
    }
}