package com.onthemove.responseClasses;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

public class AddFailedReasonModel {

    public static class AddFailedReq
    {
        @SerializedName("task_id")
        private String fail_task_id;

        @SerializedName("image")
        private Uri fail_image;

        @SerializedName("failed_reason")
        private String fail_reason;

        @SerializedName("file_name")
        private String fail_image_name;

        private String fail_sync;

        public String getFail_sync() {
            return fail_sync;
        }

        public void setFail_sync(String fail_sync) {
            this.fail_sync = fail_sync;
        }

        public String getFail_task_id() {
            return fail_task_id;
        }

        public void setFail_task_id(String fail_task_id) {
            this.fail_task_id = fail_task_id;
        }

        public Uri getFail_image() {
            return fail_image;
        }

        public void setFail_image(Uri fail_image) {
            this.fail_image = fail_image;
        }

        public String getFail_reason() {
            return fail_reason;
        }

        public void setFail_reason(String fail_reason) {
            this.fail_reason = fail_reason;
        }

        public String getFail_image_name() {
            return fail_image_name;
        }

        public void setFail_image_name(String fail_image_name) {
            this.fail_image_name = fail_image_name;
        }
    }
    public static class AddFailRes{

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
}
