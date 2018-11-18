package com.example.kensi.infosys1d;

public class Product {

    private int id;
    private String title, shortdesc;
    private String price;
    private int image;
    private int qty;

    public Product(int id, String title, String shortdesc, String price, int image, int qty) {
        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        this.price = price;
        this.image = image;
        this.qty = qty;
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

    public String getPrice() {
        return price;
    }

    public int getImage() {
        return image;
    }

    public int getQty() {
        return qty;
    }
}
