package com.onthemove.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.iceteck.silicompressorr.SiliCompressor;
import com.onthemove.R;
import com.onthemove.commons.baseClasses.BaseActivity;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.databinding.ActivityAddVehicleHealthImageBinding;
import com.onthemove.interfaces.AuthContract;
import com.onthemove.interfaces.VehicalDetails;
import com.onthemove.modelClasses.OnDutyModel;
import com.onthemove.responseClasses.VehicalHealthModel;
import com.onthemove.responseClasses.VehicalListModel;
import com.onthemove.responseClasses.VehicleSubmitDetailsInteriorModel;
import com.onthemove.viewModel.AuthViewModel;
import com.onthemove.viewModel.OrderViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;


public class AddVehicleHealthImageActivity extends BaseActivity implements VehicalDetails.VehicalView, AuthContract.AuthView {
    private static final String TAG = "AddVehicleHealthImageActivity";
    ActivityAddVehicleHealthImageBinding binding;

    ArrayList<VehicleSubmitDetailsInteriorModel> interiorDataListIntent;
    ArrayList<VehicleSubmitDetailsInteriorModel> exteriorDataListIntent;
    String vehicleId;
    String imagepathcamera;
    AuthViewModel authViewModel;
    private File mediaFile;
    private static final int IMAGE_CAPTURE = 1;
    private int SIGNATURE_IMAGE = 99;

    OrderViewModel orderViewModel;
    private String type = "";
    private Uri uri;
    private String signatureImage = "";
    private Uri signatureImageUrl = null;
    private String frontImg = "", backImg = "", leftImg = "", rightImg = "";
    private Uri frontImgUrl = null, backImgUrl = null, leftImgUrl = null, rightImgUrl = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_vehicle_health_image);

        init();
    }
    private void init(){

        interiorDataListIntent = new ArrayList<>();
        exteriorDataListIntent = new ArrayList<>();

        Bundle extras = getIntent().getExtras();

        if(extras != null){
            exteriorDataListIntent = (ArrayList<VehicleSubmitDetailsInteriorModel>) extras.getSerializable("exteriorList");
            interiorDataListIntent = (ArrayList<VehicleSubmitDetailsInteriorModel>) extras.getSerializable("interiorList");
            vehicleId = extras.getString("vehicleId");
            AppLog.e(TAG,"size"+new Gson().toJson(exteriorDataListIntent));
            AppLog.e(TAG,"size"+new Gson().toJson(interiorDataListIntent));
            AppLog.e(TAG,"vehicleId"+vehicleId);

        }
        else
        {
            AppLog.e(TAG,"getintent not null");
        }


        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.setVehicalView(this);

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.setAuthView(this);
        binding.top.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid())
                {
                    getSubmitData();
                }
            }
        });
        binding.cardSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SigntureActivity.class);
                startActivityForResult(i, SIGNATURE_IMAGE);
            }
        });

        binding.rlFrontImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "front";
                cameraIntent();
                //cameraDialog.show(getSupportFragmentManager(), "");
            }
        });

        binding.rlBackImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "back";
                cameraIntent();
                //  cameraDialog.show(getSupportFragmentManager(), "");
            }
        });

        binding.rlLeftImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "left";
                cameraIntent();
                //    cameraDialog.show(getSupportFragmentManager(), "");
            }
        });

        binding.rlRightImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "right";
                cameraIntent();
                // cameraDialog.show(getSupportFragmentManager(), "");
            }
        });
    }

    private void getSubmitData() {


        if (isOnline())
        {
            orderViewModel.addVehicleHealthData(Integer.parseInt(vehicleId),interiorDataListIntent,exteriorDataListIntent,frontImgUrl,backImgUrl,leftImgUrl,rightImgUrl,signatureImageUrl,getApplicationContext(),getInputStreamByUri(getApplicationContext(),frontImgUrl),getInputStreamByUri(getApplicationContext(),backImgUrl),getInputStreamByUri(getApplicationContext(),leftImgUrl),getInputStreamByUri(getApplicationContext(),rightImgUrl),getInputStreamByUri(getApplicationContext(),signatureImageUrl));
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Required Internet",Toast.LENGTH_SHORT).show();
        }

    }

    public InputStream getInputStreamByUri(Context context, Uri uri) {
        try {
            InputStream inputStream;
            inputStream = context.getContentResolver().openInputStream(uri);
            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public void onBackPressed() {
    }

    public boolean hasPermission(Context context, String[] permissions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED)
                    return false;
            }
        }
        return true;
    }
    private void cameraIntent() {
        if (hasPermission(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            mediaFile = getOutputMediaFile();

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", mediaFile);
            } else {
                uri = Uri.fromFile(mediaFile);
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intent, IMAGE_CAPTURE);
        } else {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
    private boolean isValid() {
        if (frontImg.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Select Front Image",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (backImg.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Select Back Image",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (leftImg.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Select Left Image",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (rightImg.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Select Right Image",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (signatureImage.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"Select Signature Image",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (hasPermission(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}))
                    cameraIntent();
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String imagePath;
        if (requestCode == IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
          //  imagePath = mediaFile.getAbsolutePath();

            if (uri != null) {

                if (!TextUtils.isEmpty(uri.toString())) {
                    onImageSelected(uri);
                } else {
                    Toast.makeText(this, "Can't get image", Toast.LENGTH_SHORT).show();
                }
            } else {

            }
        }

        if (requestCode == SIGNATURE_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {

                    String path = data.getStringExtra("image");
                    String taskId = data.getStringExtra("taskId");

                    try {

                        File f = new File(path);
                        signatureImage = f.getAbsolutePath();

                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            signatureImageUrl = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f);
                        } else {
                            signatureImageUrl = Uri.fromFile(f);
                        }


                        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                        binding.imgSignature.setImageBitmap(b);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String getPath(Uri uri)
    {
        String[] projection = {OpenableColumns.DISPLAY_NAME };
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =  cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }



    public void onImageSelected(Uri imagePath) {
        AppLog.e(TAG,"ON IMAGE SELECTED");
        try {


            String filePath = SiliCompressor.with(context).compress(imagePath.toString(), new File(context.getCacheDir(),getPath(imagePath)));

            imagePath = Uri.parse(filePath);



            imagepathcamera = filePath;

            if (type.equalsIgnoreCase("front")) {
                frontImg = imagepathcamera;
                frontImgUrl = imagePath;
                Glide.with(this).load(imagepathcamera).into(binding.imgFront);
            } else if (type.equalsIgnoreCase("back")) {
                backImg = imagepathcamera;
                backImgUrl = imagePath;
                Glide.with(this).load(imagepathcamera).into(binding.imgBack);
            }else if (type.equalsIgnoreCase("left"))
            {
                leftImg = imagepathcamera;
                leftImgUrl = imagePath;
                Glide.with(this).load(imagepathcamera).into(binding.imgLeft);
            }else if (type.equalsIgnoreCase("right"))
            {
                rightImg = imagepathcamera;
                rightImgUrl = imagePath;
                Glide.with(this).load(imagepathcamera).into(binding.imgRight);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public File getOutputMediaFile() {

        // External sdcard location
        File mediaStorageDir = getCacheDir();

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        return new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + "_" + new Random().nextInt() + ".jpg");

    }

    @Override
    public void OnSuccess() {

        authViewModel.onDuty();

        Intent intent = new Intent(AddVehicleHealthImageActivity.this, MainActivity2.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void vehicalList(ArrayList<VehicalListModel.VehicalListData> vehicalListData) {

    }

    @Override
    public void vehicalInteriorList(ArrayList<VehicalHealthModel.VehicleInteriorData> interiorData) {

    }

    @Override
    public void vehicalExteriorList(ArrayList<VehicalHealthModel.VehicleExteriorData> exteriorData) {

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
}