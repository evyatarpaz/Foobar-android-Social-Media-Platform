package com.example.foobarpart2.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.foobarpart2.databinding.ActivitySignUpBinding;
import com.example.foobarpart2.db.entity.User;
import com.example.foobarpart2.ui.viewmodels.UserViewModel;
import com.example.foobarpart2.utilities.ImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import kotlin.text.Regex;

public class SignUp extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    private static final int GALLERY_REQUEST_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 100;

    private Uri imageUri = null;
    private User user;
    SwitchCompat switchMode;
    boolean nightMode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        switchMode = binding.switchMode;

        sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("nightMode", false);

        if (!nightMode) {
            switchMode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        switchMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nightMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("nightMode", false);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("nightMode", true);
                }
                editor.apply();
            }
        });

        binding.uploadPhotoBtn.setOnClickListener(v -> showImageSourceDialog());

        binding.retSignIn.setOnClickListener(v -> {
            Intent i = new Intent(this, SignIn.class);
            startActivity(i);
        });

        binding.btnSignUp.setOnClickListener(v -> {

            String username = binding.usernameTextSignup.getText().toString();
            String password = binding.passwordTextSignup.getText().toString();
            String confirmPassword = binding.confirmPasswordTextSignup.getText().toString();
            String displayName = binding.displayNameTextSignup.getText().toString();

            if (validateInput(username, password, confirmPassword, displayName, this.imageUri)) {
                String imageBase64 = null;
                try {
                    imageBase64 = ImageUtils.convertToBase64(this.imageUri);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                user = new User(username, password, displayName, imageBase64,new ArrayList<>(),new ArrayList<>());
                UserViewModel userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
                userViewModel.add(user);
                userViewModel.getSignUpResult().observe(this, isSuccess -> {
                    if (isSuccess) {
                        // Navigate to the next activity
                        Intent intent = new Intent(this, SignIn.class);
                        startActivity(intent);
                    } else {
                        // Show error message
                        Toast.makeText(this, "Sign up failed. Try again later.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image Source");
        builder.setItems(new String[]{"Gallery", "Camera"}, (dialog, which) -> {
            if (which == 0) {
                // Open gallery
                openGallery();
            } else if (which == 1) {
                // Open camera
                openCamera();
            }
        });
        builder.show();
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        try {
            startActivityForResult(intent, GALLERY_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("SignUp", "Exception occurred: " + e.getMessage(), e);
        }
    }

    private void openCamera() {
        try {
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "no camera", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUri = null;
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null && extras.containsKey("data")) {
                this.imageUri = data.getData();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                String savedImagePath = storeImageBitmap(imageBitmap);
                this.imageUri = Uri.parse("file://" + savedImagePath);
                binding.profileImage.setImageBitmap(imageBitmap);
            } else {
                binding.profileImage.setImageBitmap(null);
                Toast.makeText(this, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // Set the selected image URI to the ImageView
                binding.profileImage.setImageURI(selectedImageUri);
                this.imageUri = selectedImageUri;
            } else {
                binding.profileImage.setImageBitmap(null);
                Toast.makeText(this, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    boolean validateInput(String username, String password, String confirmPassword,
                          String displayName, Uri imageUri) {
        if (username.isEmpty() || password.isEmpty()
                || confirmPassword.isEmpty() || displayName.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields"
                    , Toast.LENGTH_LONG).show();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match"
                    , Toast.LENGTH_LONG).show();
            return false;
        }
        Regex passwordRegex = new Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}$");
        if (!passwordRegex.matches(password)) {
            Toast.makeText(this,
                    "Password must be at least 8 characters with uppercase, " +
                            "lowercase, and numeric characters"
                    , Toast.LENGTH_LONG).show();
            return false;
        }
        if (imageUri == null) {
            Toast.makeText(this, "must upload a profile image"
                    , Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private String storeImageBitmap(Bitmap imageBitmap) {
        // Create a unique file name
        String fileName = "temp_image_" + System.currentTimeMillis() + ".jpg";

        // Get the external storage directory
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Create the file
        File imageFile = new File(storageDir, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Return null if saving failed
        }

        // Return the saved image path
        return imageFile.getAbsolutePath();
    }
}