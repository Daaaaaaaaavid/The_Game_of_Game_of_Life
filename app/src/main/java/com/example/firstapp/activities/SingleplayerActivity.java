package com.example.firstapp.activities;

import com.example.firstapp.R;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SingleplayerActivity extends AppCompatActivity {

    private Button btnStartGame;
    private TextView txtDifficulty;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer);

        initViews();
        setupClickListeners();
    }

    private void initViews() {
        btnStartGame = findViewById(R.id.btnStartSingleplayerGame);
        txtDifficulty = findViewById(R.id.txtDifficulty);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        btnStartGame.setOnClickListener(v -> {
            Intent intent = new Intent(
                    SingleplayerActivity.this,
                    GameActivity.class
            );
            startActivity(intent);
        });
    }
}
