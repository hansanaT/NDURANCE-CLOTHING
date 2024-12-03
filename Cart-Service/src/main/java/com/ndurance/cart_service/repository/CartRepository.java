package com.ndurance.cart_service.repository;

import com.ndurance.cart_service.entity.CartEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends CrudRepository<CartEntity, Long> {
    List<CartEntity> findByUser(String user);
    CartEntity findByCartId(String cartId);
    CartEntity findByProductId(String productId);
}
