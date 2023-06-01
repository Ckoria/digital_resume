package com.onthemove.responseClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class VehicalHealthModel {

    public static class VehicleHealthRes
    {
        @SerializedName("status")
        @Expose
        private boolean status;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("data")
        @Expose
        private VehicleHealthData vehicalHealthListData;

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

        public VehicleHealthData getVehicalHealthListData() {
            return vehicalHealthListData;
        }

        public void setVehicalHealthListData(VehicleHealthData vehicalHealthListData) {
            this.vehicalHealthListData = vehicalHealthListData;
        }
    }
    public static class VehicleHealthData implements Serializable
    {
        @SerializedName("exterior")
        @Expose
        private ArrayList<VehicleExteriorData> vehicalHealthExteriorData;

        @SerializedName("interior")
        @Expose
        private ArrayList<VehicleInteriorData> vehicalHealthInteriorData;

        public ArrayList<VehicleExteriorData> getVehicalHealthExteriorData() {
            return vehicalHealthExteriorData;
        }

        public void setVehicalHealthExteriorData(ArrayList<VehicleExteriorData> vehicalHealthExteriorData) {
            this.vehicalHealthExteriorData = vehicalHealthExteriorData;
        }

        public ArrayList<VehicleInteriorData> getVehicalHealthInteriorData() {
            return vehicalHealthInteriorData;
        }

        public void setVehicalHealthInteriorData(ArrayList<VehicleInteriorData> vehicalHealthInteriorData) {
            this.vehicalHealthInteriorData = vehicalHealthInteriorData;
        }
    }

    public static class VehicleExteriorData implements Serializable
    {
        @SerializedName("id")
        @Expose
        private int ext_id;

        @SerializedName("type")
        @Expose
        private String ext_type;

        @SerializedName("title")
        @Expose
        private String ext_type_title;

        public int getExt_id() {
            return ext_id;
        }

        public void setExt_id(int ext_id) {
            this.ext_id = ext_id;
        }

        public String getExt_type() {
            return ext_type;
        }

        public void setExt_type(String ext_type) {
            this.ext_type = ext_type;
        }

        public String getExt_type_title() {
            return ext_type_title;
        }

        public void setExt_type_title(String ext_type_title) {
            this.ext_type_title = ext_type_title;
        }
    }

    public static class VehicleInteriorData implements Serializable
    {
        @SerializedName("id")
        @Expose
        private int int_id;

        @SerializedName("type")
        @Expose
        private String int_type;

        @SerializedName("title")
        @Expose
        private String int_type_title;

        public int getInt_id() {
            return int_id;
        }

        public void setInt_id(int int_id) {
            this.int_id = int_id;
        }

        public String getInt_type() {
            return int_type;
        }

        public void setInt_type(String int_type) {
            this.int_type = int_type;
        }

        public String getInt_type_title() {
            return int_type_title;
        }

        public void setInt_type_title(String int_type_title) {
            this.int_type_title = int_type_title;
        }
    }



}
