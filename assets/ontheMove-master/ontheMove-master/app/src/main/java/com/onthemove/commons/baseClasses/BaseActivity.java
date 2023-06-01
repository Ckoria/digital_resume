package com.onthemove.commons.baseClasses;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.onthemove.R;
import com.onthemove.activities.LoginActivity;
import com.onthemove.activities.MainActivity2;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.commons.utils.AppDialog;
import com.onthemove.commons.utils.AppPref;
import com.onthemove.services.FirebaseLocationService;

import java.io.InputStream;
import java.util.ArrayList;


public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    protected Context context;
    protected ProgressDialog dialog;
    protected AppPref appPref;
    protected String[] permissionLocation = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    protected static final int LOCATION_REQ = 11;
    protected static final int LOCATION_ENABLE_REQ_CODE = 12;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        context = BaseActivity.this;
        appPref = AppPref.getInstance(context);

    }


    //For... show toast msg.
    public void showToast(String msg) {
        Toast.makeText(this, msg + "", Toast.LENGTH_SHORT).show();
    }



    //for....prefrences
    public AppPref getPref() {
        return AppPref.getInstance(this);
    }



    //For...check internet is on or off
    public boolean isOnline() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
//        if (!isAvailable) {
//            AppDialog.showNoNetworkDialog(this);
//        }
        return isAvailable;
    }



    //For...check permission
    public boolean hasPermission(String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED)
                    return false;
            }
        }
        return true;
    }



    //For....validation of EditText
    public boolean valid(EditText editText, int errorMsg) {
        if (TextUtils.isEmpty(editText.getText().toString().trim())) {
            showToast(getResources().getString(errorMsg));
            return false;
        }
        return true;
    }

    public boolean valid(TextView editText, int errorMsg) {
        if (TextUtils.isEmpty(editText.getText().toString().trim())) {
            showToast(getResources().getString(errorMsg));
            return false;
        }
        return true;
    }


    //For....validation of email
    public boolean validEmail(EditText editText, int errorMsg) {
        if (!Patterns.EMAIL_ADDRESS.matcher(editText.getText().toString()).matches()) {
            showToast(getResources().getString(errorMsg));
            return false;
        }
        return true;
    }



    //For...check location
    public void checkLocationRequirement() {
        AppLog.e(TAG, "checkLocationRequirement()");
        if (!hasPermission(permissionLocation)) {
            ActivityCompat.requestPermissions(this, permissionLocation, LOCATION_REQ);
            return;
        }
        if (!isGpsOn()) {
            return;
        }
        onLocationSatisfy();
    }



    //For... check if gps on or off
    public boolean isGpsOn() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled) {
            displayLocationSettingsRequest(this);
        }
        return gps_enabled;
    }



    //For...location satisfy
    public void onLocationSatisfy() {


    }



    //For...location
    public void displayLocationSettingsRequest(Activity context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("BLEFragment", "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("BLEFragment", "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(BaseActivity.this, LOCATION_ENABLE_REQ_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i("BLEFragment", "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("BLEFragment", "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult() : " + requestCode);
        if (requestCode == LOCATION_ENABLE_REQ_CODE && resultCode == Activity.RESULT_OK) {
            onLocationSatisfy();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQ) {
            boolean isAllPermissionGranted = true;
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        AppLog.e(TAG, "grantResults- IF " + grantResult);
                        isAllPermissionGranted = false;
                        break;
                    }
                }
            }
            AppLog.e(TAG, "isAllPermissionGranted : " + isAllPermissionGranted);
            if (isAllPermissionGranted) {
                checkLocationRequirement();
            }
        }
    }


    //For...show loading process during api call
    public void showLoading(String msg) {
        if (dialog != null)
            hideLoading();
        if (dialog == null) {
            dialog = new ProgressDialog(this);
        }
        dialog.setMessage(!TextUtils.isEmpty(msg) ? msg : getResources().getString(R.string.loading));
        if (!dialog.isShowing())
            dialog.show();
    }


    //For...hide loading dialog after getting response
    public void hideLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }



    //For....if user is unauthorized by token
    public void unAuthorized() {
        showToast(getResources().getString(R.string.unautorized));
        logout();
    }



    //For...if response is null from api.
    public void responseNull() {
        showToast(getResources().getString(R.string.responseNull));
    }



    //For...if api calling is fail
    public void onFailure(String message) {
        showToast(message);
    }



    //For...change activity
    public void gotoActivity(Class className, Bundle bundle, boolean isClearStack) {
        Intent intent = new Intent(this, className);

        if (bundle != null)
            intent.putExtras(bundle);

        if (isClearStack) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }



    //For....change fragmemts
    public void changeFragment(Fragment fragment, boolean isBackStack, boolean isPopBack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();

        Bundle b = fragment.getArguments();

        if (isPopBack) {
            fm.popBackStack();

        }
        if (isBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(R.id.container, fragment);
        try {
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            fragmentTransaction.commitAllowingStateLoss();
        }
    }



    //For...logout
    public void logout() {

        new LogoutTask().execute();
    }



    //For...logout
    public class LogoutTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading(getResources().getString(R.string.logout));
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                appPref.clearSession();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            hideLoading();
            gotoActivity(LoginActivity.class, null, true);
            finish();
        }
    }


}
