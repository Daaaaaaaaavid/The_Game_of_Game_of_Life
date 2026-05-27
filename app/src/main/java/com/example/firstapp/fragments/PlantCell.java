package com.example.firstapp.fragments;

import android.graphics.Color;

public class PlantCell extends StandardCell {
    @Override
    public int getColor() {
        return Color.GREEN;
    }

    @Override
    public Cell nextState(Cell[][] grid, int x, int y) {
        if (hasNeighbor(grid, x, y, FireCell.class)) return null;
        return super.nextState(grid, x, y);
    }
}