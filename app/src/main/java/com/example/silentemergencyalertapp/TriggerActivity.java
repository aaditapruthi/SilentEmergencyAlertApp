package com.example.silentemergencyalertapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TriggerActivity extends AppCompatActivity {

    Button back;

    private int pressCount = 0;
    private long lastPressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);

        back=findViewById(R.id.btn);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {

            long currentTime = System.currentTimeMillis();

            if (currentTime - lastPressTime < 2000) {
                pressCount++;
            } else {
                pressCount = 1;
            }

            lastPressTime = currentTime;

            if (pressCount == 3) {
                triggerAlert();
                pressCount = 0;
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void triggerAlert() {
        Toast.makeText(this, "Emergency Triggered!", Toast.LENGTH_SHORT).show();
        startService(new Intent(this, TriggerService.class));
    }
}