package com.ndurance.cart_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
public class CartEntity implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String cartId;
    private String productId;
    private String name;
    private List<String> images;
    private int price;
    private String user;
    private int quantity;
}
