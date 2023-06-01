package com.onthemove.requestClasses;

import android.net.Uri;

public class SubmitUserRequest {


    private Uri image;
    private int id;
    private String signature;

    public SubmitUserRequest() {

        this.image = image;
        this.id = id;
        this.signature = signature;
    }


    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
