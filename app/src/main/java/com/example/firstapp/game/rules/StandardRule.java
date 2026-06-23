package com.example.firstapp.game.rules;

import com.example.firstapp.game.Cell;
import com.example.firstapp.game.Grid;

public class StandardRule implements CellRule {
    @Override
    public Cell apply(Grid grid, int x, int y) {
        int neighbors = grid.countLivingNeighbors(x, y);

        if (neighbors < 2 || neighbors > 3) return null;
        return grid.getCell(x, y);
    }
}