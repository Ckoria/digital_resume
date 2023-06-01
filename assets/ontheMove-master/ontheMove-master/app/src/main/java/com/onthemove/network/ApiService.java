package com.onthemove.network;


import com.onthemove.modelClasses.OnDutyModel;
import com.onthemove.modelClasses.PartModel;
import com.onthemove.requestClasses.LoginRequest;
import com.onthemove.requestClasses.UpdateLocationRequest;
import com.onthemove.responseClasses.AddFailedReasonModel;
import com.onthemove.responseClasses.AddRefuelingModel;
import com.onthemove.responseClasses.CompleteTaskModel;
import com.onthemove.responseClasses.LoginResponse;
import com.onthemove.responseClasses.LogoutResponse;
import com.onthemove.responseClasses.MyTaskModel;
import com.onthemove.responseClasses.NewTaskModel;
import com.onthemove.responseClasses.OrderDetailsResponse;
import com.onthemove.responseClasses.SubmitOrderResponse;
import com.onthemove.responseClasses.SyncStatusDataModel;
import com.onthemove.responseClasses.TaskStatusChangeModel;
import com.onthemove.responseClasses.UpdateLocationResponse;
import com.onthemove.responseClasses.VehicalHealthModel;
import com.onthemove.responseClasses.VehicalListModel;
import com.onthemove.responseClasses.VehicleStatusChangeModel;
import com.onthemove.responseClasses.uploadImageResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("login")
    Call<LoginResponse> getLogin(@Body LoginRequest loginRequest);

    @Multipart
    @POST("submitOrderDetails")
    Call<SubmitOrderResponse> submitOrder(@Part("ticket_number") RequestBody ticket_number,
                                          @Part("comment") RequestBody comment,
                                          @Part("name_of_the_image") RequestBody name_of_the_image,
                                          @Part MultipartBody.Part[] image,
                                          @Part("lat") RequestBody lat,
                                          @Part("lng") RequestBody lng,
                                          @Part("address") RequestBody address);

    /*NOT USE*/
    @Multipart
    @POST("completeTaskUploadPictures")
    Call<uploadImageResponse> submitDetails(
            @Part("task_id") RequestBody id,
            @Part("signature") RequestBody signature,
            @Part MultipartBody.Part image
    );

    @GET("orderDetailsList")
    Call<OrderDetailsResponse> getOrderDetails();

    @GET("https://maps.googleapis.com/maps/api/geocode/json?")
    Call<ResponseBody> getGeoAddress(@Query("latlng") String latLng, @Query("key") String key);

    @GET("logout")
    Call<LogoutResponse> getLogout();

    @POST("updateCurrentLocation")
    Call<UpdateLocationResponse> updateCurrentLocation(@Body UpdateLocationRequest updateLocationRequest);

    @GET("newTaskList")
    Call<NewTaskModel.NewTaskRes> newTaskList();

    @GET("freeVehicles")
    Call<VehicalListModel.VehicalRes> vehicalList();

    @GET("myTaskList")
    Call<MyTaskModel.MyTaskRes> myTaskList();

    @GET("taskDetails/{taskId}")
    Call<PartModel> getPartData(@Path("taskId") int taskid);

    @POST("selectVehicle")
    Call<VehicleStatusChangeModel.VehicleStatusChangeRes> changeVehicleStatus(@Body VehicleStatusChangeModel.VehicleStatusChangeReq vehicleStatusChangeReq);

    @POST("changeTaskStatus")
    Call<TaskStatusChangeModel.TaskStatusChangeRes> changeTaskStatus(@Body TaskStatusChangeModel.TaskStatusChangeReq taskStatusChangeReq);

    @Multipart
    @POST("addCompletedTaskPictures")
    Call<CompleteTaskModel.CompleteTaskRes> addCompletedTaskPictures(@Part("task_id") RequestBody task_id,
                                                                     @Part MultipartBody.Part[] pob,
                                                                     @Part MultipartBody.Part[] poa,
                                                                     @Part MultipartBody.Part signature,
                                                                     @Part("task_products_used") RequestBody task_products_used,
                                                                     @Part("comments") RequestBody comments);

    @POST("syncTaskStatus")
    Call<SyncStatusDataModel.SyncStatusDataRes> syncTaskStatus(@Body SyncStatusDataModel.SyncStatusDataReq syncStatusDataReq);

    @Multipart
    @POST("submitVehiclesRefuelingDetails")
    Call<AddRefuelingModel.AddRefuelingRes> submitVehiclesRef(@Part("fk_vehicle_id") RequestBody fk_vehicle_id,
                                                              @Part("meter_reading") RequestBody meter_reading,
                                                              @Part("receipt_number") RequestBody receipt_number,
                                                              @Part("liter") RequestBody liter,
                                                              @Part("price") RequestBody price,
                                                              @Part MultipartBody.Part receipt_image,
                                                              @Part MultipartBody.Part mr_image);


    @GET("onOffDuty")
    Call<OnDutyModel.OnDutyRes> onDuty();

    @Multipart
    @POST("submitFailedTask")
    Call<AddFailedReasonModel.AddFailRes> submitFailedRes(@Part("task_id") RequestBody task_id,
                                                          @Part MultipartBody.Part failed_image,
                                                          @Part("failed_reason") RequestBody reason,
                                                          @Part("file_name") RequestBody image_name);

    @POST("vehicleHealthList")
    Call<VehicalHealthModel.VehicleHealthRes> vehicleHealthList();

    @Multipart
    @POST("vehicleHealthDetails")
    Call<SubmitOrderResponse> submitVehicleHealth(@Part("vehicle_id") RequestBody vehicle_id,
                                                  @Part("vehicle_health_details") RequestBody vehicle_health_details,
                                                  @Part MultipartBody.Part leftimage,
                                                  @Part MultipartBody.Part rightimage,
                                                  @Part MultipartBody.Part frontimage,
                                                  @Part MultipartBody.Part backimage,
                                                  @Part MultipartBody.Part signatureimage);
}
