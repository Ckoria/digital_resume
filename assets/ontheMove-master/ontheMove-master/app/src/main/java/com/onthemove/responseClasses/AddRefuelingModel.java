package com.onthemove.responseClasses;

import android.net.Uri;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddRefuelingModel {

    public static class AddRefuelingReq {

        @SerializedName("fk_vehicle_id")
        private String fk_vehicle_id;

        @SerializedName("meter_reading")
        private String meter_reading;

        @SerializedName("receipt_number")
        private String receipt_number;

        @SerializedName("liter")
        private String liter;

        @SerializedName("price")
        private String price;

        @SerializedName("receipt_image")
        private Uri receipt_image;

        @SerializedName("mr_image")
        private Uri dashboard_image;


        private String sync;

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSync() {
            return sync;
        }

        public void setSync(String sync) {
            this.sync = sync;
        }

        public String getFk_vehicle_id() {
            return fk_vehicle_id;
        }

        public void setFk_vehicle_id(String fk_vehicle_id) {
            this.fk_vehicle_id = fk_vehicle_id;
        }

        public String getMeter_reading() {
            return meter_reading;
        }

        public void setMeter_reading(String meter_reading) {
            this.meter_reading = meter_reading;
        }

        public String getReceipt_number() {
            return receipt_number;
        }

        public void setReceipt_number(String receipt_number) {
            this.receipt_number = receipt_number;
        }

        public String getLiter() {
            return liter;
        }

        public void setLiter(String liter) {
            this.liter = liter;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public Uri getReceipt_image() {
            return receipt_image;
        }

        public void setReceipt_image(Uri receipt_image) {
            this.receipt_image = receipt_image;
        }

        public Uri getDashboard_image() {
            return dashboard_image;
        }

        public void setDashboard_image(Uri dashboard_image) {
            this.dashboard_image = dashboard_image;
        }
    }

    public static class AddRefuelingRes {

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
