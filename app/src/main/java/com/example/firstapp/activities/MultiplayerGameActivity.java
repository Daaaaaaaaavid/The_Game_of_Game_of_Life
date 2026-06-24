package com.example.firstapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

        TextView status = new TextView(this);
        status.setText("Raum: " + roomId + " | Spieler " + playerId);

        MultiplayerGameView gameView =
                new MultiplayerGameView(this, roomId, playerId, isHost, status);

        LinearLayout toolbar = new LinearLayout(this);

        addButton(toolbar, "🔥", CellType.FIRE, gameView);
        addButton(toolbar, "💧", CellType.WATER, gameView);
        addButton(toolbar, "🟫", CellType.EARTH, gameView);
        addButton(toolbar, "🌱", CellType.PLANT, gameView);

        Button startPause = new Button(this);
        startPause.setText("Start/Pause");
        startPause.setOnClickListener(v -> gameView.toggleRunning());
        toolbar.addView(startPause);

        root.addView(status);
        root.addView(gameView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1
        ));
        root.addView(toolbar);

        setContentView(root);
    }

    private void addButton(
            LinearLayout toolbar,
            String text,
            CellType type,
            MultiplayerGameView gameView
    ) {
        Button button = new Button(this);
        button.setText(text);
        button.setOnClickListener(v -> gameView.setSelectedCellType(type));
        toolbar.addView(button);
    }
}