package com.example.firstapp.game.rules;

import com.example.firstapp.game.Cell;
import com.example.firstapp.game.Grid;

public interface CellRule {
    Cell apply(Grid grid, int x, int y);
}