package com.example.firstapp.fragments;

public class PhysicsSystem {

    private static final double WATER_FALL_CHANCE = 0.90;
    private static final double FIRE_RISE_CHANCE = 0.80;

    public void apply(Cell[][] grid, int cols, int rows) {
        applyWaterGravity(grid, cols, rows);
        applyFireLift(grid, cols, rows);
    }

    private void applyWaterGravity(Cell[][] grid, int cols, int rows) {
        for (int y = rows - 2; y >= 0; y--) {
            for (int x = 0; x < cols; x++) {
                if (grid[x][y] instanceof WaterCell && grid[x][y + 1] == null) {
                    if (Math.random() < WATER_FALL_CHANCE) {
                        grid[x][y + 1] = grid[x][y];
                        grid[x][y] = null;
                    }
                }
            }
        }
    }

    private void applyFireLift(Cell[][] grid, int cols, int rows) {
        for (int y = 1; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (grid[x][y] instanceof FireCell && grid[x][y - 1] == null) {
                    if (Math.random() < FIRE_RISE_CHANCE) {
                        grid[x][y - 1] = grid[x][y];
                        grid[x][y] = null;
                    }
                }
            }
        }
    }
}