package com.ndurance.mobileapp.service;

import com.ndurance.mobileapp.model.response.ProductResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductService {
    @GET("products")
    Call<ProductResponse> getProducts(
            @Query("page") int page,
            @Query("size") int size
    );
}
