package mobdev.agrikita.models.products;

import com.google.gson.annotations.SerializedName;

public class UploadProductImageResponse {
    @SerializedName("url")
    private String imageUrl;
    public String getImageUrl() { return imageUrl; }
}
