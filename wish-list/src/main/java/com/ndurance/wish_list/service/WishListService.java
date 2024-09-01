package com.ndurance.wish_list.service;

import com.ndurance.wish_list.shared.dto.WishListDTO;
import com.ndurance.wish_list.shared.model.request.WishListRequestModel;

import java.util.List;

public interface WishListService {
    void saveWishList(WishListRequestModel requestModel);
    List<WishListDTO> getWishList(String userId);
    void deleteWishList(String id, String userid);
}
