package com.ndurance.order_service.controller;

import com.ndurance.order_service.exceptions.OrderServiceException;
import com.ndurance.order_service.feign_client.model.CartModel;
import com.ndurance.order_service.feign_client.model.OrderRequestModelC;
import com.ndurance.order_service.service.OrderService;
import com.ndurance.order_service.shared.model.request.OrderRequestModel;
import com.ndurance.order_service.shared.model.response.ErrorMessages;
import com.ndurance.order_service.shared.model.response.OrderRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public void saveOrder(@RequestBody OrderRequestModel orderRequestModel, @RequestHeader(value = "Authorization") String authorizationHeader, @RequestParam(name = "addressSame") boolean addressSame){
        orderService.saveOrder(orderRequestModel, addressSame ,authorizationHeader);
    }

    @GetMapping("/{userId}/{orderId}")
    public OrderRest getOrder(@PathVariable String userId,@PathVariable String orderId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        if(!Objects.equals(username, userId))
            throw new OrderServiceException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage());

        return orderService.getOrder(orderId);
    }

    @GetMapping
    public List<OrderRest> orderRests(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return orderService.getOrders(username);
    }

    @PostMapping("/cart/checkOut/{userId}")
    public String checkOut(@PathVariable String userId, @RequestBody OrderRequestModelC requestModelC, @RequestHeader(value = "Authorization") String authorizationHeader){
        OrderRequestModel requestModel = new OrderRequestModel();

        List<CartModel> cart = requestModelC.getCart();
        Map<String, Integer> products = new HashMap<>();

        cart.forEach(c->{
            products.put(c.getProductId(), c.getPrice());
        });

        requestModel.setProducts(products);
        requestModel.setUser(userId);

        orderService.saveOrder(requestModel, true ,authorizationHeader);

        return "String";
    }
}
