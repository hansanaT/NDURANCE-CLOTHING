package com.ndurance.ProductService.io.repository;

import com.ndurance.ProductService.io.entity.ClothEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothRepository extends CrudRepository<ClothEntity, Long> {
}
