package com.example.silentemergencyalertapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    TextView tvWelcome;
    Button btnContacts, btnAlert, btnHistory, btnTrigger,btnQuit;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvWelcome = findViewById(R.id.tvWelcome);
        btnContacts = findViewById(R.id.btnContacts);
        btnAlert = findViewById(R.id.btnAlert);
        btnHistory = findViewById(R.id.btnHistory);
        btnQuit = findViewById(R.id.btnQuit);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        String name = sharedPreferences.getString("name", "User");
        tvWelcome.setText("Welcome, " + name);

        btnContacts.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, ContactsActivity.class);
            startActivity(intent);
        });

        btnAlert.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, AlertActivity.class);
            startActivity(intent);
        });

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(DashboardActivity.this, HistoryActivity.class);
            startActivity(intent);
        });

        btnTrigger = findViewById(R.id.btnTrigger);

        btnTrigger.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, TriggerActivity.class));
        });

        btnQuit.setOnClickListener(v -> {
            finishAffinity();
        });
    }
}