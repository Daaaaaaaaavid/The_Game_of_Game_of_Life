package com.example.firstapp.activities;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.firstapp.fragments.GameView;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gameView = new GameView(this);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);

        LinearLayout toolbar = new LinearLayout(this);
        toolbar.setOrientation(LinearLayout.HORIZONTAL);
        toolbar.setGravity(Gravity.CENTER);
        toolbar.setPadding(8, 8, 8, 8);

        addCellButton(toolbar, "Standard", GameView.SelectedCellType.STANDARD);
        addCellButton(toolbar, "Feuer", GameView.SelectedCellType.FIRE);
        addCellButton(toolbar, "Wasser", GameView.SelectedCellType.WATER);
        addCellButton(toolbar, "Erde", GameView.SelectedCellType.EARTH);
        addCellButton(toolbar, "Pflanze", GameView.SelectedCellType.PLANT);

        root.addView(gameView, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1
        ));

        root.addView(toolbar, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        setContentView(root);
    }

    private void addCellButton(
            LinearLayout toolbar,
            String text,
            GameView.SelectedCellType cellType
    ) {
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