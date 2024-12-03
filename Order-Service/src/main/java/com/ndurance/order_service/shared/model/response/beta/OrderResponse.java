package com.ndurance.order_service.shared.model.response.beta;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {
    private String id;
    private String date;
    private double price;

    public OrderResponse(String id, String date, double price) {
        this.id = id;
        this.date = date;
        this.price = price;
    }

    public String getId() { return id; }
    public String getDate() { return date; }
    public double getPrice() { return price; }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", price=" + price +
                '}';
    }
}
