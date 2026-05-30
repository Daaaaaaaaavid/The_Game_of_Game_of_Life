package com.example.firstapp;


import com.example.firstapp.fragments.Cell;
import com.example.firstapp.fragments.StandardCell;
import com.example.firstapp.fragments.FireCell;
import com.example.firstapp.fragments.WaterCell;
import com.example.firstapp.fragments.EarthCell;
import com.example.firstapp.fragments.PlantCell;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellBehaviorTest {

    @Test
    public void standardCellDiesWithFewerThanTwoNeighbors() {
        Cell[][] grid = new Cell[3][3];
        grid[1][1] = new StandardCell();

        Cell result = grid[1][1].nextState(grid, 1, 1);

        assertNull(result);
    }

    @Test
    public void standardCellSurvivesWithTwoNeighbors() {
        Cell[][] grid = new Cell[3][3];
        grid[1][1] = new StandardCell();
        grid[0][1] = new StandardCell();
        grid[2][1] = new StandardCell();

        Cell result = grid[1][1].nextState(grid, 1, 1);

        assertNotNull(result);
        assertTrue(result instanceof StandardCell);
    }

    @Test
    public void fireCellDiesWhenTouchingWater() {
        Cell[][] grid = new Cell[3][3];
        grid[1][1] = new FireCell();
        grid[1][2] = new WaterCell();

        Cell result = grid[1][1].nextState(grid, 1, 1);

        assertNull(result);
    }

    @Test
    public void waterCellDisappearsWhenTouchingEarth() {
        Cell[][] grid = new Cell[3][3];
        grid[1][1] = new WaterCell();
        grid[1][2] = new EarthCell();

        Cell result = grid[1][1].nextState(grid, 1, 1);

        assertNull(result);
    }

    @Test
    public void earthCellTurnsIntoPlantWhenTouchingWater() {
        Cell[][] grid = new Cell[3][3];
        grid[1][1] = new EarthCell();
        grid[1][2] = new WaterCell();

        Cell result = grid[1][1].nextState(grid, 1, 1);

        assertNotNull(result);
        assertTrue(result instanceof PlantCell);
    }
}