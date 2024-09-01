package com.ndurance.order_service.shared.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ndurance.order_service.shared.AddressesModel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRest {
    private String orderId;
    List<ProductRest> products;
    private int price;
    private int quantity;
    private int totalPrice;
    private Date orderDate;
    private boolean isRefund;
    private boolean isDelivered;
    private AddressesModel shippingAddress;
    private AddressesModel billingAddress;
}
