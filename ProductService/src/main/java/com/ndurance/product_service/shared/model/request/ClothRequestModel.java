package com.ndurance.product_service.shared.model.request;

import com.ndurance.product_service.shared.ProductType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClothRequestModel {
    private String name;
    private String description;
    private ProductType type;
    private int price;
}
