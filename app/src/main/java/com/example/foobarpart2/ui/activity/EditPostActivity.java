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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.foobarpart2.R;
import com.example.foobarpart2.db.entity.Post;
import com.example.foobarpart2.ui.viewmodels.PostsViewModel;
import com.example.foobarpart2.utilities.ImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditPostActivity extends AppCompatActivity {
    private EditText editTextContent;
    private PostsViewModel postViewModel;
    private Button btnEditP;
    private Button btnSave;
    private ImageView photo;
    private Uri photoUri;
    private  String image;
    private int postId;
    private String serverId;
    private Post editedPost;
    private static final int GALLERY_REQUEST_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postViewModel = new ViewModelProvider(this).get(PostsViewModel.class);
        setContentView(R.layout.activity_edit_post);

        editTextContent = findViewById(R.id.editTextContent);
        btnSave = findViewById(R.id.btnSave);
        btnEditP = findViewById(R.id.editPhotoBtn);
        photo = findViewById(R.id.photoImg);

        // Get the post ID and current content from the intent
        postId = getIntent().getIntExtra("postId", -1);

        String content = getIntent().getStringExtra("content");
        editTextContent.setText(content);

        postViewModel.getPostFromDao(postId);
        postViewModel.getPost().observe(this, post -> {
            image = post.getImage();
            serverId = post.get_id();
            Bitmap photoS = ImageUtils.decodeBase64ToBitmap(image);
            photo.setImageBitmap(photoS);
        });


        btnSave.setOnClickListener(v -> {
            String updatedContent = editTextContent.getText().toString();

            try {
                if (photoUri != null){
                    image = ImageUtils.convertToBase64(photoUri);
                }
                postViewModel.edit(serverId, updatedContent, image);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            setResult(RESULT_OK);
            finish();
        });

        btnEditP.setOnClickListener(v -> {
            showImageSourceDialog();
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
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null && extras.containsKey("data")) {
                this.photoUri = data.getData();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                String savedImagePath = storeImageBitmap(imageBitmap);
                this.photoUri = Uri.parse("file://" + savedImagePath);
                photo.setImageBitmap(imageBitmap);
            } else {
                photo.setImageBitmap(null);
                Toast.makeText(this, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                // Set the selected image URI to the ImageView
                photo.setImageURI(selectedImageUri);
                this.photoUri = selectedImageUri;
            } else {
                photo.setImageBitmap(null);
                Toast.makeText(this, "Failed to retrieve image", Toast.LENGTH_SHORT).show();
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

