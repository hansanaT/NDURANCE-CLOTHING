package com.ndurance.wish_list.repository;

import com.ndurance.wish_list.entity.WishListEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends CrudRepository<WishListEntity, Long> {
    List<WishListEntity> findByUser(String user);
    WishListEntity findByWishId(String wishId);
}
