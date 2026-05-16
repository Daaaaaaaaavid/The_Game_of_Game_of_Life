package com.example.firstapp.activities;
import com.example.firstapp.R;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        setupClickListeners();
    }

    private void initViews() {

        btnBack = findViewById(R.id.btnBack);
    }

    private void setupClickListeners() {

        btnBack.setOnClickListener(v -> finish());
    }
}
