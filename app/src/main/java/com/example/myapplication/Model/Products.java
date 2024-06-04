package com.example.myapplication.Model;

import java.io.Serializable;

public class Products implements Serializable {
    private String name, category, description, price;
    private String latitude, longitude;
    public Products(String name, String category, String description, String price, String latitude, String longitude)
    {
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public Products(){}

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}
