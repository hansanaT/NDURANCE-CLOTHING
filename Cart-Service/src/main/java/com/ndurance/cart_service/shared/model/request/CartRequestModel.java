package com.ndurance.cart_service.shared.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartRequestModel {
    private String productId;;
    private int price;
    private int quantity;
    private String name;
    private List<String> images;
}
