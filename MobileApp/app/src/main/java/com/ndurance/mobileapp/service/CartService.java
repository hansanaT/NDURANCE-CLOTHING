package com.ndurance.mobileapp.service;

import com.ndurance.mobileapp.model.dto.CartItem;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface CartService {
    @GET("cart-service/cart/checkout/{userid}")
    Call<Void> checkout(
            @Path("userid") String userid,
            @Query("addressSame") boolean addressSame,
            @Header("Authorization") String authToken
    );
    @GET("cart-service/cart/{userId}")
    Call<List<CartItem>> fetchCart(@Path("userId") String userId, @Header("Authorization") String token);
    @DELETE("cart-service/cart/{userId}/{cartId}")
    Call<Void> removeFromCart(@Path("userId") String userId, @Path("cartId") String cartId, @Header("Authorization") String token);
}
