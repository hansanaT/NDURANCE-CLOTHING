package com.ndurance.order_service.shared.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ndurance.order_service.shared.AddressesModel;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderRequestModel {
    private Map<String, Integer> products;
    private String user;
    private AddressesModel shippingAddress;
    private AddressesModel billingAddresses;

    public Map<String, Integer> getProducts() {
        return products;
    }

    public void setProducts(Map<String, Integer> products) {
        this.products = products;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public AddressesModel getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(AddressesModel shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public AddressesModel getBillingAddresses() {
        return billingAddresses;
    }

    public void setBillingAddresses(AddressesModel billingAddresses) {
        this.billingAddresses = billingAddresses;
    }
}
