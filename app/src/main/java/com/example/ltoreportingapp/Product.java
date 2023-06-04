package com.example.ltoreportingapp;

public class Product {

    private int id;
    private String title;
    private String shortdesc;
    private String status;
    private String image;
    private String time;

    public Product(int id, String title, String shortdesc,String status, String image, String time) {
        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        this.status = status;
        this.image = image;
        this.time = time;
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public String getstatus() {return status;}


    public String getImage() {
        return image;
    }

    public String getTime() {
        return time;
    }
}
