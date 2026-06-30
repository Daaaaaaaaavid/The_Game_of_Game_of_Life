package com.example.firstapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.firstapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MultiplayerActivity extends AppCompatActivity {

    private Button btnHostGame;
    private Button btnJoinGame;
    private EditText etRoomId;
    private ImageButton btnBack;

    // Firebase URL
    private final String dbUrl = "https://the-game-of-game-of-life-default-rtdb.europe-west1.firebasedatabase.app";

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
        etRoomId = findViewById(R.id.etRoomId);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        // HOST erstellt einen Raum
        btnHostGame.setOnClickListener(v -> {
            String roomId = String.valueOf((int) (Math.random() * 90000) + 10000);
            
            DatabaseReference roomRef = FirebaseDatabase.getInstance(dbUrl).getReference("rooms").child(roomId);
            roomRef.child("status").setValue("open"); // Raum registrieren

            Intent intent = new Intent(this, MultiplayerGameActivity.class);
            intent.putExtra("roomId", roomId);
            intent.putExtra("playerId", 1);
            intent.putExtra("isHost", true);
            startActivity(intent);
        });

        // JOIN prüft die ID
        btnJoinGame.setOnClickListener(v -> {
            String roomId = etRoomId.getText().toString().trim();

            if (roomId.isEmpty()) {
                Toast.makeText(this, "Bitte gib eine Raum-ID ein!", Toast.LENGTH_SHORT).show();
                return;
            }

            btnJoinGame.setEnabled(false);
            btnJoinGame.setText("Suche Raum...");

            DatabaseReference roomRef = FirebaseDatabase.getInstance(dbUrl).getReference("rooms").child(roomId);
            roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // Raum existiert -> Beitreten
                        Intent intent = new Intent(MultiplayerActivity.this, MultiplayerGameActivity.class);
                        intent.putExtra("roomId", roomId);
                        intent.putExtra("playerId", 2);
                        intent.putExtra("isHost", false);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MultiplayerActivity.this, "Raum nicht gefunden!", Toast.LENGTH_LONG).show();
                    }
                    btnJoinGame.setEnabled(true);
                    btnJoinGame.setText("JOIN GAME");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(MultiplayerActivity.this, "Fehler: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    btnJoinGame.setEnabled(true);
                    btnJoinGame.setText("JOIN GAME");
                }
            });
        });
    }
}
