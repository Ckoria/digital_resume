package com.onthemove.responseClasses;

import com.google.gson.annotations.SerializedName;

public class SubmitOrderResponse {

    /**
     * status : true
     * message : Details add successfully
     * result : {"message":"Processing - refresh ticket in a moment to see if it worked."}
     */

    @SerializedName("status")
    private boolean status;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private String result;

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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
