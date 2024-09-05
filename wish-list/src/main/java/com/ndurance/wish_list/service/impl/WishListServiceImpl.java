package com.ndurance.wish_list.service.impl;

import com.ndurance.wish_list.entity.WishListEntity;
import com.ndurance.wish_list.exceptions.WishListNotFoundServiceException;
import com.ndurance.wish_list.repository.WishListRepository;
import com.ndurance.wish_list.service.WishListService;
import com.ndurance.wish_list.shared.Utils;
import com.ndurance.wish_list.shared.dto.WishListDTO;
import com.ndurance.wish_list.shared.model.request.WishListRequestModel;
import com.ndurance.wish_list.shared.model.response.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class WishListServiceImpl implements WishListService {

    @Autowired
    private WishListRepository wishListRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Autowired
    private Utils utils;

    @Override
    public void saveWishList(WishListRequestModel requestModel) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        WishListEntity wishList = new WishListEntity();

        wishList.setUser(username);
        wishList.setWishId(utils.generateAddressId(20));
        wishList.setProductId(requestModel.getProductId());
        wishList.setPrice(requestModel.getPrice());

        wishListRepository.save(wishList);
    }

    @Override
    public List<WishListDTO> getWishList(String userId) {
        List<WishListDTO> wishListDTOS = new ArrayList<>();
        wishListRepository.findByUser(userId).forEach(i->{
            wishListDTOS.add(modelMapper.map(i, WishListDTO.class));
        });
        return wishListDTOS;
    }

    @Override
    public void deleteWishList(String id, String userid) {
        List<WishListEntity> wishList = wishListRepository.findByUser(userid);

        if(wishList == null)
            throw new WishListNotFoundServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        wishList.forEach(wishListEntity -> {
            if(Objects.equals(wishListEntity.getWishId(), id)){
                wishListRepository.deleteById(wishListEntity.getId());
            }
        });
    }
}
