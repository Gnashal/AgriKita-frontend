package mobdev.agrikita.controllers;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
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
            ContentResolver contentResolver = context.getContentResolver();
            InputStream inputStream = contentResolver.openInputStream(fileUri);
            String mimeType = contentResolver.getType(fileUri);

            if (inputStream == null || mimeType == null) {
                throw new IOException("Cannot open input stream or get MIME type.");
            }

            byte[] bytes = readBytes(inputStream);
            RequestBody requestBody = RequestBody.create(MediaType.parse(mimeType), bytes);

            // Optional: Get filename (some URIs may not have real names)
            String fileName = "upload_" + System.currentTimeMillis();

            return MultipartBody.Part.createFormData(partName, fileName, requestBody);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

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
