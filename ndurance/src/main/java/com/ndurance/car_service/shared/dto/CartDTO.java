package com.ndurance.car_service.shared.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
