package com.onthemove.viewModel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.onthemove.R;
import com.onthemove.commons.utils.AppPref;
import com.onthemove.interfaces.AuthContract;
import com.onthemove.commons.baseClasses.BaseView;
import com.onthemove.commons.baseClasses.BaseViewModel;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.commons.utils.Constants;
import com.onthemove.modelClasses.OnDutyModel;
import com.onthemove.requestClasses.LoginRequest;
import com.onthemove.responseClasses.LoginResponse;
import com.onthemove.responseClasses.LogoutResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends BaseViewModel implements AuthContract.AuthPresenter {

    private static final String TAG = "AuthViewModel";
    Context context;
    AuthContract.AuthView view;
    LoginRequest loginRequest;

//    public MutableLiveData<Boolean> loginStatus = new MutableLiveData<>();
    public MutableLiveData<LoginResponse.DataBean> loginLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> logoutStatus = new MutableLiveData<>();

    public AuthViewModel() {
        super();
    }

    @Override
    public BaseView getView() {
        return view;
    }

    public void setAuthView(AuthContract.AuthView authView) {
        this.view = authView;
    }


    //Get Login Model
    public LoginRequest getLoginRequest() {
        return loginRequest;
    }


    //Set Login Model
    public void setLoginRequest(LoginRequest loginRequest) {
        this.loginRequest = loginRequest;
    }



    @Override
    public void registerUser() {

    }

    @Override
    public void loginUser() {

        if (!view.isOnline()) {
            return;
        }
        AppLog.e(TAG, "LOGIN REQEST DATA IN AVM = " + new Gson().toJson(loginRequest));
        view.showLoading(getRs().getString(R.string.loading));

        apiService.getLogin(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                AppLog.e(TAG, "LOGIN RESPONSE = " + response.message());
                AppLog.e(TAG, "LOGIN RESPONSE DATA = " + new Gson().toJson(response.body()));

                view.hideLoading();

                if (checkResponse(response)) {

                    LoginResponse loginResponse = response.body();
                    boolean status = loginResponse.isStatus();

                    if (status) {

                        appPref.set(Constants.IS_SIGN_UP, true);
                        appPref.set(Constants.IS_LOGIN, true);
                        saveLoginSession(loginResponse.getData());

//                        loginStatus.postValue(true);
                        loginLiveData.postValue(loginResponse.getData());

                        view.showToast("" + loginResponse.getMessage());
                    } else {
                        view.showToast("" + loginResponse.getMessage());
                    }
                } else {
                    view.showToast("Response is null");
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                view.hideLoading();
                view.onFailure(t.getMessage());
            }
        });
    }


    //Save Login Sesion
    private void saveLoginSession(LoginResponse.DataBean data) {

        appPref.set(Constants.USER_TOKEN, data.getToken());
        appPref.set(Constants.USER_ID, data.getAgentId());
        appPref.set(Constants.USER_NAME, data.getAgentName());
        appPref.set(Constants.MOBILE_NO, data.getMobileNumber());
        appPref.set(Constants.EMAIL, data.getEmail());
        appPref.set(Constants.ROLE, data.getRole());
        appPref.setLoginSession(data);
    }

    public void onDuty(){

        apiService.onDuty()
                .enqueue(new Callback<OnDutyModel.OnDutyRes>() {
                    @Override
                    public void onResponse(Call<OnDutyModel.OnDutyRes> call, Response<OnDutyModel.OnDutyRes> response) {
                        AppLog.e(TAG, "OnDutyRes: "+new Gson().toJson(response));
                        AppLog.e(TAG, "OnDutyRes.body(): "+new Gson().toJson(response.body()));

                        if (checkResponse(response)){

                            OnDutyModel.OnDutyRes res = response.body();

                            if (res != null){
                                if (res.isStatus()){
                                    view.OnDutyRes(res.getOnDutyData());
                                }else {
                                    view.showToast(response.body().getMessage());
                                }
                            }else {
                                view.showToast("Please try again");
                            }
                        }else {
                            if (response.body() != null){
                                view.showToast(response.body().getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OnDutyModel.OnDutyRes> call, Throwable t) {
                        view.hideLoading();
                        view.onFailure(t.getMessage());
                    }
                });
    }

    @Override
    public void sentOTP(Context ex) {

    }

    @Override
    public void verifyOTP(String otp) {

    }

    public void getLogout() {

        if (!view.isOnline()) {
            return;
        }

        view.showLoading(getRs().getString(R.string.loading));

        apiService.getLogout().enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {

                AppLog.e(TAG, "LOGOUT RESPONSE = " + response.message());
                AppLog.e(TAG, "LOGOUT RESPONSE DATA = " + new Gson().toJson(response.body()));

                view.hideLoading();

                if (checkResponse(response)) {

                    LogoutResponse logoutResponse = response.body();

                    logoutStatus.postValue(true);
                    view.showToast(logoutResponse.getMessage());

                } else {
                    view.showToast("Response is null");
                }

            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {

                view.hideLoading();
                view.onFailure(t.getMessage());
            }
        });
    }

}
