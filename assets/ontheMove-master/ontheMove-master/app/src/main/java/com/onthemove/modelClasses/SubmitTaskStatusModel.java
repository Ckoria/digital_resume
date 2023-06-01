package com.onthemove.modelClasses;

import com.google.gson.annotations.SerializedName;

public class SubmitTaskStatusModel {

    @SerializedName("task_id")
    String id;
    @SerializedName("date_time")
    String date;
    @SerializedName("status")
    String status;

    String sync = "false";

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
