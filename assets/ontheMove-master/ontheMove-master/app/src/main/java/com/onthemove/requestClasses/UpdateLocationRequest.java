package com.onthemove.requestClasses;

public class UpdateLocationRequest {

    private String current_lat;
    private String current_lng;

    public UpdateLocationRequest(String current_lat, String current_lng) {
        this.current_lat = current_lat;
        this.current_lng = current_lng;
    }

    public String getCurrent_lat() {
        return current_lat;
    }

    public void setCurrent_lat(String current_lat) {
        this.current_lat = current_lat;
    }

    public String getCurrent_lng() {
        return current_lng;
    }

    public void setCurrent_lng(String current_lng) {
        this.current_lng = current_lng;
    }
}
