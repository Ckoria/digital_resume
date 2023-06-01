package com.onthemove.activities;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.onthemove.R;
import com.onthemove.adapters.VehicalHealthInteriorAdapter;
import com.onthemove.adapters.VehicleHealthAdapter;
import com.onthemove.commons.baseClasses.BaseActivity;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.commons.utils.AppPref;
import com.onthemove.commons.utils.Constants;
import com.onthemove.commons.utils.FragmentOnlyCameraDialog;
import com.onthemove.commons.utils.FragmentSelectImageDialog;
import com.onthemove.databinding.ActivityVehicleHealthBinding;
import com.onthemove.interfaces.VehicalDetails;
import com.onthemove.responseClasses.VehicalHealthModel;
import com.onthemove.responseClasses.VehicalListModel;
import com.onthemove.responseClasses.VehicleSubmitDetailsExteriorModel;
import com.onthemove.responseClasses.VehicleSubmitDetailsInteriorModel;
import com.onthemove.viewModel.OrderViewModel;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;



public class VehicleHealthActivity extends BaseActivity implements  VehicalDetails.VehicalView, VehicalHealthInteriorAdapter.onClickItemListner, VehicleHealthAdapter.OnRadioGroupItemListner {

    public static final String TAG = "vehicleHealthActivity";

    private ActivityVehicleHealthBinding binding;

    String imagepathcamera;

    private static final int IMAGE_CAPTURE = 1;
    private File mediaFile;

    private VehicleHealthAdapter adapter;
    private VehicalHealthInteriorAdapter interiorAdapter;
    private int id = 0;
    ArrayList<VehicleSubmitDetailsInteriorModel> interiorDataList;
    ArrayList<VehicleSubmitDetailsInteriorModel> exteriorDataList;
    OrderViewModel orderViewModel;
    private int idinterior = 0;
    private String type = "";
    private int SIGNATURE_IMAGE = 99;
    private String signatureImage = "";
    private String frontImg = "", backImg = "", leftImg = "", rightImg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_vehicle_health);
        init();
        if (isOnline())
        {
            orderViewModel.vehicleHealthlist();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void init() {

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.setVehicalView(this);

        adapter = new VehicleHealthAdapter(this);
        binding.rvVehicleHealth.setAdapter(adapter);
        binding.rvVehicleHealth.setNestedScrollingEnabled(false);

        interiorAdapter = new VehicalHealthInteriorAdapter(this);
        binding.rvVehicleHealthinterior.setAdapter(interiorAdapter);
        binding.rvVehicleHealthinterior.setNestedScrollingEnabled(false);
        binding.rvVehicleHealthinterior.getAdapter().notifyDataSetChanged();

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSubmitData();

            }
        });


        binding.top.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void getSubmitData() {

        int VehicleId = Integer.parseInt(appPref.getString(AppPref.VEHICLE_NO));
        AppLog.e(TAG,"VehicleID"+VehicleId);

        if (isOnline()) {
            Intent i = new Intent(this,AddVehicleHealthImageActivity.class);
            i.putExtra("exteriorList",exteriorDataList);
            i.putExtra("interiorList",interiorDataList);
            i.putExtra("vehicleId",String.valueOf(VehicleId));
            startActivity(i);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Check Your Internet", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void OnSuccess() {


    }

    @Override
    public void vehicalList(ArrayList<VehicalListModel.VehicalListData> vehicalListData) {

    }

    @Override
    public void vehicalInteriorList(ArrayList<VehicalHealthModel.VehicleInteriorData> interiorData) {


            if (interiorData != null && interiorData.size() > 0)
            {

                ArrayList<VehicalHealthModel.VehicleInteriorData> vehicleInteriorData = new ArrayList<>();

                for (int i = 0; i < interiorData.size(); i++) {
                    {

                        VehicalHealthModel.VehicleInteriorData newInteriorData = new VehicalHealthModel.VehicleInteriorData();

                        newInteriorData.setInt_id(interiorData.get(i).getInt_id());
                        newInteriorData.setInt_type(interiorData.get(i).getInt_type());
                        newInteriorData.setInt_type_title(interiorData.get(i).getInt_type_title());

                        vehicleInteriorData.add(newInteriorData);
                    }
                }
                AppLog.e(TAG,"intsize"+interiorData.size());
                interiorDataList = new ArrayList<VehicleSubmitDetailsInteriorModel>();
                for(int i =0 ; i < vehicleInteriorData.size(); i++)
                    interiorDataList.add(new VehicleSubmitDetailsInteriorModel());

                interiorAdapter.addItem(vehicleInteriorData);

            }
    }

    @Override
    public void vehicalExteriorList(ArrayList<VehicalHealthModel.VehicleExteriorData> exteriorData) {
        if (exteriorData != null && exteriorData.size() > 0)
        {

            ArrayList<VehicalHealthModel.VehicleExteriorData> vehicleExteriorData = new ArrayList<>();

            for (int i = 0; i < exteriorData.size(); i++) {
                {

                    VehicalHealthModel.VehicleExteriorData newExterior = new VehicalHealthModel.VehicleExteriorData();

                    newExterior.setExt_id(exteriorData.get(i).getExt_id());
                    newExterior.setExt_type(exteriorData.get(i).getExt_type());
                    newExterior.setExt_type_title(exteriorData.get(i).getExt_type_title());

                    vehicleExteriorData.add(newExterior);
                }
            }
            AppLog.e(TAG,"extsize"+exteriorData.size());
            exteriorDataList = new ArrayList<VehicleSubmitDetailsInteriorModel>();
            for(int i =0 ; i < vehicleExteriorData.size(); i++)
                exteriorDataList.add(new VehicleSubmitDetailsInteriorModel());

            adapter.addItem(vehicleExteriorData);

        }
    }




    //Exterior Adapter value


    @Override
    public void onSelectedExteriorValueItem(int pos, int value, int idtype) {

        AppLog.e(TAG,"onSelectedItemExteriorPostion:"+pos+"\nvalue:"+value);

        VehicleSubmitDetailsInteriorModel vehicleSubmitDetailsModel = new VehicleSubmitDetailsInteriorModel();

        vehicleSubmitDetailsModel.setVehicle_health_id(String.valueOf(idtype));
        vehicleSubmitDetailsModel.setVehicle_condition_id(String.valueOf(value));

        exteriorDataList.set(pos,vehicleSubmitDetailsModel);
    }

    @Override
    public void OnSelectedRadioButtonExtComment(int pos, String value) {
        AppLog.e(TAG,"onSelectedItemExteriorPostion:"+pos+"\ncomment:"+value);

        VehicleSubmitDetailsInteriorModel vehicleSubmitDetailsModel = exteriorDataList.get(pos);

        vehicleSubmitDetailsModel.setComment(value);

        exteriorDataList.set(pos,vehicleSubmitDetailsModel);
    }

    //Interior Adapter
    @Override
    public void onSelectedInteriorValueItem(int pos, int value, int idType) {
        AppLog.e(TAG,"onSelectedItemPostion:"+pos+"\nvalue:"+value);

        VehicleSubmitDetailsInteriorModel vehicleSubmitDetailsModel = new VehicleSubmitDetailsInteriorModel();

        vehicleSubmitDetailsModel.setVehicle_health_id(String.valueOf(idType));
        vehicleSubmitDetailsModel.setVehicle_condition_id(String.valueOf(value));


        interiorDataList.set(pos,vehicleSubmitDetailsModel);
    }

    @Override
    public void OnSelectedRadioButtonIntComment(int pos, String value) {

        AppLog.e(TAG,"onSelectedItemInteriorPostion:"+pos+"\ncomment:"+value);
        VehicleSubmitDetailsInteriorModel vehicleSubmitDetailsModel = interiorDataList.get(pos);

        vehicleSubmitDetailsModel.setComment(value);

        interiorDataList.set(pos,vehicleSubmitDetailsModel);
    }
}