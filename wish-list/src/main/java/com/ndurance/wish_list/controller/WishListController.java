package com.ndurance.wish_list.controller;

import com.ndurance.wish_list.exceptions.WishListNotFoundServiceException;
import com.ndurance.wish_list.service.WishListService;
import com.ndurance.wish_list.shared.dto.WishListDTO;
import com.ndurance.wish_list.shared.model.request.WishListRequestModel;
import com.ndurance.wish_list.shared.model.response.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/wishlist")
public class WishListController {
    @Autowired
    private WishListService wishListService;

    private final ModelMapper modelMapper = new ModelMapper();

    @GetMapping("/{userid}")
    public List<WishListDTO> getWishList(@PathVariable String userid){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(!Objects.equals(username, userid))
            throw new WishListNotFoundServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        return wishListService.getWishList(userid);
    }

    @PostMapping("/{userid}")
    public void save(@RequestBody WishListRequestModel requestModel, @PathVariable String userid){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(!Objects.equals(username, userid))
            throw new WishListNotFoundServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        wishListService.saveWishList(requestModel);
    }

    @DeleteMapping("/{userid}/{wishlistId}")
    public void deleteWishList(@PathVariable String wishlistId, @PathVariable String userid){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if(!Objects.equals(username, userid))
            throw new WishListNotFoundServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        wishListService.deleteWishList(wishlistId, userid);
    }
}
