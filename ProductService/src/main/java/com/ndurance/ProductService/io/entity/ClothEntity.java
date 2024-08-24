package com.ndurance.ProductService.io.entity;

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
public class ClothEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String publicId;
    private String name;
    private String description;

    private List<String> pictures;

    @ManyToOne
    private List<CommentEntity> comments;
}
