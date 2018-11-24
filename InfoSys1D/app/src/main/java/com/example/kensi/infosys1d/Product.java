package com.example.kensi.infosys1d;

public class Product {

    private int id;
    private String title, shortdesc, category;
    private String price;
    private int image;
    private String imageUrl;
    private int qty;

    public Product(int id, String title, String shortdesc, String category, String price, int image, int qty) {
        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        this.category = category;
        this.price = price;
        this.image = image;
        this.qty = qty;
    }

    public Product(int id, String title, double price, int image){
        this.id = id;
        this.title = title;
        this.price = String.valueOf(price);
        this.image = image;
    }

    public Product(int id, String title, String shortdesc, String category, String price, String imageUrl, int qty) {
        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
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

    public String getCategory() {
        return category;
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

    public String getImageURL(){
        return imageUrl;
    }
}