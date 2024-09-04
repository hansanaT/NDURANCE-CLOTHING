package com.ndurance.order_service.feign_client.model;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
public class CartModel implements Serializable {
    private String cartId;
    private String products;
    private int price;
    private String user;
    private int quantity;
}
