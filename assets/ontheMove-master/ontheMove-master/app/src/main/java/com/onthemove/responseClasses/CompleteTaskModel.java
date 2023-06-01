package com.onthemove.responseClasses;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.onthemove.modelClasses.PartModel;

import java.util.ArrayList;

public class CompleteTaskModel {

    public static class CompleteTaskReq {

        @SerializedName("task_id")
        private String task_id;
        @SerializedName("pob")
        private ArrayList<Uri> pob;
        @SerializedName("poa")
        private ArrayList<Uri> poa;
        @SerializedName("signature")
        private Uri signature;

        @SerializedName("task_products_used")
        @Expose
        private ArrayList<PartModel.TaskProductDataUsed> task_products_used;

        @SerializedName("comments")
        private String comments;

        public ArrayList<PartModel.TaskProductDataUsed> getTask_products_used() {
            return task_products_used;
        }

        public void setTask_products_used(ArrayList<PartModel.TaskProductDataUsed> task_products_used) {
            this.task_products_used = task_products_used;
        }

        public String getTask_id() {
            return task_id;
        }

        public void setTask_id(String task_id) {
            this.task_id = task_id;
        }

        public ArrayList<Uri> getPob() {
            return pob;
        }

        public void setPob(ArrayList<Uri> pob) {
            this.pob = pob;
        }

        public ArrayList<Uri> getPoa() {
            return poa;
        }

        public void setPoa(ArrayList<Uri> poa) {
            this.poa = poa;
        }

        public Uri getSignature() {
            return signature;
        }

        public void setSignature(Uri signature) {
            this.signature = signature;
        }

        public String getComments() {
            return comments;
        }

        public void setComments(String comments) {
            this.comments = comments;
        }
    }

    public static class CompleteTaskRes {

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
