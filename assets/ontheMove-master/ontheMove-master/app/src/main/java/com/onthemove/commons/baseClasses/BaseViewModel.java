package com.onthemove.commons.baseClasses;

import android.content.res.Resources;

import androidx.lifecycle.ViewModel;


import com.onthemove.commons.utils.AppPref;
import com.onthemove.network.ApiService;
import com.onthemove.network.RetroClient;

import retrofit2.Response;

public abstract class BaseViewModel extends ViewModel {

    protected ApiService apiService;
    protected AppPref appPref;

    public BaseViewModel() {
        apiService = RetroClient.getApiService();
        appPref = AppPref.getInstance(MyApp.getMyApp());
    }

    public Resources getRs(){
        return MyApp.getMyApp().getResources();
    }


    protected boolean checkResponse(Response response) {
        if(response.code()==401){
            getView().unAuthorized();
            return false;
        }else if(response==null){
            getView().responseNull();
            return false;
        }
//        else if(!response.isStatus()){
//            getView().showToast(baseRes.getMessage());
//            return false;
//        }
        return true;
    }

    public abstract BaseView getView();
}
