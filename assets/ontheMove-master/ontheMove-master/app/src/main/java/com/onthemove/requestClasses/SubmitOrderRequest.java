package com.onthemove.requestClasses;

import android.net.Uri;

import java.util.ArrayList;

public class SubmitOrderRequest {

    private String ticket_number;
    private String comment;
    private String name_of_the_image;
    private ArrayList<Uri> image;
    private Double lat;
    private Double lng;
    private String address;

    public String getTicket_number() {
        return ticket_number;
    }

    public void setTicket_number(String ticket_number) {
        this.ticket_number = ticket_number;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName_of_the_image() {
        return name_of_the_image;
    }

    public void setName_of_the_image(String name_of_the_image) {
        this.name_of_the_image = name_of_the_image;
    }

    public ArrayList<Uri> getImage() {
        return image;
    }

    public void setImage(ArrayList<Uri> image) {
        this.image = image;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
