package com.onthemove.modelClasses;

public class Demo {

    private String date;
    private String name;
    private String address;

    public Demo(String date, String name, String address) {
        this.date = date;
        this.name = name;
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
