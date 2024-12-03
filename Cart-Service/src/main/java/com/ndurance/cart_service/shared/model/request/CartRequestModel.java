package com.ndurance.cart_service.shared.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartRequestModel {
    private String productId;;
    private int price;
    private int quantity;
    private String name;
    private List<String> images;

    @Override
    public String toString() {
        return "CartRequestModel{" +
                "productId='" + productId + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", name='" + name + '\'' +
                ", images=" + images +
                '}';
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
