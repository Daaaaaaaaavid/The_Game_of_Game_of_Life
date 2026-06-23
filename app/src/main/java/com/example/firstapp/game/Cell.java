package com.example.firstapp.game;

public class Cell {
    private final CellType type;

    public Cell(CellType type) {
        this.type = type;
    }

    public CellType getType() {
        return type;
    }
}