package com.example.foobarpart2.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;

import com.example.foobarpart2.MyApplication;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageUtils {
    public static String convertToBase64(Uri imageUri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(MyApplication.context.getContentResolver(), imageUri);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();

        return "data:image/png;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
    public static Bitmap decodeBase64ToBitmap(String base64String) {
        // Check if the base64 string contains the metadata prefix
        if (base64String.startsWith("data:image")) {
            // Strip off the metadata part (e.g., "data:image/png;base64,")
            base64String = base64String.replaceFirst("^data:image/[a-zA-Z]+;base64,", "");
        }
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
