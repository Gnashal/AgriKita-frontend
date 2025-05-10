package mobdev.agrikita.controllers;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.activity.result.ActivityResultLauncher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ImagePickerUtil {

    public static void launchImagePicker(Context context, ActivityResultLauncher<Intent> imagePickerLauncher) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    public static MultipartBody.Part prepareFilePart(Context context, String partName, Uri fileUri) {
        try {
            // Decode URI into Bitmap
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), fileUri);

            // Compress the bitmap to JPEG format (80% quality)
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bos);  // You can adjust quality: 0 (low) to 100 (high)
            byte[] compressedBytes = bos.toByteArray();

            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), compressedBytes);
            String fileName = "upload_" + System.currentTimeMillis() + ".jpg";

            return MultipartBody.Part.createFormData(partName, fileName, requestBody);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

//    Lets keep this in case we want to upload images without compressing
    private static byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
