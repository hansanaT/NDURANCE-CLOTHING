package com.ndurance.product_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class CommentEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String comment;
    private String userPublicId;
    @ManyToOne
    private ClothEntity cloth;
}