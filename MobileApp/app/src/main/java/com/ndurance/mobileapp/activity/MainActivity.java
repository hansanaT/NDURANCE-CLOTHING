package com.ndurance.mobileapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ndurance.mobileapp.R;
import com.ndurance.mobileapp.utils.TokenManager;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    TokenManager tokenManager = new TokenManager(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String userId = tokenManager.getUserId();
        String jwtToken = tokenManager.getJwtToken();

        Log.d("%%%%%%%%%%%%%%%%%%% UserID *********", userId);
        Log.d("@@@@@@@@@@@@@@@@@@@@@ Authorization ****** ", jwtToken);

        if((userId != null && jwtToken != null) || (!userId.isEmpty() && !jwtToken.isEmpty())){
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_main);

        etEmail = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        TextView tvCreateAccount = findViewById(R.id.tvCreateAccount);
        tvCreateAccount.setOnClickListener(v -> {
            // Navigate to RegisterActivity
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    handleSignIn();
                } catch (Exception e) {
                    Log.e("LoginError", "Unexpected error occurred", e);
                    System.out.println(e);
                }
            }
        });
    }

    private void handleSignIn() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email or password is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create JSON payload
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", password);
        } catch (Exception e) {
            Log.e("JSONError", "Error creating JSON payload", e);
        }

        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);

        Request request = new Request.Builder()
                .url("http://10.0.2.2:8080/user-service/users/login")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("Request failed: " + e.getMessage());
                    e.printStackTrace();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();

                tokenManager.saveTokenAndUserId(response.header("UserID"), response.header("Authorization").split(" ")[1]);

                String userId = tokenManager.getUserId();
                String jwtToken = tokenManager.getJwtToken();

                Log.d("£££££££££££££££££££££££££ UserID *********", userId);
                Log.d("£££££££££££££££££££££££££ Authorization ****** ", jwtToken);

                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Log.d("LoginSuccess", responseBody);

                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        System.out.println("Login failed with response code: " + response.code());
                        System.out.println("Response body: " + responseBody);

                        try {
                            JSONObject errorResponse = new JSONObject(responseBody);
                            String errorMessage = errorResponse.optString("message", "Login Failed");
                            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            System.out.println("Error parsing response: " + e.getMessage());
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Unexpected error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}