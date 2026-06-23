package com.example.firstapp.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import com.example.firstapp.game.Cell;
import com.example.firstapp.game.CellType;
import com.example.firstapp.game.GameEngine;
import com.example.firstapp.game.Grid;

public class GameView extends View {

    private static final int COLS = 80;
    private static final int ROWS = 160;
    private static final int TICK_DELAY = 200;

    private final GameEngine gameEngine;
    private final Paint cellPaint;
    private final Handler handler;

    private float cellWidth;
    private float cellHeight;

    private boolean running = true;
    private CellType selectedCellType = CellType.STANDARD;

    public GameView(Context context) {
        super(context);

        gameEngine = new GameEngine(COLS, ROWS);
        cellPaint = new Paint();
        handler = new Handler();

        startLoop();
    }

    public void setSelectedCellType(CellType selectedCellType) {
        this.selectedCellType = selectedCellType;
    }

    private void startLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (running) {
                    gameEngine.step();
                    invalidate();
                }

                handler.postDelayed(this, TICK_DELAY);
            }
        }, TICK_DELAY);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        cellWidth = (float) w / COLS;
        cellHeight = (float) h / ROWS;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        Grid grid = gameEngine.getGrid();

        for (int x = 0; x < grid.getCols(); x++) {
            for (int y = 0; y < grid.getRows(); y++) {
                Cell cell = grid.getCell(x, y);

                if (cell != null) {
                    cellPaint.setColor(cell.getType().getColor());

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
        if (event.getAction() == MotionEvent.ACTION_DOWN
                || event.getAction() == MotionEvent.ACTION_MOVE) {

            int x = (int) (event.getX() / cellWidth);
            int y = (int) (event.getY() / cellHeight);

            if (gameEngine.getGrid().isInside(x, y)) {
                gameEngine.setCell(x, y, selectedCellType);
                invalidate();
            }
        }

        return true;
    }

    public void toggleRunning() {
        running = !running;
    }
}