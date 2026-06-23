package com.example.firstapp.fragments;

public abstract class Cell {
    public boolean alive = true;

    public abstract int getColor();

    public Cell nextState(Cell[][] grid, int x, int y) {
        int neighbors = countAliveNeighbors(grid, x, y);

        if (neighbors < 2 || neighbors > 3) {
            return null;
        }

        return this;
    }

    protected boolean hasNeighbor(Cell[][] grid, int x, int y, Class<?> type) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int nx = x + dx;
                int ny = y + dy;

                if (nx >= 0 && nx < grid.length && ny >= 0 && ny < grid[0].length) {
                    if (type.isInstance(grid[nx][ny])) return true;
                }
            }
        }

        return false;
    }

    protected int countAliveNeighbors(Cell[][] grid, int x, int y) {
        int count = 0;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int nx = x + dx;
                int ny = y + dy;

                if (nx >= 0 && nx < grid.length && ny >= 0 && ny < grid[0].length) {
                    if (grid[nx][ny] != null) count++;
                }
            }
        }

        return count;
    }
}