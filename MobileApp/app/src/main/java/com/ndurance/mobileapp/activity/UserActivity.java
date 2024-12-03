package com.ndurance.mobileapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.ndurance.mobileapp.R;
import com.ndurance.mobileapp.utils.TokenManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 100;
    private static final int GALLERY_REQUEST = 101;
    private TokenManager tokenManager = new TokenManager(this);
    private ImageView profileImage;
    private EditText firstName, lastName, email, city, country, street, postalCode, currentPassword, newPassword, confirmPassword;
    private Button btnUpdateProfile, btnUpdateAddress, btnUpdatePassword, btnUploadImage;
    private ImageView ivCart, order_icon;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

//        if (requestCode == 100) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Permissions denied. This feature requires permissions to function.", Toast.LENGTH_LONG).show();
//            }
//        }
//
//        if (requestCode == 101) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
//            }
//        }
    }
    private void checkAndRequestPermissions() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check if permission is already granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Request permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // For Android 11+ MANAGE_EXTERNAL_STORAGE
                if (!Environment.isExternalStorageManager()) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        checkAndRequestPermissions();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_REQUEST);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        }
        // Initialize Views
        profileImage = findViewById(R.id.profile_image);
        firstName = findViewById(R.id.et_first_name);
        lastName = findViewById(R.id.et_last_name);
        email = findViewById(R.id.et_email);
        city = findViewById(R.id.et_city);
        country = findViewById(R.id.et_country);
        street = findViewById(R.id.et_street);
        postalCode = findViewById(R.id.et_postal_code);
        currentPassword = findViewById(R.id.et_current_password);
        newPassword = findViewById(R.id.et_new_password);
        confirmPassword = findViewById(R.id.et_confirm_password);

        btnUpdateProfile = findViewById(R.id.btn_update_account);
        btnUpdateAddress = findViewById(R.id.btn_update_address);
        btnUpdatePassword = findViewById(R.id.btn_update_password);
        btnUploadImage = findViewById(R.id.btn_upload_image);
        ivCart = findViewById(R.id.ivCart);
        order_icon = findViewById(R.id.order_icon);

        ivCart.setOnClickListener(v->{
            Intent intent = new Intent(UserActivity.this, CartActivity.class);
            startActivity(intent);
        });

        order_icon.setOnClickListener(view -> {
            Intent intent = new Intent(UserActivity.this, ActivityOrder.class);
            startActivity(intent);
        });

        loadUserData();

        btnUploadImage.setOnClickListener(v -> showImagePickerOptions());

        btnUpdatePassword.setOnClickListener(v -> {
            String userId = tokenManager.getUserId();
            String password = currentPassword.getText().toString();
            String newUserPassword = newPassword.getText().toString();
            String confirm_password = confirmPassword.getText().toString();
            changePassword(userId, password, newUserPassword, confirm_password);
        });

        btnUpdateProfile.setOnClickListener(v -> {
            String userId = tokenManager.getUserId();
            String firstNameField = firstName.getText().toString();
            String lastNameField = lastName.getText().toString();
            String emailField = email.getText().toString();
            updateUserProfile(userId, firstNameField, lastNameField, emailField);
        });

        btnUpdateAddress.setOnClickListener(v -> {
            String userId = tokenManager.getUserId();
            String cityField = city.getText().toString();
            String countryField = country.getText().toString();
            String streetField = street.getText().toString();
            String postalCodeField = postalCode.getText().toString();
            updateUserAddress(userId, cityField, countryField, streetField, postalCodeField);
        });
    }

    private File uriToFile(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        File tempFile = new File(getCacheDir(), "temp_image.jpg");
        try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }

    private InputStream getInputStreamFromUri(Uri uri) {
        try {
            return getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void fetchUserProfilePicture(String userId) {
        new Thread(() -> {
            try {
                String jwtToken = tokenManager.getJwtToken();
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/user-service/users/image/" + userId)
                        .addHeader("Authorization", "Bearer " + jwtToken)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    // Update the UI on the main thread
                    runOnUiThread(() -> profileImage.setImageBitmap(bitmap));
                } else {
                    Log.e("ProfileImage", "Failed to fetch image: " + response.message());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void loadUserData() {
        String userId = tokenManager.getUserId();
        String jwtToken = tokenManager.getJwtToken();
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/user-service/users/" + userId)
                        .addHeader("Authorization", "Bearer " + jwtToken)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    JSONObject userData = new JSONObject(response.body().string());
                    runOnUiThread(() -> {
                        try {
                            firstName.setText(userData.getString("firstName"));
                            lastName.setText(userData.getString("lastName"));
                            email.setText(userData.getString("email"));

                            JSONObject address = userData.getJSONArray("addresses").getJSONObject(0);
                            city.setText(address.getString("city"));
                            country.setText(address.getString("country"));
                            street.setText(address.getString("streetName"));
                            postalCode.setText(address.getString("postalCode"));

                            fetchUserProfilePicture(userId);

                        } catch (Exception e) {
                            Toast.makeText(UserActivity.this, "Failed to parse user data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(UserActivity.this, "Failed to load user data", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void showImagePickerOptions() {
        String[] options = {"Camera", "Gallery"};
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Upload Image")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            openCamera();
                        } else {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
                        }
                    } else {
                        openGallery();
                    }
                });
        builder.show();
    }

    private static final int CAMERA_REQUEST_CODE = 102;
    private Uri photoUri;

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  // Prefix
                ".jpg",         // Suffix
                storageDir      // Directory
        );
    }
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            try {
                File photoFile = createImageFile();
                if (photoFile != null) {
                    photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                }
            } catch (IOException e) {
                Toast.makeText(this, "Error creating image file", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
                if (photoUri != null) {

                    profileImage.setImageURI(photoUri);
                    File photoFile = new File(photoUri.getPath());
                    uploadProfilePictureFromFile(photoFile);

                    /*Bitmap photo = (Bitmap) data.getExtras().get("data");
                    profileImage.setImageBitmap(photo);

                    // Upload the image
                    uploadProfilePictureFromBitmap(photo);*/
                }
            } else if (requestCode == GALLERY_REQUEST && data != null) {
                Uri selectedImageUri = data.getData();
                try {
                    File imageFile = uriToFile(selectedImageUri);
                    uploadProfilePictureFromFile(imageFile);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to access the selected image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void uploadProfilePictureFromFile(File file) {
        new Thread(() -> {
            try {
                String userId = tokenManager.getUserId();
                String jwtToken = tokenManager.getJwtToken();

                OkHttpClient client = new OkHttpClient();
                RequestBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image", file.getName(),
                                RequestBody.create(file, MediaType.parse("image/*")))
                        .build();

                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/user-service/users/upload-pic/" + userId)
                        .put(body)
                        .addHeader("Authorization", "Bearer " + jwtToken)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void uploadProfilePictureFromBitmap(Bitmap bitmap) {
        new Thread(() -> {
            try {
                File tempFile = new File(getCacheDir(), "temp_image.jpg");
                FileOutputStream fos = new FileOutputStream(tempFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();

                uploadProfilePictureFromFile(tempFile);

            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
    private void uploadProfilePicture_(Object image) {
        new Thread(() -> {
            try {
                String userId = tokenManager.getUserId();
                String jwtToken = tokenManager.getJwtToken();

                OkHttpClient client = new OkHttpClient();
                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                if (image instanceof Bitmap) {
                    // Handle bitmap (convert to file if necessary)
                } else if (image instanceof File) {
                    builder.addFormDataPart("image", ((File) image).getName(),
                            RequestBody.create((File) image, MediaType.parse("image/*")));
                }

                RequestBody body = builder.build();
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/user-service/users/upload-pic/" + userId)
                        .post(body)
                        .addHeader("Authorization", "Bearer " + jwtToken)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(UserActivity.this, "Profile picture updated", Toast.LENGTH_SHORT).show());
                }else {
                    runOnUiThread(() -> Toast.makeText(this, "Failed to update user details", Toast.LENGTH_SHORT).show());
                }
            } catch (IOException e) {
                runOnUiThread(() -> Toast.makeText(UserActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void updateUserProfile(String userId, String firstName, String lastName, String email) {
        new Thread(() -> {
            try {
                String jwtToken = tokenManager.getJwtToken();

                OkHttpClient client = new OkHttpClient();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("firstName", firstName);
                jsonObject.put("lastName", lastName);
                jsonObject.put("email", email);

                RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/user-service/users/" + userId)
                        .put(body)
                        .addHeader("Authorization", "Bearer " + jwtToken) // Replace with actual JWT token
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(this, "User details updated successfully", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Failed to update user details", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void changePassword(String userId, String currentPassword, String newPassword, String confirmPassword) {
        new Thread(() -> {
            try {
                String jwtToken = tokenManager.getJwtToken();
                if(!newPassword.equals(confirmPassword))
                    Toast.makeText(this, "Error: Password Miss Match", Toast.LENGTH_SHORT).show();

                OkHttpClient client = new OkHttpClient();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", userId);
                jsonObject.put("currentPassword", currentPassword);
                jsonObject.put("newPassword", newPassword);
                jsonObject.put("confirmPassword", confirmPassword);

                RequestBody body = RequestBody.create(jsonObject.toString(), MediaType.parse("application/json"));
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/user-service/users/reset-password/" + userId)
                        .post(body)
                        .addHeader("Authorization", "Bearer " + jwtToken) // Replace with actual JWT token
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show());
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void updateUserAddress(String userId, String city, String country, String streetName, String postalCode) {
        new Thread(() -> {
            try {
                String jwtToken = tokenManager.getJwtToken();

                OkHttpClient client = new OkHttpClient();
                JSONObject addressObject = new JSONObject();
                addressObject.put("city", city);
                addressObject.put("country", country);
                addressObject.put("streetName", streetName);
                addressObject.put("postalCode", postalCode);

                JSONObject requestBody = new JSONObject();

                requestBody.put("addresses", new JSONArray().put(addressObject)); // Wrapping address in an array

                RequestBody body = RequestBody.create(requestBody.toString(), MediaType.parse("application/json"));
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/user-service/users/" + userId)
                        .put(body)
                        .addHeader("Authorization", "Bearer " + jwtToken) // Replace with actual JWT token
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(this, "Address updated successfully", Toast.LENGTH_SHORT).show());
                } else {
                    Log.d("FUCKING ERROR", response.toString());
                    runOnUiThread(() -> Toast.makeText(this, "Failed to update address", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}