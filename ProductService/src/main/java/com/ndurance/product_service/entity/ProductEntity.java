package com.ndurance.product_service.entity;

import com.ndurance.product_service.shared.ProductType;
import jakarta.persistence.*;
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
    @OneToMany(cascade = CascadeType.ALL)
    private List<CommentEntity> comments;
}
