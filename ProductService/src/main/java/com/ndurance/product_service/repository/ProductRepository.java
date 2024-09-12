package com.ndurance.product_service.repository;

import com.ndurance.product_service.entity.ProductEntity;
import com.ndurance.product_service.shared.ProductType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
    ProductEntity findByProductId(String productId);
    void deleteByProductId(String productId);

    Page<ProductEntity> findAll(Pageable pageable);
    Page<ProductEntity> findByType(ProductType type, Pageable pageable);
}
