package com.example.firstapp.fragments;

import android.graphics.Color;

public class WaterCell extends StandardCell {
    @Override
    public int getColor() {
        return Color.BLUE;
    }

    @Override
    public Cell nextState(Cell[][] grid, int x, int y) {
        if (hasNeighbor(grid, x, y, EarthCell.class)) return null;
        return super.nextState(grid, x, y);
    }
}