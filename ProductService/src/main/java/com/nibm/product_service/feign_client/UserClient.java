package com.nibm.product_service.feign_client;

import com.nibm.product_service.model.UserRest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {
    @GetMapping("/users/{userid}")
    @Retry(name="USER-SERVICE")
    @CircuitBreaker(name="USER-SERVICE", fallbackMethod="getCustomerByIdFallback")
    UserRest getCustomerById(@PathVariable("userid") String userid, @RequestHeader("Authorization") String token);

    default UserRest getCustomerByIdFallback(String userid, String token, Throwable exception){
        UserRest userRest = new UserRest();
        userRest.setEmail("test@gmail.com");
        userRest.setFirstName("Test");
        return userRest;
    }
}
