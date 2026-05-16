package com.example.firstapp.fragments;
public class Cell {

    public boolean alive;

    public Cell(boolean alive) {
        this.alive = alive;
    }

    public Cell copy() {
        return new Cell(alive);
    }
}