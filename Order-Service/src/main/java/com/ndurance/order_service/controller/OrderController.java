package com.ndurance.order_service.controller;

import com.ndurance.order_service.exceptions.OrderServiceException;
import com.ndurance.order_service.feign_client.model.CartModel;
import com.ndurance.order_service.feign_client.model.OrderRequestModelC;
import com.ndurance.order_service.service.OrderService;
import com.ndurance.order_service.shared.model.request.OrderRequestModel;
import com.ndurance.order_service.shared.model.response.ErrorMessages;
import com.ndurance.order_service.shared.model.response.OrderRest;
import com.ndurance.order_service.shared.model.response.beta.OrderResponse;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper = new ModelMapper();

    @PostMapping
    public void saveOrder(@RequestBody OrderRequestModel orderRequestModel, @RequestHeader(value = "Authorization") String authorizationHeader, 
    		@RequestParam(name = "changeAddress")boolean changeAddress, @RequestParam(name = "addressSame") boolean addressSame){
    	
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    	String username = authentication.getName();
        if(!Objects.equals(username, orderRequestModel.getUser()))
            throw new OrderServiceException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage());
        
        orderService.saveOrder(orderRequestModel,changeAddress, addressSame ,authorizationHeader);
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

    @GetMapping("/{userId}")
    public List<OrderResponse> getAllOrders(@PathVariable String userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

//        System.out.println(username);
        System.out.println(userId);

//        if(!Objects.equals(username, userId))
//            throw new OrderServiceException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage());

        List<OrderRest> orders = orderService.getOrders(userId);
        List<OrderResponse> ordersRes = new ArrayList<>();

        orders.forEach(order->{
            OrderResponse orderResponse = new OrderResponse(order.getOrderId(), order.getOrderDate().toString(), order.getPrice());
            ordersRes.add(orderResponse);
        });

        ordersRes.forEach(order->{
            System.out.println("FUCK YOU ------------ " + order.getId());
        });

        return ordersRes;
    }

    @PostMapping("/cart/checkOut/{userId}")
    public String checkOut(@PathVariable String userId, @RequestBody OrderRequestModelC requestModelC, @RequestHeader(value = "Authorization") String authorizationHeader){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    	String username = authentication.getName();
        if(!Objects.equals(username, userId))
            throw new OrderServiceException(ErrorMessages.AUTHENTICATION_FAILED.getErrorMessage());
        
       orderService.saveOrder(requestModelC,authorizationHeader, userId);

       return "String";
    }
}
