package com.onthemove.interfaces;

import android.content.Context;

import com.onthemove.commons.baseClasses.BaseView;
import com.onthemove.modelClasses.OnDutyModel;

public interface AuthContract {

    interface AuthView extends BaseView {
        public void otpSent();
        public void otpVerified();
        void submitReport(String message);
//        void profileRes(LoginRes body);
        void approvedAccount(String account_status);

        void OnDutyRes(OnDutyModel.OnDutyData onDutyData);
    }
     interface AuthPresenter {
         void sentOTP(Context ex);
         void verifyOTP(String otp);
         void registerUser();
         void loginUser();
    }
}
