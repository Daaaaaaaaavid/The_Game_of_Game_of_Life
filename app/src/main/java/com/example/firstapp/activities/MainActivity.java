package com.example.firstapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firstapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class MainActivity extends AppCompatActivity {

    boolean isOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseReference ref =
                FirebaseDatabase.getInstance().getReference("test");

        ref.setValue("Hallo Firebase")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Firebase Write erfolgreich", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Firebase Fehler: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });

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