package com.buck.product_service.dto;

import lombok.*;


public class ProductRequest {
    private String name;
    private String description;
    private double price;
    private int stock;
    private String category;
    private String imageUrl;

    public ProductRequest(String name, String imageUrl, String category, int stock, double price, String description) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.category = category;
        this.stock = stock;
        this.price = price;
        this.description = description;
    }

    public ProductRequest() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
