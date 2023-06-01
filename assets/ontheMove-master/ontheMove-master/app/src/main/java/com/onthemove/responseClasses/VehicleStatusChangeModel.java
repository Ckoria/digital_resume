package com.onthemove.responseClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class VehicleStatusChangeModel {

    public static class VehicleStatusChangeReq {

        @SerializedName("pk_vehicle_id")
        @Expose
        private int vehicle_id;

        public int getVehicle_id() {
            return vehicle_id;
        }

        public void setVehicle_id(int vehicle_id) {
            this.vehicle_id = vehicle_id;
        }
    }
    public static class VehicleStatusChangeRes {

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
  /*  public class VehicleStatusData implements Serializable
    {
        @SerializedName("pk_vehicle_id")
        @Expose
        private int pk_vehicle_id;

        @SerializedName("make")
        @Expose
        private String make;

        @SerializedName("model")
        @Expose
        private String model;

        @SerializedName("reg_number")
        @Expose
        private String reg_no;

        @SerializedName("km")
        @Expose
        private String km;

        @SerializedName("next_service")
        @Expose
        private String next_service;

        @SerializedName("license_expiry")
        @Expose
        private String lic_exp;

        @SerializedName("status")
        @Expose
        private String status;

        @SerializedName("created_at")
        @Expose
        private String created_at;

        @SerializedName("updated_at")
        @Expose
        private String updated_at;


        public int getPk_vehicle_id() {
            return pk_vehicle_id;
        }

        public void setPk_vehicle_id(int pk_vehicle_id) {
            this.pk_vehicle_id = pk_vehicle_id;
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

        public String getReg_no() {
            return reg_no;
        }

        public void setReg_no(String reg_no) {
            this.reg_no = reg_no;
        }

        public String getKm() {
            return km;
        }

        public void setKm(String km) {
            this.km = km;
        }

        public String getNext_service() {
            return next_service;
        }

        public void setNext_service(String next_service) {
            this.next_service = next_service;
        }

        public String getLic_exp() {
            return lic_exp;
        }

        public void setLic_exp(String lic_exp) {
            this.lic_exp = lic_exp;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }*/
}
