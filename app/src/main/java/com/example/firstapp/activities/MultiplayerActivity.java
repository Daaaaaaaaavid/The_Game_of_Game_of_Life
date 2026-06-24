
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
            String roomId = String.valueOf(System.currentTimeMillis()).substring(8);

            Intent intent = new Intent(this, MultiplayerGameActivity.class);
            intent.putExtra("roomId", roomId);
            intent.putExtra("playerId", 1);
            intent.putExtra("isHost", true);
            startActivity(intent);
        });

        btnJoinGame.setOnClickListener(v -> {
            String roomId = "1234"; // später aus EditText lesen

            Intent intent = new Intent(this, MultiplayerGameActivity.class);
            intent.putExtra("roomId", roomId);
            intent.putExtra("playerId", 2);
            intent.putExtra("isHost", false);
            startActivity(intent);
        });

        btnRandomMatch.setOnClickListener(v -> {
            Intent intent = new Intent(MultiplayerActivity.this, MultiplayerGameActivity.class);
            startActivity(intent);
        });
    }
}