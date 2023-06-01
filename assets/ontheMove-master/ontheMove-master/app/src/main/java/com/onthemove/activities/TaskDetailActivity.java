package com.onthemove.activities;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.onthemove.R;
import com.onthemove.adapters.UploadAfterTaskImageAdapter;
import com.onthemove.adapters.UploadTaskImageAdapter;
import com.onthemove.commons.baseClasses.BaseActivity;
import com.onthemove.commons.utils.AppDialog;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.commons.utils.AppPref;
import com.onthemove.commons.utils.Constants;
import com.onthemove.commons.utils.DateTimeHelper;
import com.onthemove.commons.utils.FragmentAddQtyDialog;
import com.onthemove.commons.utils.FragmentSelectImageDialog;
import com.onthemove.database.TaskListDatabaseManager;
import com.onthemove.databinding.ActivityTaskDetailBinding;
import com.onthemove.databinding.DailogFailreasonBinding;
import com.onthemove.databinding.DialogDetailsSucessfullyBinding;
import com.onthemove.databinding.DialogSaveImageBinding;
import com.onthemove.interfaces.OrderContract;
import com.onthemove.modelClasses.PartModel;
import com.onthemove.modelClasses.UploadAfterTaskImageModel;
import com.onthemove.modelClasses.UploadTaskImageModel;
import com.onthemove.responseClasses.AddFailedReasonModel;
import com.onthemove.responseClasses.CompleteTaskModel;
import com.onthemove.responseClasses.NewTaskModel;
import com.onthemove.responseClasses.TaskStatusChangeModel;
import com.onthemove.services.ApiCallStatusServices;
import com.onthemove.services.FloatingFaceBubbleService;
import com.onthemove.viewModel.OrderViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class TaskDetailActivity extends BaseActivity implements OrderContract.OrderView, FragmentSelectImageDialog.ImageSelectListener, UploadTaskImageAdapter.UploadTaskImageListener, UploadAfterTaskImageAdapter.UploadTaskImageListener, FragmentAddQtyDialog.OnClick {
    private static final String TAG = "TaskDetailsActivity";
    Toolbar toolbar;

    ActivityTaskDetailBinding binding;
    int imageType = 0;
    int failimageType = 0;
    String encoded;
    String value_last = null;
    private FragmentSelectImageDialog imageDialog;
    private FragmentAddQtyDialog qtyDialog;
    private ArrayList<UploadTaskImageModel> uploadBeforeImageList = new ArrayList<>(10);
    private ArrayList<UploadAfterTaskImageModel> uploadAfterImageList = new ArrayList<>(10);
    private UploadTaskImageAdapter uploadTaskImageAdapter;
    private NewTaskModel.NewTaskData taskData = new NewTaskModel.NewTaskData();
    private OrderViewModel orderViewModel;
    private TaskListDatabaseManager manager;
    String StatusUpdate;
    String signatureImage = "";
    Uri signatureImageUri = null;
    private UploadTaskImageModel uploadTaskImageModel;
    private UploadAfterTaskImageModel uploadAfterTaskImageModel;
    private UploadAfterTaskImageAdapter uploadAfterTaskImageAdapter;
    String uri;
    private FloatingFaceBubbleService mService;
    private boolean mBound;
    Dialog dialogReason;
    String imageuri = "no image";
    Uri imageUri;
    DailogFailreasonBinding dailogFailreasonBinding;
    private int SIGNATURE_IMAGE = 99;
    private int BUBBLE_SERVICECODE = 1;

    private String TaskID;

    private Uri reasoniamge = null;
    private String reasonname = "";
    private String reasonimagename = "";

    private ArrayList<PartModel.TaskProductData> productData = new ArrayList<>();


    private InputStream inputStream;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_detail);
        manager = new TaskListDatabaseManager(this);
        toolbar = findViewById(R.id.tvTop);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!AppPref.getInstance(context).getBoolean(Constants.switchChecked))
        {
            binding.tvAccept.setVisibility(View.GONE);
            binding.tvDecline.setVisibility(View.GONE);
        }
    }

    private void init() {

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        orderViewModel.setView(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        binding.rcvImages.setLayoutManager(linearLayoutManager);
        uploadTaskImageAdapter = new UploadTaskImageAdapter(context, getLayoutInflater(), uploadBeforeImageList, this);
        binding.rcvImages.setAdapter(uploadTaskImageAdapter);

        uploadAfterTaskImageAdapter = new UploadAfterTaskImageAdapter(context, getLayoutInflater(), uploadAfterImageList, this);
        binding.rcvAfterImages.setAdapter(uploadAfterTaskImageAdapter);

        imageDialog = FragmentSelectImageDialog.newInstance();
        imageDialog.setListener(this);

        qtyDialog = FragmentAddQtyDialog.newInstance();

        if (getIntent() != null) {

            taskData = (NewTaskModel.NewTaskData) getIntent().getSerializableExtra("taskData");

            TaskID = taskData.getTaskId();

            Log.e(TAG,"Task ID"+TaskID);

            String from = getIntent().getStringExtra("from");

            if (from != null) {
                if (from.equalsIgnoreCase("new")) {
                    binding.tvDecline.setVisibility(View.VISIBLE);
                    binding.tvAccept.setVisibility(View.VISIBLE);
                    binding.LayoutTaskData.setVisibility(View.GONE);
                } else {

                    if (taskData.getTaskStatus().equalsIgnoreCase("completed")) {
                        binding.tvAccept.setVisibility(View.GONE);
                        binding.tvDecline.setVisibility(View.GONE);
                        binding.LayoutTaskData.setVisibility(View.GONE);
                    } else if (taskData.getTaskStatus().equalsIgnoreCase("accepted")) {
                        binding.tvAccept.setText(getResources().getString(R.string.lbl_drive));
                        binding.tvDecline.setText(getResources().getString(R.string.lbl_fail));
                        binding.LayoutTaskData.setVisibility(View.GONE);
                    } else if (taskData.getTaskStatus().equalsIgnoreCase("start_drive")) {
                        binding.tvAccept.setText(getResources().getString(R.string.lbl_arrived));
                        binding.tvDecline.setText(getResources().getString(R.string.lbl_fail));
                        binding.LayoutTaskData.setVisibility(View.GONE);
                    }
                    else if (taskData.getTaskStatus().equalsIgnoreCase("failed")) {
                        binding.tvAccept.setVisibility(View.GONE);
                        binding.tvDecline.setVisibility(View.GONE);
                        binding.imgCheck.setColorFilter(ContextCompat.getColor(context, R.color.colorRed), android.graphics.PorterDuff.Mode.SRC_IN);
                        binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.colorRed));
                        binding.LayoutTaskData.setVisibility(View.GONE);
                    }
                    else if (taskData.getTaskStatus().equalsIgnoreCase("arrived")) {
                        binding.LayoutTaskData.setVisibility(View.VISIBLE);
                        binding.tvAccept.setText(getResources().getString(R.string.lbl_complete));
                        binding.tvDecline.setText(getResources().getString(R.string.lbl_fail));
                    }
                }
            }

            AppLog.e(TAG,"lat"+taskData.getLat());
            binding.tvDate.setText(DateTimeHelper.convertFormat(taskData.getCreatedAt(), "yyyy-MM-dd HH:mm:ss", "dd MMMM, yyyy hh:mm a"));
            binding.tvId.setText("#" + taskData.getTicketNumber());
            binding.tvAddress.setText(taskData.getPickupAddress());
            binding.tvHouseNumber.setText(taskData.getHouseNumber());
            binding.tvComplexName.setText(taskData.getComplexName());
            binding.tvStatus.setText(taskData.getTaskStatus());
            binding.tvDistance.setText(taskData.getPickupDistance() + " km away from current location");
            binding.tvPerson.setText(taskData.getCustomerName());
            binding.tvMobile.setText(taskData.getMobileNumber());
            binding.tvDesc.setText(taskData.getDescription());
        }
        else
        {
            Log.e(TAG,"getIntentNull");
        }

        binding.signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SigntureActivity.class);
                i.putExtra("taskId", taskData.getTaskId());
                startActivityForResult(i, SIGNATURE_IMAGE);
            }
        });

        binding.tvDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.tvDecline.getText().toString().equalsIgnoreCase("Fail"))
                {
                    openReasonDialog("failed");
                }

            }
        });
        binding.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+taskData.getMobileNumber()));
                startActivity(callIntent);
            }
        });
        binding.startDriveRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateStart();
            }
        });
        binding.tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = "", name = "", nextStatus = "";
                if (binding.tvAccept.getText().toString().equalsIgnoreCase("Accept")) {
                    type = "accepted";
                    name = "Accept";
                }
                if (binding.tvAccept.getText().toString().equalsIgnoreCase("Start Drive")) {
                    type = "start_drive";
                    name = "Start Drive";
                }
                if (binding.tvAccept.getText().toString().equalsIgnoreCase("Arrived")) {
                    type = "arrived";
                    name = "Arrived";
                }
                if (binding.tvAccept.getText().toString().equalsIgnoreCase("Complete")) {
                    type = "completed";
                    name = "Complete";
                }

                if (type.equalsIgnoreCase("completed")) {

                    if (appPref.getString(AppPref.TASK_ID_PART_DATA) != null && !appPref.getString(AppPref.TASK_ID_PART_DATA).isEmpty() && appPref.getString(AppPref.TASK_ID_PART_DATA).equalsIgnoreCase(taskData.getTaskId()))
                    {
                        if (isValid()) {
                            sendData(type, name);
                        }
                    }
                    else
                    {
                        if (manager.getProductData(TaskID).size()>0)
                        {
                            openPartQtyUsedDialog(TaskID);
                        }
                        else
                        {
                            if (isValid()) {
                                sendData(type, name);
                            }
                        }
                    }
                }
                else {
                    sendData(type, name);
                }
            }
        });

    }

    private void openPartQtyUsedDialog(String taskID) {

        qtyDialog.addTaskID(taskID,this);
        qtyDialog.show(getSupportFragmentManager(), "");

    }
    /*public NewTaskModel.NewTaskData passData()
    {
        taskData = (NewTaskModel.NewTaskData) getIntent().getSerializableExtra("taskData");
        return taskData;
    }*/

    private boolean isValid() {
        if (uploadBeforeImageList.size() == 0) {
            showToast("Upload Before Repair Images");
            return false;
        }
        if (uploadAfterImageList.size() == 0) {
            showToast("Upload After Repair Images");
            return false;
        }
        if (TextUtils.isEmpty(signatureImage)) {
            showToast("Upload Signature");
            return false;
        }

        return true;
    }

    private void sendData(String type, String name) {

        AppDialog.showConfirmDialog(TaskDetailActivity.this, "Are you sure you want to " + name + "?", new AppDialog.AppDialogListener() {
            @Override
            public void okClick(DialogInterface dialog) {

                TaskStatusChangeModel.TaskStatusChangeReq taskStatusChangeReq = new TaskStatusChangeModel.TaskStatusChangeReq();
                taskStatusChangeReq.setTask_id(taskData.getTaskId());
                taskStatusChangeReq.setStatus(type);

                String id = taskData.getTaskId();
                String name = taskData.getCustomerName();
                String address = taskData.getPickupAddress();
                String phone_no = taskData.getMobileNumber();
                String distance = taskData.getPickupDistance();
                String description = taskData.getDescription();
                String date = taskData.getCreatedAt();
                String ticketNo = taskData.getTicketNumber();

                StatusUpdate = type;

              /*  if (type.equalsIgnoreCase("start_drive"))
                {
                    navigateStart();
                }*/
                if (type.equalsIgnoreCase("completed")) {

                    manager.updateTask(id, name, address, phone_no, distance, description, date, type, signatureImage, binding.etComments.getText().toString(),ticketNo);
                }
                else {
                    manager.updateTask(id, name, address, phone_no, distance, description, date, type, "", "",ticketNo);
                }
                String currentDate = DateTimeHelper.convertFormat(new Date(), "yyyy-MM-dd HH:mm:ss");

                if (isConnected())
                {
                    if (type.equalsIgnoreCase("failed"))
                    {
                        AddFailedReasonModel.AddFailedReq addFailedReq = new AddFailedReasonModel.AddFailedReq();
                        addFailedReq.setFail_task_id(taskData.getTaskId());
                        addFailedReq.setFail_image(reasoniamge);
                        addFailedReq.setFail_reason(reasonname);
                        addFailedReq.setFail_image_name(reasonimagename);

                        getInputStreamByUri(getApplicationContext(),reasoniamge);

                        Log.e(TAG,"requestFail"+new Gson().toJson(addFailedReq));
                        orderViewModel.addFailReason(addFailedReq,inputStream,getApplicationContext());
                    }
                    else
                    {
                        Log.e(TAG,"status"+type);
                        manager.addTaskStatus(id, currentDate, type, "true");
                        orderViewModel.changeTaskStatus(taskStatusChangeReq);
                    }
                }
                else
                    {
                    manager.addTaskStatus(id, currentDate, type, "false");
                    if (type.equalsIgnoreCase("completed"))
                    {
                        int imageCount = 0;
                        if (uploadBeforeImageList.size() > 0) {
                            storeBeforeImage(id, imageCount);
                        }
                    }
                    else {
                        Log.e(TAG,"send data on TaskChange");
                        onTaskStatusChange("", taskStatusChangeReq.getTask_id());
                    }
                }
            }
        });
    }

    public InputStream getInputStreamByUri(Context context, Uri uri) {
        try {

            inputStream = context.getContentResolver().openInputStream(uri);

            return inputStream;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void storeBeforeImage(String id, int imageCount) {

        if (isConnected()) {
            manager.uploadTaskImages(id, "before", uploadBeforeImageList.get(imageCount).getImgPath().toString(), "false");
        } else {
            manager.uploadTaskImages(id, "before", uploadBeforeImageList.get(imageCount).getImgPath().toString(), "false");
        }

        imageCount++;

        if (uploadBeforeImageList.size() != imageCount) {
            storeBeforeImage(id, imageCount);
        } else {
            if (uploadAfterImageList.size() > 0) {
                imageCount = 0;
                storeAfterImage(id, imageCount);
            }
        }
    }
    private void storeAfterImage(String id, int imageCount) {

        if (isConnected()) {
            manager.uploadTaskImages(id, "after", uploadAfterImageList.get(imageCount).getImgPath().toString(), "false");
        } else {
            manager.uploadTaskImages(id, "after", uploadAfterImageList.get(imageCount).getImgPath().toString(), "false");
        }

        imageCount++;

        if (uploadAfterImageList.size() != imageCount) {
            storeAfterImage(id, imageCount);
        } else {

            if (isConnected()) {
                manager.uploadTaskImages(id, "signature", signatureImage, "false");
              //  completeTaskApi();
                StatusUpdate = "done";
                onTaskStatusChange("", "");
                ApiCallStatusServices.start(this);
            } else {
                manager.uploadTaskImages(id, "signature", signatureImage, "false");
                hideLoading();
                onTaskStatusChange("", id);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGNATURE_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String path = data.getStringExtra("image");
                    String taskId = data.getStringExtra("taskId");

                    try {
                        ContextWrapper cw = new ContextWrapper(getApplicationContext());
                        File directory = cw.getDir("Signature", Context.MODE_PRIVATE);
                        File f = new File(directory, "signature_" + taskId + ".jpg");
                        signatureImage = f.getAbsolutePath();


                        signatureImageUri = Uri.parse(path);

                        /*if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                            signatureImageUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", f);
                        } else {
                            signatureImageUri = Uri.fromFile(f);
                        }*/

                        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
                        binding.signature.setImageBitmap(b);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        else if(requestCode == BUBBLE_SERVICECODE)
        {
         if (resultCode == RESULT_OK)
         {
             try
             {
                 AppLog.e("BUBBLE", "onActivityResult " + requestCode + " " + resultCode);
                 if (Build.VERSION.SDK_INT >= 23) {
                     if (Settings.canDrawOverlays(getApplicationContext())) {
                         navigateAndStartBubbleService();
                     }
                 }
             } catch (Exception e) {
                 AppLog.e("Exception: ", "error: " + e.getMessage());
             }
         }
        }
    }

    @Override
    public void onTaskStatusChange(String message, String task_id) {

        if (StatusUpdate.equals("accepted")) {
            binding.LayoutTaskData.setVisibility(View.GONE);
            binding.tvDecline.setVisibility(View.GONE);
            binding.tvAccept.setText("Start Drive");
            binding.tvStatus.setText(StatusUpdate);
        }
        if (StatusUpdate.equals("start_drive")) {
            binding.LayoutTaskData.setVisibility(View.GONE);
            binding.tvAccept.setText("Arrived");
            binding.tvStatus.setText(StatusUpdate);
        }
        if (StatusUpdate.equals("arrived")) {
            binding.LayoutTaskData.setVisibility(View.VISIBLE);
            binding.tvAccept.setText("Complete");
            binding.tvStatus.setText(StatusUpdate);
        }
        if (StatusUpdate.equals("failed")) {
            Log.e(TAG,"failed status call");
            binding.LayoutTaskData.setVisibility(View.GONE);
            binding.tvAccept.setVisibility(View.GONE);
            binding.tvDecline.setVisibility(View.GONE);
            binding.tvStatus.setText(StatusUpdate);
            onBackPressed();
        }
        if (StatusUpdate.equals("completed")) {

            if (isConnected()) {
                int imageCount = 0;
                if (uploadBeforeImageList.size() > 0) {
                    storeBeforeImage(task_id, imageCount);
                }
            } else {
                binding.tvStatus.setText(StatusUpdate);
                onBackPressed();
            }
        }
        if (StatusUpdate.equalsIgnoreCase("done")) {
            onBackPressed();
        }
    }

    @Override
    public void newTaskList(ArrayList<NewTaskModel.NewTaskData> newTaskData) {

    }

    @Override
    public void myTaskList(ArrayList<NewTaskModel.NewTaskData> myTaskData) {

    }

    @Override
    public void completedTaskRes(String message) {
        StatusUpdate = "done";
        onTaskStatusChange("", "");
    }

    @Override
    public void addPart(int id, ArrayList<PartModel.TaskProductData> productData) {

    }

    @Override
    public void addRefuelingRes(String message) {

    }

    private void completeTaskApi() {

        ArrayList<Uri> pob = new ArrayList<>();
        for (int i = 0; i < uploadBeforeImageList.size(); i++) {
            pob.add(uploadBeforeImageList.get(i).getImgPath());
        }

        ArrayList<Uri> poa = new ArrayList<>();
        for (int i = 0; i < uploadAfterImageList.size(); i++) {
            poa.add(uploadAfterImageList.get(i).getImgPath());
        }

        ArrayList<PartModel.TaskProductDataUsed> productDataUsed = new ArrayList<>();

        for (int i=0;i<productData.size();i++)
        {
            PartModel.TaskProductDataUsed dataUsed= new PartModel.TaskProductDataUsed();
            dataUsed.setProduct_id(productData.get(i).getProduct_id());
            dataUsed.setProduct_name(productData.get(i).getProduct_name());
            dataUsed.setUpc_code(productData.get(i).getUpc_code());
            dataUsed.setQty(productData.get(i).getUsed_qty());
            productDataUsed.add(dataUsed);
        }
        CompleteTaskModel.CompleteTaskReq completeTaskReq = new CompleteTaskModel.CompleteTaskReq();
        completeTaskReq.setTask_id(taskData.getTaskId());
        completeTaskReq.setPob(pob);
        completeTaskReq.setPoa(poa);
        completeTaskReq.setTask_products_used(productDataUsed);
        completeTaskReq.setSignature(signatureImageUri);


        completeTaskReq.setComments(binding.etComments.getText().toString());

        orderViewModel.completeTask(completeTaskReq);

    }

    @Override
    public void orderPlaced(String message) {


    }

    @Override
    public void onPaymentSuccess() {

    }

    @Override
    public void onCancelOrder(String message) {

        if (message.equalsIgnoreCase("Failed Task"))
        {
            onBackPressed();
        }
    }

    @Override
    public void Duty(String message) {

    }

    @Override
    public void OnSuccess() {

    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            AppLog.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    private void openSuccessfullDialog(String message) {

        uploadBeforeImageList.clear();

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
                uploadBeforeImageList.clear();
                uploadTaskImageAdapter.notifyDataSetChanged();
            }
        });
        dialog.show();
    }

    @Override
    public void onNewImage(int value) {

        if (value == 1) {
            imageType = 1;
            imageDialog.show(getSupportFragmentManager(), "");

        }
        if (value == 0) {
            imageType = 0;
            imageDialog.show(getSupportFragmentManager(), "");
        }
    }

    @Override
    public void onDeleteImage(int position, int value) {

        if (value == 1) {
            uploadAfterImageList.remove(position);
            uploadAfterTaskImageAdapter.notifyDataSetChanged();
        }
        if (value == 0) {
            uploadBeforeImageList.remove(position);
            uploadTaskImageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onEditName(UploadAfterTaskImageModel uploadAfterTaskImageModel) {
        openImgSaveDialog(uploadAfterTaskImageModel.getImgPath(), uploadAfterTaskImageModel.getImgName());
    }

    @Override
    public void onEditName(UploadTaskImageModel uploadTaskImageModel) {
        openImgSaveDialog(uploadTaskImageModel.getImgPath(),uploadTaskImageModel.getImgName());
    }

    @Override
    public void onImageSelected(Uri path) {

        imageDialog.dismiss();

        if (path != null) {
            openImgSaveDialog(path, "");
        }
       /* if (imageType == 0) {
            uploadTaskImageModel = new UploadTaskImageModel();
            uploadTaskImageModel.setImgPath(path);
            uploadTaskImageModel.setImgName(appPref.getInt(Constants.USER_ID) + "_" + new Random().nextInt(61) + 20);
            uploadBeforeImageList.add(uploadTaskImageModel);
            uploadTaskImageAdapter.addImageData(uploadBeforeImageList);

        }
        if (imageType == 1) {
            uploadAfterTaskImageModel = new UploadAfterTaskImageModel();
            uploadAfterTaskImageModel.setImgPath(path);
            uploadAfterTaskImageModel.setImgName(appPref.getInt(Constants.USER_ID) + "_" + new Random().nextInt(61) + 20);
            uploadAfterImageList.add(uploadAfterTaskImageModel);
            uploadAfterTaskImageAdapter.addImageData(uploadAfterImageList);
        }*/
    }

    @Override
    public void onVideoSelected(String path) {

    }

    @Override
    public void onImageSelected(ArrayList<Uri> listImages) {

        imageDialog.dismiss();


        if (imageType == 0) {
            if (uploadBeforeImageList.size() <= 10) {

                for (int i = 0; i < listImages.size(); i++) {

                    uploadTaskImageModel = new UploadTaskImageModel();
                    uploadTaskImageModel.setImgPath(listImages.get(i));
                    uploadTaskImageModel.setImgName(appPref.getInt(Constants.USER_ID) + "_" + new Random().nextInt(61) + 20);
                    uploadBeforeImageList.add(uploadTaskImageModel);
                }

                uploadTaskImageAdapter.notifyDataSetChanged();
            } else {
                showToast("Upload maximum 10 photos");
            }
        }
        if (imageType == 1) {
            if (uploadAfterImageList.size() <= 10) {

                for (int i = 0; i < listImages.size(); i++) {

                    uploadAfterTaskImageModel = new UploadAfterTaskImageModel();
                    uploadAfterTaskImageModel.setImgPath(listImages.get(i));
                    uploadAfterTaskImageModel.setImgName(appPref.getInt(Constants.USER_ID) + "_" + new Random().nextInt(61) + 20);
                    uploadAfterImageList.add(uploadAfterTaskImageModel);
                }
                uploadAfterTaskImageAdapter.notifyDataSetChanged();
            } else {
                showToast("Upload maximum 10 photos");
            }
        }
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

                    if (failimageType == 1)
                    {
                        imageuri = path.toString();
                        imageUri = path;
                        Glide.with(context).load(path).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(dailogFailreasonBinding.ReasonImage);
                        dailogFailreasonBinding.nameofiamge.setText(dialogSaveImageBinding.etNameOfImg.getText().toString());
                        failimageType = 0;
                    }
                    else
                    {
                        if (imageType == 0) {
                            uploadTaskImageModel = new UploadTaskImageModel();

                            if (!TextUtils.isEmpty(imgName)) {

                                for (int i = 0; i < uploadBeforeImageList.size(); i++) {
                                    if (uploadBeforeImageList.get(i).getImgName().equalsIgnoreCase(imgName)) {
                                        uploadBeforeImageList.remove(i);
                                    }
                                }

                                Log.e(TAG,"ListImageNameif"+dialogSaveImageBinding.etNameOfImg.getText().toString());
                                uploadTaskImageModel.setImgPath(path);
                                uploadTaskImageModel.setImgName(dialogSaveImageBinding.etNameOfImg.getText().toString());
                                uploadBeforeImageList.add(uploadTaskImageModel);
                                uploadTaskImageAdapter.addImageData(uploadBeforeImageList);
                            } else {
                                Log.e(TAG,"ListImageNameelse"+dialogSaveImageBinding.etNameOfImg.getText().toString());
                                uploadTaskImageModel.setImgPath(path);
                                uploadTaskImageModel.setImgName(dialogSaveImageBinding.etNameOfImg.getText().toString());
                                uploadBeforeImageList.add(uploadTaskImageModel);
                                uploadTaskImageAdapter.addImageData(uploadBeforeImageList);
                            }

                        }
                        if (imageType == 1) {
                            uploadAfterTaskImageModel = new UploadAfterTaskImageModel();

                            if (!TextUtils.isEmpty(imgName)) {

                                for (int i = 0; i < uploadAfterImageList.size(); i++) {
                                    if (uploadAfterImageList.get(i).getImgName().equalsIgnoreCase(imgName)) {
                                        uploadAfterImageList.remove(i);
                                    }
                                }
                                uploadAfterTaskImageModel.setImgPath(path);
                                uploadAfterTaskImageModel.setImgName(dialogSaveImageBinding.etNameOfImg.getText().toString());
                                uploadAfterImageList.add(uploadAfterTaskImageModel);
                                uploadAfterTaskImageAdapter.addImageData(uploadAfterImageList);
                            } else {

                                uploadAfterTaskImageModel.setImgPath(path);
                                uploadAfterTaskImageModel.setImgName(dialogSaveImageBinding.etNameOfImg.getText().toString());
                                uploadAfterImageList.add(uploadAfterTaskImageModel);
                                uploadAfterTaskImageAdapter.addImageData(uploadAfterImageList);
                            }
                        }
                    }

                }
            }
        });
        dialog.show();
    }


    private void openReasonDialog(String type) {

        dialogReason = new Dialog(context, R.style.AppDialog);
        dailogFailreasonBinding =DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dailog_failreason, null, false);
        dialogReason.setContentView(dailogFailreasonBinding.getRoot());
        dialogReason.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogReason.setCancelable(false);
        dialogReason.setCanceledOnTouchOutside(false);

        dailogFailreasonBinding.rgCheck.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton btn = (RadioButton) group.findViewById(checkedId);
                String value = btn.getText().toString();

                switch (checkedId)
                {
                    case R.id.rb1:
                        dailogFailreasonBinding.tvReason.setVisibility(View.GONE);
                        dailogFailreasonBinding.etReasonofFail.setVisibility(View.GONE);
                        value_last = dailogFailreasonBinding.rb1.getText().toString();
                        break;

                    case R.id.rb2:
                        dailogFailreasonBinding.tvReason.setVisibility(View.VISIBLE);
                        dailogFailreasonBinding.etReasonofFail.setVisibility(View.VISIBLE);
                        value_last = dailogFailreasonBinding.rb2.getText().toString();
                        break;
                }
            }
        });


        dailogFailreasonBinding.ReasonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                failimageType = 1;
                imageDialog.show(getSupportFragmentManager(), "");
            }
        });

        dailogFailreasonBinding.btnCancelReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                value_last = null;
                imageuri = "no image";
                dialogReason.dismiss();
            }
        });

        dailogFailreasonBinding.btnSaveReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG,"fail reason "+value_last);
                if (dailogFailreasonBinding.etReasonofFail.getVisibility()==View.VISIBLE && dailogFailreasonBinding.etReasonofFail.getText().toString().isEmpty()) {
                    showToast("Enter Reason");
                }
                else if (value_last == null || value_last.isEmpty())
                {
                    showToast("select Reason");
                }
                else if (dailogFailreasonBinding.nameofiamge.equals("Name of Image"))
                {
                    showToast("Image Name Required");
                }
                else if (imageuri.equalsIgnoreCase("no image"))
                {
                    showToast("Select Image Reason");
                }
                else {

                    if (dailogFailreasonBinding.etReasonofFail.getVisibility()==View.VISIBLE)
                    {
                        value_last = dailogFailreasonBinding.etReasonofFail.getText().toString();
                    }
                    String reason = value_last;
                    String name = "failed";
                    Uri imagepath = imageUri;
                    String statusType = type;

                    reasonname = reason;
                    reasoniamge = imagepath;
                    reasonimagename = dailogFailreasonBinding.nameofiamge.getText().toString();

                    Log.e(TAG,"dailogvalue: "+reason+"\n"+statusType);
                    Log.e(TAG,"taskid"+taskData.getTaskId());

                    if (type.equalsIgnoreCase("failed")) {

                        value_last = null;
                        imageuri = "no image";

                        if (isConnected())
                        {
                            //manager.addFailTaskStatus(taskData.getTaskId(),reason,imagepath,reasonimagename,true);
                            sendData(type, name);
                        }
                        else
                        {
                            manager.addFailTaskStatus(taskData.getTaskId(),reason,imagepath.toString(),reasonimagename,false);
                            sendData(type, name);
                        }
                    }
                    dialogReason.dismiss();
                }
            }
        });

        dialogReason.show();
    }


    private void navigateStart(){
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            checkPerms();
        }
        else {
            navigateAndStartBubbleService();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void navigateAndStartBubbleService() {

        Log.e(TAG,"LAT"+taskData.getLat()+"\nLANG"+taskData.getLng());

        Intent i = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + taskData.getLat() + "," + taskData.getLng()));
        i.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        try {
            startActivityForResult(i,BUBBLE_SERVICECODE);
            startBubbleService();
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://maps.google.com?daddr=" + taskData.getLat() + "," + taskData.getLng()));
                startActivityForResult(unrestrictedIntent,BUBBLE_SERVICECODE);
                startBubbleService();
            } catch (ActivityNotFoundException innerEx) {
                Toast.makeText(getApplicationContext(), "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
    }
    public static final int OVERLAY_PERMISSION_REQ_CODE = 1235;
    public void checkPerms() {
        try {
            // Checking if device version > 22 and we need to use new permission model
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1)
            {
                // Checking if we can draw window overlay
                if (!Settings.canDrawOverlays(getApplicationContext()))
                {
                    // Requesting permission for window overlay(needed for all react-native apps)
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                }
                else
                {
                    navigateAndStartBubbleService();
                }
            }
        } catch (Exception e) {
            AppLog.e("Exception: ", "error: " + e.getMessage());
        }
    }
   /* @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG , "ONACTIVITYRESULT CALLED");

            try
            {
                AppLog.e("BUBBLE", "onActivityResult " + requestCode + " " + resultCode);
                if (Build.VERSION.SDK_INT >= 23) {
                    if (Settings.canDrawOverlays(getApplicationContext())) {
                        navigateAndStartBubbleService();
                    }
                }
            } catch (Exception e) {
                AppLog.e("Exception: ", "error: " + e.getMessage());
            }
    }*/


    public void startBubbleService() {
        AppLog.e(TAG,"startBubbleService()");
        Intent uploadIntent2 = new Intent(this, FloatingFaceBubbleService.class);
        uploadIntent2.putExtra("from", "myTask");
        uploadIntent2.putExtra("taskData", taskData);
        bindService(uploadIntent2, mConnection, Context.BIND_AUTO_CREATE);
    }
    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            FloatingFaceBubbleService.LocalBinder binder = (FloatingFaceBubbleService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    public void removeBubble() {
        AppLog.e("BubbleService", "removeView Activity");
        if (mService != null) {
            mService.removeBubble();
            if (mBound) {
                try {
                    unbindService(mConnection);
                    AppLog.e("BubbleService", "unbindService Activity");
                } catch (Exception e) {
                    AppLog.e("BubbleService", "unbindService error is " + e.toString());
                }
            }
        }
    }

    @Override
    public void addList(ArrayList<PartModel.TaskProductData> productData) {
        this.productData = productData;
        Log.e(TAG,"productData"+this.productData.size());

        for (int i=0;i<productData.size();i++)
        {
            manager.updatePart(TaskID,String.valueOf(productData.get(i).getProduct_id()),productData.get(i).getProduct_name(),productData.get(i).getUpc_code(),productData.get(i).getQty(),productData.get(i).getUsed_qty());
        }
    }
}