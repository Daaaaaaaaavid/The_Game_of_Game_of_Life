package com.example.firstapp.game;

import android.graphics.Color;

public enum CellType {
    EMPTY,
    STANDARD,
    FIRE,
    WATER,
    EARTH,
    PLANT,
    SMOKE;

    public int getColor() {
        switch (this) {
            case FIRE: return Color.RED;
            case WATER: return Color.BLUE;
            case EARTH: return Color.rgb(120, 80, 40);
            case PLANT: return Color.GREEN;
            case SMOKE: return Color.GRAY;
            default: return Color.BLACK;
        }
    }
}