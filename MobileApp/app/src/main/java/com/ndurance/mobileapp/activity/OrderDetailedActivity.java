package com.ndurance.mobileapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ndurance.mobileapp.R;
import com.ndurance.mobileapp.adapter.OrderDetailedAdapter;
import com.ndurance.mobileapp.adapter.SpaceItemDecoration;
import com.ndurance.mobileapp.model.dto.CartItem;
import com.ndurance.mobileapp.model.response.OrderRest;
import com.ndurance.mobileapp.model.response.ProductRest;
import com.ndurance.mobileapp.service.OrderService;
import com.ndurance.mobileapp.utils.TokenManager;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OrderDetailedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvTotal, tvOrderSummary, tvEmptyCartMessage, tvShippingAddress, tvBillingAddress;
    private TokenManager tokenManager;
    private OrderService orderService;
    private OrderDetailedAdapter orderDetailedAdapter;
    private String orderId;
    private OrderRest order;
    private LinearLayout layoutExpandable;
    private boolean isExpanded = false;
    private ImageView cart_icon, ivUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detailed);

        tvShippingAddress = findViewById(R.id.tvShippingAddress);
        tvBillingAddress = findViewById(R.id.tvBillingAddress);
        layoutExpandable = findViewById(R.id.layoutExpandable);
        tvOrderSummary = findViewById(R.id.tvOrderSummary);
        cart_icon = findViewById(R.id.cart_icon);

        cart_icon.setOnClickListener(view -> {
            Intent intent = new Intent(OrderDetailedActivity.this, CartActivity.class);
            startActivity(intent);
        });

        // Retrieve orderId from Intent
        orderId = getIntent().getStringExtra("orderId");

        tokenManager = new TokenManager(this);

        recyclerView = findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvTotal = findViewById(R.id.tvTotal);
        tvEmptyCartMessage = findViewById(R.id.tvEmptyCartMessage);
        ivUser = findViewById(R.id.ivUser);

        ivUser.setOnClickListener(view -> {
            Intent intent = new Intent(OrderDetailedActivity.this, UserActivity.class);
            startActivity(intent);
        });

        int spacing = getResources().getDimensionPixelSize(R.dimen.recycler_item_spacing);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacing));

        setupRetrofit();

        String userId = tokenManager.getUserId();
        String jwtToken = tokenManager.getJwtToken();

        if (userId != null && !userId.isEmpty() && jwtToken != null && !jwtToken.isEmpty() && orderId != null) {
            fetchOrderData(userId, jwtToken);
        } else {
            Toast.makeText(this, "Invalid User or Order ID", Toast.LENGTH_SHORT).show();
        }

        tvOrderSummary.setOnClickListener(view -> toggleExpandableSection());

    }

    private void setupRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        orderService = retrofit.create(OrderService.class);
    }

    private void fetchOrderData(String userId, String token) {
        orderService.getOrder("Bearer " + token, userId, orderId).enqueue(new Callback<OrderRest>() {
            @Override
            public void onResponse(Call<OrderRest> call, Response<OrderRest> response) {
                if (response.isSuccessful() && response.body() != null) {
                    order = response.body();
                    calculatePrices(order);
                    updateUI();
                } else {
                    tvEmptyCartMessage.setVisibility(View.VISIBLE);
                    Toast.makeText(OrderDetailedActivity.this, "Failed to load order details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderRest> call, Throwable t) {
                tvEmptyCartMessage.setVisibility(View.VISIBLE);
                Toast.makeText(OrderDetailedActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculatePrices(OrderRest orderRest) {
        int originalPrice = 0;

        for (ProductRest product : orderRest.getProducts()) {
            originalPrice += product.getPrice() * product.getQuantity();
        }
        tvTotal.setText("Total: $" + originalPrice);
    }


    private void updateUI() {
        if (order != null && order.getProducts() != null && !order.getProducts().isEmpty()) {
            tvEmptyCartMessage.setVisibility(View.GONE);
            orderDetailedAdapter = new OrderDetailedAdapter(order, this);
            recyclerView.setAdapter(orderDetailedAdapter);

            if (order.getShippingAddress() != null) {
                String address = order.getShippingAddress().getStreetName() + " " + order.getShippingAddress().getCity() + " " + order.getShippingAddress().getCountry() + " -- " + order.getShippingAddress().getPostalCode();
                tvShippingAddress.setText(address);
            } else {
                tvShippingAddress.setText("Shipping address not available");
            }

            if (order.getBillingAddress() != null) {
                String address = order.getBillingAddress().getStreetName() + " " + order.getBillingAddress().getCity() + " " + order.getBillingAddress().getCountry() + " -- " + order.getBillingAddress().getPostalCode();
                tvBillingAddress.setText(address);
            } else {
                tvBillingAddress.setText("Billing address not available");
            }
        } else {
            tvEmptyCartMessage.setVisibility(View.VISIBLE);
        }
    }

    private void toggleExpandableSection() {
        if (isExpanded) {
            layoutExpandable.setVisibility(View.GONE);
            isExpanded = false;
        } else {
            layoutExpandable.setVisibility(View.VISIBLE);
            isExpanded = true;
        }
    }
}
