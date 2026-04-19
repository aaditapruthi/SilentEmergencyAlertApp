package com.example.silentemergencyalertapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AlertActivity extends AppCompatActivity {

    Button btnSMS, btnCall, btnReturn;
    DBHelper db;

    private static final int SMS_PERMISSION_CODE = 1;
    private static final int CALL_PERMISSION_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        btnSMS = findViewById(R.id.btnSendSMS);
        btnCall = findViewById(R.id.btnCall);
        btnReturn=findViewById(R.id.btnReturn);

        db = new DBHelper(this);

        btnSMS.setOnClickListener(v -> checkSMSPermission());
        btnCall.setOnClickListener(v -> checkCallPermission());

        btnReturn.setOnClickListener(v -> {
            finish();
        });
    }

    private void checkSMSPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_CODE);
        } else {
            sendSMS();
        }
    }

    private void checkCallPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PERMISSION_CODE);
        } else {
            makeCall();
        }
    }

    private void sendSMS() {
        Cursor cursor = db.getAllContacts();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No contacts found", Toast.LENGTH_SHORT).show();
            return;
        }

        while (cursor.moveToNext()) {
            String phone = cursor.getString(2);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null,
                    "Emergency! Please help me.", null, null);
        }

        Toast.makeText(this, "SMS sent to all contacts", Toast.LENGTH_SHORT).show();
    }

    private void makeCall() {
        Cursor cursor = db.getAllContacts();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No contacts found", Toast.LENGTH_SHORT).show();
            return;
        }

        cursor.moveToFirst();
        String phone = cursor.getString(2);

        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phone));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMS();
            } else {
                Toast.makeText(this, "SMS Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == CALL_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall();
            } else {
                Toast.makeText(this, "Call Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}