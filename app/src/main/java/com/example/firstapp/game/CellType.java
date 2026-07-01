package com.example.firstapp.game;

import android.graphics.Color;

public enum CellType {
    EMPTY,
    STANDARD,
    FIRE,
    WATER,
    EARTH,
    PLANT,
    SMOKE,
    SAND,
    ACID,
    BARRIER;

    public int getColor() {
        switch (this) {
            case FIRE: return Color.RED;
            case WATER: return Color.BLUE;
            case EARTH: return Color.rgb(120, 80, 40);
            case PLANT: return Color.GREEN;
            case SMOKE: return Color.GRAY;
            case SAND: return Color.rgb(240, 200, 100);
            case ACID: return Color.rgb(150, 255, 0);
            case BARRIER: return Color.BLACK;
            default: return Color.BLACK;
        }
    }
}