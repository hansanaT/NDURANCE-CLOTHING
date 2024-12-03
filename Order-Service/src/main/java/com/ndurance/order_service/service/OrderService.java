package com.ndurance.order_service.service;

import com.ndurance.order_service.feign_client.model.OrderRequestModelC;
import com.ndurance.order_service.shared.model.request.OrderRequestModel;
import com.ndurance.order_service.shared.model.response.OrderRest;

import java.util.List;

public interface OrderService {
    void saveOrder(OrderRequestModel orderRequestModel,boolean changeAddress, boolean addressSame,String token);
    void saveOrder(OrderRequestModelC orderRequestModel,String token, String user);
    OrderRest getOrder(String publicId);
    List<OrderRest> getOrders(String user);

}
