package com.example.silentemergencyalertapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;

public class AlertActivity extends AppCompatActivity {

    MaterialCardView cardSOS, cardAmbulance, cardPolice, cardFire, cardBack;
    DBHelper db;

    private static final int CALL_PERMISSION_CODE = 1;
    private String pendingNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        db = new DBHelper(this);

        cardSOS = findViewById(R.id.cardSOS);
        cardAmbulance = findViewById(R.id.cardAmbulance);
        cardPolice = findViewById(R.id.cardPolice);
        cardFire = findViewById(R.id.cardFire);
        cardBack = findViewById(R.id.cardBack);

        cardSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callFirstContact();
            }
        });
        cardAmbulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCallPermission("108");
            }
        });
        cardPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCallPermission("100");
            }
        });
        cardFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCallPermission("101");
            }
        });
        cardBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void callFirstContact() {
        Cursor cursor = db.getAllContacts();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No emergency contacts saved", Toast.LENGTH_SHORT).show();
            return;
        }

        cursor.moveToFirst();
        String phone = cursor.getString(2);

        checkCallPermission(phone);
    }

    private void checkCallPermission(String number) {
        pendingNumber = number;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PERMISSION_CODE);
        } else {
            makeCall(number);
        }
    }

    private void makeCall(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CALL_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall(pendingNumber);
            } else {
                Toast.makeText(this, "Call Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}