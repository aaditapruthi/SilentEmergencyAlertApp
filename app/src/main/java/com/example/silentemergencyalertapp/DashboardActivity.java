package com.example.silentemergencyalertapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class DashboardActivity extends AppCompatActivity {

    TextView tvWelcome;

    Button btnContacts,
            btnAlert,
            btnHistory,
            btnTrigger,
            btnStealth,
            btnQuit;

    SharedPreferences sharedPreferences;

    private static final int LOCATION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvWelcome = findViewById(R.id.tvWelcome);

        btnContacts = findViewById(R.id.btnContacts);
        btnAlert = findViewById(R.id.btnAlert);
        btnHistory = findViewById(R.id.btnHistory);
        btnTrigger = findViewById(R.id.btnTrigger);
        btnStealth = findViewById(R.id.btnStealth);
        btnQuit = findViewById(R.id.btnQuit);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        String name = sharedPreferences.getString("name", "User");
        tvWelcome.setText("Welcome, " + name);

        // 🔥 REQUEST LOCATION PERMISSION HERE (IMPORTANT FIX)
        requestLocationPermission();

        btnContacts.setOnClickListener(v ->
                startActivity(new Intent(this, ContactsActivity.class))
        );

        btnAlert.setOnClickListener(v ->
                startActivity(new Intent(this, AlertActivity.class))
        );

        btnHistory.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class))
        );

        btnTrigger.setOnClickListener(v ->
                startActivity(new Intent(this, TriggerActivity.class))
        );

        btnStealth.setOnClickListener(v ->
                startActivity(new Intent(this, CalculatorActivity.class))
        );

        btnQuit.setOnClickListener(v -> finishAffinity());
    }

    // 🚨 LOCATION PERMISSION REQUEST
    private void requestLocationPermission() {

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_REQUEST_CODE
            );
        }
    }

    // 📌 HANDLE USER RESPONSE
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {

            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.d("PERMISSION", "Location permission granted");

            } else {

                Log.e("PERMISSION", "Location permission denied");
            }
        }
    }
}