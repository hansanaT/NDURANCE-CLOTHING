package com.ndurance.order_service.feign_client.model;


import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class OrderRequestModelC {
    private List<CartModel> cart;
}