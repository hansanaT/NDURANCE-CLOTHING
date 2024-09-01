package com.ndurance.order_service.repository;


import com.ndurance.order_service.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
    OrderEntity findByOrderId(String orderId);

    List<OrderEntity> findByUser(String user);
    void deleteByOrderId(String publicId);
}
