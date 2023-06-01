package com.onthemove.modelClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OnDutyModel {

    public static class OnDutyRes {

        @SerializedName("status")
        private boolean status;

        @SerializedName("message")
        private String message;

        @SerializedName("data")
        private OnDutyData onDutyData;

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

        public OnDutyData getOnDutyData() {
            return onDutyData;
        }

        public void setOnDutyData(OnDutyData onDutyData) {
            this.onDutyData = onDutyData;
        }
    }

    public static class OnDutyData{

        @SerializedName("pk_agent_vehicle_id")
        @Expose
        private String pkAgentVehicleId;
        @SerializedName("fk_agent_id")
        @Expose
        private String fkAgentId;
        @SerializedName("fk_vehicle_id")
        @Expose
        private String fkVehicleId;
        @SerializedName("assign_for_date")
        @Expose
        private String assignForDate;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("pk_agent_id")
        @Expose
        private String pkAgentId;
        @SerializedName("agent_name")
        @Expose
        private String agentName;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("mobile_number")
        @Expose
        private String mobileNumber;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("profile_image")
        @Expose
        private String profileImage;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("busy_free")
        @Expose
        private String busyFree;
        @SerializedName("role")
        @Expose
        private String role;
        @SerializedName("current_lat")
        @Expose
        private String currentLat;
        @SerializedName("current_lng")
        @Expose
        private String currentLng;
        @SerializedName("app_version")
        @Expose
        private String appVersion;
        @SerializedName("device_model")
        @Expose
        private String deviceModel;
        @SerializedName("device_version")
        @Expose
        private String deviceVersion;
        @SerializedName("device_type")
        @Expose
        private String deviceType;
        @SerializedName("device_token")
        @Expose
        private String deviceToken;
        @SerializedName("last_login_datetime")
        @Expose
        private String lastLoginDatetime;
        @SerializedName("pk_vehicle_id")
        @Expose
        private String pkVehicleId;
        @SerializedName("make")
        @Expose
        private String make;
        @SerializedName("model")
        @Expose
        private String model;
        @SerializedName("reg_number")
        @Expose
        private String regNumber;
        @SerializedName("km")
        @Expose
        private String km;
        @SerializedName("next_service")
        @Expose
        private String nextService;
        @SerializedName("license_expiry")
        @Expose
        private String licenseExpiry;


        public String getPkAgentVehicleId() {
            return pkAgentVehicleId;
        }

        public void setPkAgentVehicleId(String pkAgentVehicleId) {
            this.pkAgentVehicleId = pkAgentVehicleId;
        }

        public String getFkAgentId() {
            return fkAgentId;
        }

        public void setFkAgentId(String fkAgentId) {
            this.fkAgentId = fkAgentId;
        }

        public String getFkVehicleId() {
            return fkVehicleId;
        }

        public void setFkVehicleId(String fkVehicleId) {
            this.fkVehicleId = fkVehicleId;
        }

        public String getAssignForDate() {
            return assignForDate;
        }

        public void setAssignForDate(String assignForDate) {
            this.assignForDate = assignForDate;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getPkAgentId() {
            return pkAgentId;
        }

        public void setPkAgentId(String pkAgentId) {
            this.pkAgentId = pkAgentId;
        }

        public String getAgentName() {
            return agentName;
        }

        public void setAgentName(String agentName) {
            this.agentName = agentName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getProfileImage() {
            return profileImage;
        }

        public void setProfileImage(String profileImage) {
            this.profileImage = profileImage;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getBusyFree() {
            return busyFree;
        }

        public void setBusyFree(String busyFree) {
            this.busyFree = busyFree;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public String getCurrentLat() {
            return currentLat;
        }

        public void setCurrentLat(String currentLat) {
            this.currentLat = currentLat;
        }

        public String getCurrentLng() {
            return currentLng;
        }

        public void setCurrentLng(String currentLng) {
            this.currentLng = currentLng;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getDeviceModel() {
            return deviceModel;
        }

        public void setDeviceModel(String deviceModel) {
            this.deviceModel = deviceModel;
        }

        public String getDeviceVersion() {
            return deviceVersion;
        }

        public void setDeviceVersion(String deviceVersion) {
            this.deviceVersion = deviceVersion;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getDeviceToken() {
            return deviceToken;
        }

        public void setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
        }

        public String getLastLoginDatetime() {
            return lastLoginDatetime;
        }

        public void setLastLoginDatetime(String lastLoginDatetime) {
            this.lastLoginDatetime = lastLoginDatetime;
        }

        public String getPkVehicleId() {
            return pkVehicleId;
        }

        public void setPkVehicleId(String pkVehicleId) {
            this.pkVehicleId = pkVehicleId;
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

        public String getRegNumber() {
            return regNumber;
        }

        public void setRegNumber(String regNumber) {
            this.regNumber = regNumber;
        }

        public String getKm() {
            return km;
        }

        public void setKm(String km) {
            this.km = km;
        }

        public String getNextService() {
            return nextService;
        }

        public void setNextService(String nextService) {
            this.nextService = nextService;
        }

        public String getLicenseExpiry() {
            return licenseExpiry;
        }

        public void setLicenseExpiry(String licenseExpiry) {
            this.licenseExpiry = licenseExpiry;
        }
    }
}
