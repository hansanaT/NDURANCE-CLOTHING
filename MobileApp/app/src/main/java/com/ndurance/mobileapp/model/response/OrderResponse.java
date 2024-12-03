package com.ndurance.mobileapp.model.response;

public class OrderResponse {
    private final String id;
    private final String date;
    private final double price;

    public OrderResponse(String id, String date, double price) {
        this.id = id;
        this.date = date;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", price=" + price +
                '}';
    }
}
