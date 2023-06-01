package com.onthemove.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.provider.OpenableColumns;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.onthemove.activities.MainActivity;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.database.DatabaseManager;
import com.onthemove.modelClasses.SubmitOrderModel;
import com.onthemove.network.RetroClient;
import com.onthemove.requestClasses.SubmitOrderRequest;
import com.onthemove.responseClasses.SubmitOrderResponse;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCallServices extends Service {

    private static final String TAG = "ApiCallServices";

    private static ApiCallServices apiCallServices;
    private Context context;
    private DatabaseManager databaseManager;
    //  private TaskStatusDatabaseManager statusDatabaseManager;
    ArrayList<SubmitOrderModel> list;
    ArrayList<InputStream> inpuStreamList = new ArrayList<>();
    //    ArrayList<SubmitTaskStatusModel> listtask;
    ArrayList<Uri> image;
    private int count = 0;
    SubmitOrderRequest submitOrderRequest;
    //   SubmitTaskStatusRequest statusRequest;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        apiCallServices = this;
        context = this;
        databaseManager = new DatabaseManager(context);
        //    statusDatabaseManager = new TaskStatusDatabaseManager(context);
        list = new ArrayList<>();
        //     listtask = new ArrayList<>();
        image = new ArrayList<>();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        apiCallServices = this;

        init();

        return START_NOT_STICKY;
    }

    public static void start(Context context) {
        if (apiCallServices == null) {
            context.startService(new Intent(context, ApiCallServices.class));
        } else {
            apiCallServices.init();
        }
    }

    private void init() {
        //  listtask.clear();
        list.clear();
        image.clear();
        inpuStreamList.clear();

        list = databaseManager.getSubmitData();

        //    listtask = statusDatabaseManager.getSubmitTaskStatusData();

        AppLog.e(TAG, "LIST SIZE IN SERVICE = " + list.size());


        if (list.size() > 0) {

            callAPI(count);
        }

       /* for (int i = 0; i < list.size(); i++) {

            String id = list.get(i).getId();

            submitOrderRequest = new SubmitOrderRequest();

            List<String> imageList = Arrays.asList(list.get(i).getImage().split(","));
            image.addAll(imageList);
            submitOrderRequest.setTicket_number(list.get(i).getTicket_number());
            submitOrderRequest.setComment(list.get(i).getComment());
            submitOrderRequest.setName_of_the_image(list.get(i).getName_of_the_image());
            submitOrderRequest.setImage(image);
            submitOrderRequest.setLat(list.get(i).getLat());
            submitOrderRequest.setLng(list.get(i).getLng());
            submitOrderRequest.setAddress(list.get(i).getAddress());
            AppLog.e(TAG, "SUBMIT ORDER DATA IN SERVICE = " + new Gson().toJson(submitOrderRequest));
            AppLog.e(TAG, "CALL API");
            callSbmitApi(id);
        }*/

    }

    private void callAPI(int i) {

        String id = list.get(i).getId();

        submitOrderRequest = new SubmitOrderRequest();

        List<String> imageList = Arrays.asList(list.get(i).getImage().split(","));
        List<Uri> imageList1 =new ArrayList<>();
        image.clear();
        inpuStreamList.clear();

        for (int j = 0;j<imageList.size();j++)
        {
            imageList1.add(Uri.parse(imageList.get(i)));
        }
        image.addAll(imageList1);


        getInputStreamByUri(context,image);

        submitOrderRequest.setTicket_number(list.get(i).getTicket_number());
        submitOrderRequest.setComment(list.get(i).getComment());
        submitOrderRequest.setName_of_the_image(list.get(i).getName_of_the_image());
        submitOrderRequest.setImage(image);
        submitOrderRequest.setLat(list.get(i).getLat());
        submitOrderRequest.setLng(list.get(i).getLng());
        submitOrderRequest.setAddress(list.get(i).getAddress());

        AppLog.e(TAG, "SUBMIT ORDER DATA IN SERVICE = " + new Gson().toJson(submitOrderRequest));
        AppLog.e(TAG, "CALL API");

        callSbmitApi(id);
    }

    public ArrayList<InputStream> getInputStreamByUri(Context context, ArrayList<Uri> uri) {
        try {
            for (int i = 0; i<uri.size();i++)
            {
                inpuStreamList.add(context.getContentResolver().openInputStream(uri.get(i)));
            }
            return inpuStreamList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void callSbmitApi(String id) {

        RequestBody ticket_number = RequestBody.create(MediaType.parse("text/plain"), submitOrderRequest.getTicket_number());
        RequestBody comment = RequestBody.create(MediaType.parse("text/plain"), submitOrderRequest.getComment());
        RequestBody name_of_the_image = RequestBody.create(MediaType.parse("text/plain"), submitOrderRequest.getName_of_the_image());

     /*   MultipartBody.Part[] img = null;

        if (submitOrderRequest.getImage() != null) {

            img = new MultipartBody.Part[submitOrderRequest.getImage().size()];
            for (int j = 0; j < submitOrderRequest.getImage().size(); j++) {
                File file = new File(submitOrderRequest.getImage().get(j));
                RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                img[j] = MultipartBody.Part.createFormData("image[" + j + "]", file.getName(), fileBody);
            }
        } else {
            img = null;
        }
*/

        MultipartBody.Part[] img = new MultipartBody.Part[submitOrderRequest.getImage().size()];

        if (submitOrderRequest.getImage() != null) {
            for (int i = 0;i<submitOrderRequest.getImage().size();i++)
            {
                try {
                    File file = new File(getPath(submitOrderRequest.getImage().get(i)));
                    Log.e(TAG,"file front name"+file.getName());
                    byte[] recordData = readBytes(inpuStreamList.get(i));
                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), recordData);
                    img[i] = MultipartBody.Part.createFormData("image[" + i + "]", file.getName(), fileBody);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG,"Exception e"+e.getMessage());
                }

         /*   File filefront = new File(String.valueOf(uploadImages.get(i).getImgPath()));
            RequestBody fileBodyfront = RequestBody.create(MediaType.parse("multipart/form-data"), filefront);
            images[i] = MultipartBody.Part.createFormData("images["+i+"]", filefront.getName(), fileBodyfront);*/
            }

        }
        else
        {
            img = null;
        }


        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(submitOrderRequest.getLat()));
        RequestBody lng = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(submitOrderRequest.getLng()));
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), submitOrderRequest.getAddress());

        RetroClient.getApiService().submitOrder(ticket_number, comment, name_of_the_image, img, lat, lng, address).enqueue(new Callback<SubmitOrderResponse>() {
            @Override
            public void onResponse(Call<SubmitOrderResponse> call, Response<SubmitOrderResponse> response) {

                AppLog.e(TAG, "SUBMIT ORDER RESPONSE MSG IN SERVICE = " + response.message());
                AppLog.e(TAG, "SUBMIT ORDER RESPONSE CODE IN SERVICE = " + response.code());
                AppLog.e(TAG, "SUBMIT ORDER RESPONSE DATA IN SERVICE = " + new Gson().toJson(response.body()));

                if (response != null) {

                    AppLog.e(TAG, "SUCCEFULL PASS DATA TO SERVER");
                    AppLog.e(TAG, "LIST SIZE IN RESPONSE = " + list.size());

                    databaseManager.deleteData(id);

                    if (list.size() != (count + 1)) {
                        count++;
                        callAPI(count);
                    }else {
                        MainActivity.syncCompleted();
                    }

                } else {
                    AppLog.e(TAG, "FAIL PASS DATA O SERVER");
                }

            }

            @Override
            public void onFailure(Call<SubmitOrderResponse> call, Throwable t) {
                AppLog.e(TAG, "UPDATE DATA FAIL = " + t.getMessage());
            }
        });
    }

    public static void stop() {
        if (apiCallServices != null) {
            apiCallServices.stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        apiCallServices = null;
        AppLog.e(TAG, "onDestroy()");
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

}
