
package com.example.firstapp.activities;
import com.example.firstapp.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

//import com.yourstudio.gameoflife.R;

public class MultiplayerActivity extends AppCompatActivity {

    private Button btnHostGame;
    private Button btnJoinGame;
    private Button btnRandomMatch;

    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);

        initViews();
        setupClickListeners();
    }

    private void initViews() {

        btnHostGame = findViewById(R.id.btnHostGame);
        btnJoinGame = findViewById(R.id.btnJoinGame);
        btnRandomMatch = findViewById(R.id.btnRandomMatch);

        btnBack = findViewById(R.id.btnBack);
    }

    private void setupClickListeners() {

        btnBack.setOnClickListener(v -> finish());

        btnHostGame.setOnClickListener(v -> {
            Toast.makeText(this,
                    "Host Lobby Placeholder",
                    Toast.LENGTH_SHORT).show();
        });

        btnJoinGame.setOnClickListener(v -> {
            Toast.makeText(this,
                    "Join Lobby Placeholder",
                    Toast.LENGTH_SHORT).show();
        });

        btnRandomMatch.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MultiplayerActivity.this,
                    GameActivity.class
            );

            startActivity(intent);
        });
    }
}