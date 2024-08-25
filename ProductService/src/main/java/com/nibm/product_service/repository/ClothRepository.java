package com.nibm.product_service.repository;

import com.nibm.product_service.entity.ClothEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothRepository extends CrudRepository<ClothEntity, Long> {
    ClothEntity findByPublicId(String publicId);
    void deleteByPublicId(String publicId);
}
