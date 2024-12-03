package com.ndurance.mobileapp.model.Request;

import java.util.List;

public class CartRequestModel {
    private String productId;
    private int price;
    private int quantity;
    private String name;
    private List<String> images;

    public CartRequestModel(String productId, int price, int quantity, String name, List<String> images) {
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
        this.name = name;
        this.images = images;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
