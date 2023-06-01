package com.onthemove.responseClasses;

import com.google.gson.annotations.SerializedName;

public class UpdateLocationResponse {


    /**
     * status : true
     * message : Current location update successfully
     */

    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
