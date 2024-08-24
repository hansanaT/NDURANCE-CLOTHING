package com.nibm.ProductService.FeignClient;

import com.nibm.ProductService.ui.model.UserRest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "customer-service", url = "http://localhost:8080")
public interface UserClient {
    @GetMapping("/users/{id}")
    UserRest getCustomerById(@PathVariable("id") Long id, @RequestHeader("Authorization") String token);
}
