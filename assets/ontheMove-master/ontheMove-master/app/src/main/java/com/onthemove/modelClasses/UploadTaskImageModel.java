package com.onthemove.modelClasses;

import android.net.Uri;

public class UploadTaskImageModel {

    private Uri imgPath;
    private String imgName;

    public Uri getImgPath() {
        return imgPath;
    }

    public void setImgPath(Uri imgPath) {
        this.imgPath = imgPath;
    }

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }
}
