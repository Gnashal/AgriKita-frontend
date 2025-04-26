package mobdev.agrikita.models.products;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAllProductsResponse {
    @SerializedName("message")
    private String message;

    @SerializedName("products")
    private List<Products> products;

    public String getMessage() {
        return message;
    }

    public List<Products> getProducts() {
        return products;
    }
}
