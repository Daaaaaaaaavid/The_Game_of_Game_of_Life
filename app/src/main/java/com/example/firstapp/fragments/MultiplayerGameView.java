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
import com.google.firebase.database.*;

import java.util.HashMap;
import java.util.Map;

public class MultiplayerGameView extends View {

    private final int cols = 80;
    private final int rows = 160;

    private final GameEngine engine = new GameEngine(cols, rows);
    private final int[][] owners = new int[cols][rows];

    private final Paint paint = new Paint();
    private final Handler handler = new Handler();

    private final DatabaseReference roomRef;
    private final int playerId;
    private final boolean isHost;
    private final TextView statusText;

    private CellType selectedCellType = CellType.FIRE;

    private float cellWidth;
    private float cellHeight;

    private boolean running = false;
    private boolean applyingRemoteUpdate = false;

    public MultiplayerGameView(
            Context context,
            String roomId,
            int playerId,
            boolean isHost,
            TextView statusText
    ) {
        super(context);

        this.playerId = playerId;
        this.isHost = isHost;
        this.statusText = statusText;

        roomRef = FirebaseDatabase
                .getInstance()
                .getReference("rooms")
                .child(roomId);

        listenForCells();
        listenForRunningState();

        if (isHost) {
            roomRef.child("host").setValue(playerId);
        }

        startLoop();
    }

    public void setSelectedCellType(CellType type) {
        selectedCellType = type;
    }

    public void toggleRunning() {
        running = !running;
        roomRef.child("running").setValue(running);
        updateStatus();
    }

    private void listenForRunningState() {
        roomRef.child("running").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Boolean value = snapshot.getValue(Boolean.class);
                running = value != null && value;
                updateStatus();
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    private void listenForCells() {
        roomRef.child("cells").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                applyingRemoteUpdate = true;

                engine.getGrid().clear();

                for (int x = 0; x < cols; x++) {
                    for (int y = 0; y < rows; y++) {
                        owners[x][y] = 0;
                    }
                }

                for (DataSnapshot cellSnapshot : snapshot.getChildren()) {
                    String key = cellSnapshot.getKey();
                    if (key == null || !key.contains("_")) continue;

                    String[] parts = key.split("_");
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);

                    String typeName = cellSnapshot.child("type").getValue(String.class);
                    Integer owner = cellSnapshot.child("owner").getValue(Integer.class);

                    if (typeName == null) continue;

                    CellType type = CellType.valueOf(typeName);

                    engine.setCell(x, y, type);
                    owners[x][y] = owner == null ? 0 : owner;
                }

                applyingRemoteUpdate = false;
                invalidate();
                updateStatus();
            }

            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    private void startLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (running && isHost && !applyingRemoteUpdate) {
                    engine.step();
                    uploadFullGrid();
                    invalidate();
                }

                updateStatus();
                handler.postDelayed(this, 250);
            }
        }, 250);
    }

    private void uploadFullGrid() {
        Map<String, Object> cells = new HashMap<>();
        Grid grid = engine.getGrid();

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                Cell cell = grid.getCell(x, y);

                if (cell != null) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("type", cell.getType().name());
                    data.put("owner", owners[x][y]);
                    cells.put(x + "_" + y, data);
                }
            }
        }

        roomRef.child("cells").setValue(cells);
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
        if (event.getAction() != MotionEvent.ACTION_DOWN &&
                event.getAction() != MotionEvent.ACTION_MOVE) {
            return true;
        }

        int x = (int) (event.getX() / cellWidth);
        int y = (int) (event.getY() / cellHeight);

        if (x < 0 || x >= cols || y < 0 || y >= rows) {
            return true;
        }

        engine.setCell(x, y, selectedCellType);
        owners[x][y] = playerId;

        Map<String, Object> data = new HashMap<>();
        data.put("type", selectedCellType.name());
        data.put("owner", playerId);

        roomRef.child("cells").child(x + "_" + y).setValue(data);

        invalidate();
        updateStatus();

        return true;
    }

    private void updateStatus() {
        if (statusText == null) return;

        int ownCells = 0;
        int enemyCells = 0;

        Grid grid = engine.getGrid();

        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                if (grid.getCell(x, y) != null) {
                    if (owners[x][y] == playerId) {
                        ownCells++;
                    } else if (owners[x][y] != 0) {
                        enemyCells++;
                    }
                }
            }
        }

        statusText.setText(
                "Spieler " + playerId +
                        (isHost ? " | Host" : " | Client") +
                        " | Eigene Zellen: " + ownCells +
                        " | Gegner: " + enemyCells +
                        (running ? " | Läuft" : " | Pausiert")
        );
    }
}