package mobdev.agrikita.models.products;

import com.google.gson.annotations.SerializedName;

public class CreateProductResponse {

    @SerializedName("message")
    private String message;

    @SerializedName("productId")
    private String productId;

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
