package com.onthemove.requestClasses;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {


    /**
     * username : 8160301515
     * password : 123456
     * device_type : android
     * device_token : test
     */

    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("device_type")
    private String deviceType;
    @SerializedName("device_token")
    private String deviceToken;
    @SerializedName("device_model")
    private String device_model;
    @SerializedName("device_version")
    private String device_version;
    @SerializedName("app_version")
    private String app_version;

    public String getDevice_model() {
        return device_model;
    }

    public void setDevice_model(String device_model) {
        this.device_model = device_model;
    }

    public String getDevice_version() {
        return device_version;
    }

    public void setDevice_version(String device_version) {
        this.device_version = device_version;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
