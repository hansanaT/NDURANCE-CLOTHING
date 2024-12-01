package com.ndurance.mobileapp.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.ndurance.mobileapp.R;
import com.ndurance.mobileapp.adapter.ImageSliderAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProductDetailsActivity extends AppCompatActivity {
    private ViewPager2 imageSlider;
    private TextView productName, productPrice, productCategory;
    private Button btnPrevImage, btnNextImage, btnUpdateProduct, btnDeleteProduct, btnSubmitComment;
    private EditText commentInput;
    private List<String> imageUrls = new ArrayList<>();
    private int currentImageIndex = 0;

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Initialize views
        imageSlider = findViewById(R.id.imageSlider);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productCategory = findViewById(R.id.productCategory);
        btnPrevImage = findViewById(R.id.btnPrevImage);
        btnNextImage = findViewById(R.id.btnNextImage);
        btnUpdateProduct = findViewById(R.id.btnUpdateProduct);
        btnDeleteProduct = findViewById(R.id.btnDeleteProduct);
        btnSubmitComment = findViewById(R.id.btnSubmitComment);
        commentInput = findViewById(R.id.commentInput);

        // Get product ID from intent
        String productId = getIntent().getStringExtra("productId");

        // Fetch product details from the server
        fetchProductDetails(productId);

        // Set up image navigation buttons
        btnPrevImage.setOnClickListener(v -> navigateImages(-1));
        btnNextImage.setOnClickListener(v -> navigateImages(1));
    }

    private void fetchProductDetails(String productId) {
        // HTTP request to the server
        String url = "http://10.0.2.2:8080/product-service/products/" + productId;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(ProductDetailsActivity.this, "Failed to load product details", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> {
                        Toast.makeText(ProductDetailsActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                try {
                    String responseBody = response.body().string();
                    JSONObject productData = new JSONObject(responseBody);

                    // Parse product details
                    String name = productData.getString("name");
                    double price = productData.getDouble("price");
                    String category = productData.getString("type");

                    // Parse images
                    JSONArray images = productData.getJSONArray("images");
                    imageUrls.clear();
                    for (int i = 0; i < images.length(); i++) {
                        imageUrls.add(images.getString(i));
                    }

                    // Update UI on the main thread
                    runOnUiThread(() -> {
                        productName.setText(name);
                        productPrice.setText("Price: $" + price);
                        productCategory.setText("Category: " + category);

                        // Set adapter for image slider
                        ImageSliderAdapter adapter = new ImageSliderAdapter(imageUrls);
                        imageSlider.setAdapter(adapter);
                    });

                } catch (Exception e) {
                    runOnUiThread(() -> {
                        Toast.makeText(ProductDetailsActivity.this, "Failed to parse product details", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    private void navigateImages(int direction) {
        currentImageIndex += direction;
        if (currentImageIndex < 0) currentImageIndex = imageUrls.size() - 1;
        if (currentImageIndex >= imageUrls.size()) currentImageIndex = 0;
        imageSlider.setCurrentItem(currentImageIndex, true);
    }
}