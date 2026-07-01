package com.example.firstapp.activities;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firstapp.fragments.MultiplayerGameView;
import com.example.firstapp.game.CellType;

public class MultiplayerGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String roomId = getIntent().getStringExtra("roomId");
        int playerId = getIntent().getIntExtra("playerId", 1);
        boolean isHost = getIntent().getBooleanExtra("isHost", false);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.WHITE);

        // Permanente Raum-ID Anzeige
        TextView roomIdDisplay = new TextView(this);
        roomIdDisplay.setTextColor(Color.parseColor("#2E7D32"));
        roomIdDisplay.setTextSize(24);
        roomIdDisplay.getPaint().setFakeBoldText(true);
        roomIdDisplay.setGravity(Gravity.CENTER);
        roomIdDisplay.setPadding(0, 50, 0, 10);
        roomIdDisplay.setText("RAUM-ID: " + roomId);
        root.addView(roomIdDisplay);

        // Status Header
        TextView status = new TextView(this);
        status.setTextColor(Color.BLACK);
        status.setPadding(30, 0, 30, 30);
        status.setGravity(Gravity.CENTER);
        status.setTextSize(14);
        status.setText("Verbindung zum Raum wird hergestellt...");

        MultiplayerGameView gameView = new MultiplayerGameView(this, roomId, playerId, isHost, status);

        // Simulations-Steuerung und Barrier (OBERHALB der Zellbuttons)
        LinearLayout topToolbar = new LinearLayout(this);
        topToolbar.setOrientation(LinearLayout.HORIZONTAL);
        topToolbar.setGravity(Gravity.CENTER);
        topToolbar.setPadding(10, 10, 10, 0);

        Button barrierButton = new Button(this);
        barrierButton.setText("🚧 BARRIER");
        barrierButton.setOnClickListener(v -> gameView.setSelectedCellType(CellType.BARRIER));
        topToolbar.addView(barrierButton, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.2f));

        Button startPause = new Button(this);
        startPause.setText("RUN/STOP");
        startPause.setOnClickListener(v -> gameView.toggleRunning());
        topToolbar.addView(startPause, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));

        Button btnExit = new Button(this);
        btnExit.setText("EXIT");
        btnExit.setOnClickListener(v -> finish());
        topToolbar.addView(btnExit, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.8f));

        // Zell-Steuerung (UNTERHALB der Simulation-Steuerung)
        LinearLayout cellToolbar = new LinearLayout(this);
        cellToolbar.setOrientation(LinearLayout.HORIZONTAL);
        cellToolbar.setGravity(Gravity.CENTER);
        cellToolbar.setPadding(10, 0, 10, 20);

        addButton(cellToolbar, "🔥", CellType.FIRE, gameView);
        addButton(cellToolbar, "💧", CellType.WATER, gameView);
        addButton(cellToolbar, "🟫", CellType.EARTH, gameView);
        addButton(cellToolbar, "🌱", CellType.PLANT, gameView);
        addButton(cellToolbar, "⏳", CellType.SAND, gameView);
        addButton(cellToolbar, "🧪", CellType.ACID, gameView);

        root.addView(status);
        root.addView(gameView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        root.addView(topToolbar);
        root.addView(cellToolbar);

        setContentView(root);
    }

    private void addButton(LinearLayout toolbar, String text, CellType type, MultiplayerGameView gameView) {
        Button button = new Button(this);
        button.setText(text);
        button.setPadding(0, 0, 0, 0);
        button.setOnClickListener(v -> gameView.setSelectedCellType(type));
        toolbar.addView(button, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
    }
}
