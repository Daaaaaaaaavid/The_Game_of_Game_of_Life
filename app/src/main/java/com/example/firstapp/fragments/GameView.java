package com.example.firstapp.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;



public class GameView extends View {

    public enum SelectedCellType {
        STANDARD, FIRE, WATER, EARTH, PLANT
    }

    private final int cols = 80;
    private final int rows = 160;

    private Cell[][] grid;
    private Cell[][] buffer;

    private Paint gridPaint;
    private Paint cellPaint;

    private float cellWidth;
    private float cellHeight;

    private Handler handler = new Handler();
    private boolean running = true;
    private int tickDelay = 200;

    private PhysicsSystem physicsSystem;

    private SelectedCellType selectedCellType = SelectedCellType.STANDARD;

    public GameView(Context context) {
        super(context);

        grid = new Cell[cols][rows];
        buffer = new Cell[cols][rows];

        gridPaint = new Paint();
        gridPaint.setColor(Color.LTGRAY);

        cellPaint = new Paint();

        physicsSystem = new PhysicsSystem();

        startLoop();
    }

    public void setSelectedCellType(SelectedCellType selectedCellType) {
        this.selectedCellType = selectedCellType;
    }

    private void startLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (running) {
                    stepGame();
                    invalidate();
                }
                handler.postDelayed(this, tickDelay);
            }
        }, tickDelay);
    }

    private void stepGame() {
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                Cell current = grid[x][y];

                if (current == null) {
                    int neighbors = countAliveNeighbors(x, y);

                    if (neighbors == 3) {
                        buffer[x][y] = createCellFromNeighbors(x, y);
                    } else {
                        buffer[x][y] = null;
                    }
                } else {
                    buffer[x][y] = current.nextState(grid, x, y);
                }
            }
        }

        physicsSystem.apply(buffer, cols, rows);

        Cell[][] temp = grid;
        grid = buffer;
        buffer = temp;
    }

    private int countAliveNeighbors(int x, int y) {
        int count = 0;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int nx = x + dx;
                int ny = y + dy;

                if (nx >= 0 && nx < cols && ny >= 0 && ny < rows) {
                    if (grid[nx][ny] != null) count++;
                }
            }
        }

        return count;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        cellWidth = (float) w / cols;
        cellHeight = (float) h / rows;
    }

    private Cell createCellFromNeighbors(int x, int y) {
        int fire = 0;
        int water = 0;
        int earth = 0;
        int plant = 0;
        int standard = 0;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;

                int nx = x + dx;
                int ny = y + dy;

                if (nx >= 0 && nx < cols && ny >= 0 && ny < rows) {
                    Cell cell = grid[nx][ny];

                    if (cell instanceof FireCell) fire++;
                    else if (cell instanceof WaterCell) water++;
                    else if (cell instanceof EarthCell) earth++;
                    else if (cell instanceof PlantCell) plant++;
                    else if (cell instanceof StandardCell) standard++;
                }
            }
        }

        if (fire >= water && fire >= earth && fire >= plant && fire >= standard) {
            return new FireCell();
        } else if (water >= earth && water >= plant && water >= standard) {
            return new WaterCell();
        } else if (earth >= plant && earth >= standard) {
            return new EarthCell();
        } else if (plant >= standard) {
            return new PlantCell();
        } else {
            return new StandardCell();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                Cell cell = grid[x][y];

                if (cell != null) {
                    cellPaint.setColor(cell.getColor());

                    float left = x * cellWidth;
                    float top = y * cellHeight;

                    canvas.drawRect(
                            left,
                            top,
                            left + cellWidth,
                            top + cellHeight,
                            cellPaint
                    );
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN ||
                event.getAction() == MotionEvent.ACTION_MOVE) {

            int x = (int) (event.getX() / cellWidth);
            int y = (int) (event.getY() / cellHeight);

            if (x >= 0 && x < cols && y >= 0 && y < rows) {
                grid[x][y] = createSelectedCell();
                invalidate();
            }
        }

        return true;
    }

    private Cell createSelectedCell() {
        switch (selectedCellType) {
            case FIRE:
                return new FireCell();
            case WATER:
                return new WaterCell();
            case EARTH:
                return new EarthCell();
            case PLANT:
                return new PlantCell();
            case STANDARD:
            default:
                return new StandardCell();
        }
    }

    public void toggleRunning() {
        running = !running;
    }
}