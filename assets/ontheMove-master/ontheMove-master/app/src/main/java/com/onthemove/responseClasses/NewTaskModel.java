package com.onthemove.responseClasses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class NewTaskModel {

    public static class NewTaskRes {

        @SerializedName("status")
        @Expose
        private boolean status;

        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("duty")
        @Expose
        private String duty;

        @SerializedName("data")
        @Expose
        private ArrayList<NewTaskData> newTaskData;

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

        public String getDuty() {
            return duty;
        }

        public void setDuty(String duty) {
            this.duty = duty;
        }

        public ArrayList<NewTaskData> getNewTaskData() {
            return newTaskData;
        }

        public void setNewTaskData(ArrayList<NewTaskData> newTaskData) {
            this.newTaskData = newTaskData;
        }
    }

    public static class NewTaskData implements Serializable{

        @SerializedName("task_id")
        @Expose
        private String taskId;
        @SerializedName("customer_name")
        @Expose
        private String customerName;
        @SerializedName("mobile_number")
        @Expose
        private String mobileNumber;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("ticket_number")
        @Expose
        private String ticketNumber;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("lng")
        @Expose
        private String lng;
        @SerializedName("pickup_address")
        @Expose
        private String pickupAddress;
        @SerializedName("house_number")
        @Expose
        private String houseNumber;
        @SerializedName("complex_name")
        @Expose
        private String complexName;
        @SerializedName("pickup_distance")
        @Expose
        private String pickupDistance;

        @SerializedName("calculate_distance")
        @Expose
        private String calculate_distance;

        @SerializedName("pickup_date_time")
        @Expose
        private String pickupDateTime;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("task_status")
        @Expose
        private String taskStatus;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("assigned_datetime")
        @Expose
        private String assignedDatetime;

        private String signatureImg;
        private String comment;


        public String getCalculate_distance() {
            return calculate_distance;
        }

        public void setCalculate_distance(String calculate_distance) {
            this.calculate_distance = calculate_distance;
        }

        public String getSignatureImg() {
            return signatureImg;
        }

        public void setSignatureImg(String signatureImg) {
            this.signatureImg = signatureImg;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTicketNumber() {
            return ticketNumber;
        }

        public void setTicketNumber(String ticketNumber) {
            this.ticketNumber = ticketNumber;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getPickupAddress() {
            return pickupAddress;
        }

        public void setPickupAddress(String pickupAddress) {
            this.pickupAddress = pickupAddress;
        }

        public String getHouseNumber() {
            return houseNumber;
        }

        public void setHouseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
        }

        public String getComplexName() {
            return complexName;
        }

        public void setComplexName(String complexName) {
            this.complexName = complexName;
        }

        public String getPickupDistance() {
            return pickupDistance;
        }

        public void setPickupDistance(String pickupDistance) {
            this.pickupDistance = pickupDistance;
        }

        public String getPickupDateTime() {
            return pickupDateTime;
        }

        public void setPickupDateTime(String pickupDateTime) {
            this.pickupDateTime = pickupDateTime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getTaskStatus() {
            return taskStatus;
        }

        public void setTaskStatus(String taskStatus) {
            this.taskStatus = taskStatus;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getAssignedDatetime() {
            return assignedDatetime;
        }

        public void setAssignedDatetime(String assignedDatetime) {
            this.assignedDatetime = assignedDatetime;
        }
    }
}
