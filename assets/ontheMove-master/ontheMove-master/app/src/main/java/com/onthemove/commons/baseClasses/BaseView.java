package com.onthemove.commons.baseClasses;

public interface BaseView {

    boolean isOnline();
    void showLoading(String msg);
    void hideLoading();
    void showToast(String msg);
    void unAuthorized();
    void responseNull();
    void onFailure(String message);
    void OnSuccess();
}
