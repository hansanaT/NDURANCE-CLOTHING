package com.ndurance.product_service.shared.model.response;

import com.ndurance.product_service.shared.ProductType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductRest {
    private String productId;
    private String name;
    private String description;
    private List<String> images;
    private ProductType type;
    private int price;
}
