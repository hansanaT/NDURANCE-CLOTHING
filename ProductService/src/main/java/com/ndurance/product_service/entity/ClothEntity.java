package com.ndurance.product_service.entity;

import jakarta.persistence.*;
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

    private List<String> images;

    @OneToMany
    private List<CommentEntity> comments;
}
