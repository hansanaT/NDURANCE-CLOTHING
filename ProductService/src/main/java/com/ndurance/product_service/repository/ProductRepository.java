package com.ndurance.product_service.repository;

import com.ndurance.product_service.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
    ProductEntity findByProductId(String productId);
    void deleteByProductId(String productId);
}
