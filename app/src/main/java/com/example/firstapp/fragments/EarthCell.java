package com.example.firstapp.fragments;

import android.graphics.Color;

public class EarthCell extends StandardCell {
    @Override
    public int getColor() {
        return Color.rgb(120, 80, 40);
    }

    @Override
    public Cell nextState(Cell[][] grid, int x, int y) {
        if (hasNeighbor(grid, x, y, WaterCell.class)) return new PlantCell();
        return super.nextState(grid, x, y);
    }
}