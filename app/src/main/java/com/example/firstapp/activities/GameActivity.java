package com.example.firstapp.activities;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firstapp.fragments.GameView;
import com.example.firstapp.game.CellType;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameView(this);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        // Top toolbar for special items like Barriers
        LinearLayout topToolbar = new LinearLayout(this);
        topToolbar.setOrientation(LinearLayout.HORIZONTAL);
        topToolbar.setGravity(Gravity.CENTER);
        topToolbar.setPadding(8, 8, 8, 0);

        Button barrierButton = new Button(this);
        barrierButton.setText("🚧 BARRIER");
        barrierButton.setOnClickListener(v -> gameView.setSelectedCellType(CellType.BARRIER));
        topToolbar.addView(barrierButton, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Bottom toolbar for cell types
        LinearLayout cellToolbar = new LinearLayout(this);
        cellToolbar.setOrientation(LinearLayout.HORIZONTAL);
        cellToolbar.setGravity(Gravity.CENTER);
        cellToolbar.setPadding(8, 8, 8, 8);

        addCellButton(cellToolbar, "🔥", CellType.FIRE);
        addCellButton(cellToolbar, "💧", CellType.WATER);
        addCellButton(cellToolbar, "🪨", CellType.EARTH);
        addCellButton(cellToolbar, "🪴", CellType.PLANT);
        addCellButton(cellToolbar, "⏳", CellType.SAND);
        addCellButton(cellToolbar, "🧪", CellType.ACID);

        root.addView(gameView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1
        ));

        root.addView(topToolbar);
        root.addView(cellToolbar);

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