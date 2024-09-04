package com.ndurance.order_service.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ndurance.order_service.shared.AddressesModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {
    private Long id;
    private String orderId;
    private String productId;
    private int price;
    private int quantity;
    private int totalPrice;
    private Date orderDate;
    private boolean isRefund;
    private boolean isDelivered;
    private AddressesModel shippingAddress;
    private AddressesModel billingAddress;
    private String user;
}
