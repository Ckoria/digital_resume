package com.onthemove.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.onthemove.R;
import com.onthemove.commons.baseClasses.BaseActivity;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.commons.utils.AppPref;
import com.onthemove.commons.utils.FragmentOnlyCameraDialog;
import com.onthemove.commons.utils.FragmentSelectImageDialog;
import com.onthemove.database.TaskListDatabaseManager;
import com.onthemove.databinding.ActivityAddVehicleRefuelingBinding;
import com.onthemove.interfaces.OrderContract;
import com.onthemove.modelClasses.PartModel;
import com.onthemove.responseClasses.AddRefuelingModel;
import com.onthemove.responseClasses.NewTaskModel;
import com.onthemove.viewModel.OrderViewModel;

import java.io.InputStream;
import java.util.ArrayList;

public class AddVehicleRefuelingActivity extends BaseActivity implements OrderContract.OrderView,FragmentOnlyCameraDialog.ImageSelectListener{

    public static final String TAG = "AddVehicleRefuelingActivity";
    private ActivityAddVehicleRefuelingBinding binding;
    private String receiptImagePath = "";
    private String DashboardImagePath = "";
    private Uri receiptImage = null,DashBoardImage = null;
    private FragmentOnlyCameraDialog cameraDialog;
    private OrderViewModel orderViewModel;
    private String vehicleId = "";
    private TaskListDatabaseManager manager;
    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_vehicle_refueling);

        init();
    }

    private void init() {

        manager = new TaskListDatabaseManager(this);


        vehicleId = appPref.getString(AppPref.VEHICLE_NO);
        binding.tvTitle.setText("Vehicle Refueling"+" ("+appPref.getString(AppPref.VEHICLE_REGISTER)+")");
        AppLog.e(TAG,"vehicle id"+vehicleId);
        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.setView(this);


        cameraDialog = FragmentOnlyCameraDialog.newInstance();
        cameraDialog.setListener(this);

        binding.top.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValid()) {
                    addRefueling();
                    if (!vehicleId.isEmpty()) {
                        addRefueling();
                    } else {
                        showToast("Do duty first ON");
                    }
                }
            }
        });

        binding.rlReceiptImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "receipt";
                cameraDialog.show(getSupportFragmentManager(), "");
            }
        });

        binding.rlDashboardImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "dashboard";
                cameraDialog.show(getSupportFragmentManager(), "");
            }
        });
    }


    public InputStream getInputStreamByUri(Context context, Uri uri) {
        InputStream inputStream;
        try {

            inputStream = context.getContentResolver().openInputStream(uri);

            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addRefueling() {

        AddRefuelingModel.AddRefuelingReq req = new AddRefuelingModel.AddRefuelingReq();

        req.setFk_vehicle_id(vehicleId);
        req.setMeter_reading(binding.etOdoMeter.getText().toString());
        req.setReceipt_number(binding.etReceiptNo.getText().toString());
        req.setLiter(binding.etAmtLiters.getText().toString());
        req.setPrice(binding.etAmtRands.getText().toString());
        req.setReceipt_image(receiptImage);
        req.setDashboard_image(DashBoardImage);


        AppLog.e(TAG, "addRefuelingReq: " + new Gson().toJson(req));

        if (isOnline()) {
            orderViewModel.addRefueling(req,getInputStreamByUri(context,receiptImage),getInputStreamByUri(context,DashBoardImage),getApplicationContext());
        } else {
            manager.addRefueling(req);
            finish();
        }
    }

    private boolean isValid() {

        if (binding.etOdoMeter.getText().toString().isEmpty()) {
            showToast("Enter Odo Meter");
            return false;
        }
        if (binding.etReceiptNo.getText().toString().isEmpty()) {
            showToast("Enter Receipt Number");
            return false;
        }
        if (binding.etAmtLiters.getText().toString().isEmpty()) {
            showToast("Enter Liters");
            return false;
        }
        if (binding.etAmtRands.getText().toString().isEmpty()) {
            showToast("Enter Amount (Rands)");
            return false;
        }
        if (receiptImagePath.isEmpty()) {
            showToast("Select Receipt Image");
            return false;
        }
        if (DashboardImagePath.isEmpty()){
            showToast("Select Dashboard Image");
            return false;
        }

        return true;
    }

    /*@Override
    public void onImageSelected(String path) {
        imageDialog.dismiss();
        receiptImagePath = path;
        Glide.with(this).load(path).into(binding.imgDboard);
    }

    @Override
    public void onVideoSelected(String path) {
        imageDialog.dismiss();
    }

    @Override
    public void onImageSelected(ArrayList<String> listImages) {
        imageDialog.dismiss();
    }
*/
    @Override
    public void addRefuelingRes(String message) {
        showToast(message);
        finish();
    }

    @Override
    public void orderPlaced(String message) {

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
    public void OnSuccess() {

    }

    @Override
    public void onCameraSelected(Uri path) {
        cameraDialog.dismiss();

        if (type.equalsIgnoreCase("receipt"))
        {
            receiptImagePath = path.toString();
            receiptImage = path;
            Glide.with(this).load(path).into(binding.imgRecp);

        }else if (type.equalsIgnoreCase("dashboard"))
        {
            DashboardImagePath = path.toString();
            DashBoardImage = path;
            Glide.with(this).load(path).into(binding.imgDboard);
        }

    }
}