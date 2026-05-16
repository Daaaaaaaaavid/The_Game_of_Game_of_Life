package com.example.firstapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firstapp.R;

public class MainActivity extends AppCompatActivity {

    boolean isOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button singleplayerButton = findViewById(R.id.btnSingleplayer);
        Button multiplayerButton = findViewById(R.id.btnMultiplayer);
        Button settingsButton = findViewById(R.id.btnSettings);

        singleplayerButton.setOnClickListener(v -> {
            startActivity(new Intent(this, SingleplayerActivity.class));
        });

        multiplayerButton.setOnClickListener(v -> {
            startActivity(new Intent(this, MultiplayerActivity.class));
        });

        settingsButton.setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("state", isOn);
    }
}