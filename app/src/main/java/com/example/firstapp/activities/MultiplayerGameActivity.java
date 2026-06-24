package com.example.firstapp.activities;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firstapp.fragments.MultiplayerGameView;
import com.example.firstapp.game.CellType;

public class MultiplayerGameActivity extends AppCompatActivity {

    private MultiplayerGameView gameView;
    private TextView statusText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new MultiplayerGameView(this);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        statusText = new TextView(this);
        statusText.setGravity(Gravity.CENTER);
        statusText.setTextSize(16);
        statusText.setText("Multiplayer gestartet");
        gameView.setStatusText(statusText);

        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setOrientation(LinearLayout.HORIZONTAL);
        toolbar.setGravity(Gravity.CENTER);
        toolbar.setPadding(8, 8, 8, 8);

        addCellButton(toolbar, "🔥", CellType.FIRE);
        addCellButton(toolbar, "💧", CellType.WATER);
        addCellButton(toolbar, "🟫", CellType.EARTH);
        addCellButton(toolbar, "🌱", CellType.PLANT);

        Button pauseButton = new Button(this);
        pauseButton.setText("Pause");
        pauseButton.setOnClickListener(v -> gameView.toggleRunning());
        toolbar.addView(pauseButton, new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));

        root.addView(statusText);
        root.addView(gameView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1
        ));
        root.addView(toolbar);

        setContentView(root);
    }

    private void addCellButton(LinearLayout toolbar, String text, CellType cellType) {
        Button button = new Button(this);
        button.setText(text);
        button.setOnClickListener(v -> gameView.setSelectedCellType(cellType));

        toolbar.addView(button, new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));
    }
}