package com.ndurance.cart_service.shared.model.request;

import com.ndurance.cart_service.entity.CartEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestModel {
    private List<CartEntity> cart;
}
