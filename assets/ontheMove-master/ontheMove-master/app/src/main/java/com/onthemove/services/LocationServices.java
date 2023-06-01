package com.onthemove.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.gson.Gson;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.commons.utils.AppPref;
import com.onthemove.network.RetroClient;
import com.onthemove.requestClasses.UpdateLocationRequest;
import com.onthemove.responseClasses.UpdateLocationResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationServices extends Service {

    private static final String TAG = "LocationServices";

    private static LocationServices locationServices;
    private Context context;
    private AppPref appPref;
    final Handler handler = new Handler();
    double latitude;
    double longitude;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();

        locationServices = this;
        context = this;
        appPref = AppPref.getInstance(this);
        handler.postDelayed(runnable, 30000);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateLocationToServer();
            handler.postDelayed(runnable, 30000);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        locationServices = this;

        initLocation();

        startLocationUpdate();

        return START_NOT_STICKY;
    }


    public static void start(Context context) {
        if (locationServices == null) {
            context.startService(new Intent(context, LocationServices.class));
        } else {
            locationServices.startLocationUpdate();
        }
    }


    private void initLocation() {

        fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (appPref.isLogin()) {

                        appPref.set(AppPref.LAT, location.getLatitude() + "");
                        appPref.set(AppPref.LNG, location.getLongitude() + "");

//                        updateLocationToFirebase(location.getLatitude() + "", location.getLongitude() + "");
                    }
                    break;
                }
            }
        };

    }


    private void startLocationUpdate() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);
        if (fusedLocationClient != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        }
    }


    private void updateLocationToServer() {

        String lat = appPref.getString(AppPref.LAT, "");
        String lng = appPref.getString(AppPref.LNG, "");

        AppLog.e(TAG, "LAT LNG IN SERVICES: " + lat + " &  " + lng);

        RetroClient.getApiService().updateCurrentLocation(new UpdateLocationRequest(lat, lng)).enqueue(new Callback<UpdateLocationResponse>() {
            @Override
            public void onResponse(Call<UpdateLocationResponse> call, Response<UpdateLocationResponse> response) {
                AppLog.e(TAG, "onResponse : " + response.body());

                AppLog.e(TAG, "UPDATE LOCATION RESPONSE MSG IN SERVICE = " + response.message());
                AppLog.e(TAG, "UPDATE LOCATION RESPONSE CODE IN SERVICE = " + response.code());
                AppLog.e(TAG, "UPDATE LOCATION RESPONSE DATA IN SERVICE = " + new Gson().toJson(response.body()));


                if (response != null) {

                    UpdateLocationResponse updateLocationResponse = response.body();

                    if (updateLocationResponse!= null){

                        boolean status = updateLocationResponse.isStatus();
                        if (status) {
                            AppLog.e(TAG, "SUCCESSFUL UPDATE LOCATION");
                        }
                    }
                }


            }

            @Override
            public void onFailure(Call<UpdateLocationResponse> call, Throwable t) {
                AppLog.e(TAG, "onFailure : " + t.toString());
            }
        });

    }


    public static void stop() {
        if (locationServices != null) {
            locationServices.stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationServices = null;
        stopLocationUpdate();
        AppLog.e(TAG, "onDestroy()");
        handler.removeCallbacks(runnable);
    }

    private void stopLocationUpdate() {
        if (fusedLocationClient != null)
            fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
