package com.example.firstapp.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {

    private int cols = 80;
    private int rows = 160;

    private Cell[][] grid;
    private Cell[][] buffer;

    private Paint gridPaint;
    private Paint alivePaint;

    private float cellWidth;
    private float cellHeight;

    private Handler handler = new Handler();
    private boolean running = true;
    private int tickDelay = 200; // ms

    public GameView(Context context) {
        super(context);

        grid = new Cell[cols][rows];
        buffer = new Cell[cols][rows];

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                grid[x][y] = new Cell(false);
                buffer[x][y] = new Cell(false);
            }
        }

        gridPaint = new Paint();
        gridPaint.setColor(Color.LTGRAY);

        alivePaint = new Paint();
        alivePaint.setColor(Color.BLACK);

        startLoop();
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

                int neighbors = countNeighbors(x, y);
                boolean alive = grid[x][y].alive;

                // Conway Regeln
                if (alive && (neighbors == 2 || neighbors == 3)) {
                    buffer[x][y].alive = true;
                } else if (!alive && neighbors == 3) {
                    buffer[x][y].alive = true;
                } else {
                    buffer[x][y].alive = false;
                }
            }
        }

        // Swap grid <-> buffer
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                grid[x][y].alive = buffer[x][y].alive;
            }
        }
    }

    private int countNeighbors(int x, int y) {
        int count = 0;

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {

                if (dx == 0 && dy == 0) continue;

                int nx = x + dx;
                int ny = y + dy;

                if (nx >= 0 && nx < cols && ny >= 0 && ny < rows) {
                    if (grid[nx][ny].alive) {
                        count++;
                    }
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

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.WHITE);

        // Zellen
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                if (grid[x][y].alive) {

                    float left = x * cellWidth;
                    float top = y * cellHeight;

                    canvas.drawRect(
                            left,
                            top,
                            left + cellWidth,
                            top + cellHeight,
                            alivePaint
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
                grid[x][y].alive = true;
                invalidate();
            }
        }

        return true;
    }

    public void toggleRunning() {
        running = !running;
    }
}