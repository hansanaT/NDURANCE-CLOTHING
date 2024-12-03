package com.ndurance.mobileapp.service;

import com.ndurance.mobileapp.model.response.OrderResponse;
import com.ndurance.mobileapp.model.response.OrderRest;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface OrderService {
    @GET("order-service/orders/{userId}")
    Call<List<OrderResponse>> getOrders(@Header("Authorization") String token, @Path("userId") String userId);

    @GET("order-service/orders/{userId}/{orderId}")
    Call<OrderRest> getOrder(@Header("Authorization") String token, @Path("userId") String userId,@Path("orderId") String orderId);
}
