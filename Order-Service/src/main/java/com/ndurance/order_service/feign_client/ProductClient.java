package com.ndurance.order_service.feign_client;

import com.ndurance.order_service.feign_client.model.ProductRest;
import com.ndurance.order_service.shared.dto.ProductDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "product-service")
public interface ProductClient {
    @GetMapping("/products/{productId}")
    @Retry(name="product-service")
    @CircuitBreaker(name="user-service", fallbackMethod="getProductByIdFallback")
    ProductDTO getProductById(@PathVariable("productId") String productId, @RequestHeader("Authorization") String token);

    default ProductDTO getProductByIdFallback(String productId, String token, Throwable exception){
        return new ProductDTO();
    }
}
