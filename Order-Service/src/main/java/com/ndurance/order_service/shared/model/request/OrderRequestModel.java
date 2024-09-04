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
}
