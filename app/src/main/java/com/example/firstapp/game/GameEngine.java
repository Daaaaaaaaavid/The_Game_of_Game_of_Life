package com.example.firstapp.game;

import java.util.Random;

public class GameEngine {
    private Grid grid;
    private final int cols;
    private final int rows;
    private final Random random = new Random();

    // Reduzierte Zerfallswahrscheinlichkeiten (langsamer verschwinden)
    private static final double WATER_DECAY_CHANCE = 0.0001;
    private static final double FIRE_DECAY_CHANCE = 0.0001;
    private static final double SMOKE_DECAY_CHANCE = 0.02;

    private static final double WATER_BIRTH_CHANCE = 0.85;
    private static final double FIRE_BIRTH_CHANCE = 0.85;

    public GameEngine(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
        this.grid = new Grid(cols, rows);
    }

    public void step() {
        Grid conwayGrid = applyConwayRules();
        grid = applyMovement(conwayGrid);
    }

    private Grid applyConwayRules() {
        Grid nextGrid = new Grid(cols, rows);

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                Cell currentCell = grid.getCell(x, y);
                
                int conwayNeighbors = countConwayNeighbors(x, y);
                int totalNeighbors = grid.countLivingNeighbors(x, y);

                if (currentCell == null) {
                    if (conwayNeighbors == 3) {
                        CellType birthType = determineBirthType(x, y);

                        if (birthType != null && canBeBorn(birthType, x, y)) {
                            nextGrid.setCell(x, y, CellFactory.create(birthType));
                        }
                    }
                    continue;
                }

                CellType type = currentCell.getType();

                if (type == CellType.BARRIER) {
                    nextGrid.setCell(x, y, currentCell);
                    continue;
                }

                // Rauch-Entstehung: Feuer + Wasser
                if ((type == CellType.FIRE && grid.hasNeighborOfType(x, y, CellType.WATER))
                        || (type == CellType.WATER && grid.hasNeighborOfType(x, y, CellType.FIRE))) {
                    nextGrid.setCell(x, y, CellFactory.create(CellType.SMOKE));
                    continue;
                }

                // Säure frisst Erde -> Rauch
                if (type == CellType.EARTH && grid.hasNeighborOfType(x, y, CellType.ACID)) {
                    nextGrid.setCell(x, y, CellFactory.create(CellType.SMOKE));
                    continue;
                }

                // Sand absorbiert Säure
                if (type == CellType.ACID && grid.hasNeighborOfType(x, y, CellType.SAND)) {
                    continue; 
                }

                if (type == CellType.EARTH && grid.hasNeighborOfType(x, y, CellType.WATER)) {
                    if (conwayNeighbors == 2 || conwayNeighbors == 3) {
                        nextGrid.setCell(x, y, CellFactory.create(CellType.PLANT));
                    }
                    continue;
                }

                int neighborCountForSurvival = isConwayType(type) ? conwayNeighbors : totalNeighbors;

                if (!survives(type, x, y, neighborCountForSurvival)) {
                    continue;
                }

                nextGrid.setCell(x, y, CellFactory.create(type));
            }
        }

        return nextGrid;
    }

    private int countConwayNeighbors(int x, int y) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                int nx = x + dx;
                int ny = y + dy;
                if (grid.isInside(nx, ny)) {
                    Cell cell = grid.getCell(nx, ny);
                    if (cell != null && isConwayType(cell.getType())) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private boolean isConwayType(CellType type) {
        return type == CellType.FIRE || type == CellType.WATER || 
               type == CellType.EARTH || type == CellType.PLANT || 
               type == CellType.STANDARD;
    }

    private boolean survives(CellType type, int x, int y, int neighbors) {
        if (type == CellType.BARRIER) return true;

        if (type == CellType.WATER) {
            int waterNeighbors = grid.countNeighborsOfType(x, y, CellType.WATER);
            if (grid.hasNeighborOfType(x, y, CellType.FIRE)) return false;
            if (waterNeighbors >= 6) return false;
            return neighbors >= 1 && neighbors <= 4 && random.nextDouble() > WATER_DECAY_CHANCE;
        }

        if (type == CellType.FIRE) {
            int fireNeighbors = grid.countNeighborsOfType(x, y, CellType.FIRE);
            if (grid.hasNeighborOfType(x, y, CellType.WATER)) return false;
            if (fireNeighbors >= 6) return false;
            return neighbors >= 1 && neighbors <= 4 && random.nextDouble() > FIRE_DECAY_CHANCE;
        }

        if (type == CellType.SMOKE) {
            return random.nextDouble() > SMOKE_DECAY_CHANCE;
        }

        if (type == CellType.SAND || type == CellType.ACID) {
            return true;
        }

        return neighbors == 2 || neighbors == 3;
    }

    private boolean canBeBorn(CellType type, int x, int y) {
        if (type == CellType.BARRIER) return false;

        if (type == CellType.WATER) {
            int waterNeighbors = grid.countNeighborsOfType(x, y, CellType.WATER);
            return waterNeighbors <= 2 && random.nextDouble() < WATER_BIRTH_CHANCE;
        }

        if (type == CellType.FIRE) {
            int fireNeighbors = grid.countNeighborsOfType(x, y, CellType.FIRE);
            return fireNeighbors <= 2 && random.nextDouble() < FIRE_BIRTH_CHANCE;
        }

        if (type == CellType.SAND || type == CellType.ACID || type == CellType.SMOKE) {
            return false;
        }

        return true;
    }

    private Grid applyMovement(Grid sourceGrid) {
        Grid movedGrid = new Grid(cols, rows);

        for (int y = rows - 1; y >= 0; y--) {
            for (int x = 0; x < cols; x++) {
                Cell cell = sourceGrid.getCell(x, y);
                if (cell == null) continue;

                if (movedGrid.getCell(x, y) != null && movedGrid.getCell(x, y).getType() == CellType.BARRIER) {
                    continue;
                }

                int[] target = getMoveTarget(sourceGrid, movedGrid, cell.getType(), x, y);
                int tx = target[0];
                int ty = target[1];

                if (movedGrid.getCell(tx, ty) == null) {
                    movedGrid.setCell(tx, ty, CellFactory.create(cell.getType()));
                } else if (movedGrid.getCell(x, y) == null) {
                    movedGrid.setCell(x, y, CellFactory.create(cell.getType()));
                }
            }
        }

        return movedGrid;
    }

    private int[] getMoveTarget(Grid sourceGrid, Grid movedGrid, CellType type, int x, int y) {
        if (type == CellType.BARRIER) {
            return new int[]{x, y};
        }

        if (type == CellType.WATER || type == CellType.ACID) {
            return findRandomMove(sourceGrid, movedGrid, x, y, new int[][]{
                    {0, 1}, {-1, 1}, {1, 1}, {-1, 0}, {1, 0}
            }, 0.95);
        }

        if (type == CellType.SAND) {
            return findRandomMove(sourceGrid, movedGrid, x, y, new int[][]{
                    {0, 1}, {-1, 1}, {1, 1}
            }, 0.98);
        }

        if (type == CellType.FIRE) {
            return findRandomMove(sourceGrid, movedGrid, x, y, new int[][]{
                    {0, -1}, {-1, -1}, {1, -1}, {-1, 0}, {1, 0}
            }, 0.95);
        }

        if (type == CellType.SMOKE) {
            return findRandomMove(sourceGrid, movedGrid, x, y, new int[][]{
                    {0, -1}, {-1, -1}, {1, -1}, {-2, -1}, {2, -1}, {-1, 0}, {1, 0}
            }, 0.8);
        }

        return new int[]{x, y};
    }

    private int[] findRandomMove(Grid sourceGrid, Grid movedGrid, int x, int y, int[][] directions, double moveChance) {
        if (random.nextDouble() > moveChance) {
            return new int[]{x, y};
        }

        shuffleDirections(directions);

        for (int[] direction : directions) {
            int nx = x + direction[0];
            int ny = y + direction[1];

            if (sourceGrid.isInside(nx, ny)
                    && sourceGrid.getCell(nx, ny) == null
                    && movedGrid.getCell(nx, ny) == null) {
                return new int[]{nx, ny};
            }
        }

        return new int[]{x, y};
    }

    private void shuffleDirections(int[][] directions) {
        for (int i = directions.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int[] temp = directions[i];
            directions[i] = directions[j];
            directions[j] = temp;
        }
    }

    private CellType determineBirthType(int x, int y) {
        int fire = grid.countNeighborsOfType(x, y, CellType.FIRE);
        int water = grid.countNeighborsOfType(x, y, CellType.WATER);
        int earth = grid.countNeighborsOfType(x, y, CellType.EARTH);
        int plant = grid.countNeighborsOfType(x, y, CellType.PLANT);

        if (fire == 0 && water == 0 && earth == 0 && plant == 0) return null;

        if (plant >= fire && plant >= water && plant >= earth) return CellType.PLANT;
        if (earth >= fire && earth >= water) return CellType.EARTH;
        if (water > fire) return CellType.WATER;
        return CellType.FIRE;
    }

    public Grid getGrid() {
        return grid;
    }

    public void setCell(int x, int y, CellType type) {
        grid.setCell(x, y, CellFactory.create(type));
    }

    public void setBarrier(int x, int y) {
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 5; j++) {
                grid.setCell(x + i, y + j, CellFactory.create(CellType.BARRIER));
            }
        }
    }
}