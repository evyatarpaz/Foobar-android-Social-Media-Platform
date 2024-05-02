package com.example.foobarpart2.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.foobarpart2.R;
import com.example.foobarpart2.db.entity.User;
import com.example.foobarpart2.models.LoggedInUser;
import com.example.foobarpart2.ui.viewmodels.UserViewModel;
import com.example.foobarpart2.utilities.ImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import kotlin.text.Regex;

public class ChangeUserInfoActivity extends AppCompatActivity {
    private static final int GALLERY_REQUEST_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 100;
    private EditText displayNameEditText, passwordEditText;
    private ImageButton profileImageView;
    private Button saveChangesButton;
    private Button deletAccountButton;
    private Uri photoUri = null;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);

        displayNameEditText = findViewById(R.id.editTextDisplayName);
        passwordEditText = findViewById(R.id.editTextPassword);
        profileImageView = findViewById(R.id.imageViewProfile);
        saveChangesButton = findViewById(R.id.buttonSaveChanges);
        deletAccountButton = findViewById(R.id.deleteAccountBtn);

        User currentUser = LoggedInUser.getInstance().getUser();
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // Initialize fields with current user info
        displayNameEditText.setText(currentUser.getDisplayName());
        passwordEditText.setText(currentUser.getPassword());
        profileImageView.setImageBitmap(ImageUtils.decodeBase64ToBitmap(currentUser.getProfilePic()));

        profileImageView.setOnClickListener(view -> {
            showImageSourceDialog();
        });

        saveChangesButton.setOnClickListener(view -> {
            // Save changes to the user's profile
            String newDisplayName = displayNameEditText.getText().toString();
            String newPassword = passwordEditText.getText().toString();


            // Update profile picture if a new one is selected
            User updatedUser;
            currentUser.setDisplayName(newDisplayName);
            if (!newPassword.isEmpty()) {
                if (validatePassword(newPassword)) {
                    updatedUser = new User(currentUser.getUsername(), newPassword, newDisplayName,
                            currentUser.getProfilePic(), currentUser.getFriends(), currentUser.getFriendsRequest());
                } else {
                    return;
                }
            } else {
                updatedUser = new User(currentUser.getUsername(), null, newDisplayName,
                        currentUser.getProfilePic(), currentUser.getFriends(), currentUser.getFriendsRequest());
            }
            if (photoUri != null) {
                try {
                    String newProfilePic = ImageUtils.convertToBase64(photoUri);
                    updatedUser.setProfilePic(newProfilePic);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            userViewModel.updateUser(updatedUser);

        });

        deletAccountButton.setOnClickListener(view ->{
            userViewModel.delete(currentUser);
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);

        });

    }

    private boolean validatePassword(String newPassword) {
        Regex passwordRegex = new Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}$");
        if (!passwordRegex.matches(newPassword)) {
            Toast.makeText(this,
                    "Password must be at least 8 characters with uppercase, " +
                            "lowercase, and numeric characters"
                    , Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
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
        photoUri = null;
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null && extras.containsKey("data")) {
                photoUri = data.getData();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                String savedImagePath = storeImageBitmap(imageBitmap);
                photoUri = Uri.parse("file://" + savedImagePath);
                profileImageView.setImageBitmap(imageBitmap);
            } else {
                profileImageView.setImageBitmap(null);
                Toast.makeText(this, "Failed to retrieve image"
                        , Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // Set the selected image URI to the ImageView
                photoUri = selectedImageUri;
                profileImageView.setImageURI(selectedImageUri);
            } else {
                profileImageView.setImageBitmap(null);
                Toast.makeText(this, "Failed to retrieve image"
                        , Toast.LENGTH_SHORT).show();
            }
        }
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
