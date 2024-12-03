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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public List<ProductRest> getProducts() {
        return products;
    }

    public void setProducts(List<ProductRest> products) {
        this.products = products;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public boolean isRefund() {
        return isRefund;
    }

    public void setRefund(boolean refund) {
        isRefund = refund;
    }

    public boolean isDelivered() {
        return isDelivered;
    }

    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }

    public AddressesModel getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(AddressesModel shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public AddressesModel getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(AddressesModel billingAddress) {
        this.billingAddress = billingAddress;
    }
}
