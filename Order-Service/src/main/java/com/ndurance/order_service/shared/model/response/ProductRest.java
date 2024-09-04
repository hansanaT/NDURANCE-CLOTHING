package com.ndurance.order_service.shared.model.response;

import com.ndurance.order_service.ProductType;
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
