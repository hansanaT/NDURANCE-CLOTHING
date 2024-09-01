package com.ndurance.car_service.service.impl;

import com.ndurance.car_service.entity.CartEntity;
import com.ndurance.car_service.exceptions.CartNotFoundServiceException;
import com.ndurance.car_service.feign_client.OrderClient;
import com.ndurance.car_service.repository.CartRepository;
import com.ndurance.car_service.service.CartService;
import com.ndurance.car_service.shared.Utils;
import com.ndurance.car_service.shared.dto.CartDTO;
import com.ndurance.car_service.shared.model.request.CartRequestModel;
import com.ndurance.car_service.shared.model.request.OrderRequestModel;
import com.ndurance.car_service.shared.model.response.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private OrderClient orderClient;
    @Autowired
    private Utils utils;

    @Override
    public void saveCart(CartRequestModel requestModel) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        CartEntity cartEntity = modelMapper.map(requestModel, CartEntity.class);
        cartEntity.setUser(username);
        cartEntity.setCartId(utils.generateAddressId(20));
        cartRepository.save(cartEntity);
    }

    @Override
    public List<CartDTO> getCart(String userId) {
        List<CartDTO> wishListDTOS = new ArrayList<>();
        cartRepository.findByUser(userId).forEach(i->{
            wishListDTOS.add(modelMapper.map(i, CartDTO.class));
        });
        return wishListDTOS;
    }

    @Override
    public void deleteCart(String id, String userid) {

        List<CartEntity> cartEntityList = cartRepository.findByUser(userid);

        if(cartEntityList == null || cartEntityList.isEmpty())
            throw new CartNotFoundServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        cartEntityList.forEach(cartEntity -> {
            if(Objects.equals(cartEntity.getCartId(), id)){
                cartRepository.delete(cartEntity);
            }
        });
    }

    @Override
    public void checkout(String userId, String token, boolean addressSame) {
        List<CartEntity> cart = cartRepository.findByUser(userId);
        OrderRequestModel orderRequestModel = new OrderRequestModel();
        orderRequestModel.setCart(cart);

        orderClient.checkOut(token, userId, orderRequestModel);
    }
}
