package com.example.silentemergencyalertapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationHelper {

    public interface LocationCallback {
        void onLocationReceived(double latitude, double longitude);
        void onLocationFailed();
    }

    @SuppressLint("MissingPermission")
    public static void getLocation(Context context, LocationCallback callback) {

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            callback.onLocationFailed();
            return;
        }

        FusedLocationProviderClient fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(context);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {

                    if (location != null) {

                        callback.onLocationReceived(
                                location.getLatitude(),
                                location.getLongitude()
                        );

                    } else {
                        callback.onLocationFailed();
                    }
                })
                .addOnFailureListener(e -> callback.onLocationFailed());
    }
}