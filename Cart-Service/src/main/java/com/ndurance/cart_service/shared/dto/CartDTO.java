package com.ndurance.cart_service.shared.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDTO {
    private Long id;
    private String cartId;
    private String products;
    private int price;
    private String user;
    private int quantity;
}
