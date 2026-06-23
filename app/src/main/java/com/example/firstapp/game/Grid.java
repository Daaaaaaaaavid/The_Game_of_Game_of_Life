package com.example.firstapp.game;

public class Grid {

    private final int cols;
    private final int rows;
    private final Cell[][] cells;

    public Grid(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        this.cells = new Cell[cols][rows];
    }

    public int getCols() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public boolean isInside(int x, int y) {
        return x >= 0
                && x < cols
                && y >= 0
                && y < rows;
    }

    public Cell getCell(int x, int y) {
        if (!isInside(x, y)) {
            return null;
        }

        return cells[x][y];
    }

    public void setCell(int x, int y, Cell cell) {
        if (!isInside(x, y)) {
            return;
        }

        cells[x][y] = cell;
    }

    public void clearCell(int x, int y) {
        if (!isInside(x, y)) {
            return;
        }

        cells[x][y] = null;
    }

    public boolean isOccupied(int x, int y) {
        return getCell(x, y) != null;
    }

    public int countLivingNeighbors(int x, int y) {

        int count = 0;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {

                if (dx == 0 && dy == 0) {
                    continue;
                }

                int nx = x + dx;
                int ny = y + dy;

                if (isInside(nx, ny) && getCell(nx, ny) != null) {
                    count++;
                }
            }
        }

        return count;
    }

    public boolean hasNeighborOfType(int x, int y, CellType type) {
        return countNeighborsOfType(x, y, type) > 0;
    }

    public int countNeighborsOfType(int x, int y, CellType type) {

        int count = 0;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {

                if (dx == 0 && dy == 0) {
                    continue;
                }

                int nx = x + dx;
                int ny = y + dy;

                if (!isInside(nx, ny)) {
                    continue;
                }

                Cell cell = getCell(nx, ny);

                if (cell != null && cell.getType() == type) {
                    count++;
                }
            }
        }

        return count;
    }

    public Grid copy() {
        Grid copy = new Grid(cols, rows);

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                copy.cells[x][y] = cells[x][y];
            }
        }

        return copy;
    }
}