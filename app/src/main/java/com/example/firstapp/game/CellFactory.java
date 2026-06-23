package com.example.firstapp.game;

public class CellFactory {
    public static Cell create(CellType type) {
        return new Cell(type);
    }
}