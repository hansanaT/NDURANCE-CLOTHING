package com.ndurance.car_service.service;


import com.ndurance.car_service.shared.dto.CartDTO;
import com.ndurance.car_service.shared.model.request.CartRequestModel;

import java.util.List;

public interface CartService {
    void saveCart(CartRequestModel requestModel);
    List<CartDTO> getCart(String userId);
    void deleteCart(String id, String userid);
    void checkout(String userId, String token, boolean addressSame);
}
