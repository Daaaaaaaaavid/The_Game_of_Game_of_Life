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

    private final int cols = 80;
    private final int rows = 160;

    private final GameEngine engine;
    private final Paint paint = new Paint();
    private final Handler handler = new Handler();

    private float cellWidth;
    private float cellHeight;

    private boolean running = true;
    private int tickDelay = 200;

    private CellType selectedCellType = CellType.FIRE;

    public GameView(Context context) {
        super(context);
        engine = new GameEngine(cols, rows);
        startLoop();
    }

    public void setSelectedCellType(CellType type) {
        selectedCellType = type;
    }

    private void startLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (running) {
                    engine.step();
                    invalidate();
                }
                handler.postDelayed(this, tickDelay);
            }
        }, tickDelay);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        cellWidth = (float) w / cols;
        cellHeight = (float) h / rows;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        Grid grid = engine.getGrid();

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                Cell cell = grid.getCell(x, y);

                if (cell != null) {
                    paint.setColor(cell.getType().getColor());

                    canvas.drawRect(
                            x * cellWidth,
                            y * cellHeight,
                            (x + 1) * cellWidth,
                            (y + 1) * cellHeight,
                            paint
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
                engine.setCell(x, y, selectedCellType);
                invalidate();
            }
        }

        return true;
    }

    public void toggleRunning() {
        running = !running;
    }
}