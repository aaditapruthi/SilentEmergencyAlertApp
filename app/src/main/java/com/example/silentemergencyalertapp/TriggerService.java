package com.example.silentemergencyalertapp;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.telephony.SmsManager;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TriggerService extends Service {

    DBHelper db;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        db = new DBHelper(this);
        Cursor cursor = db.getAllContacts();

        while (cursor.moveToNext()) {
            String phone = cursor.getString(2);

            SmsManager.getDefault().sendTextMessage(
                    phone,
                    null,
                    "Emergency! I need help.",
                    null,
                    null
            );

            String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            String time = new SimpleDateFormat("hh:mm a").format(new Date());

            db.insertHistory(phone, date, time);
        }

        cursor.close();

        stopSelf();
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}