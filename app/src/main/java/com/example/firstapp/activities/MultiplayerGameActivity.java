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
        root.setBackgroundColor(0xFF111827);

        // Permanente Raum-ID Anzeige (wird nicht überschrieben)
        TextView roomIdDisplay = new TextView(this);
        roomIdDisplay.setTextColor(Color.GREEN);
        roomIdDisplay.setTextSize(24);
        roomIdDisplay.getPaint().setFakeBoldText(true);
        roomIdDisplay.setGravity(Gravity.CENTER);
        roomIdDisplay.setPadding(0, 50, 0, 10);
        roomIdDisplay.setText("RAUM-ID: " + roomId);
        root.addView(roomIdDisplay);

        // Status Header (für dynamische Updates wie Spielstand/Status)
        TextView status = new TextView(this);
        status.setTextColor(Color.WHITE);
        status.setPadding(30, 0, 30, 30);
        status.setGravity(Gravity.CENTER);
        status.setTextSize(14);
        status.setText("Verbindung zum Raum wird hergestellt...");

        MultiplayerGameView gameView = new MultiplayerGameView(this, roomId, playerId, isHost, status);

        // Element-Steuerung
        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setOrientation(LinearLayout.HORIZONTAL);
        toolbar.setGravity(Gravity.CENTER);
        toolbar.setPadding(10, 20, 10, 20);

        addButton(toolbar, "🔥", CellType.FIRE, gameView);
        addButton(toolbar, "💧", CellType.WATER, gameView);
        addButton(toolbar, "🟫", CellType.EARTH, gameView);
        addButton(toolbar, "🌱", CellType.PLANT, gameView);

        // Simulation-Steuerung
        Button startPause = new Button(this);
        startPause.setText("RUN/STOP");
        startPause.setOnClickListener(v -> gameView.toggleRunning());
        toolbar.addView(startPause);

        Button btnExit = new Button(this);
        btnExit.setText("EXIT");
        btnExit.setOnClickListener(v -> finish());
        toolbar.addView(btnExit);

        root.addView(status);
        root.addView(gameView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
        root.addView(toolbar);

        setContentView(root);
    }

    private void addButton(LinearLayout toolbar, String text, CellType type, MultiplayerGameView gameView) {
        Button button = new Button(this);
        button.setText(text);
        button.setPadding(10, 0, 10, 0);
        button.setOnClickListener(v -> gameView.setSelectedCellType(type));
        toolbar.addView(button);
    }
}
