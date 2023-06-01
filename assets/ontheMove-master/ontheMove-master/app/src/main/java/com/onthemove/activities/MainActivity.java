package com.onthemove.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.onthemove.R;
import com.onthemove.adapters.UploadImageAdapter;
import com.onthemove.commons.baseClasses.BaseActivity;
import com.onthemove.commons.utils.AppDialog;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.commons.utils.Constants;
import com.onthemove.commons.utils.DateTimeHelper;
import com.onthemove.commons.utils.FragmentSelectImageDialog;
import com.onthemove.database.DatabaseManager;
import com.onthemove.databinding.ActivityMainBinding;
import com.onthemove.databinding.DialogDetailsSucessfullyBinding;
import com.onthemove.databinding.DialogSaveImageBinding;
import com.onthemove.interfaces.AuthContract;
import com.onthemove.interfaces.OrderContract;
import com.onthemove.modelClasses.OnDutyModel;
import com.onthemove.modelClasses.PartModel;
import com.onthemove.modelClasses.SubmitOrderModel;
import com.onthemove.modelClasses.UploadImagesModel;
import com.onthemove.requestClasses.SubmitOrderRequest;
import com.onthemove.responseClasses.NewTaskModel;
import com.onthemove.services.ApiCallServices;
import com.onthemove.viewModel.AuthViewModel;
import com.onthemove.viewModel.OrderViewModel;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;



public class MainActivity extends BaseActivity implements View.OnClickListener, OrderContract.OrderView, AuthContract.AuthView,
        FragmentSelectImageDialog.ImageSelectListener, UploadImageAdapter.UploadImageListener {

    private static final String TAG = "MainActivity";
    private ArrayList<InputStream> inpuStreamList = new ArrayList<>();
    private static ActivityMainBinding binding;
    private Context context;
    private UploadImageAdapter uploadImageAdapter;
    //    private ArrayList<String> uploadImages = new ArrayList<>(4);
    private ArrayList<UploadImagesModel> uploadImages = new ArrayList<>(10);
    private UploadImagesModel uploadImagesModel;
    private FragmentSelectImageDialog imageDialog;
    private OrderViewModel orderViewModel;
    private AuthViewModel authViewModel;
    private String nameFirst, nameSecond, nameThird, nameForth;
    private ArrayList<Uri> images = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private String name;
    double latitude;
    double longitude;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationClient;
    DatabaseManager databaseManager;
    private static boolean sync = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        context = MainActivity.this;
        imageDialog = FragmentSelectImageDialog.newInstance();
        inits();
        listner();
    }

    private void inits() {
        databaseManager = new DatabaseManager(context);

        if (databaseManager.checkRecord() == true) {
            sync = true;
            binding.btnSync.setBackgroundResource(R.drawable.syncredbg_btn);
        } else {
            sync = false;
            binding.btnSync.setBackgroundResource(R.drawable.syncbg_btn);
        }

        binding.includeNav.tvUserName.setText(appPref.getString(Constants.USER_NAME));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.rcvUploadImgs.setLayoutManager(linearLayoutManager);
        uploadImageAdapter = new UploadImageAdapter(context, uploadImages, this);
        binding.rcvUploadImgs.setAdapter(uploadImageAdapter);


        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.setView(this);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.setAuthView(this);

        if (appPref.getString(Constants.ROLE).equalsIgnoreCase("serviceboy")) {

            latitude = 26.07451057434082;
            longitude = 28.0647544860839;

            binding.etAddress.setText("Morningside, Sandton, 2057, South Africa");
        } else {

            locationCallback = new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        return;
                    }
                    for (Location location : locationResult.getLocations()) {

                        //Integer.parseInt("s");
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();

                        if (fusedLocationClient != null && locationCallback != null)
                            fusedLocationClient.removeLocationUpdates(locationCallback);

                        AppLog.e(TAG, "LAT & LNG: " + latitude + " --- " + longitude);

                        if (!Geocoder.isPresent()) {

                          /*  GeoCodeApi geoCodeApi = new GeoCodeApi(latitude, longitude, MainActivity.this, new GeoCodeApi.GeoCodeListener() {
                                @Override
                                public void onGeoAddressFound(GeoCodeApi.GeoAddress geoAddress) {
                                    if (geoAddress != null) {
                                        AppLog.e(TAG, "onGeoAddressFound: " + geoAddress.getAddress());
                                        binding.etAddress.setText(geoAddress.getAddress());
                                    }
                                }
                            });
                            geoCodeApi.execute();
*/
                            Toast.makeText(MainActivity.this, "Geocoder Library Isn't Present In Your Device.", Toast.LENGTH_SHORT).show();
                        } else {

                            Geocoder geocoder = new Geocoder(MainActivity.this);
                            try {
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                if (addresses.size() > 0) {

                                    AppLog.e(TAG, "geocoder address: " + addresses.get(0).toString());
                                    binding.etAddress.setText(addresses.get(0).getAddressLine(0));
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }


                        break;
                    }
                }
            };

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

            checkLocationRequirement();
        }

    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if (isOnline()){
//            ApiCallServices.start(this);
//        }
//
//    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isOnline()) {
            ApiCallServices.start(this);
        }

    }

    public void onLocationSatisfy() {
        AppLog.e(TAG, "onLocationSatisfy()");
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startLocationUpdate();
    }

    private void startLocationUpdate() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


    private void listner() {

        imageDialog.setListener(this);
        binding.btnSync.setOnClickListener(this);
        binding.linMenu.setOnClickListener(this);
        binding.etTicketId.setOnClickListener(this);
        binding.etComments.setOnClickListener(this);
        binding.btnSubmit.setOnClickListener(this);
        binding.includeNav.linMyOrder.setOnClickListener(this);
        binding.includeNav.linLogout.setOnClickListener(this);


        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            int verCode = pInfo.versionCode;
            String verName = pInfo.versionName;

            binding.includeNav.tvVersion.setText("Version: " + verName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.linMenu:
                binding.drawer.openDrawer(GravityCompat.START);
                break;

            case R.id.etTicketId:
                binding.etTicketId.setFocusableInTouchMode(true);
                binding.etTicketId.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(binding.etTicketId, InputMethodManager.SHOW_IMPLICIT);
                break;

            case R.id.etComments:
                binding.etComments.setFocusableInTouchMode(true);
                binding.etComments.requestFocus();
                InputMethodManager imm2 = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.showSoftInput(binding.etComments, InputMethodManager.SHOW_IMPLICIT);
                break;

            case R.id.linMyOrder:
                gotoActivity(OrderDetailsActivity.class, null, false);
                break;

            case R.id.linLogout:

                AppDialog.showConfirmDialog(context, "Are you sure you want to logout?", new AppDialog.AppDialogListener() {
                    @Override
                    public void okClick(DialogInterface dialog) {

                        authViewModel.getLogout();
                        authViewModel.logoutStatus.observe(MainActivity.this, new Observer<Boolean>() {
                            @Override
                            public void onChanged(Boolean status) {

                                if (status) {

                                    logout();
                                    gotoActivity(LoginActivity.class, null, false);
                                    finish();
                                }
                            }
                        });
                    }
                });

                break;

            case R.id.btnSync:

                if (isOnline()) {
                    if (!sync) {
                        ApiCallServices.start(this);
                        sync = true;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Required Internet", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btnSubmit:

                if (verifyCode()) {

                    if (uploadImages.size() > 0) {

                        //names.clear();
                        //images.clear();

                        StringBuilder imgNames = new StringBuilder();
                        StringBuilder imagesList = new StringBuilder();
                        //For names
                        for (int i = 0; i < uploadImages.size(); i++) {
                            imgNames.append(uploadImages.get(i).getImgName()).append(",");
                        }

                        images.clear();
                        for (int i = 0; i < uploadImages.size(); i++) {
                            Log.e(TAG,"images path in list" +uploadImages.get(i).getImgPath());
                            images.add(uploadImages.get(i).getImgPath());
                            imagesList.append(uploadImages.get(i).getImgPath()).append(",");
                        }

                        if (isOnline()) {

                            SubmitOrderRequest submitOrderRequest = new SubmitOrderRequest();

                            submitOrderRequest.setTicket_number(binding.etTicketId.getText().toString());
                            submitOrderRequest.setComment(binding.etComments.getText().toString());
                            if (imgNames.length() > 0) {
                                imgNames.setLength(imgNames.length() - 1);
                            }
                            AppLog.e(TAG, "imgNames: " + imgNames);

                            AppLog.e(TAG, "images: " + images.size());

                            submitOrderRequest.setName_of_the_image(imgNames.toString());
                            submitOrderRequest.setImage(images);
                            submitOrderRequest.setLat(latitude);
                            submitOrderRequest.setLng(longitude);
                            submitOrderRequest.setAddress(binding.etAddress.getText().toString());

                            AppLog.e(TAG, "SUBMIT ORDER DATA = " + new Gson().toJson(submitOrderRequest));

                            getInputStreamByUri(this,images);

                            orderViewModel.submitOrder(submitOrderRequest,inpuStreamList,this);

                        } else {

                            SubmitOrderModel submitOrderModel = new SubmitOrderModel();

                            submitOrderModel.setTicket_number(binding.etTicketId.getText().toString());
                            submitOrderModel.setComment(binding.etComments.getText().toString());
                            if (imgNames.length() > 0) {
                                imgNames.setLength(imgNames.length() - 1);
                            }
                            submitOrderModel.setName_of_the_image(imgNames.toString());
                            if (imagesList.length() > 0) {
                                imagesList.setLength(imagesList.length() - 1);
                            }
                            submitOrderModel.setImage(imagesList.toString());
                            submitOrderModel.setLat(latitude);
                            submitOrderModel.setLng(longitude);
                            submitOrderModel.setAddress(binding.etAddress.getText().toString());
                            submitOrderModel.setSync("false");
                            submitOrderModel.setDateTime(DateTimeHelper.convertFormat(new Date(),"yyyy-MM-dd HH:mm:ss"));

                            AppLog.e(TAG, "SUBMIT ORDER DATA FOR SQLITE = " + new Gson().toJson(submitOrderModel));

                            databaseManager.insertSubmitData(submitOrderModel);

                            openSuccessfullDialog("Data add successfully");

                        }

                    } else {

                        showToast("Select image");
                    }

                }

                break;
        }
    }

    public ArrayList<InputStream> getInputStreamByUri(Context context, ArrayList<Uri> uri) {
        try {
            for (int i = 0; i<uri.size();i++)
            {
                inpuStreamList.add(context.getContentResolver().openInputStream(uri.get(i)));
            }
            return inpuStreamList;
        } catch (Exception e) {
            Log.e(TAG,"Exception "+e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public static void syncCompleted() {
        sync = false;
        binding.btnSync.setBackgroundResource(R.drawable.syncbg_btn);
    }

    private boolean verifyCode() {

        return valid(binding.etTicketId, R.string.errorTicketNumber)
//                &&valid(binding.etComments,R.string.errorComment);
                && valid(binding.etAddress, R.string.errorAddress);
    }

    private void openSuccessfullDialog(String message) {

        uploadImages.clear();

        Dialog dialog = new Dialog(context, R.style.AppDialog);
        DialogDetailsSucessfullyBinding dialogDetailsSucessfullyBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_details_sucessfully, null, false);
        dialog.setContentView(dialogDetailsSucessfullyBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialogDetailsSucessfullyBinding.tvMsg.setText(message);

        dialogDetailsSucessfullyBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                binding.etTicketId.getText().clear();
                binding.etComments.getText().clear();
                binding.etTicketId.setFocusable(false);
                binding.etComments.setFocusable(false);
                uploadImages.clear();
                uploadImageAdapter.notifyDataSetChanged();
                inits();
            }
        });

        dialog.show();
    }


    @Override
    public void onNewImage() {
        AppLog.e(TAG, "multiple");
        imageDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onEditName(UploadImagesModel data) {

        openImgSaveDialog(data.getImgPath(), data.getImgName());
    }

    @Override
    public void onDeleteImage(int position) {

        uploadImages.remove(position);
        uploadImageAdapter.notifyDataSetChanged();
    }


    @Override
    public void onImageSelected(Uri path) {
        imageDialog.dismiss();
        Log.e(TAG, "imageDialog not dismiss");

        if (path != null) {
            openImgSaveDialog(path, "");
        }
    }

    @Override
    public void onImageSelected(ArrayList<Uri> listImages) {

        Log.e("multiple error","find error");
        imageDialog.dismiss();

        if (uploadImages.size() <= 10) {

            for (int i = 0; i < listImages.size(); i++) {

                Log.e("multiple error","listimageurl"+listImages.get(i));

                uploadImagesModel = new UploadImagesModel();
                uploadImagesModel.setImgPath(listImages.get(i));
                uploadImagesModel.setImgName(appPref.getInt(Constants.USER_ID) + "_" + new Random().nextInt(61) + 20);
                uploadImages.add(uploadImagesModel);
            }

            uploadImageAdapter.notifyDataSetChanged();
        } else {
            showToast("Upload maximum 10 photos");
        }
        //openImgSaveDialog(listImages.get(0), "");
    }

    private void openImgSaveDialog(Uri path, String imgName) {

        Dialog dialog = new Dialog(context, R.style.AppDialog);
        DialogSaveImageBinding dialogSaveImageBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_save_image, null, false);
        dialog.setContentView(dialogSaveImageBinding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        if (!TextUtils.isEmpty(imgName)) {
            dialogSaveImageBinding.etNameOfImg.setText(imgName);
        }

        Glide.with(context).load(path).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(dialogSaveImageBinding.img);

        dialogSaveImageBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dialogSaveImageBinding.etNameOfImg.getText().toString().isEmpty()) {

                    showToast("Enter name of image");
                } else {

                    dialog.dismiss();
                    AppLog.e("ImagePath: ", path + "");
                    uploadImagesModel = new UploadImagesModel();

                    if (!TextUtils.isEmpty(imgName)) {

                        for (int i = 0; i < uploadImages.size(); i++) {
                            if (uploadImages.get(i).getImgName().equalsIgnoreCase(imgName)) {
                                uploadImages.remove(i);
                            }
                        }
                        uploadImagesModel.setImgPath(path);
                        uploadImagesModel.setImgName(dialogSaveImageBinding.etNameOfImg.getText().toString());
                        uploadImages.add(uploadImagesModel);
                    } else {

                        uploadImagesModel.setImgPath(path);
                        uploadImagesModel.setImgName(dialogSaveImageBinding.etNameOfImg.getText().toString());
                        uploadImages.add(uploadImagesModel);
                    }
                    uploadImageAdapter.notifyDataSetChanged();
                }
            }
        });

        dialog.show();
    }


    @Override
    public void onVideoSelected(String path) {

    }


    @Override
    public void OnSuccess() {


    }

    @Override
    public void orderPlaced(String message) {

        openSuccessfullDialog(message);
    }

    @Override
    public void onPaymentSuccess() {

    }

    @Override
    public void onCancelOrder(String message) {

    }

    @Override
    public void Duty(String message) {

    }

    @Override
    public void onTaskStatusChange(String message, String task_id) {

    }

    @Override
    public void newTaskList(ArrayList<NewTaskModel.NewTaskData> newTaskData) {

    }

    @Override
    public void myTaskList(ArrayList<NewTaskModel.NewTaskData> myTaskData) {

    }

    @Override
    public void completedTaskRes(String message) {

    }

    @Override
    public void addPart(int id, ArrayList<PartModel.TaskProductData> productData) {

    }

    @Override
    public void addRefuelingRes(String message) {

    }


    @Override
    public void otpSent() {

    }

    @Override
    public void otpVerified() {

    }

    @Override
    public void submitReport(String message) {

    }

    @Override
    public void approvedAccount(String account_status) {

    }

    @Override
    public void OnDutyRes(OnDutyModel.OnDutyData onDutyData) {

    }


    @Override
    public void onBackPressed() {

        finish();
    }
}
