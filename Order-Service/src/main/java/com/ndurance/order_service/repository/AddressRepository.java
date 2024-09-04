package com.ndurance.order_service.repository;

import com.ndurance.order_service.entity.AddressEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
	AddressEntity findByAddressId(String addressId);
}
