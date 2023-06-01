package com.onthemove.responseClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TaskStatusChangeModel {

    public static class TaskStatusChangeReq {

        @SerializedName("task_id")
        @Expose
        private String task_id;

        @SerializedName("status")
        @Expose
        private String status;

        public String getTask_id() {
            return task_id;
        }

        public void setTask_id(String task_id) {
            this.task_id = task_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class TaskStatusChangeRes {

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("status")
        @Expose
        private boolean status;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }
    }
}
