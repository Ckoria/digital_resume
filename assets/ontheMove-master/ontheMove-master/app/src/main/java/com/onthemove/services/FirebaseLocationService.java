package com.onthemove.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import com.onthemove.R;
import com.onthemove.activities.MainActivity;
import com.onthemove.activities.MainActivity2;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.commons.utils.AppPref;
import com.onthemove.commons.utils.Constants;
import com.onthemove.network.ApiService;
import com.onthemove.network.RetroClient;
import com.onthemove.requestClasses.UpdateLocationRequest;
import com.onthemove.responseClasses.UpdateLocationResponse;


import java.text.DecimalFormat;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseLocationService extends Service {

    private static final String TAG = FirebaseLocationService.class.getSimpleName();

    private static FirebaseLocationService firebaseLocationService;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;
    private Context context;
    private AppPref appPref;
    private ApiService apiService;
    private double km = 0;
    double diff = 0;
    private String dis = "0.0";

    final Handler handler = new Handler();
    @Override
    public void onCreate() {
        super.onCreate();
        firebaseLocationService = this;
        AppLog.e(TAG, "onCreate()");
        context = this;
        apiService = RetroClient.getApiService();
        appPref = AppPref.getInstance(this);
   //   handler.postDelayed(runnable,30000);
    }
   /* Runnable runnable = new Runnable() {
        @Override
        public void run() {

            handler.postDelayed(runnable,30000);
        }
    };*/
    public static void start(Context context) {
        if (firebaseLocationService == null) {
           context.startService(new Intent(context, FirebaseLocationService.class));
        } else {
            firebaseLocationService.startLocationUpdate();
        }
    }

    public static void stop() {

        if (firebaseLocationService != null) {
            firebaseLocationService.stopSelf();
        }
    }

    private void StartForeground() {

        Log.e(TAG,"startForeground");

        Intent intent;

        if (appPref.getString(Constants.ROLE).equalsIgnoreCase("serviceboy")){
            intent = new Intent(context, MainActivity.class);
        }
        else {
            intent = new Intent(context, MainActivity2.class);

        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        String CHANNEL_ID = "channel_location";
        String CHANNEL_NAME = "channel_location";

        NotificationCompat.Builder builder = null;

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            channel.setSound(null,null);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
            builder.setChannelId(CHANNEL_ID);
            builder.setBadgeIconType(NotificationCompat.BADGE_ICON_NONE);
        } else {
            builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        }

        builder.setContentTitle("Live Tracking");
        builder.setContentText("You are now online");
        builder.setVibrate(null);
        builder.setOngoing(true);
        builder.setAutoCancel(false);
        builder.setSmallIcon(R.drawable.app_icon);
        builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        startForeground(101, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        appPref.set("onService", true);

        firebaseLocationService = this;
        StartForeground();
        initFirebaseLocation();
        startLocationUpdate();

        return START_STICKY;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.e(TAG,"onStart");
        super.onStart(intent, startId);
    }

    private void initFirebaseLocation() {

        Log.e(TAG,"initFirebaseLocation");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.e(TAG,"onLocationResult");
                if (locationResult == null) {
                    Log.e(TAG,"locationResult null");
                    return;
                }
                for (Location location : locationResult.getLocations()) {

                    Log.e(TAG,"locationResult location");

                    if (appPref.isLogin()) {

                        AppLog.e(TAG, "Location Changed Latitude : " + location.getLatitude() + "\tLongitude : " + location.getLongitude());

                        /*if (!TextUtils.isEmpty(appPref.getString(AppPref.LAT))) {

                            Location locationA = new Location("point A");
                            locationA.setLatitude(Double.valueOf(appPref.getString(AppPref.LAT)));
                            locationA.setLongitude(Double.valueOf(appPref.getString(AppPref.LNG)));

                            Location locationB = new Location("point B");
                            locationB.setLatitude(location.getLatitude());
                            locationB.setLongitude(location.getLongitude());

                            double meter = locationA.distanceTo(locationB);

                            DecimalFormat df = new DecimalFormat("0.0000");

                            AppLog.e(TAG, "distance:" + df.format(meter / 1000));

                            AppLog.e(TAG, "meter:" + meter);

                            if (meter >= 5)
                            {
                                if (!TextUtils.isEmpty(appPref.getString(AppPref.LAT)) && !appPref.getString(AppPref.LNG).equalsIgnoreCase("") && !appPref.getString(AppPref.LAST_DIST).isEmpty())
                                {
                                    km = (meter / 1000) + Double.valueOf(appPref.getString(AppPref.LAST_DIST));
                                } else
                                {
                                    km = (meter / 1000);
                                }
                                //dis = df.format(km);
                                dis = String.format("%.4f" , km);
                                AppLog.e(TAG, "Total distance: if:" + dis);

                            }
                            else
                            {
                                if (!TextUtils.isEmpty(appPref.getString(AppPref.LAST_DIST))) {
                                    dis = appPref.getString(AppPref.LAST_DIST);
                                } else {
                                    dis = String.format("%.4f" , meter / 1000); //df.format(meter / 1000);
                                }
                                AppLog.e(TAG, "Total distance: else:" + dis);
                            }
                        }*/

                     //   appPref.set(AppPref.LAST_DIST, dis);

                        appPref.set(AppPref.LAT, String.valueOf(location.getLatitude()));
                        appPref.set(AppPref.LNG, String.valueOf(location.getLongitude()));

                        try {
                       //     updateLocationToFirebase(location.getLatitude() + "", location.getLongitude() + "",Double.parseDouble(dis));

                            updateLocationToServer(location.getLatitude() + "",location.getLongitude() + "");
                        }catch (Exception e)
                        {
                            Log.e(TAG,"Exception "+e);
                        }

                    }
                    break;
                }
            }
            @Override
            public void onLocationAvailability(LocationAvailability availability) {

                Log.e(TAG,"onLocationAvailability"+availability.isLocationAvailable());


            }
        };


    }



    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {

        Log.e(TAG,"startLocationUpdate");

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10 * 1000);
        locationRequest.setFastestInterval(10 * 1000);

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

    private void stopLocationUpdate() {
        if (fusedLocationClient != null)
            fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    //{message={"notification_type":"account_status_approved","driver_id":"22","message":"You are approved by admin","title":"Approved"}}


    public boolean isOnline() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        @SuppressLint("MissingPermission") NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    protected void updateLocationToFirebase(String lat, String lng, double km) {


       /* EmpLocationModel.EmpLocationReq empLocationReq = new EmpLocationModel.EmpLocationReq();
        empLocationReq.setCurrent_lat(String.valueOf(lat));
        empLocationReq.setCurrent_lng(String.valueOf(lng));
        empLocationReq.setEmp_id(appPref.getString(AppPref.USER_ID));
        empLocationReq.setTraveld_km(String.valueOf(km));
        try {
            empLocation(empLocationReq);
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        updateLocationToServer(lat,lng);
    }

    private void updateLocationToServer(String lat, String lng) {
/*
        String lat = appPref.getString(AppPref.LAT, "");
        String lng = appPref.getString(AppPref.LNG, "");*/

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


    @Override
    public void onDestroy() {
        super.onDestroy();

        appPref.set("onService", false);

        firebaseLocationService = null;
        stopLocationUpdate();
        AppLog.e(TAG, "onDestroy()");
    //    handler.removeCallbacks(runnable);
    }

    public static class DriverLocationData {
        private String driver_id;
        private String lat;
        private String lng;

        public String getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(String driver_id) {
            this.driver_id = driver_id;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        @Override
        public String toString() {
            return "DriverLocationData{" +
                    "driver_id='" + driver_id + '\'' +
                    ", lat='" + lat + '\'' +
                    ", lng='" + lng + '\'' +
                    '}';
        }
    }
}
