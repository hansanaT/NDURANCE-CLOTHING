package com.ndurance.car_service.controller;

import com.ndurance.car_service.exceptions.CartServiceException;
import com.ndurance.car_service.service.CartService;
import com.ndurance.car_service.shared.dto.CartDTO;
import com.ndurance.car_service.shared.model.request.CartRequestModel;
import com.ndurance.car_service.shared.model.response.ErrorMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userid}")
    public List<CartDTO> getCart(@PathVariable String userid){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        if(!Objects.equals(username, userid))
            throw new CartServiceException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage());

        return cartService.getCart(userid);
    }

    @PostMapping
    public void saveCart(@RequestBody CartRequestModel requestModel){
        cartService.saveCart(requestModel);
    }

    @DeleteMapping("/{userid}/{cartid}")
    public void delete(@PathVariable String userid ,@PathVariable String cartid){
        cartService.deleteCart(cartid, userid);
    }

    @GetMapping("/checkout/{userid}")
    public void checkOut(@PathVariable String userid, @RequestHeader("Authorization") String token, @RequestParam(name = "addressSame") boolean addressSame){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        if(!Objects.equals(username, userid))
            throw new CartServiceException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage());

        cartService.checkout(userid, token, addressSame);
    }
}
