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

    private final int cols = 40; 
    private final int rows = 60;

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

    public MultiplayerGameView(Context context, String roomId, int playerId, boolean isHost, TextView statusText) {
        super(context);
        this.playerId = playerId;
        this.isHost = isHost;
        this.statusText = statusText;

        // Firebase Initialisierung mit der korrekten URL
        roomRef = FirebaseDatabase
                .getInstance("https://the-game-of-game-of-life-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("rooms")
                .child(roomId);

        if (isHost) {
            roomRef.child("running").setValue(false);
            roomRef.child("cells").removeValue();
        }

        listenForRunningState();
        listenForCells();
        startLoop();
    }

    public void setSelectedCellType(CellType type) {
        this.selectedCellType = type;
    }

    public void toggleRunning() {
        this.running = !this.running;
        roomRef.child("running").setValue(this.running);
    }

    private void listenForRunningState() {
        roomRef.child("running").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Boolean value = snapshot.getValue(Boolean.class);
                running = value != null && value;
                invalidate();
            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    private void listenForCells() {
        roomRef.child("cells").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // Host übernimmt nur Fremdzellen (vom Client), Client übernimmt alles
                updateLocalGrid(snapshot);
            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }

    private void updateLocalGrid(DataSnapshot snapshot) {
        if (!isHost) {
            engine.getGrid().clear();
            for (int x = 0; x < cols; x++) for (int y = 0; y < rows; y++) owners[x][y] = 0;
        }

        for (DataSnapshot cellSnap : snapshot.getChildren()) {
            try {
                String[] parts = cellSnap.getKey().split("_");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                Integer owner = cellSnap.child("owner").getValue(Integer.class);
                String typeName = cellSnap.child("type").getValue(String.class);

                // Wenn Host: Nur Daten vom Client (Player 2) übernehmen
                // Wenn Client: Alles übernehmen
                if (!isHost || (owner != null && owner != playerId)) {
                    if (typeName != null) {
                        engine.setCell(x, y, CellType.valueOf(typeName));
                        owners[x][y] = (owner != null) ? owner : 0;
                    }
                }
            } catch (Exception ignored) {}
        }
        invalidate();
    }

    private void startLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (running && isHost) {
                    engine.step();
                    syncGridToFirebase();
                }
                updateStatus();
                invalidate();
                handler.postDelayed(this, 500);
            }
        }, 500);
    }

    private void syncGridToFirebase() {
        Map<String, Object> cells = new HashMap<>();
        Grid grid = engine.getGrid();
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                Cell cell = grid.getCell(x, y);
                if (cell != null) {
                    if (owners[x][y] == 0) owners[x][y] = 1; // Zellen aus Evolution gehören Host
                    Map<String, Object> data = new HashMap<>();
                    data.put("type", cell.getType().name());
                    data.put("owner", owners[x][y]);
                    cells.put(x + "_" + y, data);
                } else {
                    owners[x][y] = 0;
                }
            }
        }
        roomRef.child("cells").setValue(cells);
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
                    paint.setStyle(owners[x][y] == playerId ? Paint.Style.FILL_AND_STROKE : Paint.Style.FILL);
                    canvas.drawRect(x * cellWidth + 1, y * cellHeight + 1, (x + 1) * cellWidth - 1, (y + 1) * cellHeight - 1, paint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            int x = (int) (event.getX() / cellWidth);
            int y = (int) (event.getY() / cellHeight);
            if (x >= 0 && x < cols && y >= 0 && y < rows) {
                engine.setCell(x, y, selectedCellType);
                owners[x][y] = playerId;
                
                Map<String, Object> data = new HashMap<>();
                data.put("type", selectedCellType.name());
                data.put("owner", playerId);
                roomRef.child("cells").child(x + "_" + y).setValue(data);
                invalidate();
            }
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        cellWidth = (float) w / cols;
        cellHeight = (float) h / rows;
    }

    private void updateStatus() {
        if (statusText == null) return;
        int own = 0, enemy = 0;
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                if (engine.getGrid().getCell(x, y) != null) {
                    if (owners[x][y] == playerId) own++;
                    else if (owners[x][y] != 0) enemy++;
                }
            }
        }
        statusText.setText("Spieler " + playerId + " | Eigene: " + own + " | Gegner: " + enemy + (running ? " | LÄUFT" : " | PAUSE"));
    }
}
