package com.onthemove.responseClasses;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class VehicleSubmitDetailsInteriorModel implements Serializable {

    public String vehicle_health_id;
    public String vehicle_condition_id;
    public String comment;

    public String getVehicle_health_id() {
        return vehicle_health_id;
    }

    public void setVehicle_health_id(String vehicle_health_id) {
        this.vehicle_health_id = vehicle_health_id;
    }

    public String getVehicle_condition_id() {
        return vehicle_condition_id;
    }

    public void setVehicle_condition_id(String vehicle_condition_id) {
        this.vehicle_condition_id = vehicle_condition_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
