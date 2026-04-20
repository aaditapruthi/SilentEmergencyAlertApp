package com.example.silentemergencyalertapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
        btnTrigger = findViewById(R.id.btnTrigger);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        String name = sharedPreferences.getString("name", "User");
        tvWelcome.setText("Welcome, " + name);

        btnContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, ContactsActivity.class);
                startActivity(intent);
            }
        });

        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, AlertActivity.class);
                startActivity(intent);
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });


        btnTrigger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, TriggerActivity.class);
                startActivity(intent);
            }
        });

        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
    }
}