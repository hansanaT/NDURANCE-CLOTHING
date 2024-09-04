package com.ndurance.product_service.shared.dto;

import com.ndurance.product_service.shared.ProductType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDTO {

    private Long id;
    private String productId;
    private String name;
    private String description;
    private List<CommentDTO> comments;
    private List<String> images;
    private ProductType type;
    private int price;
}
