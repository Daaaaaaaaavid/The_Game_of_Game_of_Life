package com.example.firstapp.game;

public class GameEngine {

    private Grid grid;
    private final int cols;
    private final int rows;

    public GameEngine(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        this.grid = new Grid(cols, rows);
    }

    public void step() {
        Grid nextGrid = new Grid(cols, rows);

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                Cell next = calculateNextCell(x, y);
                nextGrid.setCell(x, y, next);
            }
        }

        grid = nextGrid;
    }

    private Cell calculateNextCell(int x, int y) {
        Cell currentCell = grid.getCell(x, y);

        if (currentCell == null) {
            return null;
        }

        switch (currentCell.getType()) {
            case FIRE:
                if (grid.hasNeighborOfType(x, y, CellType.WATER)) {
                    return null;
                }
                return CellFactory.create(CellType.FIRE);

            case WATER:
                return CellFactory.create(CellType.WATER);

            case EARTH:
                if (grid.hasNeighborOfType(x, y, CellType.WATER)) {
                    return CellFactory.create(CellType.PLANT);
                }
                return CellFactory.create(CellType.EARTH);

            case PLANT:
                return CellFactory.create(CellType.PLANT);

            default:
                return null;
        }
    }

    public Grid getGrid() {
        return grid;
    }

    public void setCell(int x, int y, CellType type) {
        grid.setCell(x, y, CellFactory.create(type));
    }
}