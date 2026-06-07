package com.example.silentemergencyalertapp;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;

import com.google.android.gms.location.*;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TriggerService extends Service {

    DBHelper db;
    FusedLocationProviderClient fusedLocationClient;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        db = new DBHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        fetchLocationAndSendSMS();

        return START_NOT_STICKY;
    }

    private void fetchLocationAndSendSMS() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            sendSMS("Emergency! I need help.\nLocation: Permission not granted");
            stopSelf();
            return;
        }

        // 🔥 NEW: More reliable than getLastLocation()
        fusedLocationClient.getCurrentLocation(
                        Priority.PRIORITY_HIGH_ACCURACY,
                        null
                )
                .addOnSuccessListener(location -> {

                    String message = buildMessage(location);
                    sendSMS(message);

                })
                .addOnFailureListener(e -> {

                    Log.e("SOS", "Location failed: " + e.getMessage());
                    sendSMS("Emergency! I need help.\nLocation: Not available");

                });
    }

    private String buildMessage(Location location) {

        String base = "Emergency! I need help.";

        if (location != null) {

            String link = "https://maps.google.com/?q=" +
                    location.getLatitude() + "," +
                    location.getLongitude();

            base += "\nLocation: " + link;

        } else {
            base += "\nLocation: Not available";
        }

        return base;
    }

    private void sendSMS(String message) {

        Cursor cursor = db.getAllContacts();

        if (cursor != null && cursor.moveToFirst()) {

            do {

                String phone = cursor.getString(2);

                SmsManager.getDefault().sendTextMessage(
                        phone,
                        null,
                        message,
                        null,
                        null
                );

                String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                String time = new SimpleDateFormat("hh:mm a").format(new Date());

                db.insertHistory(phone, date, time);

            } while (cursor.moveToNext());

            cursor.close();
        }

        callFirstContact();
        stopSelf();
    }

    private void callFirstContact() {

        Cursor cursor = db.getAllContacts();

        if (cursor != null && cursor.moveToFirst()) {

            String phone = cursor.getString(2);
            cursor.close();

            try {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(android.net.Uri.parse("tel:" + phone));
                callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED) {

                    startActivity(callIntent);
                }

            } catch (Exception e) {
                Log.e("SOS", "Call failed: " + e.getMessage());
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}