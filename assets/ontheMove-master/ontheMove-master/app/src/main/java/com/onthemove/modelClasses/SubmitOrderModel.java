package com.onthemove.modelClasses;

import java.util.ArrayList;

public class SubmitOrderModel {

    private String id;
    private String ticket_number;
    private String comment;
    private String name_of_the_image;
//    private ArrayList<String> image;
    private String image;
    private Double lat;
    private Double lng;
    private String address;
    private String sync;
    private String dateTime;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

//   public ArrayList<String> getImage() {
//        return image;
//    }
//
//    public void setImage(ArrayList<String> image) {
//        this.image = image;
//    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
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

    public String getSync() {
        return sync;
    }

    public void setSync(String sync) {
        this.sync = sync;
    }
}
