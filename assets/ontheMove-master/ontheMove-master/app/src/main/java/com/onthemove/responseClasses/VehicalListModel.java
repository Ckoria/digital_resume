package com.onthemove.responseClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class VehicalListModel {

    public static class VehicalRes {

        @SerializedName("status")
        @Expose
        private boolean status;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("data")
        @Expose
        private ArrayList<VehicalListData> vehicalListData;

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

        public ArrayList<VehicalListData> getVehicalListData() {
            return vehicalListData;
        }

        public void setVehicalListData(ArrayList<VehicalListData> vehicalListData) {
            this.vehicalListData = vehicalListData;
        }
    }
    public static class VehicalListData implements Serializable {

        @SerializedName("pk_vehicle_id")
        @Expose
        private int i;

        @SerializedName("make")
        @Expose
        private String make;

        @SerializedName("model")
        @Expose
        private String model;

        @SerializedName("reg_number")
        @Expose
        private String reg_number;

        @SerializedName("km")
        @Expose
        private String km;

        @SerializedName("next_service")
        @Expose
        private String nxt_service;

        @SerializedName("license_expiry")
        @Expose
        private String lice_exp;

        public int getI() {
            return i;
        }

        public void setI(int i) {
            this.i = i;
        }

        public String getMake() {
            return make;
        }

        public void setMake(String make) {
            this.make = make;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getReg_number() {
            return reg_number;
        }

        public void setReg_number(String reg_number) {
            this.reg_number = reg_number;
        }

        public String getKm() {
            return km;
        }

        public void setKm(String km) {
            this.km = km;
        }

        public String getNxt_service() {
            return nxt_service;
        }

        public void setNxt_service(String nxt_service) {
            this.nxt_service = nxt_service;
        }

        public String getLice_exp() {
            return lice_exp;
        }

        public void setLice_exp(String lice_exp) {
            this.lice_exp = lice_exp;
        }
    }
}
