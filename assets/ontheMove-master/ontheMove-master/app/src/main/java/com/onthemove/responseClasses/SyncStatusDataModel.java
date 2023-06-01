package com.onthemove.responseClasses;

import com.google.gson.annotations.SerializedName;
import com.onthemove.modelClasses.SubmitTaskStatusModel;

import java.util.ArrayList;

public class SyncStatusDataModel {

    public static class SyncStatusDataReq {

        @SerializedName("status_data")
        private ArrayList<SubmitTaskStatusModel> syncStatusData;

        public ArrayList<SubmitTaskStatusModel> getSyncStatusData() {
            return syncStatusData;
        }

        public void setSyncStatusData(ArrayList<SubmitTaskStatusModel> syncStatusData) {
            this.syncStatusData = syncStatusData;
        }
    }

    public static class SyncStatusDataRes {

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
