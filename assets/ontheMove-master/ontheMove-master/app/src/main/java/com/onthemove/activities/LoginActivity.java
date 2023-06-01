package com.onthemove.activities;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.onthemove.R;
import com.onthemove.commons.baseClasses.BaseActivity;
import com.onthemove.commons.utils.AppLog;
import com.onthemove.databinding.ActivityLoginBinding;
import com.onthemove.interfaces.AuthContract;
import com.onthemove.modelClasses.OnDutyModel;
import com.onthemove.requestClasses.LoginRequest;
import com.onthemove.responseClasses.LoginResponse;
import com.onthemove.viewModel.AuthViewModel;

public class LoginActivity extends BaseActivity implements View.OnClickListener, AuthContract.AuthView {

    private static final String TAG = "LoginActivity";

    private ActivityLoginBinding binding;
    private Context context;
    private AuthViewModel authViewModel;
    private String versionRelease;
    private String model;
    private int verCode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        context = LoginActivity.this;

        inits();
        listner();
    }

    private void inits() {

        model = Build.MODEL;
        versionRelease = Build.VERSION.RELEASE;

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            verCode = pInfo.versionCode;
            AppLog.e(TAG, "verCode: " + verCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        authViewModel.setAuthView(this);

        subscribe();
    }



    private void listner() {

        binding.etPhone.setOnClickListener(this);
        binding.etPassword.setOnClickListener(this);
        binding.btnLogin.setOnClickListener(this);
    }

    private void subscribe() {

//        authViewModel.loginStatus.observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean status) {
//
//                if (status) {
//
//                    appPref.set(Constants.IS_SIGN_UP, true);
//                    appPref.set(Constants.IS_LOGIN, true);
//
//                    gotoActivity(MainActivity.class, null, true);
//                    finish();
//                }
//
//            }
//        });

        authViewModel.loginLiveData.observe(this, new Observer<LoginResponse.DataBean>() {
            @Override
            public void onChanged(LoginResponse.DataBean loginData) {

                if (loginData.getRole().equalsIgnoreCase("serviceboy")){

                    gotoActivity(MainActivity.class,null,false);
                    finish();
                }
                else {

                    gotoActivity(MainActivity2.class,null,false);
                    finish();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.etPhone:
                binding.etPhone.setFocusableInTouchMode(true);
                binding.etPhone.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(binding.etPhone, InputMethodManager.SHOW_IMPLICIT);
                break;

            case R.id.etPassword:
                binding.etPassword.setFocusableInTouchMode(true);
                binding.etPassword.requestFocus();
                InputMethodManager imm2 = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.showSoftInput(binding.etPassword, InputMethodManager.SHOW_IMPLICIT);
                break;

            case R.id.btnLogin:
                if (verifyCode()) {

                    authViewModel.setLoginRequest(getLoginModel());
                    authViewModel.loginUser();
                }
                break;
        }
    }



    private boolean verifyCode() {

        return valid(binding.etPhone,R.string.errorPhoneUserName)
                &&valid(binding.etPassword,R.string.errorPassword);
    }

    private LoginRequest getLoginModel() {

        LoginRequest loginRequest = new LoginRequest();

        loginRequest.setUsername(binding.etPhone.getText().toString());
        loginRequest.setPassword(binding.etPassword.getText().toString());
        loginRequest.setDeviceType("android");
        loginRequest.setDeviceToken("test");
        loginRequest.setApp_version(String.valueOf(verCode));
        loginRequest.setDevice_model(model);
        loginRequest.setDevice_version(versionRelease);

        AppLog.e(TAG,"LOGIN REQUEST DATA = " + new Gson().toJson(loginRequest));

        return loginRequest;
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
    public void OnSuccess() {

    }
}
