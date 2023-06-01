package com.onthemove.responseClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class MyTaskModel {

    public static class MyTaskRes {

        @SerializedName("status")
        @Expose
        private boolean status;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("data")
        @Expose
        private ArrayList<NewTaskModel.NewTaskData> myTaskData;

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

        public ArrayList<NewTaskModel.NewTaskData> getMyTaskData() {
            return myTaskData;
        }

        public void setMyTaskData(ArrayList<NewTaskModel.NewTaskData> myTaskData) {
            this.myTaskData = myTaskData;
        }
    }
}
