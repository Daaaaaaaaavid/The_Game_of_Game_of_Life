package com.example.firstapp.fragments;

import android.graphics.Color;

public class FireCell extends Cell {
    @Override
    public int getColor() {
        return Color.RED;
    }

    @Override
    public Cell nextState(Cell[][] grid, int x, int y) {
        if (hasNeighbor(grid, x, y, WaterCell.class)) return null;
        return super.nextState(grid, x, y);
    }
}