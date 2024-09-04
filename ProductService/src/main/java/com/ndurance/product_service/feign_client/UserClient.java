package com.ndurance.product_service.feign_client;

import com.ndurance.product_service.feign_client.model.UserRest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/users/{userid}")
    @Retry(name="user-service")
    @CircuitBreaker(name="user-service", fallbackMethod="getCustomerByIdFallback")
    UserRest getCustomerById(@PathVariable("userid") String userid, @RequestHeader("Authorization") String token);

    default UserRest getCustomerByIdFallback(String userid, String token, Throwable exception){
        UserRest userRest = new UserRest();
        userRest.setEmail("test@gmail.com");
        userRest.setFirstName("Test");
        return userRest;
    }
}
