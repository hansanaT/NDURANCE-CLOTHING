package com.ndurance.order_service.shared.dto;

import com.ndurance.order_service.ProductType;
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
