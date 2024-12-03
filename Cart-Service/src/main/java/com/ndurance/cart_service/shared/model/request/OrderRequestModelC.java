package com.ndurance.cart_service.shared.model.request;

import com.ndurance.cart_service.entity.CartEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestModelC {
    private List<CartEntity> cart;

    public List<CartEntity> getCart() {
        return cart;
    }

    public void setCart(List<CartEntity> cart) {
        this.cart = cart;
    }
}
