package com.ndurance.order_service.entity;

import com.ndurance.order_service.ProductType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Entity
@Getter
@Setter
public class ProductEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String productId;
    private String name;
    private String description;
    private ProductType type;
    private List<String> images;
    private int price;
    private int quantity;
    @ManyToOne
    private OrderEntity order;
}
