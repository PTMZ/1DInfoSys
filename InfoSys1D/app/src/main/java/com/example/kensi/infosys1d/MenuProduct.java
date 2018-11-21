package com.example.kensi.infosys1d;
public class MenuProduct{
    private int id;
    private String title;
    private String shortdesc;
    private double rating;
    private double price;
    private int image;

    public MenuProduct(int id, String title, double price, int image) {
        this.id = id;
        this.title = title;
        //this.shortdesc = shortdesc;
        //this.rating = rating;
        this.price = price;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

//    //public String getShortdesc() {
//        return shortdesc;
//    }

//    //public double getRating() {
//        return rating;
//    }

    public double getPrice() {
        return price;
    }

    public int getImage() {
        return image;
    }
}