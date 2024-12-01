package com.ndurance.mobileapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ndurance.mobileapp.R;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActivityRegister extends AppCompatActivity {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText etFirstName = findViewById(R.id.etFirstName);
        EditText etLastName = findViewById(R.id.etLastName);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String fname = etFirstName.getText().toString().trim();
            String lname = etLastName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (fname.isEmpty() || lname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("firstName", fname);
                jsonObject.put("lastName", lname);
                jsonObject.put("email", email);
                jsonObject.put("password", password);
            } catch (Exception e) {
                Log.e("RegisterError", "Error creating JSON payload", e);
            }

            RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
            Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/user-service/users")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(ActivityRegister.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                        Log.e("RegisterError", "Request failed", e);
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(() -> {
                        if (response.isSuccessful()) {
                            Toast.makeText(ActivityRegister.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                            finish(); // Navigate back to Login
                        } else {
                            Toast.makeText(ActivityRegister.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            Log.e("RegisterError", "Response: " + response.message());
                        }
                    });
                }
            });
        });
    }
}