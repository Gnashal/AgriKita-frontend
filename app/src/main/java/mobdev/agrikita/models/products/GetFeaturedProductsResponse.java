package mobdev.agrikita.models.products;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetFeaturedProductsResponse {
    private String message;
    private List<Products> products;

    // Getter and Setter methods
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }
}
