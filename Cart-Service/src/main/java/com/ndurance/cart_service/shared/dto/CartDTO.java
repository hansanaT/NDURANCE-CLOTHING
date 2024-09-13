package com.ndurance.cart_service.shared.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDTO {
    private Long id;
    private String cartId;
    private String productId;
    private int price;
    private String user;
    private String name;
    private List<String> images;
    private int quantity;
}
