package com.onthemove.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.OpenableColumns;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.onthemove.activities.MainActivity2;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.database.TaskListDatabaseManager;
import com.onthemove.modelClasses.PartModel;
import com.onthemove.modelClasses.SubmitTaskStatusModel;
import com.onthemove.network.RetroClient;
import com.onthemove.responseClasses.AddRefuelingModel;
import com.onthemove.responseClasses.CompleteTaskModel;
import com.onthemove.responseClasses.SyncStatusDataModel;
import com.onthemove.responseClasses.UploadedImageModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCallStatusServices extends Service {

    private static final String TAG = "ApiCallStatusServices";

    private static ApiCallStatusServices callStatusServices;
    private Context context;
    private ArrayList<SubmitTaskStatusModel> list;
    private ArrayList<UploadedImageModel> imageList;
    private ArrayList<InputStream> inputStreams;
    private InputStream inputStreamSign;
    private TaskListDatabaseManager manager;
    private int imgCount = 0, refuelCount = 0;

    private ArrayList<InputStream> inputStreamsPob;
    private ArrayList<InputStream> inputStreamsPoa;

    private ArrayList<AddRefuelingModel.AddRefuelingReq> refuelingList = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        callStatusServices = this;
        context = this;
        inputStreamSign = null;
        manager = new TaskListDatabaseManager(context);
        list = new ArrayList<>();
        imageList = new ArrayList<>();
        inputStreams = new ArrayList<>();
        inputStreamsPob = new ArrayList<>();
        inputStreamsPoa = new ArrayList<>();

        AppLog.e(TAG, "call this services");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppLog.e(TAG, "call start services");
        callStatusServices = this;
        init();
        return START_NOT_STICKY;
    }

    public static void start(Context context) {
        if (callStatusServices == null) {
            context.startService(new Intent(context, ApiCallStatusServices.class));
        } else {
            callStatusServices.init();
        }
    }

    public byte[] readBytes(InputStream inputStream) throws IOException {
        // this dynamically extends to take the bytes you read
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        // this is storage overwritten on each iteration with bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        // we need to know how may bytes were read to write them to the byteBuffer
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        // and then we can return your byte array.
        return byteBuffer.toByteArray();
    }

    public String getPath(Uri uri)
    {
        String[] projection = {OpenableColumns.DISPLAY_NAME };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =  cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    public ArrayList<InputStream> getInputStreamByUri(Context context, ArrayList<Uri> uri) {

        ArrayList<InputStream> inputStreams = new ArrayList<>();
        try {
            for (int i = 0; i<uri.size();i++)
            {
                inputStreams.add(context.getContentResolver().openInputStream(uri.get(i)));
            }
            return inputStreams;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

    private void init() {

        list.clear();
        imageList.clear();
        inputStreams.clear();
        inputStreamsPoa.clear();
        inputStreamsPob.clear();
        inputStreamSign = null;

        list = manager.getTaskStatus();

        if (list.size() > 0) {

            SyncStatusDataModel.SyncStatusDataReq syncStatusDataReq = new SyncStatusDataModel.SyncStatusDataReq();
            syncStatusDataReq.setSyncStatusData(list);
            syncTaskStatus(syncStatusDataReq);

        } else {
            callImageUploadApi();
        }
    }

    private void callImageUploadApi() {

        imageList = manager.getUploadedImages();

        if (imageList.size() > 0) {

            ArrayList<CompleteTaskModel.CompleteTaskReq> completeTaskList = new ArrayList<>();
            ArrayList<String> idList = manager.getId();

            for (int i = 0; i < idList.size(); i++) {

                ArrayList<UploadedImageModel> addList = manager.getUploadedImagesId(idList.get(i));

                CompleteTaskModel.CompleteTaskReq data = new CompleteTaskModel.CompleteTaskReq();
                ArrayList<Uri> poa = new ArrayList<>();
                ArrayList<Uri> pob = new ArrayList<>();

                for (int j = 0; j < addList.size(); j++) {


                    data.setSignature(Uri.parse(manager.getSignatureImage(addList.get(i).getId())));
                    data.setComments(manager.getComment(addList.get(i).getId()));
                    data.setTask_id(addList.get(j).getId());
                    data.setTask_products_used(manager.getUsedProductData(addList.get(i).getId()));

                    if (addList.get(j).getImage_type().equalsIgnoreCase("before")) {
                        pob.add(Uri.parse(addList.get(j).getImage()));
                    }
                    if (addList.get(j).getImage_type().equalsIgnoreCase("after")) {
                        poa.add(Uri.parse(addList.get(j).getImage()));
                    }
                    data.setPob(pob);
                    data.setPoa(poa);
                }

                completeTaskList.add(data);
            }

            AppLog.e(TAG, "LISTTTT: " + new Gson().toJson(completeTaskList));

            imgCount = 0;
            uploadImageApi(completeTaskList);
        } else {

            refuelingList = manager.getRefuelingList();

            if (refuelingList.size() > 0) {

                refuelCount = 0;
                addRefueling(refuelingList.get(refuelCount));
            } else {
                MainActivity2.syncDone();
            }
        }
    }

    private void uploadImageApi(ArrayList<CompleteTaskModel.CompleteTaskReq> completeTaskList) {

        completeTask(completeTaskList.get(imgCount), completeTaskList);
    }

    public void completeTask(CompleteTaskModel.CompleteTaskReq completeTaskReq, ArrayList<CompleteTaskModel.CompleteTaskReq> completeTaskList) {

        AppLog.e(TAG, "completeTaskReq: " + new Gson().toJson(completeTaskReq));

        RequestBody taskId = RequestBody.create(MediaType.parse("text/plain"), completeTaskReq.getTask_id());
        RequestBody comment = RequestBody.create(MediaType.parse("text/plain"), completeTaskReq.getComments());

        inputStreamsPob.addAll(getInputStreamByUri(context,completeTaskReq.getPob()));
        inputStreamsPoa.addAll(getInputStreamByUri(context,completeTaskReq.getPoa()));
        inputStreamSign = getInputStreamByUri(context,completeTaskReq.getSignature());

        MultipartBody.Part[] img_pob = null;
        if (completeTaskReq.getPob() != null) {
            img_pob = new MultipartBody.Part[completeTaskReq.getPob().size()];
            for (int i = 0; i < completeTaskReq.getPob().size(); i++) {

                try {
                    File file = new File(getPath(completeTaskReq.getPob().get(i)));
                    byte[] recordData = readBytes(inputStreamsPob.get(i));
                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), recordData);
                    img_pob[i] = MultipartBody.Part.createFormData("pob[" + i + "]", file.getName(), fileBody);
                }catch (Exception e)
                {

                }
            }
        } else {
            img_pob = null;
        }

        MultipartBody.Part[] img_poa = null;
        if (completeTaskReq.getPoa() != null) {
            img_poa = new MultipartBody.Part[completeTaskReq.getPoa().size()];
            for (int i = 0; i < completeTaskReq.getPoa().size(); i++) {

                try {

                    File file = new File(getPath(completeTaskReq.getPoa().get(i)));
                    byte[] recordData = readBytes(inputStreamsPoa.get(i));
                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), recordData);
                    img_poa[i] = MultipartBody.Part.createFormData("poa[" + i + "]", file.getName(), fileBody);
                }catch (Exception e)
                {

                }

            }
        } else {
            img_poa = null;
        }

        MultipartBody.Part signature_img = null;
        if (completeTaskReq.getSignature() != null) {
            try {
                File file = new File(getPath(completeTaskReq.getSignature()));
                byte[] recordData = readBytes(inputStreamSign);
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), recordData);
                signature_img = MultipartBody.Part.createFormData("signature", file.getName(), fileBody);
            }catch (Exception e)
            {
                signature_img = null;
            }

        } else {
            signature_img = null;
        }

        RequestBody part_qty_used = RequestBody.create(MediaType.parse("text/plain"), new Gson().toJson(completeTaskReq.getTask_products_used()).toString());


        RetroClient.getApiService().addCompletedTaskPictures(taskId, img_pob, img_poa, signature_img, part_qty_used, comment)
                .enqueue(new Callback<CompleteTaskModel.CompleteTaskRes>() {
                    @Override
                    public void onResponse(Call<CompleteTaskModel.CompleteTaskRes> call, Response<CompleteTaskModel.CompleteTaskRes> response) {
                        AppLog.e(TAG, "CompleteTaskRes: " + response);
                        AppLog.e(TAG, "CompleteTaskRes.body(): " + new Gson().toJson(response.body()));

                        if (response.body() != null) {
                            if (response.body().isStatus()) {

                                if (completeTaskList.size() != (imgCount + 1)) {
                                    imgCount++;
                                    uploadImageApi(completeTaskList);
                                } else {
                                    AppLog.e(TAG, "IMAGE UPLOADED-----: ");
                                    MainActivity2.syncDone();
                                }
                                manager.updateSyncTaskImage(completeTaskReq.getTask_id());
                            } else {
                                AppLog.e(TAG, "UPDATE DATA = " + response.body().getMessage());
                            }
                        } else {
                            AppLog.e(TAG, "Please try again");
                        }
                    }

                    @Override
                    public void onFailure(Call<CompleteTaskModel.CompleteTaskRes> call, Throwable t) {
                        AppLog.e(TAG, "UPDATE DATA FAIL = " + t.getMessage());
                    }
                });
    }

    public void syncTaskStatus(SyncStatusDataModel.SyncStatusDataReq syncStatusDataReq) {

        AppLog.e(TAG, "syncStatusDataReq: " + new Gson().toJson(syncStatusDataReq));

        RetroClient.getApiService().syncTaskStatus(syncStatusDataReq)
                .enqueue(new Callback<SyncStatusDataModel.SyncStatusDataRes>() {
                    @Override
                    public void onResponse(Call<SyncStatusDataModel.SyncStatusDataRes> call, Response<SyncStatusDataModel.SyncStatusDataRes> response) {
                        AppLog.e(TAG, "SyncStatusDataRes: " + response);
                        AppLog.e(TAG, "SyncStatusDataRes.body(): " + new Gson().toJson(response.body()));

                        if (response.body() != null) {
                            if (response.body().isStatus()) {
                                AppLog.e(TAG, "DATA uploadedd..");
                                manager.updateTaskStatus();
                                callImageUploadApi();
                            } else {
                                AppLog.e(TAG, "UPDATE DATA = " + response.body().getMessage());
                            }
                        } else {
                            AppLog.e(TAG, "Please try again");
                        }
                    }

                    @Override
                    public void onFailure(Call<SyncStatusDataModel.SyncStatusDataRes> call, Throwable t) {
                        AppLog.e(TAG, "UPDATE DATA FAIL = " + t.getMessage());
                    }
                });
    }

    public void addRefueling(AddRefuelingModel.AddRefuelingReq req) {

        RequestBody vehicleId = RequestBody.create(MediaType.parse("text/plain"), req.getFk_vehicle_id());
        RequestBody meterReading = RequestBody.create(MediaType.parse("text/plain"), req.getMeter_reading());
        RequestBody liter = RequestBody.create(MediaType.parse("text/plain"), req.getLiter());
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"), req.getPrice());
        RequestBody recpNo = RequestBody.create(MediaType.parse("text/plain"), req.getReceipt_number());

        MultipartBody.Part receiptImg = null;
        MultipartBody.Part dashboardImg = null;
        try {
            File filefront = new File(getPath(req.getReceipt_image()));
            byte[] data =  readBytes(getInputStreamByUri(context,req.getReceipt_image()));
            RequestBody fileBodyfront = RequestBody.create(MediaType.parse("image/*"), data);
            receiptImg = MultipartBody.Part.createFormData("receipt_image", filefront.getName(), fileBodyfront);


            File fileback = new File(getPath(req.getDashboard_image()));
            byte[] data1 =  readBytes(getInputStreamByUri(context,req.getDashboard_image()));
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), data1);
            dashboardImg = MultipartBody.Part.createFormData("mr_image", fileback.getName(), requestBody);

        }catch (Exception e)
        {

        }


        RetroClient.getApiService().submitVehiclesRef(vehicleId, meterReading, recpNo, liter, price, receiptImg,dashboardImg)
                .enqueue(new Callback<AddRefuelingModel.AddRefuelingRes>() {
                    @Override
                    public void onResponse(Call<AddRefuelingModel.AddRefuelingRes> call, Response<AddRefuelingModel.AddRefuelingRes> response) {
                        AppLog.e(TAG, "AddRefuelingResApiCallStatus: " + response);
                        AppLog.e(TAG, "AddRefuelingRes.body(): " + new Gson().toJson(response.body()));

                        AddRefuelingModel.AddRefuelingRes addRefuelingRes = response.body();

                        if (addRefuelingRes != null) {

                            boolean status = addRefuelingRes.isStatus();

                            if (status) {

                                if (refuelingList.size() != (refuelCount + 1)) {
                                    refuelCount++;
                                    addRefueling(refuelingList.get(refuelCount));
                                } else {
                                    AppLog.e(TAG, "Refueling UPLOADED-----: ");
                                    MainActivity2.syncDone();
                                }
                                manager.updateRefuelingSync(req.getId());
                            } else {
                                if (response.body() != null) {
                                    AppLog.e(TAG, "" + response.body().getMessage());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AddRefuelingModel.AddRefuelingRes> call, Throwable t) {
                        AppLog.e(TAG, "onFailure:" + t.getMessage());
                    }
                });

    }

    public static void stop() {
        if (callStatusServices != null) {
            callStatusServices.stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callStatusServices = null;
        AppLog.e(TAG, "onDestroy()");
    }
}
