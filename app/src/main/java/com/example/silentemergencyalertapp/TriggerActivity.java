package com.example.silentemergencyalertapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class TriggerActivity extends AppCompatActivity implements SensorEventListener {

    ImageButton back;
    TextView tvTriggerInfo;
    Button btnCancelSOS;

    private static final int LOCATION_PERMISSION_CODE = 101;

    private int pressCount = 0;
    private long lastPressTime = 0;

    private CountDownTimer countDownTimer;
    private boolean sosCancelled = false;

    // Shake Detection
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private long lastShakeTime = 0;
    private static final float SHAKE_THRESHOLD = 15.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);

        checkLocationPermission();

        back = findViewById(R.id.btnbk);
        tvTriggerInfo = findViewById(R.id.tvTriggerInfo);
        btnCancelSOS = findViewById(R.id.btnCancelSOS);

        // Initialize Accelerometer
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCancelSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sosCancelled = true;

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }

                tvTriggerInfo.setText("SOS Cancelled");
                btnCancelSOS.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (accelerometer != null) {
            sensorManager.registerListener(
                    this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL
            );
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
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

        // Prevent multiple countdowns
        if (btnCancelSOS.getVisibility() == View.VISIBLE) {
            return;
        }

        sosCancelled = false;

        btnCancelSOS.setVisibility(View.VISIBLE);

        countDownTimer = new CountDownTimer(5000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                tvTriggerInfo.setText(
                        "Sending SOS in " +
                                (millisUntilFinished / 1000) +
                                " seconds..."
                );
            }

            @Override
            public void onFinish() {

                if (!sosCancelled) {

                    tvTriggerInfo.setText("SOS Sent");

                    startService(
                            new Intent(
                                    TriggerActivity.this,
                                    TriggerService.class
                            )
                    );

                    Toast.makeText(
                            TriggerActivity.this,
                            "Emergency Triggered!",
                            Toast.LENGTH_SHORT
                    ).show();
                }

                btnCancelSOS.setVisibility(View.GONE);
            }
        };

        countDownTimer.start();
    }

    private void checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                    },
                    LOCATION_PERMISSION_CODE
            );
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        double acceleration =
                Math.sqrt(x * x + y * y + z * z);

        if (acceleration > SHAKE_THRESHOLD) {

            long currentTime = System.currentTimeMillis();

            if (currentTime - lastShakeTime > 3000) {

                lastShakeTime = currentTime;

                Toast.makeText(
                        this,
                        "Shake Detected!",
                        Toast.LENGTH_SHORT
                ).show();

                triggerAlert();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not needed
    }
}