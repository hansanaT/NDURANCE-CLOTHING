package com.ndurance.order_service.feign_client;

import com.ndurance.order_service.shared.dto.ProductDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/products/product/{productId}")
    @Retry(name = "product-service")
    @CircuitBreaker(name = "product-service", fallbackMethod = "getProductByIdFallback")
    ProductDTO getProductById(@PathVariable("productId") String productId);

    default ProductDTO getProductByIdFallback(String productId, Throwable exception) {
        System.err.println("Fallback invoked for productId: " + productId + ". Exception: " + exception.getMessage());
        ProductDTO fallbackProduct = new ProductDTO();
        fallbackProduct.setProductId(productId);
        fallbackProduct.setName("Unknown Product");
        fallbackProduct.setPrice(0);
        return fallbackProduct;
    }
}