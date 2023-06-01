package com.onthemove.viewModel;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.onthemove.R;
import com.onthemove.commons.baseClasses.BaseView;
import com.onthemove.commons.baseClasses.BaseViewModel;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.commons.utils.ImageUtils;
import com.onthemove.interfaces.OrderContract;
import com.onthemove.interfaces.VehicalDetails;
import com.onthemove.modelClasses.PartModel;
import com.onthemove.requestClasses.SubmitOrderRequest;
import com.onthemove.requestClasses.SubmitUserRequest;
import com.onthemove.responseClasses.AddFailedReasonModel;
import com.onthemove.responseClasses.AddRefuelingModel;
import com.onthemove.responseClasses.CompleteTaskModel;
import com.onthemove.responseClasses.MyTaskModel;
import com.onthemove.responseClasses.NewTaskModel;
import com.onthemove.responseClasses.OrderDetailsResponse;
import com.onthemove.responseClasses.SubmitOrderResponse;
import com.onthemove.responseClasses.TaskStatusChangeModel;
import com.onthemove.responseClasses.VehicalHealthModel;
import com.onthemove.responseClasses.VehicalListModel;
import com.onthemove.responseClasses.VehicleStatusChangeModel;
import com.onthemove.responseClasses.VehicleSubmitDetailsExteriorModel;
import com.onthemove.responseClasses.VehicleSubmitDetailsInteriorModel;
import com.onthemove.responseClasses.uploadImageResponse;

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

public class OrderViewModel extends BaseViewModel implements OrderContract,VehicalDetails {

    private static final String TAG = "OrderViewModel";
    Context context;
    OrderContract.OrderView view;
    VehicalDetails.VehicalView vehicalView;

    public MutableLiveData<ArrayList<OrderDetailsResponse.DataBean>> orderDetailsLiveDataList = new MutableLiveData<>();

    @Override
    public BaseView getView() {
        return view;
    }

    public void setView(OrderContract.OrderView view) {
        this.view = view;
    }

    public void setVehicalView(VehicalDetails.VehicalView view1) {
        this.vehicalView = view1;
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



    public void submitOrder(SubmitOrderRequest submitOrderRequest, ArrayList<InputStream> inputStreams,Context context) {

        if (!view.isOnline()) {
            return;
        }

        this.context = context;

        AppLog.e(TAG, "SUBMIT ORDER DATA REQUEST DATA IN OVM= " + new Gson().toJson(submitOrderRequest));

        view.showLoading(getRs().getString(R.string.loadingSubmitOrder));

        RequestBody ticket_number = RequestBody.create(MediaType.parse("text/plain"), submitOrderRequest.getTicket_number());
        RequestBody comment = RequestBody.create(MediaType.parse("text/plain"), submitOrderRequest.getComment());
        RequestBody name_of_the_image = RequestBody.create(MediaType.parse("text/plain"), submitOrderRequest.getName_of_the_image());


        MultipartBody.Part[] img = new MultipartBody.Part[submitOrderRequest.getImage().size()];


        if (submitOrderRequest.getImage() != null && inputStreams != null && inputStreams.size() > 0) {

            for (int i = 0;i<submitOrderRequest.getImage().size();i++)
            {
              //  Log.e(TAG,"input stream "+inputStreams.get(i).toString());

                try {
                    File file = new File(getPath(submitOrderRequest.getImage().get(i)));
                    Log.e(TAG,"file front name"+file.getName());
                    byte[] recordData = readBytes(inputStreams.get(i));
                    Log.e(TAG,"recordData "+recordData.length);
                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), recordData);

                    Log.e(TAG,"fileBody "+fileBody.contentLength());

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



        /*
        if (submitOrderRequest.getImage() != null) {
            img = new MultipartBody.Part[submitOrderRequest.getImage().size()];
            for (int i = 0; i < submitOrderRequest.getImage().size(); i++) {
                File file = new File(submitOrderRequest.getImage().get(i));
                RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                img[i] = MultipartBody.Part.createFormData("image[" + i + "]", file.getName(), fileBody);
            }
        } else {
            img = null;
        }*/

        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(submitOrderRequest.getLat()));
        RequestBody lng = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(submitOrderRequest.getLng()));
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), submitOrderRequest.getAddress());

        apiService.submitOrder(ticket_number, comment, name_of_the_image, img, lat, lng, address).enqueue(new Callback<SubmitOrderResponse>() {
            @Override
            public void onResponse(Call<SubmitOrderResponse> call, Response<SubmitOrderResponse> response) {

                AppLog.e(TAG, "SUBMIT ORDER RESPONSE MSG = " + response.message());
                AppLog.e(TAG, "SUBMIT ORDER RESPONSE CODE = " + response.code());
                AppLog.e(TAG, "SUBMIT ORDER RESPONSE DATA = " + new Gson().toJson(response.body()));

                view.hideLoading();

                if (checkResponse(response)) {

                    SubmitOrderResponse submitOrderResponse = response.body();

                    if (submitOrderResponse != null) {

                        boolean status = submitOrderResponse.isStatus();

                        if (status) {

//                            view.showToast("" + submitOrderResponse.getMessage());
//                            view.OnSuccess();
                            view.orderPlaced(submitOrderResponse.getMessage());

                        } else {
                            view.showToast("" + submitOrderResponse.getMessage());
                        }
                    } else {
                        view.showToast("Please try again");
                    }
                } else {
                    view.showToast("Response is null");
                }
            }

            @Override
            public void onFailure(Call<SubmitOrderResponse> call, Throwable t) {

                view.hideLoading();
                view.onFailure(t.getMessage());

                AppLog.e(TAG, "UPDATE DATA FAIL = " + t.getMessage());
            }
        });
    }


    //Comment Not USED
    public void completeTask(CompleteTaskModel.CompleteTaskReq completeTaskReq) {

        view.showLoading("Loading...");

        AppLog.e(TAG, "completeTaskReq: " + new Gson().toJson(completeTaskReq));

        RequestBody taskId = RequestBody.create(MediaType.parse("text/plain"), completeTaskReq.getTask_id());
        RequestBody comment = RequestBody.create(MediaType.parse("text/plain"), completeTaskReq.getComments());
        RequestBody part_qty_used = RequestBody.create(MediaType.parse("text/plain"), new Gson().toJson(completeTaskReq.getTask_products_used()).toString());

        MultipartBody.Part[] img_pob = null;
        /*if (completeTaskReq.getPob() != null) {
            img_pob = new MultipartBody.Part[completeTaskReq.getPob().size()];
            for (int i = 0; i < completeTaskReq.getPob().size(); i++) {
                File file = new File(completeTaskReq.getPob().get(i));
                RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                img_pob[i] = MultipartBody.Part.createFormData("pob[" + i + "]", file.getName(), fileBody);
            }
        } else {
            img_pob = null;
        }
*/
        MultipartBody.Part[] img_poa = null;
     /*   if (completeTaskReq.getPoa() != null) {
            img_poa = new MultipartBody.Part[completeTaskReq.getPoa().size()];
            for (int i = 0; i < completeTaskReq.getPoa().size(); i++) {
                File file = new File(completeTaskReq.getPoa().get(i));
                RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                img_poa[i] = MultipartBody.Part.createFormData("poa[" + i + "]", file.getName(), fileBody);
            }
        } else {
            img_poa = null;
        }



        MultipartBody.Part signature_img = null;
        if (completeTaskReq.getSignature() != null) {
            File file = new File(completeTaskReq.getSignature());
            RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            signature_img = MultipartBody.Part.createFormData("signature", file.getName(), fileBody);
        } else {
            signature_img = null;
        }

*/
        MultipartBody.Part signature_img = null;

        apiService.addCompletedTaskPictures(taskId, img_pob, img_poa, signature_img, part_qty_used,comment)
                .enqueue(new Callback<CompleteTaskModel.CompleteTaskRes>() {
                    @Override
                    public void onResponse(Call<CompleteTaskModel.CompleteTaskRes> call, Response<CompleteTaskModel.CompleteTaskRes> response) {
                        AppLog.e(TAG, "CompleteTaskRes: " + response);
                        AppLog.e(TAG, "CompleteTaskRes.body(): " + new Gson().toJson(response.body()));
                        view.hideLoading();

                        if (response.body() != null) {
                            if (response.body().isStatus()) {
                                view.completedTaskRes(response.body().getMessage());
                            } else {
                                view.showToast(response.body().getMessage());
                            }
                        } else {
                            view.showToast("Please try again");
                        }
                    }

                    @Override
                    public void onFailure(Call<CompleteTaskModel.CompleteTaskRes> call, Throwable t) {
                        view.hideLoading();
                        view.onFailure(t.getMessage());
                    }
                });
    }

    public void getOrderDetails() {

        if (!view.isOnline()) {
            return;
        }

        view.showLoading(getRs().getString(R.string.loading));

        apiService.getOrderDetails().enqueue(new Callback<OrderDetailsResponse>() {
            @Override
            public void onResponse(Call<OrderDetailsResponse> call, Response<OrderDetailsResponse> response) {

                AppLog.e(TAG, "ORDER DETAILS RESPONSE = " + response.code());
                AppLog.e(TAG, "ORDER DETAILS RESPONSE = " + response.message());
                AppLog.e(TAG, "ORDER DETAILS RESPONSE DATA = " + new Gson().toJson(response.body()));

                view.hideLoading();

                if (checkResponse(response)) {

                    OrderDetailsResponse orderDetailsResponse = response.body();

                    if (orderDetailsResponse != null) {

                        boolean status = orderDetailsResponse.isStatus();

                        if (status) {

                            orderDetailsLiveDataList.postValue(orderDetailsResponse.getData());

                        } else {

                            view.showToast("" + orderDetailsResponse.getMessage());
                        }
                    }


                } else {

                    view.showToast("Response is null");
                }
            }

            @Override
            public void onFailure(Call<OrderDetailsResponse> call, Throwable t) {

                view.hideLoading();
                view.onFailure(t.getMessage());
            }
        });
    }
    public void vehicleHealthlist()
    {
        if (!vehicalView.isOnline()) {
            Log.e(TAG,"no");
            return;
        }
        vehicalView.showLoading(getRs().getString(R.string.loading));

        apiService.vehicleHealthList().enqueue(new Callback<VehicalHealthModel.VehicleHealthRes>() {
            @Override
            public void onResponse(Call<VehicalHealthModel.VehicleHealthRes> call, Response<VehicalHealthModel.VehicleHealthRes> response) {
                AppLog.e(TAG, "VEHICAL DETAILS RESPONSE= " + response.code());
                AppLog.e(TAG, "VEHICAL DETAILS RESPONSE = " + response.message());
                AppLog.e(TAG, "VEHICAL DETAILS RESPONSE DATA = " + new Gson().toJson(response.body()));
                vehicalView.hideLoading();

                if (checkResponse(response))
                {
                    VehicalHealthModel.VehicleHealthRes healthRes = response.body();

                    if (healthRes != null)
                    {
                        boolean status = healthRes.isStatus();
                        if (status) {

                            vehicalView.vehicalInteriorList(healthRes.getVehicalHealthListData().getVehicalHealthInteriorData());
                            vehicalView.vehicalExteriorList(healthRes.getVehicalHealthListData().getVehicalHealthExteriorData());
                            //newTaskDataList.postValue(newTaskRes.getNewTaskData());
                        } else {
                            vehicalView.vehicalInteriorList(null);
                            vehicalView.vehicalExteriorList(null);
                            //newTaskDataList.postValue(null);
                        }
                    }
                    else
                    {
                        vehicalView.showToast("Response is null");
                    }
                }
            }

            @Override
            public void onFailure(Call<VehicalHealthModel.VehicleHealthRes> call, Throwable t) {
                AppLog.e(TAG,"onfailvehicleHealth"+t);
                vehicalView.hideLoading();
                vehicalView.onFailure(t.getMessage());
            }
        });
    }

    public void vehicalList() {

        if (!vehicalView.isOnline()) {
            Log.e(TAG,"no");
            return;
        }

        vehicalView.showLoading(getRs().getString(R.string.loading));

        apiService.vehicalList().enqueue(new Callback<VehicalListModel.VehicalRes>() {
            @Override
            public void onResponse(Call<VehicalListModel.VehicalRes> call, Response<VehicalListModel.VehicalRes> response) {

                AppLog.e(TAG, "VEHICAL DETAILS RESPONSE= " + response.code());
                AppLog.e(TAG, "VEHICAL DETAILS RESPONSE = " + response.message());
                AppLog.e(TAG, "VEHICAL DETAILS RESPONSE DATA = " + new Gson().toJson(response.body()));
                vehicalView.hideLoading();

                if (checkResponse(response)) {

                    VehicalListModel.VehicalRes vehicalRes = response.body();

                    if (vehicalRes != null) {

                        boolean status = vehicalRes.isStatus();
                        if (status) {
                            vehicalView.vehicalList(vehicalRes.getVehicalListData());
                            //newTaskDataList.postValue(newTaskRes.getNewTaskData());
                        } else {
                            vehicalView.vehicalList(null);
                            //newTaskDataList.postValue(null);
                        }
                    }
                } else {

                    vehicalView.showToast("Response is null");
                }
            }

            @Override
            public void onFailure(Call<VehicalListModel.VehicalRes> call, Throwable t) {
                AppLog.e(TAG,"onfail"+t);
                vehicalView.hideLoading();
                vehicalView.onFailure(t.getMessage());
            }
        });

    }

    public void newTaskList() {

        if (!view.isOnline()) {
            return;
        }

        view.showLoading(getRs().getString(R.string.loading));

        apiService.newTaskList().enqueue(new Callback<NewTaskModel.NewTaskRes>() {
            @Override
            public void onResponse(Call<NewTaskModel.NewTaskRes> call, Response<NewTaskModel.NewTaskRes> response) {

                AppLog.e(TAG, "ORDER DETAILS RESPONSE NEW TaSk= " + response.code());
                AppLog.e(TAG, "ORDER DETAILS RESPONSE = " + response.message());
                AppLog.e(TAG, "ORDER DETAILS RESPONSE DATA = " + new Gson().toJson(response.body()));

                view.hideLoading();

                if (checkResponse(response)) {

                    NewTaskModel.NewTaskRes newTaskRes = response.body();

                    if (newTaskRes != null) {

                        boolean status = newTaskRes.isStatus();
                        String duty = newTaskRes.getDuty();
                        view.Duty(duty);
                        AppLog.e(TAG,"duty"+duty);

                        if (status) {
                            //newTaskDataList.postValue(newTaskRes.getNewTaskData());
                            view.newTaskList(newTaskRes.getNewTaskData());
                        } else {
                            //newTaskDataList.postValue(null);
                            view.newTaskList(null);
                        }
                    }


                } else {
                    view.showToast("Response is null");
                }
            }

            @Override
            public void onFailure(Call<NewTaskModel.NewTaskRes> call, Throwable t) {

                view.hideLoading();
                view.onFailure(t.getMessage());
            }
        });
    }

    public void myTaskList() {

        if (!view.isOnline()) {
            return;
        }

        view.showLoading(getRs().getString(R.string.loading));

        apiService.myTaskList().enqueue(new Callback<MyTaskModel.MyTaskRes>() {
            @Override
            public void onResponse(Call<MyTaskModel.MyTaskRes> call, Response<MyTaskModel.MyTaskRes> response) {

                AppLog.e(TAG, "ORDER DETAILS RESPONSE mytask= " + response);
                AppLog.e(TAG, "ORDER DETAILS RESPONSE = " + response.message());
                AppLog.e(TAG, "ORDER DETAILS RESPONSE DATA = " + new Gson().toJson(response.body()));

                view.hideLoading();

                if (checkResponse(response)) {

                    MyTaskModel.MyTaskRes myTaskRes = response.body();

                    if (myTaskRes != null) {

                        boolean status = myTaskRes.isStatus();

                        if (status) {
                            Log.e(TAG,"call status");
                            view.myTaskList(myTaskRes.getMyTaskData());
                        } else {
                            Log.e(TAG,"call null status");
                            view.myTaskList(null);
                        }
                    }
                } else {
                    view.showToast("Response is null");
                }
            }

            @Override
            public void onFailure(Call<MyTaskModel.MyTaskRes> call, Throwable t) {

                view.hideLoading();
                view.onFailure(t.getMessage());
            }
        });
    }

    public void partData(int id)
    {
        if (!view.isOnline())
        {
            return;
        }
        AppLog.e(TAG, "id of task: " + id);

        view.showLoading("Loading...");

        apiService.getPartData(id)
                .enqueue(new Callback<PartModel>() {
                    @Override
                    public void onResponse(Call<PartModel> call, Response<PartModel> response) {
                        AppLog.e(TAG, "partData: "+response);
                        AppLog.e(TAG, "partData.body(): "+new Gson().toJson(response.body()));
                        view.hideLoading();
                        view.addPart(id,response.body().getData().getProductData());
                    }
                    @Override
                    public void onFailure(Call<PartModel> call, Throwable t) {
                        view.hideLoading();
                    }
                });
    }

    public void addFailReason(AddFailedReasonModel.AddFailedReq addFailedReq,InputStream getInputStreamByUri,Context context)
    {
        if (!view.isOnline())
        {
            return;
        }
        this.context = context;
        AppLog.e(TAG, "addFailReasonReq: " + new Gson().toJson(addFailedReq));

        view.showLoading(getRs().getString(R.string.loading));

        RequestBody task_id = RequestBody.create(MediaType.parse("text/plain"),addFailedReq.getFail_task_id());

        MultipartBody.Part fail_image;
        try {
            File file = new File(getPath(addFailedReq.getFail_image()));
            byte[] recordData = readBytes(getInputStreamByUri);
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"),recordData);
            fail_image  = MultipartBody.Part.createFormData("failed_image",file.getName(),fileBody);

        }catch (Exception e)
        {
            fail_image = null;
        }

        RequestBody failed_reason = RequestBody.create(MediaType.parse("text/plain"),addFailedReq.getFail_reason());

        RequestBody file_name = RequestBody.create(MediaType.parse("text/plain"),addFailedReq.getFail_image_name());

        apiService.submitFailedRes(task_id,fail_image,failed_reason,file_name).enqueue(new Callback<AddFailedReasonModel.AddFailRes>() {
            @Override
            public void onResponse(Call<AddFailedReasonModel.AddFailRes> call, Response<AddFailedReasonModel.AddFailRes> response) {
                AppLog.e(TAG, "AddFAiled: "+response);
                AppLog.e(TAG, "AddFaileddata.body(): "+new Gson().toJson(response.body()));
                view.hideLoading();
                if (checkResponse(response)){

                    AddFailedReasonModel.AddFailRes addFailRes = response.body();

                    if (addFailRes != null) {

                        boolean status = addFailRes.isStatus();
                        if (status) {
                            view.showToast("upload success");
                            view.onCancelOrder("Failed Task");
                        } else {

                            if (response.body() != null){
                                view.showToast(response.body().getMessage());
                            }
                        }
                    }
                }else {
                    view.hideLoading();
                    if (response.body() != null){
                        view.showToast(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<AddFailedReasonModel.AddFailRes> call, Throwable t) {
                view.hideLoading();
                view.onFailure(t.getMessage());
            }
        });


    }
    public void addVehicleHealthData(int i,ArrayList<VehicleSubmitDetailsInteriorModel> interiorDataList, ArrayList<VehicleSubmitDetailsInteriorModel> exteriorDataList, Uri frontImg, Uri backImg, Uri leftImg, Uri rightImg, Uri signatureImage,Context context,InputStream front,InputStream back,InputStream left,InputStream right,InputStream sign) {
        if (!vehicalView.isOnline())
        {
            return;
        }
        this.context = context;

        Log.e(TAG,"Request Array ext"+new Gson().toJson(exteriorDataList).toString());
        Log.e(TAG,"Request Array int"+new Gson().toJson(interiorDataList).toString());
        exteriorDataList.addAll(interiorDataList);

        vehicalView.showLoading(getRs().getString(R.string.loading));

        RequestBody vehicle_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(i));

        RequestBody vehicle_health_details = RequestBody.create(MediaType.parse("text/plain"), new Gson().toJson(exteriorDataList).toString());

        Log.e(TAG,"Request Array"+new Gson().toJson(exteriorDataList).toString());

        MultipartBody.Part frontimage = null;
        MultipartBody.Part backimage = null;
        MultipartBody.Part leftimage = null;
        try {
            File filefront = new File(getPath(frontImg));
            byte[] data = readBytes(front);
            RequestBody fileBodyfront = RequestBody.create(MediaType.parse("image/*"), data);
            frontimage = MultipartBody.Part.createFormData("front_side_photo", filefront.getName(), fileBodyfront);

        }catch (IOException e)
        {

        }

        try {

            File filebackfront = new File(getPath(backImg));
            byte[] data1 = readBytes(back);
            RequestBody fileBodyBackfront = RequestBody.create(MediaType.parse("image/*"), data1);
            backimage = MultipartBody.Part.createFormData("back_side_photo", filebackfront.getName(), fileBodyBackfront);
        }catch (IOException e)
        {

        }
        try {

            File fileleftfront = new File(getPath(leftImg));
            byte[] data2 = readBytes(left);
            RequestBody fileBodyLeftfront = RequestBody.create(MediaType.parse("image/*"), data2);
            leftimage = MultipartBody.Part.createFormData("left_side_photo", fileleftfront.getName(), fileBodyLeftfront);
        }catch (IOException e)
        {

        }

        MultipartBody.Part rightimage = null;
        try {

            File fileleftfront = new File(getPath(rightImg));
            byte[] data2 = readBytes(right);
            RequestBody fileBodyRightfront = RequestBody.create(MediaType.parse("image/*"), data2);
            rightimage = MultipartBody.Part.createFormData("right_side_photo", fileleftfront.getName(), fileBodyRightfront);
        }catch (IOException e)
        {

        }

        MultipartBody.Part signatureimage = null;
        try {

            File filesignfront = new File(getPath(signatureImage));
            byte[] data = readBytes(sign);
            RequestBody fileBodysignfront = RequestBody.create(MediaType.parse("image/*"), data);
            signatureimage = MultipartBody.Part.createFormData("signature", filesignfront.getName(), fileBodysignfront);
        }catch (IOException e)
        {

        }

        apiService.submitVehicleHealth(vehicle_id,vehicle_health_details,leftimage,rightimage,frontimage,backimage,signatureimage).enqueue(new Callback<SubmitOrderResponse>() {
            @Override
            public void onResponse(Call<SubmitOrderResponse> call, Response<SubmitOrderResponse> response) {
                AppLog.e(TAG, "submitHealthResponse: "+response);
                AppLog.e(TAG, "submitHealthResponse.body(): "+new Gson().toJson(response.body()));

                vehicalView.hideLoading();
                if (checkResponse(response)){

                    SubmitOrderResponse submitOrderResponse = response.body();


                    if (submitOrderResponse != null) {

                        boolean status = submitOrderResponse.isStatus();
                        if (status) {
                            vehicalView.showToast("upload success");
                            vehicalView.OnSuccess();
                        } else {

                            if (response.body() != null){
                                vehicalView.showToast(response.body().getMessage());
                            }
                        }
                    }
                }else {
                    vehicalView.hideLoading();
                    if (response.body() != null){
                        vehicalView.showToast(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<SubmitOrderResponse> call, Throwable t) {
                vehicalView.showToast(t.getMessage());
            }
        });


    }

    public void addRefueling(AddRefuelingModel.AddRefuelingReq addRefuelingReq,InputStream reciptImage,InputStream dashboard,Context context){

        if (!view.isOnline()) {
            return;
        }

        this.context = context;
        view.showLoading(getRs().getString(R.string.loading));

        RequestBody vehicleId = RequestBody.create(MediaType.parse("text/plain"), addRefuelingReq.getFk_vehicle_id());
        RequestBody meterReading = RequestBody.create(MediaType.parse("text/plain"), addRefuelingReq.getMeter_reading());
        RequestBody liter = RequestBody.create(MediaType.parse("text/plain"), addRefuelingReq.getLiter());
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"), addRefuelingReq.getPrice());
        RequestBody recpNo = RequestBody.create(MediaType.parse("text/plain"), addRefuelingReq.getReceipt_number());

        MultipartBody.Part receiptImg = null;
        MultipartBody.Part dashboardImg = null;
        try {
            File filefront = new File(getPath(addRefuelingReq.getReceipt_image()));
            byte[] data = readBytes(reciptImage);
            RequestBody fileBodyfront = RequestBody.create(MediaType.parse("image/*"), data);
            receiptImg = MultipartBody.Part.createFormData("receipt_image", filefront.getName(), fileBodyfront);


            File fileback = new File(getPath(addRefuelingReq.getDashboard_image()));
            byte[] data1 = readBytes(dashboard);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), data1);
            dashboardImg = MultipartBody.Part.createFormData("mr_image", fileback.getName(), requestBody);

        }catch (Exception e)
        {

        }




        apiService.submitVehiclesRef(vehicleId,meterReading,recpNo,liter,price,receiptImg,dashboardImg)
                .enqueue(new Callback<AddRefuelingModel.AddRefuelingRes>() {
                    @Override
                    public void onResponse(Call<AddRefuelingModel.AddRefuelingRes> call, Response<AddRefuelingModel.AddRefuelingRes> response) {
                        AppLog.e(TAG, "AddRefuelingResOrderView: "+response);
                        AppLog.e(TAG, "AddRefuelingRes.body(): "+new Gson().toJson(response.body()));

                        view.hideLoading();
                        if (checkResponse(response)){

                            AddRefuelingModel.AddRefuelingRes addRefuelingRes = response.body();

                            if (addRefuelingRes != null) {

                                boolean status = addRefuelingRes.isStatus();

                                if (status) {
                                    view.addRefuelingRes(addRefuelingRes.getMessage());
                                } else {
                                    if (response.body() != null){
                                        view.showToast(response.body().getMessage());
                                    }
                                }
                            }
                        }else {
                            view.hideLoading();
                            if (response.body() != null){
                                view.showToast(response.body().getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AddRefuelingModel.AddRefuelingRes> call, Throwable t) {
                        view.hideLoading();
                        view.onFailure(t.getMessage());
                    }
                });


    }

    public void userDetailsSubmit(SubmitUserRequest submitUserRequest) {
        if (!view.isOnline()) {
            return;
        }
        AppLog.e(TAG, "SUBMIT User DATA REQUEST DATA IN OVM= " + new Gson().toJson(submitUserRequest));

        view.showLoading(getRs().getString(R.string.loadingSubmitUserDetails));

        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), submitUserRequest.getId() + "");
        RequestBody sign = RequestBody.create(MediaType.parse("text/plain"), submitUserRequest.getSignature());
        File filefront = new File(ImageUtils.getPath(context, submitUserRequest.getImage()));
        RequestBody fileBodyfront = RequestBody.create(MediaType.parse("multipart/form-data"), filefront);
        MultipartBody.Part profileimg = MultipartBody.Part.createFormData("pod", filefront.getName(), fileBodyfront);


        apiService.submitDetails(id, sign, profileimg).enqueue(new Callback<uploadImageResponse>() {
            @Override
            public void onResponse(Call<uploadImageResponse> call, Response<uploadImageResponse> response) {
                AppLog.e(TAG, "SUBMIT DETAILS RESPONSE MSG = " + response.message());
                AppLog.e(TAG, "SUBMIT DETAILS RESPONSE CODE = " + response.code());
                AppLog.e(TAG, "SUBMIT DETAILS RESPONSE DATA = " + new Gson().toJson(response.body()));

                view.hideLoading();

                if (checkResponse(response)) {

                    uploadImageResponse uploadImageResponse = response.body();

                    if (uploadImageResponse != null) {

                        boolean status = uploadImageResponse.isStatus();


                        if (status) {

                            view.showToast("" + uploadImageResponse.getMessage());

//                            view.OnSuccess();
                        } else {
                            view.showToast("" + uploadImageResponse.getMessage());

                        }
                    } else {
                        view.showToast("Please try again");
                    }
                } else {
                    view.showToast("Response is null");
                }
            }

            @Override
            public void onFailure(Call<uploadImageResponse> call, Throwable t) {
                view.hideLoading();
                view.onFailure(t.getMessage());
                AppLog.e(TAG, "UPDATE DATA FAIL = " + t.getMessage());
            }
        });
    }

    public void changeVehicleStatus(VehicleStatusChangeModel.VehicleStatusChangeReq vehicleStatusChangeReq)
    {
        if (!vehicalView.isOnline())
        {
            return;
        }
        AppLog.e(TAG,"changeVehicleStatusReq"+new Gson().toJson(vehicleStatusChangeReq));

        vehicalView.showLoading(getRs().getString(R.string.loading));

        apiService.changeVehicleStatus(vehicleStatusChangeReq).enqueue(new Callback<VehicleStatusChangeModel.VehicleStatusChangeRes>() {
            @Override
            public void onResponse(Call<VehicleStatusChangeModel.VehicleStatusChangeRes> call, Response<VehicleStatusChangeModel.VehicleStatusChangeRes> response) {
                AppLog.e(TAG, "CHANGE TASK STATUS RESPONSE = " + response.code());
                AppLog.e(TAG, "CHANGE TASK STATUS RESPONSE = " + response.message());
                AppLog.e(TAG, "CHANGE TASK STATUS RESPONSE DATA = " + new Gson().toJson(response.body()));

                vehicalView.hideLoading();

                if (checkResponse(response)) {

                    VehicleStatusChangeModel.VehicleStatusChangeRes changeRes = response.body();


                    if (changeRes != null) {
                        boolean status = changeRes.isStatus();

                        if (status) {
                            vehicalView.showToast("" + changeRes.getMessage());
                            AppLog.e(TAG,"vehicleStatus"+changeRes.getMessage());
                        } else {
                            vehicalView.showToast("" + changeRes.getMessage());
                        }
                    }
                } else {

                    vehicalView.showToast("Response is null");
                }
            }

            @Override
            public void onFailure(Call<VehicleStatusChangeModel.VehicleStatusChangeRes> call, Throwable t) {

            }
        });
    }



    public void changeTaskStatus(TaskStatusChangeModel.TaskStatusChangeReq taskStatusChangeReq) {

        if (!view.isOnline()) {
            return;
        }

        AppLog.e(TAG, "taskStatusChangeReq: " + new Gson().toJson(taskStatusChangeReq));

        view.showLoading(getRs().getString(R.string.loading));

        apiService.changeTaskStatus(taskStatusChangeReq).enqueue(new Callback<TaskStatusChangeModel.TaskStatusChangeRes>() {
            @Override
            public void onResponse(Call<TaskStatusChangeModel.TaskStatusChangeRes> call, Response<TaskStatusChangeModel.TaskStatusChangeRes> response) {

                AppLog.e(TAG, "CHANGE TASK STATUS RESPONSE = " + response);
                AppLog.e(TAG, "CHANGE TASK STATUS RESPONSE = " + response.message());
                AppLog.e(TAG, "CHANGE TASK STATUS RESPONSE DATA = " + new Gson().toJson(response.body()));

                view.hideLoading();

                if (checkResponse(response)) {

                    TaskStatusChangeModel.TaskStatusChangeRes myTaskRes = response.body();

                    if (myTaskRes != null) {
                        boolean status = myTaskRes.isStatus();

                        if (status) {
                            view.onTaskStatusChange(response.body().getMessage(),taskStatusChangeReq.getTask_id());
                        } else {
                            AppLog.e(TAG,"statusupdate"+myTaskRes.getMessage());
                            view.showToast("statuschange" + myTaskRes.getMessage());
                        }
                    }
                } else {

                    view.showToast("Response is null");
                }
            }

            @Override
            public void onFailure(Call<TaskStatusChangeModel.TaskStatusChangeRes> call, Throwable t) {
                view.hideLoading();
                view.onFailure(t.getMessage());
            }
        });
    }


}
