package com.example.kensi.infosys1d;

import org.json.JSONException;
import org.json.JSONObject;

public class Product {

    private int id;
    private String title, shortdesc, category;
    private String price;
    private int image;
    private String imageUrl;
    private int qty;
    private JSONObject productJSON = new JSONObject();

    public Product(int id, String title, String shortdesc, String category, String price, int image, int qty) throws JSONException {
        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        this.category = category;
        this.price = price;
        this.image = image;
        this.qty = qty;
        makeJSON();
    }

    public Product(int id, String title, double price, String imageUrl) throws JSONException {
        this.id = id;
        this.title = title;
        this.price = String.valueOf(price);
        this.imageUrl = imageUrl;
        makeJSON();
    }

    public Product(int id, String title, String shortdesc, String category, String price, String imageUrl, int qty) throws JSONException {
        this.id = id;
        this.title = title;
        this.shortdesc = shortdesc;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        this.qty = qty;
        makeJSON();
    }

    public void makeJSON() throws JSONException {
        productJSON.put("item_name",title);
        productJSON.put("qty",qty);
    }

    public JSONObject getJSON() throws JSONException {
        productJSON.put("qty", getQty());
        return productJSON;
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

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getImageURL(){
        return imageUrl;
    }
}