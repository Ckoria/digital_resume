package com.onthemove.responseClasses;

import com.google.gson.annotations.SerializedName;

public class LogoutResponse {


    /**
     * message : You are logged out
     */

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
