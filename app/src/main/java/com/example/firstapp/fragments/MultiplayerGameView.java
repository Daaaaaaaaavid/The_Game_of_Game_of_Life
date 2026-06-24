package com.example.firstapp.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.firstapp.game.Cell;
import com.example.firstapp.game.CellType;
import com.example.firstapp.game.GameEngine;
import com.example.firstapp.game.Grid;

public class MultiplayerGameView extends View {

    private final int cols = 80;
    private final int rows = 160;

    private final GameEngine engine;
    private final Paint cellPaint = new Paint();
    private final Paint playerOverlayPaint = new Paint();
    private final Handler handler = new Handler();

    private final int[][] owners = new int[cols][rows];

    private float cellWidth;
    private float cellHeight;

    private boolean running = true;
    private final int tickDelay = 200;

    private CellType selectedCellType = CellType.FIRE;
    private int currentPlayer = 1;

    private TextView statusText;

    public MultiplayerGameView(Context context) {
        super(context);
        engine = new GameEngine(cols, rows);
        startLoop();
    }

    public void setStatusText(TextView statusText) {
        this.statusText = statusText;
        updateStatus();
    }

    public void setSelectedCellType(CellType selectedCellType) {
        this.selectedCellType = selectedCellType;
    }

    private void startLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (running) {
                    engine.step();
                    invalidate();
                    updateStatus();
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
                    cellPaint.setColor(cell.getType().getColor());

                    float left = x * cellWidth;
                    float top = y * cellHeight;
                    float right = (x + 1) * cellWidth;
                    float bottom = (y + 1) * cellHeight;

                    canvas.drawRect(left, top, right, bottom, cellPaint);

                    if (owners[x][y] == 1) {
                        playerOverlayPaint.setColor(Color.argb(70, 0, 0, 255));
                        canvas.drawRect(left, top, right, bottom, playerOverlayPaint);
                    } else if (owners[x][y] == 2) {
                        playerOverlayPaint.setColor(Color.argb(70, 255, 0, 0));
                        canvas.drawRect(left, top, right, bottom, playerOverlayPaint);
                    }
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
                owners[x][y] = currentPlayer;

                currentPlayer = currentPlayer == 1 ? 2 : 1;

                invalidate();
                updateStatus();
            }
        }

        return true;
    }

    public void toggleRunning() {
        running = !running;
        updateStatus();
    }

    private void updateStatus() {
        if (statusText == null) return;

        int player1Cells = 0;
        int player2Cells = 0;

        Grid grid = engine.getGrid();

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                if (grid.getCell(x, y) != null) {
                    if (owners[x][y] == 1) {
                        player1Cells++;
                    } else if (owners[x][y] == 2) {
                        player2Cells++;
                    }
                }
            }
        }

        statusText.setText(
                "Spieler " + currentPlayer + " ist dran | " +
                        "P1: " + player1Cells + " | " +
                        "P2: " + player2Cells +
                        (running ? " | Läuft" : " | Pausiert")
        );
    }
}