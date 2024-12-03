package com.ndurance.mobileapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.ndurance.mobileapp.R;
import com.ndurance.mobileapp.adapter.OrderAdapter;
import com.ndurance.mobileapp.adapter.SpaceItemDecoration;
import com.ndurance.mobileapp.model.response.OrderResponse;
import com.ndurance.mobileapp.service.OrderService;
import com.ndurance.mobileapp.utils.TokenManager;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityOrder extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<OrderResponse> orders = new ArrayList<>();
    private int page = 1;
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private TokenManager tokenManager;

    private ImageView cart_icon, profile_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        tokenManager = new TokenManager(this);

        recyclerView = findViewById(R.id.recyclerView);

        cart_icon = findViewById(R.id.cart_icon);
        profile_icon = findViewById(R.id.profile_icon);

        cart_icon.setOnClickListener(view -> {
            Intent intent = new Intent(ActivityOrder.this, CartActivity.class);
            startActivity(intent);
        });

        profile_icon.setOnClickListener(view -> {
            Intent intent = new Intent(ActivityOrder.this, UserActivity.class);
            startActivity(intent);
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        int spacing = getResources().getDimensionPixelSize(R.dimen.recycler_item_spacing);
        recyclerView.addItemDecoration(new SpaceItemDecoration(spacing));

        // Set up adapter with click listener
        adapter = new OrderAdapter(orders, order -> {
            Intent intent = new Intent(ActivityOrder.this, OrderDetailedActivity.class);
            intent.putExtra("orderId", order.getId());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        fetchOrders(page);

        findViewById(R.id.nextPageButton).setOnClickListener(v -> {
            page++;
            fetchOrders(page);
        });
    }

    private void fetchOrders(int page) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OrderService orderService = retrofit.create(OrderService.class);
        String token = "Bearer " + getJwtToken();

        Call<List<OrderResponse>> call = orderService.getOrders(token, tokenManager.getUserId());

        call.enqueue(new Callback<List<OrderResponse>>() {
            @Override
            public void onResponse(Call<List<OrderResponse>> call, Response<List<OrderResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (page == 1) {
                        orders.clear();
                    }

                    orders.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ActivityOrder.this, "Failed to fetch orders", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderResponse>> call, Throwable t) {
                Log.e("ActivityOrder", "Error fetching orders", t);
                Toast.makeText(ActivityOrder.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getJwtToken() {
        return tokenManager.getJwtToken();
    }
}
