package com.ndurance.mobileapp.activity;

import android.os.Bundle;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;
import com.ndurance.mobileapp.R;
import com.ndurance.mobileapp.adapter.ProductAdapter;
import com.ndurance.mobileapp.model.dto.Product;
import com.ndurance.mobileapp.model.response.ProductResponse;
import com.ndurance.mobileapp.service.ProductService;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.product_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchProducts(0, 20);
    }

    private void fetchProducts(int page, int size) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/product-service/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ProductService productService = retrofit.create(ProductService.class);

        productService.getProducts(page, size).enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product> products = response.body().getContent();
                    products.forEach(product -> {
                        product.setImageURL("http://10.0.2.2:8080/product-service/products/images/" + product.getImages().get(0));
                    });
                    adapter = new ProductAdapter(HomeActivity.this, products);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(HomeActivity.this, "Failed to fetch products", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}