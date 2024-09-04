package com.ndurance.car_service.feign_client;

import com.ndurance.car_service.shared.model.request.OrderRequestModel;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "order-service")
public interface OrderClient {

    @PostMapping("/orders/cart/checkOut/{userId}")
    @Retry(name="order-service")
    @CircuitBreaker(name="order-service")
    String checkOut(@RequestHeader(value = "Authorization") String authorizationHeader, @PathVariable String userId, @RequestBody OrderRequestModel orderRequestModel);
}
