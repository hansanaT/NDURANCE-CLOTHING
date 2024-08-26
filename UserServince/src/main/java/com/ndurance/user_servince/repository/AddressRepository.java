package com.ndurance.user_servince.repository;

import com.ndurance.user_servince.entity.AddressEntity;
import com.ndurance.user_servince.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
	List<AddressEntity> findAllByUserDetails(UserEntity userEntity);
	AddressEntity findByAddressId(String addressId);
}
